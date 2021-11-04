package cutefulcake.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 Any field annotated with this class is considered a cake rule
 Said field should be either a String, int, long, boolean, float or double
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface CutefulCakeRule {

    /** Name of the rule, keep as default if you want rule to have the name of field */
    String name() default "";

    /** Description of the rule */
    String description() default "";

    /** Options that will be recommended to player on autocomplete, and will be shown on feature's page */
    String[] options() default {};

    /** Set to true in case you want rule to be only one of the options given */
    boolean strict() default false;
}
