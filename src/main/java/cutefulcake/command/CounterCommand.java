package cutefulcake.command;

import cutefulcake.counter.Counter;
import cutefulcake.counter.CounterRegistry;
import cutefulcake.util.MathUtil;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.SyntaxException;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CounterCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "counter";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "counter";
    }

    @Override
    public List<String> getAutoCompleteHints(CommandSource source, String[] args, BlockPos pos) {
        String prefix = args[args.length-1];
        ArrayList<String> autoCompleteHints = new ArrayList<>();
        if (args.length == 1) {
            autoCompleteHints.addAll(CounterRegistry.getCounterColors());
        } else if (args.length == 2) {
            autoCompleteHints.add("reset");
        }
        autoCompleteHints.removeIf(s -> !s.startsWith(prefix));
        return autoCompleteHints;
    }

    @Override
    public void execute(CommandSource source, String[] args) throws CommandException {
        if (!CounterRegistry.getCounterColors().contains(args[0])) throw new SyntaxException();
        if (args.length == 1) {
            source.sendMessage(getFormattedCounterInfo(args[0]));
        } else if (args[1].equals("reset")) {
            CounterRegistry.getCounter(args[0]).resetCounter();
            source.sendMessage(new LiteralText("Reset counter " + args[0]));
        }
    }

    private Text getFormattedCounterInfo(String counterColor) {
        Counter counter = CounterRegistry.getCounter(counterColor);
        Set<String> nameSet = counter.getCounterMap().keySet();
        if (nameSet.isEmpty()) return new LiteralText("The counter " + counterColor.replace('_', ' ') + " has no item");
        double hoursRunning = counter.getRunningTime() / 72000D;
        LiteralText print = new LiteralText("Counter " + counterColor.replace('_', ' ') + " (running for " + getFancyTime(hoursRunning) + ") : " + counter.getTotalCount() + " items | " + MathUtil.round(2, counter.getTotalCount() / hoursRunning) + " items/h\n");
        Iterator<String> ite = nameSet.iterator();
        String itemName = ite.next();
        while (true) {
            print.append(itemName + " : " + counter.getCounterMap().get(itemName) + " | " + MathUtil.round(2, counter.getCounterMap().get(itemName) / hoursRunning) + " items/h");
            if (ite.hasNext()) {
                itemName = ite.next();
                print.append("\n");
            } else {
                break;
            }
        }
        return print;
    }

    private String getFancyTime(double hours) {
        if (hours > 1) return MathUtil.round(2, hours) + "h";
        hours *= 60D;
        if (hours > 1) return MathUtil.round(2, hours) + "m";
        hours *= 60D;
        return MathUtil.round(2, hours) + "s";
    }
}
