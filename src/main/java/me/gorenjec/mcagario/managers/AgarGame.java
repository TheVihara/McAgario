package me.gorenjec.mcagario.managers;

import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.cache.InMemoryCache;
import me.gorenjec.mcagario.models.GameMap;

public class AgarGame {
    private final McAgario instance;
    private final SphereManager sphereManager;
    private final GameManager gameManager;
    private final PlayerManager playerManager;
    private final GameMap gameMap;
    private final InMemoryCache cache;

    public AgarGame(McAgario instance, GameMap gameMap) {
        this.instance = instance;
        this.gameMap = gameMap;
        this.cache = instance.getCache();
        this.sphereManager = new SphereManager(this);
        this.gameManager = new GameManager();
        this.playerManager = new PlayerManager(this);
    }

    public InMemoryCache getCache() {
        return cache;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public SphereManager getSphereManager() {
        return sphereManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
