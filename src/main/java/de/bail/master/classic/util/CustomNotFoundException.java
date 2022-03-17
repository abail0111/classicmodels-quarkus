package de.bail.master.classic.util;

import io.smallrye.graphql.api.ErrorCode;
import javax.ws.rs.NotFoundException;

@ErrorCode("404")
public class CustomNotFoundException extends NotFoundException {

    public CustomNotFoundException() {
        super();
    }

    public CustomNotFoundException(String message) {
        super(message);
    }

}
