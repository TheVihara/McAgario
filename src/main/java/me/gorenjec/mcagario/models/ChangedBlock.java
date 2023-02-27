package me.gorenjec.mcagario.models;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChangedBlock {
    public final Location loc;
    public final BlockData blockData;

    public ChangedBlock(Location loc, BlockData blockData) {
        this.loc = loc;
        this.blockData = blockData;
    }

    public void revert() {
        loc.getBlock().setBlockData(blockData);
    }

    public static List<ChangedBlock> snapshot(List<Block> blocks) {
        return new ArrayList<>(blocks.stream().map(b -> new ChangedBlock(b.getLocation(), b.getBlockData())).collect(Collectors.toList()));
    }
}
