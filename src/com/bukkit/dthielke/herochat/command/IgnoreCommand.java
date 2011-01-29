package com.bukkit.dthielke.herochat.command;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import com.bukkit.dthielke.herochat.HeroChatPlugin;
import com.bukkit.dthielke.herochat.HeroChatPlugin.ChatColor;
import com.bukkit.dthielke.herochat.HeroChatPlugin.PluginPermission;

public class IgnoreCommand extends Command {

    public IgnoreCommand(HeroChatPlugin plugin) {
        super(plugin);

        this.name = "ignore";
        this.identifiers.add("/ch ignore");
        this.identifiers.add("/ignore");
    }

    @Override
    public void execute(PlayerChatEvent event, Player sender, String[] args) {
        event.setCancelled(true);

        if (args.length > 1) {
            sender.sendMessage(ChatColor.ROSE.format() + "Usage: /ignore OR /ignore <player>");
            return;
        }

        if (args[0].equalsIgnoreCase(sender.getName())) {
            sender.sendMessage("HeroChat: You can't ignore yourself!");
            return;
        }

        List<String> ignoreList = plugin.getIgnoreList(sender);

        if (args[0].isEmpty())
            displayIgnoreList(sender, ignoreList);
        else
            toggleIgnore(sender, args[0].toLowerCase(), ignoreList);
    }

    private void displayIgnoreList(Player sender, List<String> ignoreList) {
        String ignoreListMsg;

        if (ignoreList.isEmpty()) {
            ignoreListMsg = "Currently ignoring no one.";
        } else {
            ignoreListMsg = "Currently ignoring: ";

            for (String s : ignoreList) {
                ignoreListMsg += s + ",";
            }
            ignoreListMsg = ignoreListMsg.substring(0, ignoreListMsg.length() - 1);
        }

        sender.sendMessage(ignoreListMsg);
    }

    private void toggleIgnore(Player sender, String name, List<String> ignoreList) {
        if (plugin.hasPermission(name, PluginPermission.ADMIN)) {
            sender.sendMessage("HeroChat: You can't ignore admins");
            return;
        }

        int index = -1;

        for (int i = 0; i < ignoreList.size(); i++) {
            if (ignoreList.get(i).equalsIgnoreCase(name)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            ignoreList.remove(index);

            sender.sendMessage("HeroChat: No longer ignoring " + name);
        } else if (plugin.getServer().getPlayer(name) != null) {
            ignoreList.add(name);

            sender.sendMessage("HeroChat: Now ignoring " + name);
        } else {
            sender.sendMessage("HeroChat: Player not found");
        }
    }

}
