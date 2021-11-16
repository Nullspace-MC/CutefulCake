package net.nullspace_mc.cutefulcake.command;

import com.mojang.authlib.GameProfile;
import net.nullspace_mc.cutefulcake.players.FakeServerPlayerEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.SyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySetHeadYawS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "player";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "player";
    }

    @Override
    public List<String> getAutoCompleteHints(CommandSource source, String[] args, BlockPos pos) {
        ArrayList<String> autoCompleteHints = new ArrayList<>();
        String prefix = args[args.length-1];
        if (args.length == 1) {
            autoCompleteHints.addAll(Arrays.asList("Alex", "Steve"));
            autoCompleteHints.addAll(Arrays.asList(MinecraftServer.getServer().getPlayerManager().getPlayerNames()));
        } else if (args.length == 2) {
            autoCompleteHints.addAll(Arrays.asList("spawn", "kill"));
        }
        autoCompleteHints.removeIf(s -> !s.startsWith(prefix));
        return autoCompleteHints;
    }

    @Override
    public void execute(CommandSource source, String[] args) throws CommandException {
        if (args.length == 1 && args[0].length() == 0) throw new SyntaxException();
        else switch(args[1]) {
            case "spawn":
                spawnPlayer(args[0], source);
                break;
            case "kill":
                killPlayer(args[0]);
        }
    }

    private void killPlayer(String playerName) throws SyntaxException {
        MinecraftServer server = MinecraftServer.getServer();
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerName);
        if (!(player instanceof FakeServerPlayerEntity)) throw new SyntaxException();
        server.getPlayerManager().remove(player);
    }

    private void spawnPlayer(String playerName, CommandSource source) throws SyntaxException {
        MinecraftServer server = MinecraftServer.getServer();
        if (server.getPlayerManager().getPlayer(playerName) != null) throw new SyntaxException();
        int dimensionId = source.getWorld().dimension.getType();
        ServerWorld worldIn = server.getWorld(dimensionId);
        UserCache cache = server.getUserCache();
        GameProfile profile = cache.findByName(playerName);
        if (profile == null) {
            profile = new GameProfile(PlayerEntity.getOfflinePlayerUuid(playerName), playerName);
        }
        if (!profile.getProperties().containsKey("texture")) {
            profile = SkullBlockEntity.method_1188(profile);
        }
        FakeServerPlayerEntity player = new FakeServerPlayerEntity(server, worldIn, profile, new ServerPlayerInteractionManager(worldIn));
        server.getPlayerManager().onPlayerConnect(new ClientConnection(NetworkSide.SERVERBOUND), player);
        Vec3d pos = source.getPos();
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;
        float yaw = -180.0F;
        float pitch = 0.0F;
        if (source.getEntity() instanceof PlayerEntity) {
            yaw = source.getEntity().yaw;
            pitch = source.getEntity().pitch;
        }
        player.networkHandler.requestTeleport(x, y, z, yaw, pitch);
        player.setHealth(20F);
        player.abilities.flying = !server.getDefaultGameMode().isSurvivalLike();
        player.stepHeight = 0.6F;
        player.interactionManager.setGameMode(server.getDefaultGameMode());
        server.getPlayerManager().sendToDimension(new EntitySetHeadYawS2CPacket(player, (byte) (player.headYaw * 256 / 360)), dimensionId);
        server.getPlayerManager().sendToDimension(new EntityPositionS2CPacket(player), dimensionId);
        server.getPlayerManager().method_6224(player);
    }
}
