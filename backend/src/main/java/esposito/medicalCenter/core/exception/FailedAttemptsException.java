package esposito.medicalCenter.core.exception;

public class FailedAttemptsException extends RuntimeException {
    public FailedAttemptsException(String message) {
        super(message);
    }
}
