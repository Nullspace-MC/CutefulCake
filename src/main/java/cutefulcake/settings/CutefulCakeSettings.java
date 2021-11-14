package cutefulcake.settings;

public class CutefulCakeSettings {
    public static final String cakeVersion = "CutefulCake_version_@VERSION@";

    // Put rules here, with annotation @CutefulCakeRule
    // Must be a field of type : String, int, long, float, double or boolean
    // If you do not define a name, it will be defined automatically
    // Description isn't required, but probably better to add one anyway
    // options is an array of options. these options must be String, not of the type of the field
    // strict may be set to true if you want players to be only able to use options defined in options

    @CutefulCakeRule(description = "Disables explosions breaking blocks")
    public static boolean explosionNoBlockDamage = false;

    @CutefulCakeRule(
            description = "Sets the ray size multiplier by this value",
            options = {"-1.0", "0.0", "0.5", "1.0"},
            validator = ExplosionRandomRatioValidator.class
    )
    public static float explosionRandomRatio = -1.0F;

    public static class ExplosionRandomRatioValidator extends Validator<Float> {
        @Override
        public Float validate(Float value) {
            return (value == -1.0F || (value >= 0F && value <= 1F)) ? value : null;
        }
    }

    @CutefulCakeRule(
            description = "Sets at which frequency the loggers' values are recalculated",
            options = {"20", "40", "100"},
            validator = LoggerRefreshRateValidator.class
    )
    public static int loggerRefreshRate = 20;

    public static class LoggerRefreshRateValidator extends Validator<Integer> {
        @Override
        public Integer validate(Integer value) {
            return (value >= 0) ? value : null;
        }
    }

    @CutefulCakeRule(description = "Enables hopper counters")
    public static boolean hopperCounters = false;
}
