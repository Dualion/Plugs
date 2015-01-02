package com.dualion.power_strip.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dualion.power_strip.R;
import com.dualion.power_strip.data.SharedData;
import com.dualion.power_strip.model.calendar.DateTime;
import com.dualion.power_strip.model.calendar.DateTimePicker;
import com.dualion.power_strip.model.calendar.SimpleDateTimePicker;
import com.dualion.power_strip.model.calendar.SimpleTimePicker;
import com.dualion.power_strip.model.calendar.Time;
import com.dualion.power_strip.model.calendar.TimePicker;
import com.dualion.power_strip.model.plug.PlugsList;
import com.dualion.power_strip.model.scheduler.Scheduler;
import com.dualion.power_strip.model.scheduler.SchedulerDiario;
import com.dualion.power_strip.model.scheduler.SchedulerSemanal;
import com.dualion.power_strip.model.ui.BaseFragment;
import com.dualion.power_strip.model.ui.SelectedBox;
import com.dualion.power_strip.restapi.PlugService;
import com.dualion.power_strip.restapi.RestPlug;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.dualion.power_strip.utils.ui.showView;

public class DetailPlugFragment extends BaseFragment implements
		CompoundButton.OnCheckedChangeListener, Callback<PlugsList> {

	private SimpleDateTimePicker initDateTimePicker;
	private SimpleDateTimePicker endDateTimePicker;
	private SimpleTimePicker initTimePicker;
	private SimpleTimePicker endTimePicker;

	private Button sendDates;
	private EditText initDate;
	private EditText endDate;
	private CheckBox checkBoxDiario;
	private CheckBox checkBoxSemanal;

	private TableLayout tablaDiasSemana;

	private Long initMillis;
	private Long endMillis;

	@Inject
	SharedData settings;

	final static String ARG_PID = "pid";
	String currentPid = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		// If activity recreated (such as from screen rotate), restore
		// the previous article selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.
		if (savedInstanceState != null) {
			currentPid = savedInstanceState.getString(ARG_PID);
		}

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.activity_dates, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initDate = (EditText) getActivity().findViewById(R.id.initDate);
		endDate = (EditText) getActivity().findViewById(R.id.endDate);
		sendDates = (Button) getActivity().findViewById(R.id.sendDates);
		checkBoxDiario = (CheckBox) getActivity().findViewById(R.id.checkBoxDiario);
		checkBoxSemanal = (CheckBox) getActivity().findViewById(R.id.checkBoxSemanal);
		tablaDiasSemana = (TableLayout) getActivity().findViewById(R.id.tablaDiasSemana);

		initMillis = 0L;
		endMillis = 0L;

		checkBoxSemanal.setOnCheckedChangeListener(this);
		checkBoxDiario.setOnCheckedChangeListener(this);

		//init DateTimePicker
		initDateTimePicker();

		//init TimePicker
		initTimePicker();

		// init Restapi
		final RestPlug restProduct = new RestPlug(settings.getURI(), settings.getUser(), settings.getCurrentPass());

		sendDates.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (initMillis >= endMillis) {
					sendDates.setError(getString(R.string.WrongDate));
					return;
				} else {
					sendDates.setError(null);
				}
				callRestApi(restProduct.getService(), Integer.parseInt(currentPid));
			}
		});

	}


	@Override
	public void onStart() {
		super.onStart();

		// During startup, check if there are arguments passed to the fragment.
		// onStart is a good place to do this because the layout has already been
		// applied to the fragment at this point so we can safely call the method
		// below that sets the article text.
		Bundle args = getArguments();
		if (args != null) {
			// Set article based on argument passed in
			updateArticleView(args.getString(ARG_PID));
		} else if (!currentPid.equals("")) {
			// Set article based on saved instance state defined during onCreateView
			updateArticleView(currentPid);
		}
	}

	public void updateArticleView(String pid) {
		TextView titleDate = (TextView) getActivity().findViewById(R.id.titleDate);
		titleDate.setText(getString(R.string.plug) + ": " + pid);
		currentPid = pid;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current article selection in case we need to recreate the fragment
		outState.putString(ARG_PID, currentPid);
	}

	private void callRestApi(PlugService service, int pid) {

		TableLayout table = tablaDiasSemana;
		TableRow row = (TableRow) table.getChildAt(0);

		if (checkBoxDiario.isChecked()) {
			SchedulerDiario schDiario = new SchedulerDiario();
			schDiario.setStart(initDate.getText().toString());
			schDiario.setStop(endDate.getText().toString());
			service.SetSchedulerDiarioFromId(schDiario, pid, this);
		} else if (checkBoxSemanal.isChecked()) {
			SchedulerSemanal schSemanal = new SchedulerSemanal();
			schSemanal.setStart(initDate.getText().toString());
			schSemanal.setStop(endDate.getText().toString());
			SchedulerSemanal.DaysOfWeek repeatOnDays = schSemanal.new DaysOfWeek();
			repeatOnDays.setMonday(((SelectedBox) row.getChildAt(0)).isChecked());
			repeatOnDays.setTuesday(((SelectedBox) row.getChildAt(1)).isChecked());
			repeatOnDays.setWednesday(((SelectedBox) row.getChildAt(2)).isChecked());
			repeatOnDays.setThursday(((SelectedBox) row.getChildAt(3)).isChecked());
			repeatOnDays.setFriday(((SelectedBox) row.getChildAt(4)).isChecked());
			repeatOnDays.setSaturday(((SelectedBox) row.getChildAt(5)).isChecked());
			repeatOnDays.setSunday(((SelectedBox) row.getChildAt(6)).isChecked());
			schSemanal.setRepeatOnDays(repeatOnDays);
			service.SetSchedulerSemanalFromId(schSemanal, pid, this);
		} else {
			Scheduler sch = new Scheduler();
			sch.setStart(initMillis);
			sch.setStop(endMillis);
			service.SetSchedulerFromId(sch, pid, this);
		}
	}

	private void initTimePicker() {
		Calendar calendar = Calendar.getInstance();
		initTimePicker = SimpleTimePicker.make(
				getString(R.string.prompt_initTime),
				new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)+1),
				new TimePicker.OnTimeSetListener() {
					@Override
					public void TimeSet(Time timeSet) {
						Time time = new Time(timeSet);
						initDate.setText(time.getTimeString());
						initMillis = time.getTimeMillis();
						if( initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
							sendDates.setEnabled(true);
						}else{
							sendDates.setEnabled(false);
						}
					}
				},
				getActivity().getSupportFragmentManager()
		);

		endTimePicker = SimpleTimePicker.make(
				getString(R.string.prompt_endTime),
				new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)+2),
				new TimePicker.OnTimeSetListener() {
					@Override
					public void TimeSet(Time timeSet) {
						Time time = new Time(timeSet);
						endDate.setText(time.getTimeString());
						endMillis = time.getTimeMillis();
						if( initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
							sendDates.setEnabled(true);
						}else{
							sendDates.setEnabled(false);
						}
					}
				},
				getActivity().getSupportFragmentManager()
		);
	}

	private void initDateTimePicker() {

		// Create a initDateTimePicker
		initDateTimePicker = SimpleDateTimePicker.make(
				getString(R.string.prompt_initDate),
				new Date(System.currentTimeMillis()+60*1000),
				new DateTimePicker.OnDateTimeSetListener() {
					@Override
					public void DateTimeSet(Date date) {
						DateTime dateTime = new DateTime(date);
						initDate.setText(dateTime.getDateString());
						initMillis = dateTime.getCalendar().getTimeInMillis();
						if( initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
							sendDates.setEnabled(true);
						}else{
							sendDates.setEnabled(false);
						}
					}
				},
				getActivity().getSupportFragmentManager()
		);

		endDateTimePicker = SimpleDateTimePicker.make(
				getString(R.string.prompt_endDate),
				new Date(System.currentTimeMillis()+2*60*1000),
				new DateTimePicker.OnDateTimeSetListener() {
					@Override
					public void DateTimeSet(Date date) {
						DateTime dateTime = new DateTime(date);
						endDate.setText(dateTime.getDateString());
						endMillis = dateTime.getCalendar().getTimeInMillis();
						if( initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
							sendDates.setEnabled(true);
						}else{
							sendDates.setEnabled(false);
						}
					}
				},
				getActivity().getSupportFragmentManager()
		);
	}

	public void selectInitDate(View view) {
		if (!checkBoxDiario.isChecked() && !checkBoxSemanal.isChecked() )
			initDateTimePicker.show();
		else
			initTimePicker.show();
	}

	public void selectEndDate(View view) {
		if (!checkBoxDiario.isChecked() && !checkBoxSemanal.isChecked() )
			endDateTimePicker.show();
		else
			endTimePicker.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()){
			case R.id.checkBoxDiario:
				if (isChecked) {
					checkBoxSemanal.setChecked(false);
				}
				break;
			case R.id.checkBoxSemanal:
				if (isChecked) {
					showView(tablaDiasSemana,
							getResources().getInteger(android.R.integer.config_shortAnimTime),
							true);
					checkBoxDiario.setChecked(false);
				}else {
					showView(tablaDiasSemana,
							getResources().getInteger(android.R.integer.config_shortAnimTime),
							false);
				}
				break;
		}
		initDate.setText("");
		endDate.setText("");
	}

	@Override
	public void success(PlugsList plugsList, Response response) {
		Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		Toast.makeText(getActivity(), "fail", Toast.LENGTH_LONG).show();
	}

}
