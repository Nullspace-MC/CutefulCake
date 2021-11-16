package net.nullspace_mc.cutefulcake.players;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;

public class FakeServerPlayerEntity extends ServerPlayerEntity {
    public FakeServerPlayerEntity(MinecraftServer server, ServerWorld world, GameProfile profile, ServerPlayerInteractionManager interactionManager) {
        super(server, world, profile, interactionManager);
    }

    @Override
    public void onKilled(DamageSource source) {
        super.onKilled(source);
        server.getPlayerManager().remove(this);
    }
}
