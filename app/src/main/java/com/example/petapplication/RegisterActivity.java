package com.example.petapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;

import com.example.petapplication.databinding.ActivityRegisterBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void showDatePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year1, month1, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year1, month1 + 1, dayOfMonth);

                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
                    String displayedDate = selectedDate.format(formatter);

                    binding.registerBirthDate.setText(displayedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    public void retrieveRegistrationToken(View view) {
    }
}