package me.gorenjec.mcagario.commands.agar;

import cloud.commandframework.Command;
import cloud.commandframework.paper.PaperCommandManager;
import me.gorenjec.mcagario.models.AstronaCommand;
import me.gorenjec.mcagario.util.HexUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AgarCommand extends AstronaCommand {
    @Override
    public Command<CommandSender> createCommand(PaperCommandManager<CommandSender> manager) {
        return manager.commandBuilder("agario", "agar", "aio")
                .handler(commandContext -> {
                    Player player = (Player) commandContext.getSender();

                    player.sendMessage(HexUtils.colorify("&cUnknown subcommand, try /club help for a list of commands!"));
                }).build();
    }
}
