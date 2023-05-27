package io.ishynss.sweety.system.bungee.Punish.Ban;


import io.ishynss.sweety.system.bungee.MainBungee;
import io.ishynss.sweety.system.bungee.Punish.PunishSQL;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Timestamp;
import java.time.Instant;

public class BanMethods {


    public static void applyBan(String sender, String target, Integer pretime, String timeunit, String reason, String server) {

        Integer recidiv = PunishSQL.getRedicive(target, reason);

        ProxiedPlayer ss = MainBungee.getINSTANCE().getProxy().getPlayer(sender);
        ss.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §aSanction en cours d'application..."));

        // GESTION DU TEMPS

        String unit = null;

        long now = Instant.now().getEpochSecond();


        switch (timeunit) {

            case "d":
            case "day":
            case "days":
            case "jour":
            case "jours":
            case "j":

                unit = "jours";

                break;



            case "h":
            case "hour":
            case "hours":
            case "heure":
            case "heures":

                unit = "heures";

                break;




            case "m":
            case "minute":
            case "minutes":
            case "min":

                unit = "minutes";

                break;


            case "s":
            case "second":
            case "seconde":
            case "secondes":
            case "seconds":
            case "sec":

                unit = "secondes";

                break;
        }





        switch (unit) {


            case "semaines":



                long newtime1 = now + ((pretime*604800) * (recidiv + 1));


                if(PunishSQL.applyBan(sender, target, now, newtime1, reason, server)) {

                    sendBanMSG(target, reason, sender);
                    return;
                } else {

                    sendError(sender,"SQL");

                    return;

                }


            case "jours":


                long newtime2 = now + ((pretime*86400) * (recidiv + 1));



                if(PunishSQL.applyBan(sender, target, now, newtime2, reason, server)) {

                    sendBanMSG(target, reason, sender);
                    return;
                } else {

                    sendError(sender,"SQL");

                    return;

                }


            case "heures":


                long newtime3 = now + ((pretime*3600) * (recidiv + 1));


                if(PunishSQL.applyBan(sender, target, now, newtime3, reason, server)) {

                    sendBanMSG(target, reason, sender);
                    return;
                } else {

                    sendError(sender,"SQL");

                    return;

                }


            case "minutes":


                long newtime = now + ((pretime*60) * (recidiv + 1));

                if(PunishSQL.applyBan(sender, target, now, newtime, reason, server)) {


                    sendBanMSG(target, reason, sender);
                    return;
                } else {

                    sendError(sender,"SQL");

                    return;

                }



            case "secondes":


                long newtime4 = now + (pretime * (recidiv + 1));


                if(PunishSQL.applyBan(sender, target, now, newtime4, reason, server)) {


                    sendBanMSG(target, reason, sender);
                    return;
                } else {

                    sendError(sender,"SQL");

                    return;

                }




        }


        return;
    }



    public static void sendError(String ss, String why) {
        ProxiedPlayer s = MainBungee.getINSTANCE().getProxy().getPlayer(ss);

        switch (why) {

            case "SQL":
                s.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cUne erreur de base de données est survenue."));
                break;

            case "ALREADY":
                s.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cCe joueur est déjà sanctionné pour ce type de raison."));
                break;

        }

    }

    public static void sendBanMSG(String p, String r, String s) {
        ProxiedPlayer pp = MainBungee.getINSTANCE().getProxy().getPlayer(p);
        ProxiedPlayer ss = MainBungee.getINSTANCE().getProxy().getPlayer(s);
        if(pp != null) {


            if (pp.isConnected()) {


                pp.disconnect(TextComponent.fromLegacyText("§cVous avez été banni\n§cInfraction: " + r));
            }
        }

        if(ss != null) {
            if (ss.isConnected()) {

                ss.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §aLa sanction a été appliquée !"));

            }

        }

    }


}





