package net.nullspace_mc.cutefulcake;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.nullspace_mc.cutefulcake.counter.CounterRegistry;
import net.nullspace_mc.cutefulcake.logging.LoggerRegistry;
import net.nullspace_mc.cutefulcake.settings.SettingsManager;
import net.nullspace_mc.cutefulcake.util.MessageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CutefulCake implements ModInitializer {
	public static final Logger LOG = LogManager.getLogger("CutefulCake");
	public static boolean isTickWarping = false;
	public static int tickWarpEnd;

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
		if (isTickWarping && MinecraftServer.getServer().getTicks() > tickWarpEnd) {
			isTickWarping = false;
			LiteralText message = new LiteralText("Tick warp completed");
			MessageUtil.sendToAllOperators(message);
		}
	}
}
