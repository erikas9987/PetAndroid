package com.example.petapplication.validators;

import com.example.petapplication.R;

public class EmptyValidator extends BaseValidator{

    private String input;

    public EmptyValidator(String input) {
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
        boolean isValid = !input.isBlank();
        int textResource = isValid ? R.string.text_validation_success : R.string.text_validation_error_blank_text;
        return new ValidationResult(isValid, textResource);
    }
}
