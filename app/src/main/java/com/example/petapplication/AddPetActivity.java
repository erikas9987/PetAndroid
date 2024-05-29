package com.example.petapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petapplication.helpers.Constants;
import com.example.petapplication.model.Gender;
import com.example.petapplication.validators.BaseValidator;
import com.example.petapplication.validators.EmptyValidator;
import com.example.petapplication.validators.ForwardDateValidator;
import com.example.petapplication.validators.NonZeroValidator;
import com.example.petapplication.validators.ValidationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddPetActivity extends AppCompatActivity {

    private com.example.petapplication.databinding.ActivityAddPetBinding binding;

    private SharedPreferences sharedPreferences;
    private RequestQueue volleyQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.petapplication.databinding.ActivityAddPetBinding.inflate(getLayoutInflater());
        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        setContentView(binding.getRoot());

        List<String> speciesValues = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, speciesValues
        );
        binding.speciesInput.setAdapter(arrayAdapter);
        volleyQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = getPetNames(speciesValues);

        volleyQueue.add(request);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Arrays.stream(Gender.values()).map(Enum::toString).collect(Collectors.toList())
        );
        binding.genderInput.setAdapter(genderAdapter);
    }

    @NonNull
    private JsonArrayRequest getPetNames(List<String> speciesValues) {
        String url = Constants.ALL_SPECIES_URL;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            speciesValues.add(obj.getString("name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token", "none");
                if ("none".equals(token)) {
                    throw new AuthFailureError("Missing token");
                }
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        return request;
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

                    binding.addPetBirthDate.setText(displayedDate);
                }, year, month, day);

        datePickerDialog.show();
    }


    public void addUserPet(View view) {
        List<ValidationResult> validations = validateFields();
        if (!validations.stream().allMatch(ValidationResult::isSuccess))
            return;

        int userId = getUserId();
        if (userId == 0)
            return;

        String url = Constants.ADD_PET_URL(userId);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("species", binding.speciesInput.getText().toString());
            jsonObject.put("name", binding.nameInput.getText().toString());
            jsonObject.put("gender", binding.genderInput.getText().toString());
            jsonObject.put("birthDate", binding.addPetBirthDate.getText().toString());
            jsonObject.put("height", binding.heightInput.getText().toString());
            jsonObject.put("weight", binding.weightInput.getText().toString());
        } catch (Exception ignored) {}

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    Snackbar.make(
                            view,
                            "Added successfully",
                            Snackbar.LENGTH_LONG
                    ).show();
                },
                error -> Snackbar.make(
                        view,
                        "Failed to add",
                        Snackbar.LENGTH_LONG
                ).show()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token", "none");
                if ("none".equals(token)) {
                    throw new AuthFailureError("Missing token");
                }
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        volleyQueue.add(request);
    }

    private int getUserId() {
        return sharedPreferences.getInt("id", 0);
    }

    private List<ValidationResult> validateFields() {
        return List.of(
                validateField(binding.nameInput, EmptyValidator.class),
                validateField(binding.speciesInput, EmptyValidator.class),
                validateField(binding.genderInput, EmptyValidator.class),
                validateField(binding.addPetBirthDate, EmptyValidator.class, ForwardDateValidator.class),
                validateField(binding.weightInput, EmptyValidator.class, NonZeroValidator.class),
                validateField(binding.heightInput, EmptyValidator.class, NonZeroValidator.class)
        );
    }

    private ValidationResult validateField(TextView textView, Class<? extends BaseValidator>... validatorClasses) {
        Editable editable;

        if (textView instanceof TextInputEditText editText) {
            editable = editText.getText();
        } else if (textView instanceof AutoCompleteTextView autoComplete) {
            editable = autoComplete.getText();
        } else {
            throw new IllegalArgumentException();
        }

        if (editable == null || editable.toString().isBlank()) {
            textView.setError(getString(R.string.text_validation_error_blank_text));
            return new ValidationResult(false, R.string.text_validation_error_blank_text);
        }

        List<BaseValidator> validators = new ArrayList<>();
        for (Class<? extends BaseValidator> clazz : validatorClasses) {
            try {
                validators.add(clazz.getConstructor(String.class).newInstance(editable.toString()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ValidationResult validationResult = BaseValidator.validate(validators.toArray(new BaseValidator[0]));
        if (!validationResult.isSuccess()) {
            textView.setError(getString(validationResult.getMessage()));
        }
        return validationResult;
    }

}