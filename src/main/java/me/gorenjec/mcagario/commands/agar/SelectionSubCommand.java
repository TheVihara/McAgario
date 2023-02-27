package me.gorenjec.mcagario.commands.agar;

import cloud.commandframework.Command;
import cloud.commandframework.paper.PaperCommandManager;
import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.cache.InMemoryCache;
import me.gorenjec.mcagario.models.AstronaCommand;
import me.gorenjec.mcagario.models.PlayerProfile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectionSubCommand extends AstronaCommand {
    private final McAgario instance;
    private final InMemoryCache cache;

    public SelectionSubCommand(McAgario instance) {
        this.instance = instance;
        this.cache = instance.getCache();
    }

    @Override
    public Command<CommandSender> createCommand(PaperCommandManager<CommandSender> manager) {
        return manager.commandBuilder("agario", "agar", "aio")
                .literal("selection")
                .handler(commandContext -> {
                    Player player = (Player) commandContext.getSender();
                    PlayerProfile playerProfile = cache.getPlayer(player);

                    if (playerProfile.isInSelectionMode()) {
                        playerProfile.setInSelectionMode(false);
                        player.sendMessage("You're not in selection mode anymore.");
                    } else {
                        playerProfile.setInSelectionMode(true);
                        player.sendMessage("You're now in selection mode.");
                    }
                }).build();
    }
}
