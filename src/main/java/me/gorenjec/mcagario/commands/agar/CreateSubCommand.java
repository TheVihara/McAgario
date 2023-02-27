package me.gorenjec.mcagario.commands.agar;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.parsers.MaterialArgument;
import cloud.commandframework.paper.PaperCommandManager;
import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.cache.InMemoryCache;
import me.gorenjec.mcagario.models.AstronaCommand;
import me.gorenjec.mcagario.models.GameMap;
import me.gorenjec.mcagario.models.PlayerProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSubCommand extends AstronaCommand {
    // TODO: create command, /agar create FFA1 10 1 SNOW_BLOCK
    private final McAgario instance;
    private final InMemoryCache cache;

    public CreateSubCommand(McAgario instance) {
        this.instance = instance;
        this.cache = instance.getCache();
    }

    @Override
    public Command<CommandSender> createCommand(PaperCommandManager<CommandSender> manager) {
        return manager.commandBuilder("agario", "agar", "aio")
                .literal("create")
                .argument(StringArgument.of("arena-name"))
                .argument(IntegerArgument.of("default-cell-size"))
                .argument(IntegerArgument.of("mass-per-food"))
                .argument(MaterialArgument.of("floor-material"))
                .handler(commandContext -> {
                    try {
                        Player player = (Player) commandContext.getSender();
                        PlayerProfile playerProfile = cache.getPlayer(player);
                        String arenaName = commandContext.get("arena-name");
                        int defaultCellSize = commandContext.get("default-cell-size");
                        int massPerFood = commandContext.get("mass-per-food");
                        Material material = commandContext.get("floor-material");
                        Location firstPosLoc = playerProfile.getFirstPosLoc();
                        Location secondPosLoc = playerProfile.getSecondPosLoc();

                        if (firstPosLoc == null && secondPosLoc == null) {
                            player.sendMessage("Please select your first and second position.");
                            return;
                        }

                        if (firstPosLoc == null) {
                            player.sendMessage("Please select your first position.");
                            return;
                        }

                        if (secondPosLoc == null) {
                            player.sendMessage("Please select your second position.");
                            return;
                        }

                        World world = firstPosLoc.getWorld();
                        int xMin = firstPosLoc.getBlockX();
                        int xMax = secondPosLoc.getBlockX();
                        int zMin = firstPosLoc.getBlockZ();
                        int zMax = secondPosLoc.getBlockZ();
                        int yLevel = secondPosLoc.getBlockY();


                        GameMap gameMap = new GameMap(instance, arenaName, xMin, xMax, zMin, zMax, yLevel, world, material, defaultCellSize, massPerFood);
                        instance.getLogger().info("Created new GameMap: " + arenaName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).build();
    }
}
