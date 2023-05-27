package io.ishynss.sweety.system.bungee.Punish;


import io.ishynss.sweety.system.bungee.MainBungee;
import io.ishynss.sweety.system.bungee.sql.DatabaseManager;
import io.ishynss.sweety.system.bungee.utils.SQL;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PunishSQL {

    public static Map<String, String> SSWasSSBy = new HashMap();
    public static Map<String, String> SSWasSsWhy = new HashMap();
    public static Map<String, String > SSWasSsEnd = new HashMap();
    public static Map<String, Long> SSWasSsEndLong = new HashMap();



    public static boolean applyMute(String sender, String target, long now, long newtime, String reason, String server) {
        try {

            String generatedString = org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(30);

            ProxiedPlayer p = MainBungee.getINSTANCE().getProxy().getPlayer(target);

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();
            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO `Sanctions`(`SSID`, `Type`, `UUID`, `Player`, `BY`, `DATE-APPLY`, `REASON`, `DATE-END`, `SERVER`, `ACTIVE`) VALUES (?,?,?,?,?,?,?,?,?,?)");
            ps2.setString(1, generatedString);
            ps2.setString(2, "Mute");
            ps2.setString(3, SQL.getUuid(target, null));
            ps2.setString(4, target);
            ps2.setString(5, sender);
            ps2.setLong(6, now);
            ps2.setString(7, reason);
            ps2.setLong(8, newtime);
            ps2.setString(9, server);
            ps2.setInt(10, 1);

            ps2.execute();
            ps2.close();
            connection.close();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean applyBan(String sender, String target, long now, long newtime, String reason, String server) {
        try {
            ProxiedPlayer p = MainBungee.getINSTANCE().getProxy().getPlayer(target);

            String generatedString = org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(30);

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();
            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO `Sanctions`(`SSID`, `Type`, `UUID`, `Player`, `BY`, `DATE-APPLY`, `REASON`, `DATE-END`, `SERVER`, `ACTIVE`) VALUES (?,?,?,?,?,?,?,?,?,?)");
            ps2.setString(1, generatedString);

            ps2.setString(2, "Ban");
            ps2.setString(3, SQL.getUuid(target, null));
            ps2.setString(4, target);
            ps2.setString(5, sender);
            ps2.setLong(6, now);
            ps2.setString(7, reason);
            ps2.setLong(8, newtime);
            ps2.setString(9, server);
            ps2.setInt(10, 1);

            ps2.execute();
            ps2.close();
            connection.close();

            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Integer getRedicive(String target, String reason) {
        try {

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM `Sanctions` WHERE  (`Player` = ?) AND (`REASON` = ?)");
            ps.setString(1, target);
            ps.setString(2, reason);
            ResultSet rs = ps.executeQuery();

            rs.next();
            int rowCount = rs.getInt(1);

            rs.close();
            ps.close();
            connection.close();
            return rowCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }


    public static Integer getActiveBanCount(String target) {
        try {

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM `Sanctions` WHERE (`Player` = ?) AND (`ACTIVE` = ?) AND (`Type` = ?)");
            ps.setString(1, target);
            ps.setString(3, "Ban");
            ps.setInt(2, 1);
            ResultSet rs = ps.executeQuery();

            rs.next();
            int rowCount = rs.getInt(1);

            rs.close();
            ps.close();
            connection.close();
            return rowCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static List<String> getActiveBans(String target, ProxiedPlayer sender) {
        if(SQL.hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT * FROM `Sanctions` WHERE (`Player` = ?) AND (`ACTIVE` = ?) AND (`Type` = ?)");
                ps.setString(1, target);
                ps.setInt(2, 1);
                ps.setString(3, "Ban");
                ResultSet rs = ps.executeQuery();

                List<String> ALL = new ArrayList<>();

                String ok1 = "by";
                long ok3 = 0;
                String ok5 = "reason";
                String ok7  = "id";


                while(rs.next()) {

                    ok7 = rs.getString("SSID");
                    String id = ok7;

                    ok1 = rs.getString("BY");
                    ok3 = rs.getLong("DATE-END");
                    ok5 = rs.getString("REASON");

                    SSWasSSBy.put(id, ok1);
                    SSWasSsEnd.put(id, SQL.getDateFromTimeStamp(ok3));
                    SSWasSsWhy.put(id, ok5);
                    SSWasSsEndLong.put(id, ok3);




                    assert false;
                    ALL.add(id);

                }

                rs.close();
                ps.close();
                connection.close();

                return ALL;

            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cUne erreur de type SQL s'est produite."));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §cCe joueur n'existe pas."));
        }

        return null;
    }

    public static void setInactiv(String target, String ok1) {
        // UPDATE `Sanctions` SET `ACTIVE`= ? WHERE  WHERE (`Player` = ?) AND (`ACTIVE` = ?)
        try {

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();
            PreparedStatement ps2 = connection.prepareStatement("UPDATE `Sanctions` SET `ACTIVE`= ? WHERE (`Player` = ?) AND (`SSID` = ?)");
            ps2.setInt(1, 0);

            ps2.setString(2, target);
            ps2.setString(3, ok1);

            ps2.execute();
            ps2.close();
            connection.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
