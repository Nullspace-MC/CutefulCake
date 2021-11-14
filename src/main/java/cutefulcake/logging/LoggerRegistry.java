package cutefulcake.logging;

import cutefulcake.mixin.core.PlayerListHeaderS2CPacketMixin;
import cutefulcake.settings.CutefulCakeSettings;
import cutefulcake.util.MathUtil;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoggerRegistry {
    private static final Map<String, Logger> loggerRegistry = new HashMap<>();

    public static void registerAllLoggers() {
        registerLogger(new Logger("tps"));
    }

    public static void registerLogger(Logger logger) {
        loggerRegistry.put(logger.getName(), logger);
    }

    public static Logger getLoggerFromName(String loggerName) {
        return loggerRegistry.get(loggerName);
    }

    public static Set<String> getAllLoggerNames() {
        return loggerRegistry.keySet();
    }

    public static void tickLoggers() {
        if (CutefulCakeSettings.loggerRefreshRate == 0) return;
        MinecraftServer server = MinecraftServer.getServer();
        if (server.getTicks() % CutefulCakeSettings.loggerRefreshRate == 0) {
            tickTpsHud(server);
        }
    }

    private static void tickTpsHud(MinecraftServer server) {
        double mspt = MathUtil.round(2, MathHelper.average(server.lastTickLengths) * 1.0E-6D);
        double tps = MathUtil.round(2, 1000 / mspt);
        if (tps > 20) tps = 20;
        Text tpsAndMsptMessage = new LiteralText("TPS: " + tps + "\nMSPT: " + mspt);
        if (mspt < 40) tpsAndMsptMessage.setStyle(new Style().setColor(Formatting.GREEN));
        else if (mspt < 50) tpsAndMsptMessage.setStyle(new Style().setColor(Formatting.YELLOW));
        else tpsAndMsptMessage.setStyle(new Style().setColor(Formatting.RED));
        for (ServerPlayerEntity e: server.getPlayerManager().getPlayers()) {
            if (getLoggerFromName("tps").isPlayerSubscribed(e.getName().asString())) {
                PlayerListHeaderS2CPacket packet = new PlayerListHeaderS2CPacket();
                ((PlayerListHeaderS2CPacketMixin)packet).setFooter(tpsAndMsptMessage);
                ((PlayerListHeaderS2CPacketMixin)packet).setHeader(new LiteralText(""));
                e.networkHandler.sendPacket(packet);
            } else {
                PlayerListHeaderS2CPacket packet = new PlayerListHeaderS2CPacket();
                ((PlayerListHeaderS2CPacketMixin)packet).setFooter(new LiteralText(""));
                ((PlayerListHeaderS2CPacketMixin)packet).setHeader(new LiteralText(""));
                e.networkHandler.sendPacket(packet);
            }
        }
    }
}
