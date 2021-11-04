package cutefulcake.settings;

public class CutefulCakeSettings {
    public static final String cakeVersion = "CutefulCake_version_1.0.0";
    private static CutefulCakeSettings instance;

    private CutefulCakeSettings() {}

    // Put rules here, with annotation @CutefulCakeRule
    // If you do not define a name, it will be defined automatically
    // Description isn't required, but probably better to add one anyway
    // options is an array of options. these options must be String, not of the type of the field
    // strict may be set to true if you want players to be only able to use options defined in options

    public static CutefulCakeSettings getInstance() {
        if (instance == null) {
            instance = new CutefulCakeSettings();
        }
        return instance;
    }
}
