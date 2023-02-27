package me.gorenjec.mcagario.models;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerProfile {
    private final OfflinePlayer player;
    private int kills;
    private int wins;
    private int losses;
    private int cellSize = 2;
    private Material cellMaterial;
    private boolean isInSelectionMode = false;
    private Location firstPosLoc = null;
    private Location secondPosLoc = null;

    public PlayerProfile(OfflinePlayer player, int kills, int wins, int losses, Material cellMaterial) {
        this.player = player;
        this.kills = kills;
        this.wins = wins;
        this.losses = losses;
        this.cellMaterial = cellMaterial;
    }

    public void setCellMaterial(Material cellMaterial) {
        this.cellMaterial = cellMaterial;
    }

    public boolean isInSelectionMode() {
        return isInSelectionMode;
    }

    public void setInSelectionMode(boolean inSelectionMode) {
        isInSelectionMode = inSelectionMode;
    }

    public Location getFirstPosLoc() {
        return firstPosLoc;
    }

    public Location getSecondPosLoc() {
        return secondPosLoc;
    }

    public void setFirstPosLoc(Location firstPosLoc) {
        this.firstPosLoc = firstPosLoc;
    }

    public void setSecondPosLoc(Location secondPosLoc) {
        this.secondPosLoc = secondPosLoc;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public void addKills(int amount) {
        this.kills = this.kills + amount;
    }

    public void addWins(int amount) {
        this.wins = this.wins + amount;
    }

    public void addLosses(int amount) {
        this.losses = this.losses + amount;
    }

    public void addCellSize(int amount) {
        this.cellSize = this.cellSize + amount;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public Material getCellMaterial() {
        return cellMaterial;
    }

    public int getKills() {
        return kills;
    }

    public int getLosses() {
        return losses;
    }

    public int getWins() {
        return wins;
    }

    public int getCellSize() {
        return cellSize;
    }
}
