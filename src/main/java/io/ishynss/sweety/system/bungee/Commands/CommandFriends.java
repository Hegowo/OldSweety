package io.ishynss.sweety.system.bungee.Commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.ishynss.sweety.system.bungee.MainBungee;
import io.ishynss.sweety.system.bungee.utils.SQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommandFriends extends Command {

    public static Map<ProxiedPlayer, ProxiedPlayer> friendCooldown = new HashMap<>();

    public static Map<ProxiedPlayer, List<ProxiedPlayer>> WhoAddedMe = new HashMap<>();

    //public static Map<UUID, ProxiedPlayer> Friends_WhoAdded = new HashMap<>();
    //public static Map<UUID, ProxiedPlayer> Friends_WhoTarget = new HashMap<>();
    //public static Map<UUID, Boolean> Friends_IDExpired = new HashMap<>();


    public CommandFriends(MainBungee mainBungee) {
        super("friends", null, "f", "ami", "amis", "friend");
        this.mainBungee = mainBungee;
    }

    private final MainBungee mainBungee;



    @Override
    public void execute(CommandSender senders, String[] args) {


        ProxiedPlayer sender = (ProxiedPlayer) senders;


        if (args.length == 0) {

            sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /f < add | remove | accept | list | join>"));
            return;
        }

        if (args[0].equalsIgnoreCase("yes") || args[0].equalsIgnoreCase("accept")) {


            if (args.length == 2) {

                ProxiedPlayer p2 = MainBungee.getINSTANCE().getProxy().getPlayer(args[1]);

                if(p2 == null) {
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Vous n'avez pas de demande en attente"));
                    return;
                }

                if(!p2.isConnected()) {
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Vous n'avez pas de demande en attente"));
                    return;
                }


                List<ProxiedPlayer> whoaddme = WhoAddedMe.get(sender);
                List<ProxiedPlayer> whoaddhim = WhoAddedMe.get(p2);

                if(whoaddme != null) {
                    if (!whoaddme.contains(p2)) {
                        sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: §cVous n'avez pas de demande d'ami de ce joueur."));
                        return;
                    }
                }

                    SQL.addFriends(p2.getUniqueId(), p2.getName(), sender.getUniqueId(), sender.getName());
                    SQL.addFriends(sender.getUniqueId(), sender.getName(), p2.getUniqueId(), p2.getName());

                    p2.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §aVous êtes désormais ami avec " + sender.getName()));
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §aVous êtes désormais ami avec " + p2.getName()));

                    whoaddme.remove(p2);
                    whoaddhim.remove(sender);

                    WhoAddedMe.replace(sender, whoaddme);
                    WhoAddedMe.replace(p2, whoaddhim);



            } else {

            sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /f accept <joueur>"));

            }

        }

        if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l")) {

            List<String> All = Objects.requireNonNull(SQL.getFriends(sender.getName(), sender));

            if(All.size() == 0) {
                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Vous n'avez aucun amis."));
                return;
            }

            Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
            // perform a check to see if globally are no players
            if ( networkPlayers == null || networkPlayers.isEmpty() )
            {
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF( "Friends" ); // the channel could be whatever you want
            out.writeUTF(String.valueOf(All.toString())); // this data could be whatever you want
            out.writeUTF(String.valueOf(sender.getName())); // this data could be whatever you want

            // we send the data to the server
            // using ServerInfo the packet is being queued if there are no players in the server
            // using only the server to send data the packet will be lost if no players are in it
            ((ProxiedPlayer) sender).getServer().getInfo().sendData( "GUI", out.toByteArray() );



        }


        if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {


            if(args.length == 2) {

                ProxiedPlayer p = MainBungee.getINSTANCE().getProxy().getPlayer(args[1]);

                if(p == null) {
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Ce joueur est introuvable."));
                    return;
                }

                if(!p.isConnected()) {
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Ce joueur est introuvable."));
                    return;
                }

                if(p.getName().equals(sender.getName())) {

                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Vous ne pouvez pas vous auto-ajouter."));

                    return;
                }

                if(!SQL.getIfPlayerAcceptFriendRequest(p)) {
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Ce joueur n'accepte pas les demandes d'amis."));
                    return;
                }


                if(Objects.requireNonNull(SQL.getFriends(sender.getName(), sender)).contains(p.getName())) {
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Vous êtes déjà ami avec ce joueur."));
                    return;
                }

                if(friendCooldown.get(p) == sender) {

                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cVeuillez patienter avant d'envoyer une demande au même joueur."));
                    return;
                }


                List<ProxiedPlayer> whoaddme2 = WhoAddedMe.get(sender);
                List<ProxiedPlayer> whoaddme = WhoAddedMe.get(p);

                if(whoaddme != null) {
                    if(whoaddme.contains(sender)) {
                        whoaddme.remove(sender);
                    }
                }

                if(whoaddme2 != null) {
                    if (whoaddme2.contains(p)) {
                        BungeeCord.getInstance().getPluginManager().dispatchCommand(sender, "f accept " + p.getName());
                        return;
                    }
                }

                whoaddme.add(sender);
                WhoAddedMe.put(p, whoaddme);

                //UUID ID = UUID.randomUUID();
                //Friends_WhoTarget.put(ID, p);
                //Friends_WhoAdded.put(ID, sender);


                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §aDemande d'ami envoyée à " + p.getName()));


                TextComponent accepter = new TextComponent(ChatColor.GREEN + "[Accepter]");
                accepter.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f accept " + sender.getName()));
                accepter.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.WHITE + "Accepter la demande ?!").create())));


                p.sendMessage(new TextComponent(ChatColor.LIGHT_PURPLE + "Satsuki " + ChatColor.DARK_GRAY + "| " + ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.GRAY + " souhaiterais devenir votre ami ! "), accepter);

                MainBungee.getINSTANCE().getProxy().getScheduler().schedule(MainBungee.getINSTANCE(), new Runnable() {
                    @Override
                    public void run() {
                        friendCooldown.remove(p, sender);
                    }
                }, 30L, TimeUnit.SECONDS);




            } else {


                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /f add <joueur>"));
            }


        }


        if (args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("remove")) {


            if(args.length == 3) {
                if(args[2].equalsIgnoreCase("confirm")) {
                    String p = args[1];
                    SQL.removeFriends(p, sender.getName());
                    SQL.removeFriends(sender.getName(), p);
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §a" + p + " a bien été supprimé de vos amis."));
                    return;
                } else {
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /f remove <joueur>"));
                }
                ;
            }


            if(args.length == 2) {

                String p = args[1];

                if(p.equals(sender.getName())) {

                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Vous ne pouvez pas vous auto-supprimer."));

                    return;
                }


                if(!Objects.requireNonNull(SQL.getFriends(sender.getName(), sender)).contains(p)) {
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Vous n'êtes pas ami avec ce joueur."));
                    return;
                }


                TextComponent accepter = new TextComponent(ChatColor.GREEN + "[Confirmer]");
                accepter.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f remove " + p + " confirm"));
                accepter.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.WHITE + "Confirmer la supression ?!").create())));


                sender.sendMessage(new TextComponent(ChatColor.LIGHT_PURPLE + "Satsuki " + ChatColor.DARK_GRAY + "| " + ChatColor.LIGHT_PURPLE + p + ChatColor.GRAY + " ne fera plus parti de vos amis ! "), accepter);
                return;




            } else {


                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /f remove <joueur>"));
            }


        }





    }
}