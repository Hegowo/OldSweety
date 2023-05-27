package io.ishynss.sweety.system.spigot.Listener;

import io.ishynss.sweety.system.spigot.Utils.SQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        SQL.check(e.getPlayer().getName());
        if(SQL.getActiveMuteCount(e.getPlayer().getName()) >= 1) {

            Long ok = SQL.getMute(e.getPlayer().getName());
            e.getPlayer().sendMessage("");
            e.getPlayer().sendMessage("§dSatsuki §8| §7Vous êtes réduit au silence pendant encore §d" + ok + " minutes §7§o(Consultez votre profil pour connaître vos sanctions).");
            e.getPlayer().sendMessage("");


            e.setCancelled(true);
        }
    }




}
