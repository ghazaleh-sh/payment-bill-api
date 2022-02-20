package ir.co.sadad.paymentBill.controllers;

import ir.co.sadad.paymentBill.exceptions.BillPaymentException;
import ir.co.sadad.paymentBill.exceptions.GlobalErrorResponse;
import ir.co.sadad.paymentBill.exceptions.ServiceUnavailableException;
import ir.co.sadad.paymentBill.exceptions.MyWebClientRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static ir.co.sadad.paymentBill.Constants.ERROR_CODE_TAIL;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class AppErrorAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GlobalErrorResponse> unhandledException(RuntimeException ex) {

        log.warn("Unhandled Exception: ", ex);

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.INTERNAL_SERVER_ERROR.value() + ERROR_CODE_TAIL)
                .setLocalizedMessage(messageSource.getMessage("unhandled.exception", null, new Locale("fa")));

        return new ResponseEntity<>(globalErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BillPaymentException.class)
    public ResponseEntity<GlobalErrorResponse> handleCoreServiceException(BillPaymentException ex) {

        log.warn("Core Service Exception: ", ex);

        if (ex.getGlobalErrorResponse() != null)
            return new ResponseEntity<>(ex.getGlobalErrorResponse(), ex.getHttpStatus());

        String localizedMessage;
        try {
            localizedMessage = messageSource.getMessage(ex.getMessage(), null, new Locale("fa"));
        } catch (NoSuchMessageException e) {
            localizedMessage = ex.getMessage();
        }

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse
                .setStatus(ex.getHttpStatus())
                .setTimestamp(new Date().getTime())
                .setCode("E" + (ex.getCode() == null ? ex.getHttpStatus().value() : ex.getCode()) + ERROR_CODE_TAIL)
                .setLocalizedMessage(localizedMessage);

        return new ResponseEntity<>(globalErrorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<GlobalErrorResponse> handleConnectException(ConnectException ex) {

        log.warn("Connection Timeout Exception: ", ex);

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse
                .setStatus(HttpStatus.REQUEST_TIMEOUT)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.REQUEST_TIMEOUT.value() + ERROR_CODE_TAIL)
                .setLocalizedMessage(messageSource.getMessage("core.service.timeout.exception", null, new Locale("fa")));

        return new ResponseEntity<>(globalErrorResponse, HttpStatus.REQUEST_TIMEOUT);

    }

    @ExceptionHandler(MyWebClientRequestException.class)
    public ResponseEntity<GlobalErrorResponse> handleWebClientRequestException(MyWebClientRequestException ex) {

        log.warn("Connection Timeout Exception: ", ex);

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse
                .setStatus(HttpStatus.REQUEST_TIMEOUT)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.REQUEST_TIMEOUT.value() + ERROR_CODE_TAIL)
                .setLocalizedMessage(messageSource.getMessage("core.shaparak.service.timeout.exception", null, new Locale("fa")));

        return new ResponseEntity<>(globalErrorResponse, HttpStatus.REQUEST_TIMEOUT);

    }

    @ExceptionHandler(JDBCConnectionException.class)
    public ResponseEntity<GlobalErrorResponse> handleJDBCConnectionException(JDBCConnectionException ex) {

        log.warn("JDBC Connection Exception: ", ex);

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.INTERNAL_SERVER_ERROR.value() + ERROR_CODE_TAIL)
                .setLocalizedMessage(messageSource.getMessage("database.connection.exception", null, new Locale("fa")));

        return new ResponseEntity<>(globalErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        log.warn("api calling exception", ex);

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse
                .setStatus(HttpStatus.BAD_REQUEST)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.BAD_REQUEST.value() + ERROR_CODE_TAIL)
                .setLocalizedMessage(messageSource.getMessage("http.message.not.readable.exception", null, new Locale("fa")));

        return new ResponseEntity<>(globalErrorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<GlobalErrorResponse> handleServiceUnavailableException(ServiceUnavailableException ex) {

        log.warn("service unavailable exception", ex);

        String localizedMessage;
        try {
            localizedMessage = messageSource.getMessage(ex.getMessage(), null, new Locale("fa"));
        } catch (NoSuchMessageException e) {
            localizedMessage = ex.getMessage();
        }

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse
                .setStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.SERVICE_UNAVAILABLE.value() + ERROR_CODE_TAIL)
                .setLocalizedMessage(localizedMessage);

        return new ResponseEntity<>(globalErrorResponse, HttpStatus.SERVICE_UNAVAILABLE);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        log.warn("validation exception", ex);

        List<GlobalErrorResponse.SubError> subErrorList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            GlobalErrorResponse.SubError subError = new GlobalErrorResponse.SubError();
            subError.setCode("E" + HttpStatus.BAD_REQUEST.value() + ERROR_CODE_TAIL);
            subError.setTimestamp(new Date().getTime());
            subError.setLocalizedMessage(error.getDefaultMessage());
            subErrorList.add(subError);
        });

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse
                .setStatus(HttpStatus.BAD_REQUEST)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.BAD_REQUEST.value() + ERROR_CODE_TAIL)
                .setLocalizedMessage(messageSource.getMessage("method.argument.not.valid", null, new Locale("fa")))
                .setSubErrors(subErrorList);
        return new ResponseEntity<>(globalErrorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GlobalErrorResponse> handleConstraintValidation(ConstraintViolationException ex) {

        log.warn("constraint validation exception", ex);

        List<GlobalErrorResponse.SubError> subErrorList = new ArrayList<>();
        ex.getConstraintViolations().forEach((error) -> {
            GlobalErrorResponse.SubError subError = new GlobalErrorResponse.SubError();
            subError.setCode("E" + HttpStatus.BAD_REQUEST.value() + ERROR_CODE_TAIL);
            subError.setTimestamp(new Date().getTime());
            subError.setLocalizedMessage(error.getMessage());
            subErrorList.add(subError);
        });

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse
                .setStatus(HttpStatus.BAD_REQUEST)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.BAD_REQUEST.value() + ERROR_CODE_TAIL)
                .setLocalizedMessage(messageSource.getMessage("method.argument.not.valid", null, new Locale("fa")))
                .setSubErrors(subErrorList);
        return new ResponseEntity<>(globalErrorResponse, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<GlobalErrorResponse> handleMissingRequestHeaderExceptions(MissingRequestHeaderException ex) {

        log.warn("missing request header exception", ex);

        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse
                .setStatus(HttpStatus.BAD_REQUEST)
                .setTimestamp(new Date().getTime())
                .setCode("E" + HttpStatus.BAD_REQUEST.value() + ERROR_CODE_TAIL)
                .setLocalizedMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(globalErrorResponse, HttpStatus.BAD_REQUEST);

    }

}
