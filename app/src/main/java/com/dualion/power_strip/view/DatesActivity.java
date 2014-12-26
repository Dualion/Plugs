package com.dualion.power_strip.view;

import android.content.Intent;
import android.os.Bundle;

import com.dualion.power_strip.R;
import com.dualion.power_strip.data.SharedData;
import com.dualion.power_strip.model.Calendar.DateTime;
import com.dualion.power_strip.model.Calendar.DateTimePicker;
import com.dualion.power_strip.model.Calendar.SimpleTimePicker;
import com.dualion.power_strip.model.Calendar.Time;
import com.dualion.power_strip.model.Calendar.TimePicker;
import com.dualion.power_strip.model.PlugsList;
import com.dualion.power_strip.model.Calendar.SimpleDateTimePicker;
import com.dualion.power_strip.model.ui.BaseFragmentActivity;
import com.dualion.power_strip.model.ui.SelectedBox;
import com.dualion.power_strip.restapi.PlugService;
import com.dualion.power_strip.restapi.RestPlug;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.dualion.power_strip.utils.ui.showView;

public class DatesActivity extends BaseFragmentActivity implements
        OnCheckedChangeListener {

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
    private String pid;

    @Inject
    SharedData settings;

    PlugService plugService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the Title bar of this activity screen
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dates);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra("pid");

        initDate = (EditText) findViewById(R.id.initDate);
        endDate = (EditText) findViewById(R.id.endDate);
        TextView titleDate = (TextView) findViewById(R.id.titleDate);
        sendDates = (Button) findViewById(R.id.sendDates);
        checkBoxDiario = (CheckBox) findViewById(R.id.checkBoxDiario);
        checkBoxSemanal = (CheckBox) findViewById(R.id.checkBoxSemanal);
        tablaDiasSemana = (TableLayout) findViewById(R.id.tablaDiasSemana);

        titleDate.setText(getString(R.string.plug) + ": " + pid);

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

                plugService = restProduct.getService();
                String body;
                try {
                    body = generateBody();
                    plugService.SetSchedulerPlugFromId(body, Integer.parseInt(pid), new Callback<PlugsList>(){

                        @Override
                        public void success(PlugsList plugsList, Response response) {
                            Toast.makeText(DatesActivity.this, "OK", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Toast.makeText(DatesActivity.this, "fail", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    Toast.makeText(DatesActivity.this, "fail json", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String generateBody() throws JSONException {

        TableLayout table=tablaDiasSemana;
        TableRow row = (TableRow)table.getChildAt(0);
        JSONObject json = new JSONObject();

        if (checkBoxDiario.isChecked()) {
            json.put("stop_at",endDate.getText());
            json.put("start_at",initDate.getText());
            json.put("repeat", "Diario");
        } else if (checkBoxSemanal.isChecked()) {
            JSONObject repeatOnDays = new JSONObject();
            repeatOnDays.put("monday", ((SelectedBox) row.getChildAt(0)).isChecked());
            repeatOnDays.put("tuesday", ((SelectedBox) row.getChildAt(1)).isChecked());
            repeatOnDays.put("wednesday", ((SelectedBox) row.getChildAt(2)).isChecked());
            repeatOnDays.put("thursday", ((SelectedBox) row.getChildAt(3)).isChecked());
            repeatOnDays.put("friday",((SelectedBox) row.getChildAt(4)).isChecked());
            repeatOnDays.put("saturday",((SelectedBox) row.getChildAt(5)).isChecked());
            repeatOnDays.put("sunday",((SelectedBox) row.getChildAt(6)).isChecked());

            json.put("repeatOnDays", repeatOnDays);
            json.put("stop_at",endDate.getText());
            json.put("start_at",initDate.getText());
            json.put("repeat", "Semanal");
        } else {
            json.put("stop_at",endMillis);
            json.put("start_at",initMillis);
            json.put("repeat", "");
        }
        return json.toString();
    }

    private void initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        initTimePicker = SimpleTimePicker.make(
                getString(R.string.prompt_initTime),
                new Time(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)+1),
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
                getSupportFragmentManager()
        );

        endTimePicker = SimpleTimePicker.make(
                getString(R.string.prompt_endTime),
                new Time(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)+2),
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
                getSupportFragmentManager()
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
                getSupportFragmentManager()
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
                getSupportFragmentManager()
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
}