package sg.edu.rp.c346.id19004731.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText firstNameEditText, lastNameEditText, phoneNumber, groupSize;
    DatePicker datePicker;
    TimePicker timePicker;
    CheckBox smokingAreaCheckBox;
    Button resetButton, confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        phoneNumber = findViewById(R.id.phoneEditText);
        groupSize = findViewById(R.id.groupSizeEditText);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        smokingAreaCheckBox = findViewById(R.id.smokeAreaCheckBox);
        resetButton = findViewById(R.id.resetButton);
        confirmButton = findViewById(R.id.confirmButton);

        setDefaultDate(datePicker);
        setDefaultTime(timePicker);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameStr = firstNameEditText.getText().toString().trim();
                String lastNameStr = lastNameEditText.getText().toString().trim();
                String phoneNumberStr = phoneNumber.getText().toString().trim();
                String groupSizeStr = groupSize.getText().toString();
                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                boolean isSmokeArea = smokingAreaCheckBox.isChecked();

                Log.d("MainActivity", "valid?" + isValidDate(year, month, day, hour, min));
                if (hasEmpty(firstNameStr, lastNameStr, phoneNumberStr, groupSizeStr)) {
                    showToast("Please fill in all fields", 1);
                } else if (isValidDate(year, month, day, hour, min) == false) {
                    showToast("Please select date and time after today", 1);
                } else {
                    showToast(showInfo(firstNameStr, lastNameStr, phoneNumberStr, groupSizeStr, hour, min, day, month, year, isSmokeArea),1);
                }
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay < 8) {
                    showToast("Restaurant opens at 8 AM", 0);
                    timePicker.setCurrentHour(8);
                } else if (hourOfDay >= 21) {
                    showToast("Restaurant closes at 9 PM", 0);
                    timePicker.setCurrentHour(20);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameEditText.getText().clear();
                lastNameEditText.getText().clear();
                smokingAreaCheckBox.setChecked(false);
                phoneNumber.getText().clear();
                groupSize.getText().clear();
                setDefaultDate(datePicker);
                setDefaultTime(timePicker);
            }
        });
    }

    public void setDefaultDate(DatePicker datePicker) {
        datePicker.updateDate(2020, 6, 1);
    }

    public void setDefaultTime(TimePicker timePicker) {
        timePicker.setCurrentHour(17);
        timePicker.setCurrentMinute(30);
    }

    public String showInfo(String firstNameStr, String lastNameStr, String phoneNumber, String groupSize,
                           int hour, int min, int day, int month, int year, boolean isSmokeArea) {
        String info = String.format("Full Name: %s %s\n", firstNameStr, lastNameStr);
        info += String.format("Phone Number: %s\n", phoneNumber);
        info += String.format("Group Size: %s\n", groupSize);
        if (isSmokeArea) { info += "Table in Smoking Area\n"; }
        else { info += "Table in Non-smoking Area\n"; }
        info += String.format("Date: %02d/%02d/%04d\n", day, (month+1), year);
        info += String.format("Time: %02d:%02d", hour, min);
        return info;
    }

    public boolean hasEmpty(String firstName, String lastName, String phoneNumber, String groupSize) {
        String[] inputFields = {firstName, lastName, phoneNumber, groupSize};
        for (String field : inputFields) {
            if (field.isEmpty()) { return true; }
        }
        return false;
    }

    public void showToast(String text, int length) {
        int len = Toast.LENGTH_SHORT;
        if (length == 1) {  len = Toast.LENGTH_LONG; }
        Toast.makeText(MainActivity.this, text, len).show();
    }

    public boolean isValidDate(int year, int month, int day, int hour, int min) {
        //Date is deprecated, use Calendar
        Calendar current = Calendar.getInstance();
        Calendar selected = Calendar.getInstance();
        selected.set(year, month, day, hour, min);
        return selected.after(current);
    }

}
