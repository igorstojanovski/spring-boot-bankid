package co.igorski.services;

class ApplicationSecurityException extends RuntimeException {
    ApplicationSecurityException(String message, Exception e) {
        super(message, e);
    }
}
