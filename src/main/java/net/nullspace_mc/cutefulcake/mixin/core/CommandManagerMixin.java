package net.nullspace_mc.cutefulcake.mixin.core;

import net.nullspace_mc.cutefulcake.command.CakeCommand;
import net.nullspace_mc.cutefulcake.command.CounterCommand;
import net.nullspace_mc.cutefulcake.command.LogCommand;
import net.nullspace_mc.cutefulcake.command.PlayerCommand;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManagerMixin extends CommandRegistry {

    @Inject(
            method = "<init>",
            at = @At(value = "TAIL")
    )
    private void addCommands(CallbackInfo ci) {
        this.registerCommand(new CakeCommand());
        this.registerCommand(new LogCommand());
        this.registerCommand(new PlayerCommand());
        this.registerCommand(new CounterCommand());
    }
}
