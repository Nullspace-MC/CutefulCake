package net.nullspace_mc.cutefulcake.counter;

import net.minecraft.server.MinecraftServer;

import java.util.HashMap;

public class Counter {
    private HashMap<String, Integer> itemCounters = new HashMap<>();
    private int tickStart;
    private int totalCount;

    public Counter() {}

    public HashMap<String, Integer> getCounterMap() {
        return itemCounters;
    }

    public void addToCounter(String itemName, int itemCount) {
        if (itemCounters.isEmpty()) tickStart = MinecraftServer.getServer().getTicks();
        if (!itemCounters.containsKey(itemName)) {
            itemCounters.put(itemName, 0);
        }
        itemCounters.put(itemName, itemCounters.get(itemName) + itemCount);
        totalCount += itemCount;
    }

    public void resetCounter() {
        itemCounters = new HashMap<>();
        totalCount = 0;
    }

    public int getRunningTime() {
        return MinecraftServer.getServer().getTicks() - this.tickStart;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
