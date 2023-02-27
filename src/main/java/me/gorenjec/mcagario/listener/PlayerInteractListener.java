package me.gorenjec.mcagario.listener;

import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.cache.InMemoryCache;
import me.gorenjec.mcagario.models.PlayerProfile;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final McAgario instance;
    private final InMemoryCache cache;

    public PlayerInteractListener(McAgario instance) {
        this.instance = instance;
        this.cache = instance.getCache();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        PlayerProfile playerProfile = cache.getPlayer(player);
        Block block = e.getClickedBlock();

        if (!playerProfile.isInSelectionMode()) {
            return;
        }

        if (block == null) {
            e.setCancelled(true);
            return;
        }

        Location blockLoc = block.getLocation();
        int x = blockLoc.getBlockX();
        int y = blockLoc.getBlockY();
        int z = blockLoc.getBlockZ();

        if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                return;
            }
        }

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            playerProfile.setFirstPosLoc(blockLoc);
            player.sendMessage("First position set at (X: " + x + " Y: " + y + " Z: " + z + ").");
            e.setCancelled(true);
        } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            playerProfile.setSecondPosLoc(blockLoc);
            player.sendMessage("Second position set at (X: " + x + " Y: " + y + " Z: " + z + ").");
            e.setCancelled(true);
        }
    }
}
