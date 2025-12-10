package com.electronic.store.exceptions;

import lombok.Builder;

@Builder
public class ResourceNoteFoundException extends RuntimeException{

    public ResourceNoteFoundException() {
        super("Resource Not Found !!");
    }

    public ResourceNoteFoundException(String message) {
        super(message);
    }
}
