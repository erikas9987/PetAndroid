package com.example.petapplication.validators;

import com.example.petapplication.R;

import java.time.LocalDate;

public class BirthDateValidator extends BaseValidator {

    private LocalDate input;
    private final int minimumAge = 14;

    public BirthDateValidator(String input) {
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
        LocalDate yearsAgo = today.minusYears(this.minimumAge);
        boolean isValid = !yearsAgo.isBefore(this.input);
        int textResource = isValid ? R.string.text_validation_success : R.string.text_validation_error_birth_date;
        return new ValidationResult(isValid, textResource);
    }
}
