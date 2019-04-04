package ch.fhnw.wodss.webapplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidActionException extends QuietRuntimeException {

    public InvalidActionException(String message) {
        super(message);
    }
}
