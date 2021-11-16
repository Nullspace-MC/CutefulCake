package net.nullspace_mc.cutefulcake.command;

import net.nullspace_mc.cutefulcake.logging.Logger;
import net.nullspace_mc.cutefulcake.logging.LoggerRegistry;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.SyntaxException;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class LogCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "log";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "log";
    }

    @Override
    public void execute(CommandSource source, String[] args) throws SyntaxException {
        if (!LoggerRegistry.getAllLoggerNames().contains(args[0]))
            throw new SyntaxException();
        Logger logger = LoggerRegistry.getLoggerFromName(args[0]);
        String sourceName = source.getEntity().getName().asString();
        if (!logger.isPlayerSubscribed(sourceName) && !logger.getOptions().isEmpty() && (args.length == 1 || !logger.getOptions().contains(args[1])) && logger.getMustBeInAChannel()) throw new SyntaxException();
        if (args.length > 2) throw new SyntaxException();

        if (args.length == 1) logger.onLogCommand(sourceName);
        else logger.onLogCommand(sourceName, args[1]);
    }

    @Override
    public List<String> getAutoCompleteHints(CommandSource source, String[] args, BlockPos pos) {
        String prefix = args[args.length - 1];
        ArrayList<String> autoCompleteHints = new ArrayList<>();
        if (args.length == 1) autoCompleteHints.addAll(LoggerRegistry.getAllLoggerNames());
        if (args.length == 2) autoCompleteHints.addAll(LoggerRegistry.getLoggerFromName(args[0]).getOptions());
        autoCompleteHints.removeIf(s -> !s.startsWith(prefix));
        return autoCompleteHints;
    }
}
