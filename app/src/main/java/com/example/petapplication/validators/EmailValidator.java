package com.example.petapplication.validators;

import android.util.Patterns;

import com.example.petapplication.R;

public class EmailValidator extends BaseValidator {

    private String input;

    public EmailValidator(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public ValidationResult validate() {
        boolean isValid = Patterns.EMAIL_ADDRESS.matcher(input).matches();
        int textResource = isValid ? R.string.text_validation_success : R.string.text_validation_error_wrong_email;
        return new ValidationResult(isValid, textResource);
    }
}
