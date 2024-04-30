package com.example.petapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petapplication.databinding.ActivityLoginBinding;
import com.example.petapplication.helpers.Constants;
import com.example.petapplication.model.Login;
import com.example.petapplication.validators.BaseValidator;
import com.example.petapplication.validators.EmailValidator;
import com.example.petapplication.validators.EmptyValidator;
import com.example.petapplication.validators.PasswordValidator;
import com.example.petapplication.validators.ValidationResult;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        setContentView(binding.getRoot());
    }

    public void retrieveToken(View view) {

        Editable emailField = binding.loginEmailAddress.getText();
        if (emailField == null) {
            binding.loginEmailAddress.setError(getString(R.string.text_validation_error_blank_text));
        }
        Editable passwordField = binding.loginPassword.getText();
        if (passwordField == null) {
            binding.loginPassword.setError(getString(R.string.text_validation_error_blank_text));
        }
        if (emailField == null || passwordField == null)
            return;
        String emailText = emailField.toString();
        ValidationResult emailResult = BaseValidator.validate(
                new EmptyValidator(emailText),
                new EmailValidator(emailText)
        );
        binding.loginEmailAddress.setError(
                emailResult.isSuccess() ? null : getString(emailResult.getMessage())
        );
        String passwordText = passwordField.toString();
        ValidationResult passwordResult = BaseValidator.validate(
                new EmptyValidator(passwordText),
                new PasswordValidator(passwordText)
        );
        binding.loginPassword.setError(
                passwordResult.isSuccess() ? null : getString(passwordResult.getMessage())
        );
        if (!(emailResult.isSuccess() && passwordResult.isSuccess()))
            return;

        Login login = new Login();
        login.setEmail(emailText);
        login.setPassword(passwordText);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.LOGIN_URL;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", emailText);
            jsonObject.put("password", passwordText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    try {
                        saveToken(response.getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Snackbar.make(
                        view,
                        "Failed to log in!",
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


}
