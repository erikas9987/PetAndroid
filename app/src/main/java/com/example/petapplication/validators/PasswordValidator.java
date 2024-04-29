package com.example.petapplication.validators;

import com.example.petapplication.R;

public class PasswordValidator extends BaseValidator {

    private String input;

    private final int minPasswordLength = 8;

    public PasswordValidator(String input) {
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
        boolean isValid = input.length() >= minPasswordLength;
        int textResource = isValid ? R.string.text_validation_success : R.string.text_validation_error_password_too_short;
        return new ValidationResult(isValid, textResource);
    }
}
