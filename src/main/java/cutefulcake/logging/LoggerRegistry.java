package cutefulcake.logging;

import cutefulcake.counter.Counter;
import cutefulcake.counter.CounterRegistry;
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

import java.util.*;

public class LoggerRegistry {
    private static final Map<String, Logger> loggerRegistry = new HashMap<>();

    public static void registerAllLoggers() {
        registerLogger(new Logger("tps"));
        registerLogger(new Logger("counter", CounterRegistry.getCounterColors(), true));
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
            updateHudLoggers(server);
        }
    }

    private static void updateHudLoggers(MinecraftServer server) {
        for (ServerPlayerEntity player: server.getPlayerManager().getPlayers()) {
            PlayerListHeaderS2CPacket packet = new PlayerListHeaderS2CPacket();
            Text header = new LiteralText("");
            Text footer = new LiteralText("");
            ArrayList<Text> headersArray = new ArrayList<>();
            ArrayList<Text> footersArray = new ArrayList<>();
            footersArray.add(tickTpsHud(server, player));
            footersArray.add(tickCounterHud(player));
            appendArrayList(header, headersArray);
            appendArrayList(footer, footersArray);
            ((PlayerListHeaderS2CPacketMixin)packet).setHeader(header);
            ((PlayerListHeaderS2CPacketMixin)packet).setFooter(footer);
            player.networkHandler.sendPacket(packet);
        }
    }

    private static void appendArrayList(Text text, ArrayList<Text> array) {
        if (array.isEmpty()) return;
        Iterator<Text> ite = array.iterator();
        Text textToAppend = ite.next();
        while (true) {
            if (!textToAppend.asString().equals("")) text.append(textToAppend);
            if (ite.hasNext()) {
                boolean isFirstTextEmpty = textToAppend.asString().equals("");
                textToAppend = ite.next();
                if (!textToAppend.asString().equals("") && !isFirstTextEmpty) text.append("\n");
            } else {
                break;
            }
        }
    }

    private static Text tickTpsHud(MinecraftServer server, ServerPlayerEntity player) {
        double mspt = MathUtil.round(2, MathHelper.average(server.lastTickLengths) * 1.0E-6D);
        double tps = MathUtil.round(2, 1000 / mspt);
        if (tps > 20) tps = 20;
        Text tpsAndMsptMessage = new LiteralText("TPS: " + tps + " MSPT: " + mspt);
        if (mspt < 40) tpsAndMsptMessage.setStyle(new Style().setColor(Formatting.GREEN));
        else if (mspt < 50) tpsAndMsptMessage.setStyle(new Style().setColor(Formatting.YELLOW));
        else tpsAndMsptMessage.setStyle(new Style().setColor(Formatting.RED));
        if (getLoggerFromName("tps").isPlayerSubscribed(player.getName().asString())) {
            return tpsAndMsptMessage;
        }
        return new LiteralText("");
    }

    private static Text tickCounterHud(ServerPlayerEntity player) {
        Logger logger = getLoggerFromName("counter");
        if (logger.isPlayerSubscribed(player.getName().asString())) {
            Counter counter = CounterRegistry.getCounter(logger.getChannelSubscriptions().get(player.getName().asString()));
            if (counter.getCounterMap().keySet().isEmpty()) return new LiteralText("The counter " + logger.getChannelSubscriptions().get(player.getName().asString()) + " has no item");
            double hoursRunning = counter.getRunningTime() / 72000D;
            return new LiteralText("Counter " + logger.getChannelSubscriptions().get(player.getName().asString()) + ": Running for " + MathUtil.getFancyTime(hoursRunning) + "\n" +
                    MathUtil.round(2, counter.getTotalCount() / hoursRunning) + " items/h").
                    setStyle(new Style().setColor(Formatting.WHITE));
        }
        return new LiteralText("");
    }
}
