package net.nullspace_mc.cutefulcake.settings;

public abstract class Validator<T> {
    public abstract T validate(T value);
}
