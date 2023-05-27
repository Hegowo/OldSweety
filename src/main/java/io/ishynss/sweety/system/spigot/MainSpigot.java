package io.ishynss.sweety.system.spigot;

import io.ishynss.sweety.system.spigot.Listener.Chat;
import io.ishynss.sweety.system.spigot.Listener.Inventory;
import io.ishynss.sweety.system.spigot.Listener.PluginMessage;
import io.ishynss.sweety.system.spigot.sql.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MainSpigot extends JavaPlugin  {


    public static MainSpigot INSTANCE;


    public static MainSpigot getPlugin() {
        return INSTANCE;
    }


    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();

        DatabaseManager.initAllDatabaseConnections();
        getServer().getPluginManager().registerEvents(new PluginMessage(this), this);
        getServer().getPluginManager().registerEvents(new Inventory(this), this);


        getServer().getPluginManager().registerEvents(new Chat(), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getMessenger().registerIncomingPluginChannel(this, "GUI", new PluginMessage(this));
    }


    @Override
    public void onDisable() {
        DatabaseManager.closeAllDatabaseConnections();
    }
}
