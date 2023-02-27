package me.gorenjec.mcagario.managers;

import me.gorenjec.mcagario.cache.InMemoryCache;
import me.gorenjec.mcagario.models.AgarCell;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerManager {
    private final AgarGame agarGame;
    private final InMemoryCache cache;

    public PlayerManager(AgarGame agarGame) {
        this.agarGame = agarGame;
        this.cache = agarGame.getCache();
    }

    public void addPlayer(Player player) {
        cache.addPlayer(player, agarGame);
        AgarCell agarCell = new AgarCell(player.getLocation(), 6, player, Material.RED_WOOL, cache.getInstance());
        agarGame.getSphereManager().setAgarCell(player, agarCell);
    }

    public AgarGame getAgarGame() {
        return agarGame;
    }
}
