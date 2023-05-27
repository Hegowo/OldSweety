package io.ishynss.sweety.system.bungee.Listeners;

import io.ishynss.sweety.system.bungee.MainBungee;
import io.ishynss.sweety.system.bungee.Punish.PunishSQL;
import io.ishynss.sweety.system.bungee.sql.DatabaseManager;

import io.ishynss.sweety.system.bungee.utils.SQL;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

public class CheckExpiration {


    public static void check(String target) {

        try {

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `Sanctions` WHERE (`Player` = ?) AND (`ACTIVE` = ?)");
            ps.setString(1, target);
            ps.setInt(2, 1);
            ResultSet rs = ps.executeQuery();


            String ok1 = "by";
            long ok3 = 0;


            while(rs.next()) {

                long now = Instant.now().getEpochSecond();
                ok1 = rs.getString("SSID");
                ok3 = rs.getLong("DATE-END");

                if(now >= ok3) {
                    MainBungee.getINSTANCE().getProxy().broadcast(TextComponent.fromLegacyText("TEST " + ok1 + " END: " + ok3 + " NOW: " + System.currentTimeMillis()));
                    PunishSQL.setInactiv(target, ok1);
                }





                assert false;

            }

            rs.close();
            ps.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void isBanned(String name) {
        if(PunishSQL.getActiveBanCount(name) != 0) {

            ProxiedPlayer pp = MainBungee.getINSTANCE().getProxy().getPlayer(name);

            List<String> bans = PunishSQL.getActiveBans(name, pp);

            MainBungee.getINSTANCE().getProxy().broadcast(TextComponent.fromLegacyText("TEST2 " + Arrays.toString(bans.toArray())));


            List<Long> timedd = new ArrayList<>();




            for(String okgoogle2 : bans) {

                Long time = PunishSQL.SSWasSsEndLong.get(okgoogle2);
                timedd.add(time);

                MainBungee.getINSTANCE().getProxy().broadcast(TextComponent.fromLegacyText("TESTX " + time));


            }

            Long endgame = Collections.max(timedd);


            String endgame1 = SQL.getDateFromTimeStamp(endgame);



            List<String> okbg = new ArrayList<>();

            okbg.add("§cVous avez été banni jusqu'au " + endgame1 + " §c!\n \n");


            for(String okgoogle : bans) {

                okbg.add("§c- " + PunishSQL.SSWasSSBy.get(okgoogle) + " §cInfraction: " + PunishSQL.SSWasSsWhy.get(okgoogle) + "\n");

            }

            String ban = okbg.toString().replaceAll(", ", "").replace("[", "").replace("]", "");
            pp.disconnect(TextComponent.fromLegacyText(ban));

        }


    }
}
