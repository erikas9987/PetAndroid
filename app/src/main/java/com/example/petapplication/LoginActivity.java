package com.example.petapplication;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petapplication.databinding.ActivityLoginBinding;
import com.example.petapplication.validators.BaseValidator;
import com.example.petapplication.validators.EmailValidator;
import com.example.petapplication.validators.EmptyValidator;
import com.example.petapplication.validators.PasswordValidator;
import com.example.petapplication.validators.ValidationResult;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void retrieveToken(View view) {
        Editable emailField = binding.loginEmailAddress.getText();
        if (emailField == null) {
            binding.loginEmailAddress.setError(getString(R.string.text_validation_error_blank_text));
        } else {
            String emailText = emailField.toString();
            ValidationResult result = BaseValidator.validate(
                    new EmptyValidator(emailText),
                    new EmailValidator(emailText)
            );
            binding.loginEmailAddress.setError(
                    result.isSuccess() ? null : getString(result.getMessage())
            );
        }
        Editable passwordField = binding.loginPassword.getText();
        if (passwordField == null) {
            binding.loginPassword.setError(getString(R.string.text_validation_error_blank_text));
        } else {
            String passwordText = passwordField.toString();
            ValidationResult result = BaseValidator.validate(
                    new EmptyValidator(passwordText),
                    new PasswordValidator(passwordText)
            );
            binding.loginPassword.setError(
                    result.isSuccess() ? null : getString(result.getMessage())
            );
        }

    }


}
