package me.gorenjec.mcagario.listener;

import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.cache.InMemoryCache;
import me.gorenjec.mcagario.managers.AgarGame;
import me.gorenjec.mcagario.managers.SphereManager;
import me.gorenjec.mcagario.models.AgarCell;
import me.gorenjec.mcagario.models.PlayerProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final McAgario instance;
    private final InMemoryCache cache;

    public PlayerMoveListener(McAgario instance) {
        this.instance = instance;
        this.cache = instance.getCache();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!cache.gamesContainPlayer(player)) {
            return;
        }
        PlayerProfile playerProfile = cache.getPlayer(player);
        AgarGame agarGame = cache.getGame(player);
        SphereManager sphereManager = agarGame.getSphereManager();
        Material blobMaterial = playerProfile.getCellMaterial();
        Location locTo = e.getTo();
        AgarCell mainCell = sphereManager.getAgarCell(player);

        if (locTo == null) {
            return;
        }

        if (sphereManager.hasPlayerJumped(player)) {
            mainCell.split();
        }

        if (!player.isOnGround()) {
            return;
        }

        Location location = player.getLocation();
        location.subtract(0, 1, 0);

        if (location.getBlock().getType() != Material.QUARTZ_BLOCK) {
            if (location.getBlock().getType() != Material.RED_WOOL) {
                return;
            }
        }

        if (sphereManager.hasPlayerMoved(player)) {
            // Move the main cell based on the player's direction
            if (!agarGame.getGameMap().getRegion().contains(player.getLocation())) {
                player.teleport(e.getFrom());
                return;
            }
            mainCell.updateCell(player.getLocation(), mainCell.getMass());
        }
    }
}
