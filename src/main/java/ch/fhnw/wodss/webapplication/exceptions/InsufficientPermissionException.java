package ch.fhnw.wodss.webapplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InsufficientPermissionException extends QuietRuntimeException {

    public InsufficientPermissionException(String message) {
        super(message);
    }
}
