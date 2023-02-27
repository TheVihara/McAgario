package me.gorenjec.mcagario.managers;

import me.gorenjec.mcagario.models.AgarCell;
import me.gorenjec.mcagario.models.ChangedBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class SphereManager {
    private final AgarGame agarGame;
    private Map<UUID, Collection<ChangedBlock>> sphereBlocks = new HashMap<>();
    private Map<UUID, AgarCell> agarCellMap = new HashMap<>();

    public SphereManager(AgarGame agarGame) {
        this.agarGame = agarGame;
    }

    public void setAgarCell(Player player, AgarCell agarCell) {
        agarCellMap.put(player.getUniqueId(), agarCell);
    }

    private Location prevLocation = null;
    public boolean hasPlayerMoved(Player player) {
        Location currLocation = player.getLocation();
        if (prevLocation == null || !currLocation.equals(prevLocation)) {
            prevLocation = currLocation;
            return true;
        }
        return false;
    }

    public boolean hasPlayerJumped(Player player) {
        Location currLocation = player.getLocation();
        if (prevLocation == null || currLocation.equals(prevLocation)) {
            prevLocation = currLocation;
            return false;
        } else return prevLocation.getY() < currLocation.getY();
    }

    public void updateSphere(Player player, int size, Material material) {
        Location location = player.getLocation();
        Set<Block> blocksChanged = new HashSet<>();

        if (sphereBlocks.containsKey(player.getUniqueId())) {
            sphereBlocks.get(player.getUniqueId()).forEach(ChangedBlock::revert);
        }

        // Create the sphere on the current location
        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                if (x * x + z * z <= size * size) {
                    Location blockLoc = location.clone().add(x, 0, z).subtract(0, 1, 0);
                    blocksChanged.add(blockLoc.getBlock());
                }
            }
        }

        sphereBlocks.put(player.getUniqueId(), ChangedBlock.snapshot(new ArrayList<>(blocksChanged)));
        blocksChanged.forEach(b -> b.setType(material));
    }

    public AgarCell getAgarCell(Player player) {
        return agarCellMap.get(player.getUniqueId());
    }
}
