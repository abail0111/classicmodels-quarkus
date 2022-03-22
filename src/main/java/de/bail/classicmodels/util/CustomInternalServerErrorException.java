package de.bail.classicmodels.util;

import io.smallrye.graphql.api.ErrorCode;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

/**
 * Custom Internal Server Error Exception
 * Will be accepted by resteasy and graphql
 */
@ErrorCode("500")
public class CustomInternalServerErrorException extends InternalServerErrorException {

    public CustomInternalServerErrorException() {
        super();
    }

    public CustomInternalServerErrorException(String message) {
        super(message);
    }

}
