package com.example.petapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petapplication.databinding.ActivityRegisterBinding;
import com.example.petapplication.helpers.Constants;
import com.example.petapplication.validators.BaseValidator;
import com.example.petapplication.validators.BirthDateValidator;
import com.example.petapplication.validators.EmailValidator;
import com.example.petapplication.validators.EmptyValidator;
import com.example.petapplication.validators.PasswordValidator;
import com.example.petapplication.validators.ValidationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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

        int childCount = binding.getRoot().getChildCount();
        boolean foundBlankFields = false;
        for (int i = 0; i < childCount; i++) {
            View v = binding.getRoot().getChildAt(i);
            if (v instanceof TextInputLayout inputLayout) {
                EditText editText = inputLayout.getEditText();
                if (editText != null) {
                    Editable editable = editText.getText();
                    if (editable == null || editable.toString().isBlank()) {
                        editText.setError(getString(R.string.text_validation_error_blank_text));
                        foundBlankFields = true;
                    }
                }

            }
        }
        if (foundBlankFields)
            return;
        List<ValidationResult> results = new ArrayList<>();
        validateField(binding.registerEmailAddress, results, EmptyValidator.class, EmailValidator.class);
        validateField(binding.registerPassword, results, EmptyValidator.class, PasswordValidator.class);
        validateField(binding.registerFirstName, results, EmptyValidator.class);
        validateField(binding.registerLastName, results, EmptyValidator.class);
        validateField(binding.registerBirthDate, results, EmptyValidator.class, BirthDateValidator.class);

        if (!results.stream().allMatch(ValidationResult::isSuccess))
            return;

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.REGISTER_URL;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", binding.registerEmailAddress.getText().toString());
            jsonObject.put("password", binding.registerPassword.getText().toString());
            jsonObject.put("firstName", binding.registerFirstName.getText().toString());
            jsonObject.put("lastName", binding.registerLastName.getText().toString());
            jsonObject.put("birthDate", binding.registerBirthDate.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    try {
                        System.out.println("Registration token: " + response.getString("token"));
                        saveToken(response.getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Snackbar.make(
                        view,
                        "Failed to register!",
                        Snackbar.LENGTH_LONG
                ).show()
        );
        queue.add(request);
    }

    private void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    private void validateField(TextInputEditText field, List<ValidationResult> results, Class<? extends BaseValidator>... validatorClasses) {
        Editable editable = field.getText();
        List<BaseValidator> validators = new ArrayList<>();
        for (Class<? extends BaseValidator> validatorClass : validatorClasses) {
            try {
                assert editable != null;
                validators.add(validatorClass.getConstructor(String.class).newInstance(editable.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ValidationResult result = BaseValidator.validate(validators.toArray(new BaseValidator[0]));
        results.add(result);
        field.setError(result.isSuccess() ? null : getString(result.getMessage()));

    }
}