package de.bail.master.classic.util;

import io.smallrye.graphql.api.ErrorCode;
import javax.ws.rs.NotFoundException;

/**
 * Custom Not Found Error Exception
 * Will be accepted by resteasy and graphql
 */
@ErrorCode("404")
public class CustomNotFoundException extends NotFoundException {

    public CustomNotFoundException() {
        super();
    }

    public CustomNotFoundException(String message) {
        super(message);
    }

}
