package net.nullspace_mc.cutefulcake.counter;

import net.minecraft.util.DyeColor;

import java.util.HashMap;
import java.util.Set;

public class CounterRegistry {
    private static HashMap<String, Counter> counters = new HashMap<>();

    public static void setupCounters() {
        for (DyeColor color : DyeColor.values()) {
            counters.put(color.asString(), new Counter());
        }
    }

    public static Counter getCounter(String color) {
        return counters.get(color);
    }

    public static Set<String> getCounterColors() {
        return counters.keySet();
    }
}
