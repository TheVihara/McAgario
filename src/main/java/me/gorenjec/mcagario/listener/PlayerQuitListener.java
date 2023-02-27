package me.gorenjec.mcagario.listener;

import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.cache.InMemoryCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final McAgario instance;
    private final InMemoryCache cache;

    public PlayerQuitListener(McAgario instance) {
        this.instance = instance;
        this.cache = instance.getCache();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
         cache.flushPlayer(e.getPlayer());
    }
}
