package de.bail.master.classic.util;

import io.smallrye.graphql.api.ErrorCode;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

@ErrorCode("500")
public class CustomInternalServerErrorException extends InternalServerErrorException {

    public CustomInternalServerErrorException() {
        super();
    }

    public CustomInternalServerErrorException(String message) {
        super(message);
    }

}