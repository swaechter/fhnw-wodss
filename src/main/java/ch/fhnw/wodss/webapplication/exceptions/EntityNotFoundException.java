package ch.fhnw.wodss.webapplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends QuietRuntimeException {

    public EntityNotFoundException(String name, Long id) {
        super("The entity " + name + " with the ID " + id + " was not found");
    }

    public EntityNotFoundException(String name, String key) {
        super("The entity " + name + " with the key " + key + " was not found");
    }
}
