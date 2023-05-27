package io.ishynss.sweety.system.bungee.Commands;

import io.ishynss.sweety.system.bungee.MainBungee;
import io.ishynss.sweety.system.bungee.utils.SQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;


public class CommandStaff extends Command
{

    public CommandStaff(MainBungee mainBungee) {
        super("staff", MainBungee.getINSTANCE().theConfig.getString("Permission.STAFFCOMMAND"));
        this.mainBungee = mainBungee;
    }

    private final MainBungee mainBungee;



    @Override
    public void execute(CommandSender commandSender, String[] args) {

        ProxiedPlayer sender = (ProxiedPlayer) commandSender;


        if (args.length == 0) {

            sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.Staffs.Line1")));
            sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.Staffs.Line2")));

            ArrayList<String> Staff = (ArrayList<String>) SQL.getStaffs(sender);

            int i = 0;

            for (String zebi : Staff) {

                if(MainBungee.getINSTANCE().getProxy().getPlayer(zebi).isConnected()) {

                    if(SQL.getRank(zebi, sender).equalsIgnoreCase(MainBungee.getINSTANCE().theConfig.getString("Staff.1"))) {
                        sender.sendMessage(TextComponent.fromLegacyText(
                                MainBungee.getINSTANCE().theConfig.getString("Messages.Staffs.Base")
                                        .replace("%name", SQL.getPrefix(zebi, sender) + zebi)
                                        .replace("%server", SQL.getServer(zebi, sender))

                        ));
                    }

                }
                sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.Staffs.Line3")));



            }


        } else {

            String msg = "";
            for (int i = 0; i != args.length; i++) msg += args[i] + " ";

            for (ProxiedPlayer p : MainBungee.getINSTANCE().getProxy().getPlayers()) {

                if(p.hasPermission(MainBungee.getINSTANCE().theConfig.getString("Permission.STAFFCOMMAND"))) {

                    p.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.StaffMSG")
                            .replace("%name", SQL.getPrefix(p.getName(), sender) + p.getName())
                            .replace("%msg", msg)
                    ));

                }

            }



        }
    }
}


