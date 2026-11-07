package esposito.medicalCenter.core.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseErrorDTO> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        log.error("Resource not found: {}", ex.getMessage());

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ResponseErrorDTO> handleExpiredTokenException(ExpiredTokenException ex, HttpServletRequest request) {
        log.error("Expired token: {}", ex.getMessage());

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Expired Token",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FailedAttemptsException.class)
    public ResponseEntity<ResponseErrorDTO> handleFailedAttemptsException(FailedAttemptsException ex, HttpServletRequest request) {
        log.error("Failed more than 3 attempts: {}", ex.getMessage());

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Too Many Failed Attempts",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(GenericReportDownloadException.class)
    public ResponseEntity<ResponseErrorDTO> handleGenericReportDownloadException(GenericReportDownloadException ex, HttpServletRequest request) {
        log.error("Failed to download report: {}", ex.getMessage());

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Download Failed",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReportFileNotFoundException.class)
    public ResponseEntity<ResponseErrorDTO> handleFailedAttemptsException(ReportFileNotFoundException ex, HttpServletRequest request) {
        log.error("Reports File Not Found: {}", ex.getMessage());

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Reports File Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ResponseErrorDTO> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex, HttpServletRequest request) {
        log.error("Email already in use: {}", ex.getMessage());

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Email Already In Use",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidStaffDataException.class)
    public ResponseEntity<ResponseErrorDTO> handleInvalidStaffDataException(InvalidStaffDataException ex, HttpServletRequest request) {
        log.error("Invalid staff data: {}", ex.getMessage());

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Staff Data",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppointmentAccessDeniedException.class)
    public ResponseEntity<ResponseErrorDTO> handleAppointmentAccessDeniedException(AppointmentAccessDeniedException ex, HttpServletRequest request) {
        log.error("Appointment access denied: {}", ex.getMessage());

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Appointment Access Denied",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidAppointmentStatusException.class)
    public ResponseEntity<ResponseErrorDTO> handleInvalidAppointmentStatusException(InvalidAppointmentStatusException ex, HttpServletRequest request) {
        log.error("Invalid appointment status transition: {}", ex.getMessage());

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Invalid Appointment Status",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ResponseErrorDTO> handleNoResourceFound(Exception ex, HttpServletRequest request) {
        log.warn("No route matched: {}", request.getRequestURI());

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "No endpoint found for this path",
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseErrorDTO> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("Method not supported: {} {}", ex.getMethod(), request.getRequestURI());

        String[] supportedMethods = ex.getSupportedMethods();
        String supported = supportedMethods != null ? String.join(", ", supportedMethods) : "none";

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                "'" + ex.getMethod() + "' is not supported on this path. Supported methods: " + supported,
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErrorDTO> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred: ", ex);

        ResponseErrorDTO error = new ResponseErrorDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred on our side",
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
