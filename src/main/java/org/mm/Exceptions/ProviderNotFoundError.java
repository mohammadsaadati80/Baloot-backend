package org.mm.Exceptions;

public class ProviderNotFoundError extends Exception {
    public String getMessage() {
        return "Provider not found";
    }
}
