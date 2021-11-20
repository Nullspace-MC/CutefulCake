package net.nullspace_mc.cutefulcake.command;

import net.nullspace_mc.cutefulcake.settings.CutefulCakeRuleAsObject;
import net.nullspace_mc.cutefulcake.settings.CutefulCakeSettings;
import net.nullspace_mc.cutefulcake.settings.SettingsManager;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.SyntaxException;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.nullspace_mc.cutefulcake.util.MessageUtil;

import java.util.*;

public class CakeCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "cake";
    }

    @Override
    public int getPermissionLevel() {
        return 2;
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "cake";
    }

    @Override
    public List<String> getAutoCompleteHints(CommandSource source, String[] args, BlockPos pos) {
        List<String> autoCompleteHints = new ArrayList<>();
        String prefix = args[args.length - 1];
        if (args.length == 1) { // cake
            autoCompleteHints.add("setDefault");
            autoCompleteHints.add("reset");
            for (CutefulCakeRuleAsObject rule : SettingsManager.rules.values()) {
                autoCompleteHints.add(rule.name);
            }
        } else if (args.length == 2 && !(args[0].equals("setDefault") || args[0].equals("reset"))) { // cake [rule]
            if (SettingsManager.rules.containsKey(args[0])) {
                Collections.addAll(autoCompleteHints, SettingsManager.rules.get(args[0]).options);
            }
        } else if (args.length == 2) { // cake setDefault\reset
            for (CutefulCakeRuleAsObject rule : SettingsManager.rules.values()) {
                autoCompleteHints.add(rule.name);
            }
        } else if (args.length == 3 && !args[1].equals("reset")) { // cake setDefault [rule]
            if (SettingsManager.rules.containsKey(args[1])) {
                Collections.addAll(autoCompleteHints, SettingsManager.rules.get(args[1]).options);
            }
        }
        autoCompleteHints.removeIf(s -> !s.startsWith(prefix));
        return autoCompleteHints;
    }

    @Override
    public void execute(CommandSource source, String[] args) throws CommandException {
        if (args.length == 0) { // cake
            source.sendMessage(listAllCakeFeatures());
        } else {
            if (args[0].equals("setDefault")) {
                if (args.length < 3) { // cake setDefault OR / cake setDefault [rule]
                    throw new SyntaxException();
                }
                CutefulCakeRuleAsObject rule = SettingsManager.rules.get(args[1]);
                if (args.length == 3) { // cake setDefault [rule] [value]
                    if (saveToFile(rule, args[2])) {
                        MessageUtil.sendToAllOperators(new LiteralText("Set the default value of " + args[1] + " to " + args[2]));
                    } else {
                        throw new SyntaxException();
                    }
                }
            } else if (args[0].equals("reset")) {
                if (args.length == 1) { // cake reset
                    for (CutefulCakeRuleAsObject rule : SettingsManager.rules.values()) {
                        if (!removeFromFile(rule) || !setRuleValue(rule, rule.defaultValue)) {
                            throw new SyntaxException();
                        }
                    }
                    MessageUtil.sendToAllOperators(new LiteralText("Successfully reset cake config!"));
                    return;
                } else if (args.length > 2) { // cake reset [X] [X]
                    throw new SyntaxException();
                }
                CutefulCakeRuleAsObject rule = SettingsManager.rules.get(args[1]);
                if (removeFromFile(rule) && setRuleValue(rule, rule.defaultValue)) { // cake reset [rule]
                    MessageUtil.sendToAllOperators(new LiteralText("Successfully reset the value of " + args[1]));
                } else {
                    throw new SyntaxException();
                }
            } else if (args.length == 1) { // cake [rule]
                if (SettingsManager.rules.containsKey(args[0])) {
                    source.sendMessage(printFeatureInfo(SettingsManager.rules.get(args[0])));
                } else {
                    throw new SyntaxException();
                }
            } else if (args.length == 2) { // cake [rule] [value]
                if (SettingsManager.rules.containsKey(args[0])) {
                    if (setRuleValue(SettingsManager.rules.get(args[0]), args[1])) {
                        LiteralText print = new LiteralText("Set value of " + args[0] + " to " + args[1] + " ");
                        print.append(new LiteralText("[Set default]").setStyle(new Style().
                                setColor(Formatting.AQUA).
                                setClickEvent(new ClickEvent(
                                        ClickEvent.Action.SUGGEST_COMMAND,
                                        "/cake setDefault " + args[0] + " " + args[1]))));
                        MessageUtil.sendToAllOperators(print);
                    } else {
                        throw new SyntaxException();
                    }
                } else { // cake [X] [X]
                    throw new SyntaxException();
                }
            }
        }
    }

    private boolean removeFromFile(CutefulCakeRuleAsObject rule) {
        if (setRuleValue(rule, rule.defaultValue)) {
            SettingsManager.removeFromCakeConfRule(rule.name);
            return true;
        }
        return false;
    }

    private boolean saveToFile(CutefulCakeRuleAsObject rule, String value) {
        if(setRuleValue(rule, value)) {
            SettingsManager.addToCakeConfRule(rule.name, value);
            return true;
        }
        return false;
    }

    private boolean setRuleValue(CutefulCakeRuleAsObject rule, String value) {
        return rule.setValue(value);
    }

    private LiteralText printCakeVersion() {
        return new LiteralText(CutefulCakeSettings.cakeVersion);
    }

    private LiteralText listAllCakeFeatures() {
        LiteralText print = new LiteralText("");
        Set<String> set = SettingsManager.rules.keySet();
        Iterator<String> ite = set.iterator();
        String key = ite.next();
        while (true) {
            CutefulCakeRuleAsObject value = SettingsManager.rules.get(key);
            print.append(printFeatureInfo(value));
            if (ite.hasNext()) {
                key = ite.next();
                print.append("\n\n");
            } else {
                break;
            }
        }
        print.append("\n");
        print.append(printCakeVersion());
        return print;
    }

    private LiteralText printFeatureInfo(CutefulCakeRuleAsObject rule) {
        LiteralText print = new LiteralText("");
        print.append(new LiteralText(rule.name)).setStyle(new Style().setBold(true));
        print.append(new LiteralText( "\n" + rule.description).setStyle(new Style().setBold(false)));
        LiteralText values = new LiteralText("\n");
        values.setStyle(new Style().setBold(true));
        Style selected = new Style().setColor(Formatting.GREEN);
        Style nonSelected = new Style().setColor(Formatting.AQUA).setUnderline(true);
        String valueInField = null;
        try {
            valueInField = String.valueOf(rule.field.get(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (String value : rule.options) {
            if (value.equals(valueInField)) {
                values.append(new LiteralText(value).setStyle(selected));
            } else {
                values.append(new LiteralText(value).setStyle(nonSelected.deepCopy().
                        setClickEvent(new ClickEvent(
                                ClickEvent.Action.SUGGEST_COMMAND,
                                "/cake " + rule.name + " " + value)).
                        setHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new LiteralText("Set value to : " + value)))));
            }
            values.append(" ");
        }
        values.append("\n");
        values.append(new LiteralText("Current value : " + valueInField + " (" + (rule.defaultValue.equals(valueInField) ? "default" : "modified") + " value)").setStyle(
                new Style().setBold(true).setColor(Formatting.RED)
        ));
        print.append(values);
        return print;
    }
}
