package net.nullspace_mc.cutefulcake;

import net.nullspace_mc.cutefulcake.counter.CounterRegistry;
import net.nullspace_mc.cutefulcake.logging.LoggerRegistry;
import net.nullspace_mc.cutefulcake.settings.SettingsManager;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CutefulCake implements ModInitializer {
	public static final Logger LOG = LogManager.getLogger("CutefulCake");

	@Override
	public void onInitialize() {
		SettingsManager.parseRules();
		CounterRegistry.setupCounters();
		LoggerRegistry.registerAllLoggers();
	}

	public static void initializeCakeServer() {
		try {
			SettingsManager.setupSettingsManager();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void tickCakeServer() {
		LoggerRegistry.tickLoggers();
	}
}
