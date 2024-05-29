package com.example.petapplication.validators;

import com.example.petapplication.R;

public class NonZeroValidator extends BaseValidator {
    private String input;

    public NonZeroValidator(String input) {
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
        boolean isSuccess = Integer.parseInt(input) > 0;
        int textResource = isSuccess ? R.string.text_validation_success : R.string.text_validation_error_not_zero;
        return new ValidationResult(isSuccess, textResource);
    }
}
