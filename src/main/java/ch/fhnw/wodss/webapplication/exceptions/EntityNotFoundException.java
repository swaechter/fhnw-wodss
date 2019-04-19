package ch.fhnw.wodss.webapplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;
import java.util.function.Supplier;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends QuietRuntimeException {

    public EntityNotFoundException(String name, UUID id) {
        super("The entity " + name + " with the ID " + id.toString() + " was not found");
    }

    public static String message(String name, UUID id)
    {
        return "The entity " + name + " with the ID " + id.toString() + " was" +
               " not found";
    }
}
