package net.nullspace_mc.cutefulcake.settings;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class CutefulCakeRuleAsObject<T> {
    public String name;
    public String description;
    public String[] options;
    public boolean strict;
    public Field field;
    public String defaultValue;
    public ArrayList<Validator<T>> validators = new ArrayList<>();

    public CutefulCakeRuleAsObject (CutefulCakeRule r, Field field) {
        name = r.name();
        if (r.name().equals("")) {
            name = field.getName();
        }
        description = r.description();
        options = r.options();
        strict = r.strict();
        this.field = field;
        try {
            defaultValue = valueOf(field.get(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        for (Class<? extends Validator> v : r.validator()) this.validators.add((Validator<T>) callConstructor(v));
    }

    private Validator<?> callConstructor(Class<? extends Validator> classToConstruct) {
        try {
            Constructor<? extends Validator> constructor = classToConstruct.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setValue(String value) {
        if (strict) {
            if (!ArrayUtils.contains(options, value)) {
                return false;
            }
        }
        try {
            switch (field.getType().getName()) {
                case "String":
                    for (Validator<T> validator : validators) if (validator.validate((T) value) == null) return false;
                    field.set(null, value);
                    break;
                case "int":
                    for (Validator<T> validator : validators) if (validator.validate((T) (Object) Integer.parseInt(value)) == null) return false;
                    field.setInt(null, Integer.parseInt(value));
                    break;
                case "long":
                    for (Validator<T> validator : validators) if (validator.validate((T) (Object) Long.parseLong(value)) == null) return false;
                    field.setLong(null, Long.parseLong(value));
                    break;
                case "float":
                    for (Validator<T> validator : validators) if (validator.validate((T) (Object) Float.parseFloat(value)) == null) return false;
                    field.setFloat(null, Float.parseFloat(value));
                    break;
                case "double":
                    for (Validator<T> validator : validators) if (validator.validate((T) (Object) Double.parseDouble(value)) == null) return false;
                    field.setDouble(null, Double.parseDouble(value));
                    break;
                case "boolean":
                    for (Validator<T> validator : validators) if (validator.validate((T) (Object) Boolean.parseBoolean(value)) == null) return false;
                    field.setBoolean(null, Boolean.parseBoolean(value));
                    break;
            }
        } catch (IllegalAccessException ignored) {
            return false;
        }
        return true;
    }
}
