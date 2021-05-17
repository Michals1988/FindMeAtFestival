package com.example.findmeatfestival;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class FestivalAddActivity extends AppCompatActivity {

    EditText editDateDestival;
    Calendar mCalender = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_add);

        editDateDestival = findViewById(R.id.editTextDate);

        editDateDestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = mCalender.get(Calendar.YEAR);
                int month = mCalender.get(Calendar.MONTH);
                int day = mCalender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(v.getContext(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editDateDestival.setText(checkDigit(dayOfMonth) + "-" + checkDigit((monthOfYear + 1)) + "-" + year);
                    }
                }, year, month, day);
                dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dp.show();
            }
        });
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}