package me.joshb.discordbotapi.server;

import me.joshb.discordbotapi.server.command.CommandManager;
import me.joshb.discordbotapi.server.config.Config;
import me.joshb.discordbotapi.server.config.LinkedAccounts;
import me.joshb.discordbotapi.server.config.Messages;
import me.joshb.discordbotapi.server.listener.DiscordMessageReceived;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public class DiscordBotAPI extends JavaPlugin {

    public static JDA jda;

    public static DiscordBotAPI plugin;

    public DiscordMessageReceived discordMessageReceived;

    public void onEnable(){
        plugin = this;

        Config.getInstance().initialize();
        Messages.getInstance().initialize();
        LinkedAccounts.getInstance().initialize();

        CommandManager cm = new CommandManager();
        Bukkit.broadcastMessage(cm.toString());
        cm.initializeSubCommands();

        getCommand("discord").setExecutor(cm);

        if (Config.getInstance().getConfig().getString("Bot.Token").equals("token_here")) {
            getLogger().severe("Plugin Disabled. The bot token is invalid.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        JDABuilder builder = JDABuilder.create(Config.getInstance().getConfig().getString("Bot.Token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MESSAGES);

        try {
            jda = builder.build();
        } catch (LoginException e) {
            getLogger().severe("Plugin Disabled. The bot token is invalid. Reason: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
        discordMessageReceived = new DiscordMessageReceived();
        registerEvent(this, discordMessageReceived);
    }

    public void onDisable() {
        if (jda != null) {
            unRegisterEvent(this, discordMessageReceived);
            jda.shutdown();
        }
    }

    public static JDA getJDA() {
        return jda;
    }

    public void registerEvent(Plugin p, Object listenerClass) {
        if(p != this){
            getLogger().info("Registering Listener from " + p.getName() + " - " + listenerClass.getClass().getName());
        }
        try {
            jda.addEventListener(listenerClass);
        } catch (Exception e) {
            getLogger().severe("Could not register listener from " + p.getName() + " - " + e.getMessage());
        }
    }

    public void unRegisterEvent(Plugin p, Object listenerClass) {
        if(p != this){
            getLogger().info("Unregistering Listener from " + p.getName() + " - " + listenerClass.getClass().getName());
        }
        try {
            jda.removeEventListener(listenerClass);
        } catch (Exception e) {
            getLogger().severe("Could not unregister listener from " + p.getName() + " - " + e.getMessage());
        }
    }

    public static AccountManager getAccountManager() {
        return AccountManager.getInstance();
    }

}
