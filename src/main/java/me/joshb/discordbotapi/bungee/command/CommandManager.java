package me.joshb.discordbotapi.bungee.command;

import me.joshb.discordbotapi.Permission;
import me.joshb.discordbotapi.bungee.DiscordBotAPI;
import me.joshb.discordbotapi.bungee.assets.Assets;
import me.joshb.discordbotapi.bungee.config.Config;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager extends Command {

    public CommandManager() { super("discord", Permission.GAME_DISCORD.getValue(), "");}

    private List<DiscordCommand> commands = new ArrayList<>();

    public void initializeSubCommands(){
        if(Config.getInstance().getConfig().getBoolean("Account-Manager")) {
            commands.add(new CommandLink());
            commands.add(new CommandUnlink());
        }
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(Assets.format("Game.Commands.Player-Only-Command"));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;

        if(args.length == 0){
            //Not Linked
            if(DiscordBotAPI.getAccountManager().getDiscordID(p.getUniqueId()) == null){
                List<String> notLinked = Assets.formatStringList("Game.Commands.Discord.Not-Linked");
                for(String s : notLinked){
                    p.sendMessage(s
                            .replaceAll("%player%", p.getName()));
                }
            //Linked
            } else {
                String discordID = DiscordBotAPI.getAccountManager().getDiscordID(p.getUniqueId());
                User u = DiscordBotAPI.plugin.getJDA().getUserById(discordID);
                List<String> linked = Assets.formatStringList("Game.Commands.Discord.Linked");
                for(String s : linked){
                    p.sendMessage(s
                            .replaceAll("%player%", p.getName())
                            .replaceAll("%discord_bot_name%", DiscordBotAPI.plugin.getJDA().getSelfUser().getName())
                            .replaceAll("%discord_author_name%", u.getName())
                            .replaceAll("%discord_author_discriminator%", u.getDiscriminator()));
                }
            }
        } else {
            DiscordCommand cmd = get(args[0]);
            if (!(cmd == null)) {
                ArrayList<String> a = new ArrayList<>(Arrays.asList(args));
                a.remove(0);
                args = a.toArray(new String[a.size()]);
                cmd.onCommand(p, args);
                return;
            }
        }
    }

    private DiscordCommand get(String name) {
        for (DiscordCommand cmd : commands) {
            if (cmd.command().equalsIgnoreCase(name))
                return cmd;
        }
        return null;
    }
}
