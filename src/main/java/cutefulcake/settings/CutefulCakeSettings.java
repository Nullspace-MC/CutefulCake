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
            options = {"-1.0", "0.0", "0.5", "1.0"})
    public static float explosionRandomRatio = -1.0F;

    @CutefulCakeRule(
            description = "Sets at which frequency the loggers' values are recalculated",
            options = {"20", "40", "100"}
    )
    public static int loggerRefreshRate = 20;
}
