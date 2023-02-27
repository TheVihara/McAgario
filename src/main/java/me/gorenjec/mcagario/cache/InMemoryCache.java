package me.gorenjec.mcagario.cache;

import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.managers.AgarGame;
import me.gorenjec.mcagario.models.GameMap;
import me.gorenjec.mcagario.models.PlayerProfile;
import me.gorenjec.mcagario.storage.SQLStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class InMemoryCache {
    private Map<UUID, PlayerProfile> playerProfileCache = new HashMap<>();
    private Map<UUID, AgarGame> inGamePlayers = new HashMap<>();
    private List<AgarGame> agarGames = new ArrayList<>();
    private List<GameMap> maps = new ArrayList<>();

    private final Random random = new Random();
    private final McAgario instance;
    private final SQLStorage storage;
    private final FileConfiguration mapsConfig;

    public InMemoryCache(McAgario instance) {
        this.instance = instance;
        this.storage = instance.getStorage();
        this.mapsConfig = instance.getMapsConfig();
        this.cacheMaps();
    }

    public void addPlayer(Player player, AgarGame agarGame) {
        inGamePlayers.put(player.getUniqueId(), agarGame);
    }

    public void removePlayer(Player player) {
        inGamePlayers.remove(player.getUniqueId());
    }

    public AgarGame getRandomGame() {
        int gameNum = random.nextInt(agarGames.size());
        return agarGames.get(gameNum);
    }

    public List<GameMap> getMaps() {
        return maps;
    }

    public void addMap(GameMap gameMap) {
        maps.add(gameMap);
    }

    public void setMaps(List<GameMap> maps) {
        this.maps = maps;
    }

    public void addGame(AgarGame agarGame) {
        agarGames.add(agarGame);
    }

    public void removeGame(AgarGame agarGame) {
        agarGames.remove(agarGame);
    }

    public void cacheMaps() {
        ConfigurationSection mapsSection = mapsConfig.getConfigurationSection("maps");
        if (mapsSection != null) {
            for (String mapName : mapsSection.getKeys(false)) {
                ConfigurationSection mapSection = mapsSection.getConfigurationSection(mapName);
                if (mapSection != null) {
                    String worldName = mapSection.getString("world");
                    int xMin = mapSection.getInt("x-min");
                    int xMax = mapSection.getInt("x-max");
                    int zMin = mapSection.getInt("z-min");
                    int zMax = mapSection.getInt("z-max");
                    int yLevel = mapSection.getInt("y-level");
                    Material mapMaterial = Material.valueOf(mapSection.getString("map-material"));
                    int massPerFood = mapsSection.getInt("mass-per-food");
                    int defaultCellSize = mapSection.getInt("default-cell-size");

                    maps.add(new GameMap(instance, mapName, xMin, xMax, zMin, zMax, yLevel, Bukkit.getWorld(worldName), mapMaterial, defaultCellSize, massPerFood));
                }
            }
        }
    }

    public boolean gamesContainPlayer(Player player) {
        return inGamePlayers.containsKey(player.getUniqueId());
    }

    public void cachePlayer(Player player) {
        // cache in the initial player profile
        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
            playerProfileCache.putIfAbsent(player.getUniqueId(), storage.getPlayer(player));
            instance.getLogger().info("Loaded player profile for: " + player.getName());
        });
    }

    public void flush() {
        // flush the cached data
        for (Player player : Bukkit.getOnlinePlayers()) {
            storage.updatePlayer(playerProfileCache.get(player.getUniqueId()));
            playerProfileCache.remove(player.getUniqueId());
        }
    }

    public void flushPlayer(Player player) {
        // flush the cached data
        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
            storage.updatePlayer(playerProfileCache.get(player.getUniqueId()));
            playerProfileCache.remove(player.getUniqueId());
        });
    }

    public AgarGame getGame(Player player) {
        return inGamePlayers.get(player.getUniqueId());
    }

    public PlayerProfile getPlayer(UUID uuid) {
        return playerProfileCache.get(uuid);
    }

    public McAgario getInstance() {
        return instance;
    }

    public PlayerProfile getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }
}
