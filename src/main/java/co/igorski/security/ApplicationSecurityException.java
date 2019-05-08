package co.igorski.security;

public class ApplicationSecurityException extends RuntimeException {
    public ApplicationSecurityException(String message, Exception e) {
        super(message, e);
    }
}
