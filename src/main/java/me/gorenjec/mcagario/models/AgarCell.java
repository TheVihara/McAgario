package me.gorenjec.mcagario.models;

import me.gorenjec.mcagario.McAgario;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AgarCell {
    private McAgario instance;
    private Location location;
    private double mass;
    private List<AgarCell> subCells;
    private List<ChangedBlock> changedBlocks;
    private Material material;
    Set<Block> blocksChanged = new HashSet<>();
    private Player player;

    public AgarCell(Location location, double mass, Player player, Material material, McAgario instance) {
        this.instance = instance;
        this.location = location;
        this.mass = mass;
        this.material = material;
        this.subCells = new ArrayList<>();
        this.changedBlocks = new ArrayList<>();
        this.blocksChanged = new HashSet<>();
        this.player = player;

        // Create the main cell sphere
        for (double x = -mass; x <= mass; x++) {
            for (double z = -mass; z <= mass; z++) {
                if (x * x + z * z <= mass * mass) {
                    Location blockLoc = location.clone().add(x, 0, z).subtract(0, 1, 0);
                    blocksChanged.add(blockLoc.getBlock());
                }
            }
        }

        this.changedBlocks = ChangedBlock.snapshot(new ArrayList<>(blocksChanged));
        blocksChanged.forEach(b -> b.setType(material));
        blocksChanged.clear();
    }

    public Location getLocation() {
        return location;
    }

    public List<AgarCell> getSubCells() {
        return subCells;
    }

    public double getMass() {
        return mass;
    }

    public Player getPlayer() {
        return player;
    }

    // Method to move the player inside the main cell
    public void movePlayerInside() {
        Location newLocation = location.clone();
        newLocation.setY(newLocation.getY() + 1.5);
        player.teleport(newLocation);
    }

    // Method to get the direction from the player's location to the center of the cell
    public double getPlayerDirection() {
        Location playerLocation = player.getLocation();
        Location center = getLocation();
        double deltaX = center.getX() - playerLocation.getX();
        double deltaZ = center.getZ() - playerLocation.getZ();
        double radians = Math.atan2(deltaZ, deltaX);
        return Math.toDegrees(radians);
    }

    // Method to split the cell into smaller cells
    public void split() {
        createSubCell(this);
        for (AgarCell agarCell : subCells) {
            createSubCell(agarCell);
        }
    }

    public void createSubCell(AgarCell mainCell) {
        if (mainCell.getMass() < 4 || mainCell.getMass() % 2 != 0) {
            return;
        }

        Location mainCellLoc = mainCell.getLocation().clone();

        // Create new cell with half the mass of the original cell
        AgarCell subCell = new AgarCell(mainCellLoc, mainCell.getMass() / 2, player, material, instance);

        mainCell.setMass(mainCell.getMass() / 2);

        // Add the new cells to the subCells list
        subCells.add(subCell);

        Location playerLoc = player.getLocation().clone();

        for (int i = 0; i < mainCell.getMass() + 10; i++) {
            // Check if player is looking along the Y or Z axis
            Vector direction = playerLoc.getDirection().normalize().multiply(1);
            mainCellLoc.add(direction);

            Bukkit.getScheduler().runTaskLater(instance, () -> {
                updateCell(subCell, mainCellLoc, mainCell.getMass());
            }, 15L);
        }
    }

    public void moveSubCells(double subCellDistance) {
        for (AgarCell subCell : subCells) {
            double direction = getPlayerDirection();
            double deltaX = subCellDistance * Math.cos(Math.toRadians(direction));
            double deltaZ = subCellDistance * Math.sin(Math.toRadians(direction));
            Location newLocation = location.clone().add(deltaX, 0, deltaZ);
            subCell.getLocation().setX(newLocation.getX() + Math.random() * 2 - 1);
            subCell.getLocation().setZ(newLocation.getZ() + Math.random() * 2 - 1);
        }
    }

    public void setMass(double amount) {
        this.mass = amount;

        this.changedBlocks.forEach(ChangedBlock::revert);
        updateCell(player.getLocation(), mass);
    }

    public void updateCell(Location location, double mass) {
        updateCell(this, location, mass);
    }

    public void updateCell(AgarCell agarCell, Location location, double mass) {
        agarCell.getChangedBlocks().forEach(ChangedBlock::revert);

        for (double x = -agarCell.getMass(); x <= agarCell.getMass(); x++) {
            for (double z = -agarCell.getMass(); z <= agarCell.getMass(); z++) {
                if (x * x + z * z <= agarCell.getMass() * agarCell.getMass()) {
                    Location blockLoc = location.clone().add(x, 0, z).subtract(0, 1, 0);
                    agarCell.getBlocksChanged().add(blockLoc.getBlock());
                }
            }
        }

        agarCell.setChangedBlocks(ChangedBlock.snapshot(new ArrayList<>(agarCell.getBlocksChanged())));
        agarCell.getBlocksChanged().forEach(b -> b.setType(material));
        agarCell.getBlocksChanged().clear();
        this.location = location;
    }

    public List<ChangedBlock> getChangedBlocks() {
        return changedBlocks;
    }

    public void setBlocksChanged(Set<Block> blocksChanged) {
        this.blocksChanged = blocksChanged;
    }

    public void setChangedBlocks(List<ChangedBlock> changedBlocks) {
        this.changedBlocks = changedBlocks;
    }

    public Set<Block> getBlocksChanged() {
        return blocksChanged;
    }

    public Material getMaterial() {
        return material;
    }
}
