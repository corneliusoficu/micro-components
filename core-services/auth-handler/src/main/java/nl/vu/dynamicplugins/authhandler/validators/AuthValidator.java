package nl.vu.dynamicplugins.authhandler.validators;

import nl.vu.dynamicplugins.authhandler.models.LoginRequest;
import nl.vu.dynamicplugins.authhandler.models.ValidationResult;

public class AuthValidator {
    public ValidationResult validateLoginRequest(LoginRequest loginRequest) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setError(false);
        validationResult.setMessage(null);
        return validationResult;
    }
}
