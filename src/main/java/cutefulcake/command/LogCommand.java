package cutefulcake.command;

import cutefulcake.logging.LoggerRegistry;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
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
    public void execute(CommandSource source, String[] args) {
        LoggerRegistry.getLoggerFromName(args[0]).onLogCommand(source.getEntity().getName().asString());
    }

    @Override
    public List<String> getAutoCompleteHints(CommandSource source, String[] args, BlockPos pos) {
        String prefix = args[args.length - 1];
        ArrayList<String> autoCompleteHints = new ArrayList<>(LoggerRegistry.getAllLoggerNames());
        autoCompleteHints.removeIf(s -> !s.startsWith(prefix));
        return autoCompleteHints;
    }
}
