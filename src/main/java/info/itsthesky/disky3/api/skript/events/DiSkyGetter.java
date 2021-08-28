package info.itsthesky.disky3.api.skript.events;

public interface DiSkyGetter<F, T> {
    F get(T arg);
}