package cutefulcake.settings;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;

public class CutefulCakeRuleAsObject {
    public String name;
    public String description;
    public String[] options;
    public boolean strict;
    public Field field;

    public CutefulCakeRuleAsObject (CutefulCakeRule r, Field field) {
        name = r.name();
        if (r.name().equals("")) {
            name = field.getName();
        }
        description = r.description();
        options = r.options();
        strict = r.strict();
        this.field = field;
    }

    public boolean setValue(String s) {
        if (strict) {
            if (!ArrayUtils.contains(options, s)) {
                return false;
            }
        }
        try {
            switch (field.getType().getName()) {
                case "String":
                    field.set(null, s);
                    break;
                case "int":
                    field.setInt(null, Integer.parseInt(s));
                    break;
                case "long":
                    field.setLong(null, Long.parseLong(s));
                    break;
                case "float":
                    field.setFloat(null, Float.parseFloat(s));
                    break;
                case "double":
                    field.setDouble(null, Double.parseDouble(s));
                    break;
                case "boolean":
                    field.setBoolean(null, Boolean.parseBoolean(s));
                    break;
            }
        } catch (IllegalAccessException ignored) {
            return false;
        }
        return true;
    }
}
