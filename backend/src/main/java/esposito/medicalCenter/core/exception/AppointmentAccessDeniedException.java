package esposito.medicalCenter.core.exception;

public class AppointmentAccessDeniedException extends RuntimeException {
    public AppointmentAccessDeniedException(String message) {
        super(message);
    }
}
