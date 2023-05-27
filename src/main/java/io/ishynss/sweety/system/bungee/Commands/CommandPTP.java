package io.ishynss.sweety.system.bungee.Commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.ishynss.sweety.system.bungee.MainBungee;
import jdk.jfr.internal.tool.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class CommandPTP extends Command {


    public CommandPTP(MainBungee mainBungee) {
        super("playertp", null, "ptp", "tp");
        this.mainBungee = mainBungee;
    }

    private final MainBungee mainBungee;



    @Override
    public void execute(CommandSender senders, String[] args) {


        ProxiedPlayer sender = (ProxiedPlayer) senders;


        if (args.length == 0) {

            sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.PTP.Syntaxe")));
            return;
        }

        ProxiedPlayer pp = MainBungee.getINSTANCE().getProxy().getPlayer(args[0]);

        if (pp != null) {
            if (pp.isConnected()) {

                String senderserver = sender.getServer().getInfo().getName();
                String playerserver = pp.getServer().getInfo().getName();

                if(senderserver != playerserver) {

                    sender.connect(MainBungee.getINSTANCE().getProxy().getServerInfo(playerserver));

                    sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.PTP.Teleported")
                            .replace("%target", pp.getName())
                            .replace("%serverdestination", playerserver)
                    ));

                    MainBungee.getINSTANCE().getProxy().getScheduler().schedule(MainBungee.getINSTANCE(), new Runnable() {
                        @Override
                        public void run() {

                            Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
                            // perform a check to see if globally are no players
                            if ( networkPlayers == null || networkPlayers.isEmpty() )
                            {
                                return;
                            }

                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF( "VERIF" ); // the channel could be whatever you want
                            out.writeUTF(String.valueOf(pp.getName())); // this data could be whatever you want
                            out.writeUTF(String.valueOf(sender.getName())); // this data could be whatever you want

                            // we send the data to the server
                            // using ServerInfo the packet is being queued if there are no players in the server
                            // using only the server to send data the packet will be lost if no players are in it
                            ((ProxiedPlayer) sender).getServer().getInfo().sendData( "TP", out.toByteArray() );


                        }
                    }, 300L, TimeUnit.MILLISECONDS);




                } else {


                            sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.PTP.TP").replace("%player", pp.getName())));

                            Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
                            // perform a check to see if globally are no players
                            if ( networkPlayers == null || networkPlayers.isEmpty() )
                            {
                                return;
                            }

                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF( "VERIF" ); // the channel could be whatever you want
                            out.writeUTF(String.valueOf(pp.getName())); // this data could be whatever you want
                            out.writeUTF(String.valueOf(sender.getName())); // this data could be whatever you want

                            // we send the data to the server
                            // using ServerInfo the packet is being queued if there are no players in the server
                            // using only the server to send data the packet will be lost if no players are in it
                            ((ProxiedPlayer) sender).getServer().getInfo().sendData( "TP", out.toByteArray() );


                }
            }
        }
    }
}
