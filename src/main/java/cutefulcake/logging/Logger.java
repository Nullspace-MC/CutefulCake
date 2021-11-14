package cutefulcake.logging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Logger {
    private String name;
    private Set<String> playersSubscribed = new HashSet<>();
    private Map<String, String> channelSubscriptions = new HashMap<>();
    private Set<String> options;
    private boolean mustBeInAChannel;

    public Logger(String name) {
        this.name = name;
        this.options = new HashSet<>();
    }
    public Logger(String name, Set<String> options, boolean mustBeInAChannel) {
        this.name = name;
        this.options = options;
        this.mustBeInAChannel = mustBeInAChannel;
    }

    public boolean isPlayerSubscribed(String playerName) {
        return playersSubscribed.contains(playerName);
    }

    public void subscribePlayer (String playerName) {
        playersSubscribed.add(playerName);
    }

    public void unsubscribePlayer (String playerName) {
        playersSubscribed.remove(playerName);
    }

    public String getName() {
        return name;
    }

    public Set<String> getOptions() {
        return options;
    }

    public Map<String, String> getChannelSubscriptions() {
        return channelSubscriptions;
    }

    public void onLogCommand(String playerName) {
        if (isPlayerSubscribed(playerName)) unsubscribePlayer(playerName);
        else subscribePlayer(playerName);
    }

    public void onLogCommand(String playerName, String channel) {
        if (isPlayerSubscribed(playerName) && channelSubscriptions.containsKey(playerName) && !channelSubscriptions.get(playerName).equals(channel))
            channelSubscriptions.put(playerName, channel); // change logger channel

        else if (!isPlayerSubscribed(playerName)) {
            subscribePlayer(playerName);
            channelSubscriptions.put(playerName, channel); // subscribe to logger with a channel
        }

        else unsubscribePlayer(playerName); // unsubscribe from logger
    }

    public boolean getMustBeInAChannel() {
        return mustBeInAChannel;
    }
}
