package net.nullspace_mc.cutefulcake.command;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.SyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.nullspace_mc.cutefulcake.CutefulCake;
import net.nullspace_mc.cutefulcake.util.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TickCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "tick";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "tick";
    }

    @Override
    public List<String> getAutoCompleteHints(CommandSource source, String[] args, BlockPos pos) {
        ArrayList<String> autoCompleteHints = new ArrayList<>();
        String prefix = args[args.length - 1];
        if (args.length == 1) autoCompleteHints.add("warp");
        else if (args[0].equals("warp")) autoCompleteHints.addAll(Arrays.asList("3600", "72000", "end"));
        autoCompleteHints.removeIf(s -> !s.startsWith(prefix));
        return autoCompleteHints;
    }

    @Override
    public void execute(CommandSource source, String[] args) throws CommandException {
        if (args[0].equals("warp")) {
            if (args.length != 2) throw new SyntaxException();
            if (args[1].equals("end")) {
                CutefulCake.isTickWarping = false;
                MessageUtil.sendToAllOperators(new LiteralText("Ended tick warp"));
            }
            else {
                try {
                    CutefulCake.tickWarpEnd = Integer.parseInt(args[1]) + MinecraftServer.getServer().getTicks();
                    CutefulCake.isTickWarping = true;
                    LiteralText text = new LiteralText("Starting tick warp ...");
                    MessageUtil.sendToAllOperators(text);
                } catch (NumberFormatException ignored) {
                    throw new SyntaxException();
                }
            }
        }
    }
}
