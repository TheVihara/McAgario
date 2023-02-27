package me.gorenjec.mcagario.listener;

import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.cache.InMemoryCache;
import me.gorenjec.mcagario.managers.AgarGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final McAgario instance;
    private final InMemoryCache cache;

    public PlayerJoinListener(McAgario instance) {
        this.instance = instance;
        this.cache = instance.getCache();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        cache.cachePlayer(e.getPlayer());
        AgarGame agarGame = cache.getRandomGame();
        agarGame.getPlayerManager().addPlayer(e.getPlayer());
    }
}
