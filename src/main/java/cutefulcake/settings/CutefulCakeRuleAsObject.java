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
        CutefulCakeSettings settings = CutefulCakeSettings.getInstance();
        if (strict) {
            if (!ArrayUtils.contains(options, s)) {
                return false;
            }
        }
        try {
            switch (field.getType().getName()) {
                case "String":
                    field.set(settings, s);
                    break;
                case "int":
                    field.setInt(settings, Integer.parseInt(s));
                    break;
                case "long":
                    field.setLong(settings, Long.parseLong(s));
                    break;
                case "float":
                    field.setFloat(settings, Float.parseFloat(s));
                    break;
                case "double":
                    field.setDouble(settings, Double.parseDouble(s));
                    break;
                case "boolean":
                    field.setBoolean(settings, Boolean.parseBoolean(s));
                    break;
            }
        } catch (IllegalAccessException ignored) {
            return false;
        }
        return true;
    }
}
