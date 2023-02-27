package me.gorenjec.mcagario.models;

import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.util.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class GameMap {
    private final McAgario instance;
    private final FileConfiguration config;
    private String mapName;
    private Region region;
    private World world;
    private Material mapMaterial;
    private int massPerFood;
    private int defaultCellSize;

    public GameMap(McAgario instance, String mapName, int xMin, int xMax, int zMin, int zMax, int yLevel, World world, Material mapMaterial, int defaultCellSize, int massPerFood) {
        this.instance = instance;
        this.config = instance.getMapsConfig();
        this.mapName = mapName;
        this.region = new Region(new Location(world, xMin, yLevel, zMin), new Location(world, xMax, yLevel, zMax));
        this.world = world;
        this.mapMaterial = mapMaterial;
        this.defaultCellSize = defaultCellSize;
        this.massPerFood = massPerFood;

        this.flush(xMin, xMax, zMin, zMax, yLevel);
    }

    public void flush(int xMin, int xMax, int zMin, int zMax, int yLevel) {
        ConfigurationSection section = config.createSection("maps." + mapName.toLowerCase());
        section.set("map-name", mapName.toLowerCase());
        section.set("x-min", xMin);
        section.set("x-max", xMax);
        section.set("z-min", zMin);
        section.set("z-max", zMax);
        section.set("y-level", yLevel);
        section.set("world", world.getName());
        section.set("map-material", mapMaterial.toString());
        section.set("mass-per-food", massPerFood);
        section.set("default-cell-size", defaultCellSize);

        instance.flushMapsFile(config);
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public int getMassPerFood() {
        return massPerFood;
    }

    public void setMassPerFood(int massPerFood) {
        this.massPerFood = massPerFood;
    }

    public void setDefaultCellSize(int defaultCellSize) {
        this.defaultCellSize = defaultCellSize;
    }

    public void setMapMaterial(Material mapMaterial) {
        this.mapMaterial = mapMaterial;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }

    public String getMapName() {
        return mapName;
    }

    public World getWorld() {
        return world;
    }

    public int getDefaultCellSize() {
        return defaultCellSize;
    }

    public Material getMapMaterial() {
        return mapMaterial;
    }
}
