package cutefulcake.settings;

public abstract class Validator<T> {
    public abstract T validate(T value);
}
