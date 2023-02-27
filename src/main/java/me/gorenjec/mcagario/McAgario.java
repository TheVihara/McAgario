package me.gorenjec.mcagario;

import me.gorenjec.mcagario.cache.InMemoryCache;
import me.gorenjec.mcagario.commands.CommandHandler;
import me.gorenjec.mcagario.commands.agar.AgarCommand;
import me.gorenjec.mcagario.commands.agar.CreateSubCommand;
import me.gorenjec.mcagario.commands.agar.ListSubCommand;
import me.gorenjec.mcagario.commands.agar.SelectionSubCommand;
import me.gorenjec.mcagario.listener.PlayerInteractListener;
import me.gorenjec.mcagario.listener.PlayerJoinListener;
import me.gorenjec.mcagario.listener.PlayerMoveListener;
import me.gorenjec.mcagario.listener.PlayerQuitListener;
import me.gorenjec.mcagario.managers.AgarGame;
import me.gorenjec.mcagario.storage.SQLStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class McAgario extends JavaPlugin {
    private InMemoryCache cache;
    private CommandHandler commandHandler;
    private SQLStorage storage;
    private File mapsConfigFile;
    private FileConfiguration mapsConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        this.constructConfigFiles();

        this.storage = new SQLStorage(this);
        this.cache = new InMemoryCache(this);
        this.cache.addGame(new AgarGame(this, cache.getMaps().get(2)));

        this.commandHandler = new CommandHandler(this);

        this.registerListeners();
        this.registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerListeners() {
        PluginManager pM = this.getServer().getPluginManager();
        pM.registerEvents(new PlayerJoinListener(this), this);
        pM.registerEvents(new PlayerQuitListener(this), this);
        pM.registerEvents(new PlayerMoveListener(this), this);
        pM.registerEvents(new PlayerInteractListener(this), this);
    }

    private void constructConfigFiles() {
        try {
            this.mapsConfigFile = new File(getDataFolder(), "maps.yml");
            this.mapsConfigFile.createNewFile();

            this.mapsConfig = YamlConfiguration.loadConfiguration(this.mapsConfigFile);
        } catch (IOException e) {
            getLogger().severe("Unable to set up maps.yml file");
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        CommandHandler.register(
                new AgarCommand(),
                new CreateSubCommand(this),
                new SelectionSubCommand(this),
                new ListSubCommand(this)
        );
    }

    public void flushMapsFile(FileConfiguration mapsConfig) {
        try {
            mapsConfig.save(this.mapsConfigFile);
        } catch (IOException e) {
            getLogger().severe("Unable to flush maps.yml file");
            e.printStackTrace();
        }
    }

    public FileConfiguration getMapsConfig() {
        return mapsConfig;
    }

    public File getMapsConfigFile() {
        return mapsConfigFile;
    }

    public InMemoryCache getCache() {
        return cache;
    }

    public SQLStorage getStorage() {
        return storage;
    }
}
