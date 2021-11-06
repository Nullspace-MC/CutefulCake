package cutefulcake.settings;

import cutefulcake.CutefulCake;
import net.minecraft.server.MinecraftServer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class SettingsManager {
    public static final HashMap<String, CutefulCakeRuleAsObject> rules = new HashMap<>();
    public static File cakeConfFile = null;

    public static void parseRules() {
        for (Field f : CutefulCakeSettings.class.getDeclaredFields()) {
            CutefulCakeRule r = f.getAnnotation(CutefulCakeRule.class);
            if (r == null) continue;
            if (f.getType().equals(boolean.class)) {
                r = makeRuleForBoolean(r);
            }
            CutefulCakeRuleAsObject parsedRule = new CutefulCakeRuleAsObject(r, f);
            rules.put(parsedRule.name, parsedRule);
        }
    }

    public static CutefulCakeRule makeRuleForBoolean(CutefulCakeRule r) {
        return new CutefulCakeRule() {
            @Override
            public String name() {
                return r.name();
            }

            @Override
            public String description() {
                return r.description();
            }

            @Override
            public String[] options() {
                return new String[]{"false", "true"};
            }

            @Override
            public boolean strict() {
                return true;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return CutefulCakeRule.class;
            }
        };
    }

    public static void setupSettingsManager() throws IOException {
        File source = MinecraftServer.getServer().getRunDirectory();
        cakeConfFile = new File(source, MinecraftServer.getServer().getLevelName() + File.separator + "cake.conf");
        if (!cakeConfFile.createNewFile()) {
            CutefulCake.LOG.info("Config file cake.conf found, now loading");
            SettingsManager.loadCakeConf();
        }
        if (!cakeConfFile.isFile()) {
            if (cakeConfFile.delete()) {
                if (!cakeConfFile.createNewFile()) {
                    CutefulCake.LOG.error("Error while creating cake.conf file");
                }
            }
        }
    }

    public static void addToCakeConfRule(String ruleName, String value) {
        try {
            boolean hasWritten = false;
            File temp = new File(cakeConfFile.getParent(), "temp");
            Scanner reader = new Scanner(cakeConfFile);
            FileWriter writer = new FileWriter(temp);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] configArgs = line.split(" ");
                if (configArgs[0].equals(ruleName)) {
                    writer.write(ruleName + " " + value);
                    hasWritten = true;
                } else {
                    writer.write(line);
                }
                writer.write("\n");
            }
            if (!hasWritten) writer.write(ruleName + " " + value + "\n");
            reader.close();
            writer.close();
            if (!cakeConfFile.delete() || !temp.renameTo(cakeConfFile)) CutefulCake.LOG.error("Issue saving rule " + ruleName);
        } catch (IOException e) {
            CutefulCake.LOG.error("Problem encountered while trying to add " + ruleName + " to the cake.conf file");
            e.printStackTrace();
        }
    }

    public static void removeFromCakeConfRule(String ruleName) {
        try {
            File temp = new File(cakeConfFile.getParent(), "temp");
            Scanner reader = new Scanner(cakeConfFile);
            FileWriter writer = new FileWriter(temp);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] configArgs = line.split(" ");
                if (configArgs[0].equals(ruleName)) continue;
                writer.write(line + "\n");
            }
            reader.close();
            writer.close();
            if (!cakeConfFile.delete() || !temp.renameTo(cakeConfFile)) CutefulCake.LOG.error("Error while trying to remove " + ruleName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadCakeConf() throws FileNotFoundException {
        Scanner reader = new Scanner(cakeConfFile);
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] configArgs = line.split(" ");
            if (configArgs.length == 1 && configArgs[0].equals("")) continue;
            if (configArgs.length != 2) {
                CutefulCake.LOG.error("Error while trying to load : " + Arrays.toString(configArgs));
                continue;
            }
            CutefulCakeRuleAsObject rule = rules.get(configArgs[0]);
            if (rule == null) {
                CutefulCake.LOG.error("Error while trying to load " + configArgs[0] + " as " + configArgs[1]);
                continue;
            }
            if (rule.setValue(configArgs[1])) {
                CutefulCake.LOG.info("Loaded config " + configArgs[0] + " as " + configArgs[1]);
            } else {
                CutefulCake.LOG.error("Error while trying to load " + configArgs[0] + " as " + configArgs[1]);
            }
        }
    }
}
