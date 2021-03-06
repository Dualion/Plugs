package com.dualion.power_strip.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class DatesFragment extends BaseFragment implements
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

	RestPlug restProduct;

	@Inject
	SharedData settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist.  The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed.  Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.dates, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (getView() == null) {
			return;
		}

		// We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		initDate = (EditText) getActivity().findViewById(R.id.initDate);
		endDate = (EditText) getActivity().findViewById(R.id.endDate);
		sendDates = (Button) getActivity().findViewById(R.id.sendDates);
		checkBoxDiario = (CheckBox) getActivity().findViewById(R.id.checkBoxDiario);
		checkBoxSemanal = (CheckBox) getActivity().findViewById(R.id.checkBoxSemanal);
		ImageButton buttonInitDate = (ImageButton) getActivity().findViewById(R.id.buttonInitDate);
		ImageButton buttonEndDate = (ImageButton) getActivity().findViewById(R.id.buttonEndDate);
		tablaDiasSemana = (TableLayout) getActivity().findViewById(R.id.tablaDiasSemana);

		initMillis = 0L;
		endMillis = 0L;

		checkBoxSemanal.setOnCheckedChangeListener(this);
		checkBoxDiario.setOnCheckedChangeListener(this);

		initDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectInitDate();
			}
		});

		endDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectEndDate();
			}
		});

		buttonInitDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectInitDate();
			}
		});

		buttonEndDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectEndDate();
			}
		});

		//init DateTimePicker
		initDateTimePicker();

		//init TimePicker
		initTimePicker();

		// init Restapi
		restProduct = new RestPlug(settings.getURI(), settings.getUser(), settings.getCurrentPass());

		sendDates.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (initMillis >= endMillis) {
					sendDates.setError(getString(R.string.wrong_date));
					return;
				} else {
					sendDates.setError(null);
				}
				callRestApi(restProduct.getService(), Integer.parseInt(getShownPid()));
			}
		});

	}

	public int getShownIndex() {
		return getArguments().getInt(ARG_INDEX, 0);
	}

	public String getShownPid() {
		return getArguments().getString(ARG_PID);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		MenuItem iStopPlug = menu.findItem(R.id.action_stop_plug);
		if (iStopPlug == null) {
			menu.add(Menu.NONE, R.id.action_stop_plug, Menu.NONE, R.string.action_stop).setIcon(R.drawable.ic_action_stop).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_stop_plug:
				restProduct.getService().SetPlugCancel(Integer.parseInt(getShownPid()), this);
				return true;
		}
		return super.onOptionsItemSelected(item);
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
				new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + 1),
				new TimePicker.OnTimeSetListener() {
					@Override
					public void TimeSet(Time timeSet) {
						Time time = new Time(timeSet);
						initDate.setText(time.getTimeString());
						initMillis = time.getTimeMillis();
						if (initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
							sendDates.setEnabled(true);
						} else {
							sendDates.setEnabled(false);
						}
					}
				},
				getActivity().getSupportFragmentManager()
		);

		endTimePicker = SimpleTimePicker.make(
				getString(R.string.prompt_endTime),
				new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + 2),
				new TimePicker.OnTimeSetListener() {
					@Override
					public void TimeSet(Time timeSet) {
						Time time = new Time(timeSet);
						endDate.setText(time.getTimeString());
						endMillis = time.getTimeMillis();
						if (initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
							sendDates.setEnabled(true);
						} else {
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
				new Date(System.currentTimeMillis() + 60 * 1000),
				new DateTimePicker.OnDateTimeSetListener() {
					@Override
					public void DateTimeSet(Date date) {
						DateTime dateTime = new DateTime(date);
						initDate.setText(dateTime.getDateString());
						initMillis = dateTime.getCalendar().getTimeInMillis();
						if (initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
							sendDates.setEnabled(true);
						} else {
							sendDates.setEnabled(false);
						}
					}
				},
				getActivity().getSupportFragmentManager()
		);

		endDateTimePicker = SimpleDateTimePicker.make(
				getString(R.string.prompt_endDate),
				new Date(System.currentTimeMillis() + 2 * 60 * 1000),
				new DateTimePicker.OnDateTimeSetListener() {
					@Override
					public void DateTimeSet(Date date) {
						DateTime dateTime = new DateTime(date);
						endDate.setText(dateTime.getDateString());
						endMillis = dateTime.getCalendar().getTimeInMillis();
						if (initMillis != 0L && endMillis != 0L && (initMillis < endMillis)) {
							sendDates.setEnabled(true);
						} else {
							sendDates.setEnabled(false);
						}
					}
				},
				getActivity().getSupportFragmentManager()
		);
	}

	public void selectInitDate() {
		if (!checkBoxDiario.isChecked() && !checkBoxSemanal.isChecked())
			initDateTimePicker.show();
		else
			initTimePicker.show();
	}

	public void selectEndDate() {
		if (!checkBoxDiario.isChecked() && !checkBoxSemanal.isChecked())
			endDateTimePicker.show();
		else
			endTimePicker.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
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
				} else {
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
