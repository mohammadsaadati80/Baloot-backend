package org.mm.Baloot.Exceptions;

public class ProviderNotFoundError extends Exception {
    public String getMessage() {
        return "Provider not found";
    }
}
