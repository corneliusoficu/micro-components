package nl.vu.dynamicplugins.authhandler.models;

public class ValidationResult {
    private boolean isError;
    private String message;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
