package com.example.petapplication.validators;

import com.example.petapplication.R;

import java.time.LocalDate;

public class ForwardDateValidator extends BaseValidator {

    private LocalDate input;

    public ForwardDateValidator(String input) {
        this.input = LocalDate.parse(input);
    }

    public LocalDate getInput() {
        return input;
    }

    public void setInput(LocalDate input) {
        this.input = input;
    }

    @Override
    public ValidationResult validate() {
        LocalDate today = LocalDate.now();
        boolean isValid = !today.isBefore(this.input);
        int textResource = isValid ? R.string.text_validation_success : R.string.text_validation_error_forward_date;
        return new ValidationResult(isValid, textResource);
    }
}
