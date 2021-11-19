package net.nullspace_mc.cutefulcake.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class MessageUtil {
    public static void sendToAllOperators(Text text) {
        PlayerManager p = MinecraftServer.getServer().getPlayerManager();
        for (ServerPlayerEntity e : p.getPlayers())
            if (p.isOperator(e.getGameProfile()))
                e.sendMessage(text);
    }
}
