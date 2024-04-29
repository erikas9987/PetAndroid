package com.example.petapplication.validators;

import com.example.petapplication.R;

public abstract class BaseValidator implements Validator {

    public static ValidationResult validate(Validator... validators) {
        for (Validator validator : validators) {
            ValidationResult result = validator.validate();
            if (!result.isSuccess())
                return result;
        }
        return new ValidationResult(true, R.string.text_validation_success);
    }

}
