package io.ishynss.sweety.system.bungee.Commands;


import io.ishynss.sweety.system.bungee.MainBungee;
import io.ishynss.sweety.system.bungee.Punish.Ban.BanMethods;
import io.ishynss.sweety.system.bungee.Punish.Ban.MuteMethods;
import io.ishynss.sweety.system.bungee.utils.SQL;
import jdk.jfr.internal.tool.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.UUID;

public class CommandADM extends Command {

    public CommandADM(MainBungee mainBungee) {
        super("adm", null);
        this.mainBungee = mainBungee;
    }

    private final MainBungee mainBungee;


    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!sender.hasPermission("SUPER_MOD")) {
            sender.sendMessage(new TextComponent("§cErreur: Vous n'avez pas la permission (< SUPER_MOD)."));
            return;
        }

        ProxiedPlayer s = (ProxiedPlayer) sender;


        if (args.length == 0) {

            sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /adm <c | b | m | y | s | a | ip | p | h | f>"));
            return;
        }



        //

        //
        // EDIT CERISE
        //

        //


        if(args[0].equalsIgnoreCase("m") || args[0].equalsIgnoreCase("mute")) {

            if(args.length >= 5) {
                if (SQL.hasAccountName(args[1], (ProxiedPlayer) sender)) {


                    ProxiedPlayer ss = (ProxiedPlayer) sender;

                    String msg = "";
                    for (int i = 4; i != args.length; i++) msg += args[i] + " ";

                    MuteMethods.applyMute(sender.getName(), args[1], Integer.valueOf(args[2]), args[3], msg, ss.getServer().getInfo().getName());
                }
            } else {
                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /adm ban <joueur> <temps> <unit> <raison>"));
                return;
            }
        }



        if(args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("cerise")) {

            if(!sender.hasPermission("SUPER_ADMIN")) {
                sender.sendMessage(new TextComponent("§cErreur: Vous n'avez pas la permission (< SUPER_ADMIN)."));
                return;
            }

            if(args.length != 4) {
                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /adm cerise set <joueur> <valeur>"));
                return;
            } else {
                if(args[1].equalsIgnoreCase("set")) {
                    try{
                        Double money = Double.parseDouble(args[2]);
                        SQL.setCerise((ProxiedPlayer) sender, String.valueOf(args[2]), money);
                    }catch(NumberFormatException e){
                        sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Veuillez indiquer une valeur numérique."));
                    }

                }
            }

        }



        //

        //
        // EDIT YEN
        //

        //



        if(args[0].equalsIgnoreCase("yen") || args[0].equalsIgnoreCase("y")) {

            if(!sender.hasPermission("SUPER_ADMIN")) {
                sender.sendMessage(new TextComponent("§cErreur: Vous n'avez pas la permission (< SUPER_ADMIN)."));
                return;
            }

            if(args.length != 4) {
                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /adm yen set <joueur> <valeur>"));
                return;
            } else {
                if(args[1].equalsIgnoreCase("set")) {
                    try {
                        Double money = Double.parseDouble(args[2]);
                        SQL.setCerise((ProxiedPlayer) sender, String.valueOf(args[2]), money);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Veuillez indiquer une valeur numérique."));
                    }
                }
            }

        }




        //

        //
        // EDIT SAGESSE
        //

        //



        if(args[0].equalsIgnoreCase("sagesse") || args[0].equalsIgnoreCase("s")) {

            if(!sender.hasPermission("SUPER_ADMIN")) {
                sender.sendMessage(new TextComponent("§cErreur: Vous n'avez pas la permission (< SUPER_ADMIN)."));
                return;
            }

            if(args.length != 4) {
                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /adm sagesse set <joueur> <valeur>"));
                return;
            } else {
                if(args[1].equalsIgnoreCase("set")) {
                    try {
                        Double money = Double.parseDouble(args[2]);
                        SQL.setCerise((ProxiedPlayer) sender, String.valueOf(args[2]), money);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cErreur: Veuillez indiquer une valeur numérique."));
                    }
                }
            }

        }





        //

        //
        // GET INFO
        //

        //





        if(args[0].equalsIgnoreCase("p") || args[0].equalsIgnoreCase("profil")) {

            if(args.length != 2) {
                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /adm profil <joueur>"));
                return;

            } else {
                SQL.getInfos((ProxiedPlayer) sender, args[1]);
            }

        }



        if(args[0].equalsIgnoreCase("ip") || args[0].equalsIgnoreCase("ips")) {

            if(args.length != 2) {
                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /adm ip <joueur>"));
                return;

            } else {
                List<String> ok = SQL.getIp(args[1], (ProxiedPlayer) sender);
                if(ok == null) {
                    sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.NoPlayer")));
                } else {
                    sender.sendMessage(TextComponent.fromLegacyText("§d"));

                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §7IPs de §d" + args[1
                            ]));

                    for (String ok2 : ok) {

                        TextComponent ip = new TextComponent(ChatColor.LIGHT_PURPLE + "[Alts]");
                        ip.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adm alts " + ok2));
                        ip.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.WHITE + "Voir les Comptes de " + ok2 + "?").create())));


                        ((ProxiedPlayer) sender).sendMessage(new TextComponent("§8- §7" + ok2 + " §d"), ((BaseComponent) ip));


                    }
                    sender.sendMessage(TextComponent.fromLegacyText("§d"));
                }
            }

        }




        if(args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("alts")) {

            if(args.length != 2) {
                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /adm alts <IP>"));
                return;

            } else {
                List<String> ok = SQL.getAlts(args[1], (ProxiedPlayer) sender);
                if(ok == null) {
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cCette IP n'existe pas."));
                } else {
                    sender.sendMessage(TextComponent.fromLegacyText("§d"));

                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §7Comptes de §d" + args[1
                            ]));

                    for (String ok2 : ok) {
                        TextComponent ip = new TextComponent(ChatColor.LIGHT_PURPLE + "[IPs]");
                        ip.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adm ips " + ok2));
                        ip.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.WHITE + "Voir les IPs de " + ok2 + "?").create())));


                        ((ProxiedPlayer) sender).sendMessage(new TextComponent("§8- §7" + ok2 + " §d"), ((BaseComponent) ip));



                    }
                    sender.sendMessage(TextComponent.fromLegacyText("§d"));
                }
            }

        }



        if(args[0].equalsIgnoreCase("h")|| args[0].equalsIgnoreCase("history") ) {

            if(args.length != 2) {
                sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cAide: /adm history <joueur>"));
                return;

            } else {
                if(SQL.hasAccountName(args[1], (ProxiedPlayer) sender)) {

                    List<String> ok = SQL.getSanctions(args[1], (ProxiedPlayer) sender);
                    if (ok.size() == 0) {
                        sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.ADM.History.Empty")));

                    } else {
                        sender.sendMessage(TextComponent.fromLegacyText("§d"));

                        sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §7Sanctions de §d" + args[1
                                ]));

                        for (String ok2 : ok) {
                            TextComponent ip = new TextComponent(ChatColor.LIGHT_PURPLE + "[?]");
                            ip.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (
                                    new ComponentBuilder(
                                            ChatColor.GRAY + "Sanctionné par: " + ChatColor.LIGHT_PURPLE + SQL.WasSSBy.get(ok2) +
                                                    "\n" + ChatColor.GRAY + "Sanctionné le: " + ChatColor.LIGHT_PURPLE + SQL.getDateFromTimeStamp(SQL.WasSsStart.get(ok2)) +
                                                    "\n" + ChatColor.GRAY + "Fin de la sanction: " + ChatColor.LIGHT_PURPLE + SQL.getDateFromTimeStamp(SQL.WasSsEnd.get(ok2)) +
                                                    "\n" + ChatColor.GRAY + "Raison: " + ChatColor.LIGHT_PURPLE + SQL.WasSsWhy.get(ok2) +
                                                    "\n" + ChatColor.GRAY + "Serveur: " + ChatColor.LIGHT_PURPLE + SQL.WasSsOnServer.get(ok2)
                                    ).create())));


                            if(SQL.WasSsActive.get(ok2) == 1) {
                                ((ProxiedPlayer) sender).sendMessage(new TextComponent("§5- §8(§a✓§8) §7[§d" + SQL.WasSsType.get(ok2) + "§7] §7ID: " + ok2 + " §d"), ((BaseComponent) ip));

                            } else {
                                ((ProxiedPlayer) sender).sendMessage(new TextComponent("§5- §8(§c✘§8)  §7[§d" + SQL.WasSsType.get(ok2) + "§7] §7ID: " + ok2 + " §d"), ((BaseComponent) ip));

                            }


                        }
                        sender.sendMessage(TextComponent.fromLegacyText("§d"));
                    }
                } else {
                    sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.NoPlayer")));
                }
            }

        }



        if(args[0].equalsIgnoreCase("b") || args[0].equalsIgnoreCase("ban")) {

            if(args.length >= 5) {
                if (SQL.hasAccountName(args[1], (ProxiedPlayer) sender)) {


                    ProxiedPlayer ss = (ProxiedPlayer) sender;

                    String msg = "";
                    for (int i = 4; i != args.length; i++) msg += args[i] + " ";

                    BanMethods.applyBan(sender.getName(), args[1], Integer.valueOf(args[2]), args[3], msg, ss.getServer().getInfo().getName());
                }
            } else {
                sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.ADM.Ban.Syntaxe")));
                    return;
            }
        }




        if(args[0].equalsIgnoreCase("f") || args[0].equalsIgnoreCase("friends")) {

            if(args.length != 2) {
                sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.ADM.Friends.Syntaxe")));
                return;

            } else {
                if (SQL.hasAccountName(args[1], (ProxiedPlayer) sender)) {
                    List<String> ok = SQL.getFriends(args[1], (ProxiedPlayer) sender);
                    if (ok.size() == 0) {
                        sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.ADM.Friends.EmptyFriends")));
                    } else {
                        sender.sendMessage(TextComponent.fromLegacyText("§d"));

                        sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.ADM.Friends.Infos.Title").replace("%target", args[1])));

                        for (String ok2 : ok) {

                            ChatColor chatColor = ChatColor.valueOf(MainBungee.getINSTANCE().theConfig.getString("Messages.ADM.Friends.Infos.ChatColor1"));
                            ChatColor chatColor2 = ChatColor.valueOf(MainBungee.getINSTANCE().theConfig.getString("Messages.ADM.Friends.Infos.ChatColor2"));

                            TextComponent ip = new TextComponent(chatColor + MainBungee.getINSTANCE().theConfig.getString("Messages.ADM.Friends.Infos.2"));

                            ip.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adm p " + ok2));
                            ip.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(chatColor2 + MainBungee.getINSTANCE().theConfig.getString("Messages.ADM.Friends.Infos.2").replace("%target", ok2)).create())));


                            ((ProxiedPlayer) sender).sendMessage(new TextComponent(MainBungee.getINSTANCE().theConfig.getString("Messages.ADM.Friends.Infos.Each").replace("%player", ok2)), ((BaseComponent) ip));


                        }
                        sender.sendMessage(TextComponent.fromLegacyText("§d"));
                    }
                } else {
                    sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.NoPlayer")));
                }
            }
        }



    }
}
