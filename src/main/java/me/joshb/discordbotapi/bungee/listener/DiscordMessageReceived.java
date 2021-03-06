package me.joshb.discordbotapi.bungee.listener;

import me.joshb.discordbotapi.bungee.assets.Assets;
import me.joshb.discordbotapi.bungee.config.Config;
import me.joshb.discordbotapi.bungee.DiscordBotAPI;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordMessageReceived extends ListenerAdapter {

    private final String command = Config.getInstance().getConfig().getString("Bot.Command-Prefix");

    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getAuthor().isBot() || !e.getMessage().getContentRaw().startsWith(command)){
            return;
        }

        User u = e.getAuthor();

        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args.length == 1){
            //help msgs
        } else if(args.length == 2){
            if(args[1].equalsIgnoreCase("link")){
                if(!e.getChannel().getType().equals(ChannelType.PRIVATE)){
                    e.getMessage().delete().queue();
                    e.getTextChannel().sendMessage(Assets.formatDiscordMessage("Discord.PM-Command-Only", u)).queue();
                    return;
                }
                //link help msgs
            }
        } else {
            if(args[1].equalsIgnoreCase("link")){
                if(!e.getChannel().getType().equals(ChannelType.PRIVATE)){
                    e.getMessage().delete().queue();
                    e.getTextChannel().sendMessage(Assets.formatDiscordMessage("Discord.PM-Command-Only", u)).queue();
                    return;
                }
                String code = args[2];
                if(!DiscordBotAPI.getAccountManager().matchCode(code)){
                    u.openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage(Assets.formatDiscordMessage("Discord.No-Code-Generated", u)).queue();
                    });
                    return;
                }
                u.openPrivateChannel().queue(privateChannel -> {
                   privateChannel.sendMessage(Assets.formatDiscordMessage("Discord.Account-Linked", u)).queue();
                });
                DiscordBotAPI.getAccountManager().setDiscordID(u.getId(), code);
            }
        }
    }
}
