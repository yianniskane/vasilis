package com.directory.exceptions;

public class ContactNotFoundException extends IllegalArgumentException {
    public ContactNotFoundException(Long id) {
        super(String.format("Contact with Id %d not found", id));
    }
}
