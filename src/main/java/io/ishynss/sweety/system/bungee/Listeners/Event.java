package io.ishynss.sweety.system.bungee.Listeners;


import io.ishynss.sweety.system.bungee.Commands.CommandFriends;
import io.ishynss.sweety.system.bungee.MainBungee;
import io.ishynss.sweety.system.bungee.utils.SQL;
import javafx.scene.control.CheckBox;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class Event implements Listener {




    @EventHandler
    public void onJoin(PostLoginEvent e) {


            List<ProxiedPlayer> ez = new ArrayList<>();
            CommandFriends.WhoAddedMe.put(e.getPlayer(), ez);


                CheckExpiration.check(e.getPlayer().getName());
                ProxiedPlayer p = e.getPlayer();

                CheckExpiration.isBanned(e.getPlayer().getName());

                MainBungee.Play15m.put(p, 0);

                if(!SQL.hasAccount(p.getUniqueId())) {
                    SQL.createPlayerAccount(p, p.getUniqueId());
                } else {
                    SQL.updateLastLogin(p);
                    SQL.updateOnline(p, true);
                }


                SQL.addNewIP(p);

                SQL.updateName(p);

                MainBungee.getINSTANCE().runPlayTime(p);





    }


    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {

        MainBungee.getINSTANCE().getProxy().getScheduler().schedule(MainBungee.getINSTANCE(), new Runnable() {
            @Override
            public void run() {
                if(!e.getPlayer().getServer().getInfo().getName().contains("Auth")) {

                    SQL.setServer(null, e.getPlayer().getName(), e.getPlayer().getServer().getInfo().getName());

                }
            }
        }, 10L, TimeUnit.MILLISECONDS);



    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {

        ProxiedPlayer p = e.getPlayer();

        if(SQL.hasAccount(p.getUniqueId())) {
            SQL.updateOnline(p, false);




        }




    }





}
