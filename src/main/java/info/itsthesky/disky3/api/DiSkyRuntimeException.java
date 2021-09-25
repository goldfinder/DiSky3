package info.itsthesky.disky3.api;

public class DiSkyRuntimeException extends RuntimeException {

    private final String message;
    private final Throwable throwable;
    public DiSkyRuntimeException(Throwable throwable) {
        this.throwable = throwable;
        this.message = throwable.getMessage();
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
