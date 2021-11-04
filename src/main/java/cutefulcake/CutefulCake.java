package cutefulcake;

import cutefulcake.settings.SettingsManager;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CutefulCake implements ModInitializer {
	public static final Logger LOG = LogManager.getLogger("CutefulCake");

	@Override
	public void onInitialize() {
		SettingsManager.parseRules();
	}
}
