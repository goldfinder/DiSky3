package info.itsthesky.disky3.api;

public class DiSkyException extends Exception {

    public DiSkyException(String message) {
        super(message);
    }

    public DiSkyException(String... messages) {
        this(String.join("\n", messages));
    }

}
