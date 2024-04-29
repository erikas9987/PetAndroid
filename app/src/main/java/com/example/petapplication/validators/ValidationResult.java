package com.example.petapplication.validators;

public class ValidationResult {
    private boolean isSuccess;
    private int message;

    public ValidationResult(boolean isSuccess, int message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

}
