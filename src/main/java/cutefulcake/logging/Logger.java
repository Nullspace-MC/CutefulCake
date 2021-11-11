package cutefulcake.logging;

import java.util.HashSet;
import java.util.Set;

public class Logger {
    private String name;
    private Set<String> playersSubscribed = new HashSet<>();

    public Logger(String name) {
        this.name = name;
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

    public void onLogCommand(String playerName) {
        if (isPlayerSubscribed(playerName)) unsubscribePlayer(playerName);
        else subscribePlayer(playerName);
    }
}
