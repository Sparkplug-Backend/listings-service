package com.sparkplug.listings.domain.exception;

import com.sparkplug.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidListingException extends ApplicationException {
    public InvalidListingException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}