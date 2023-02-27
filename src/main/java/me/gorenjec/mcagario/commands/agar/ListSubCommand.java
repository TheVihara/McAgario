package me.gorenjec.mcagario.commands.agar;

import cloud.commandframework.Command;
import cloud.commandframework.paper.PaperCommandManager;
import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.cache.InMemoryCache;
import me.gorenjec.mcagario.models.AstronaCommand;
import me.gorenjec.mcagario.models.PlayerProfile;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListSubCommand extends AstronaCommand {
    private final McAgario instance;
    private final FileConfiguration config;
    private final InMemoryCache cache;

    public ListSubCommand(McAgario instance) {
        this.instance = instance;
        this.config = instance.getMapsConfig();
        this.cache = instance.getCache();
    }

    @Override
    public Command<CommandSender> createCommand(PaperCommandManager<CommandSender> manager) {
        return manager.commandBuilder("agario", "agar", "aio")
                .literal("list")
                .handler(commandContext -> {
                    Player player = (Player) commandContext.getSender();
                    player.sendMessage("Current available maps:\n" + getSortedList(getMapNames()));
                }).build();
    }

    private List<String> getMapNames() {
        ConfigurationSection section = config.getConfigurationSection("maps");

        if (section == null) {
            return List.of("null");
        }

        return new ArrayList<>(section.getKeys(false));
    }

    private String getSortedList(List<String> list) {
        Collections.sort(list);
        return list.stream().map(s -> "- " + s).collect(Collectors.joining("\n"));
    }
}
