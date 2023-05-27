package io.ishynss.sweety.system.spigot.Utils;

import io.ishynss.sweety.system.spigot.MainSpigot;
import io.ishynss.sweety.system.spigot.sql.DatabaseManager;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


public class SQL {

    public static Map<String, String> SSWasSSBy = new HashMap();
    public static Map<String, String> SSWasSsWhy = new HashMap();
    public static Map<String, Long> SSWasSsEnd = new HashMap();
    public static Map<String, Long> SSWasSsEndLong = new HashMap();


    /**
     * SETTER
     */


    public static void setCerise(Player sender, String target, double amount) {
        if (hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `" + MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Money3") + "` = ? WHERE `Player` = ?");

                ps.setDouble(1, amount);
                ps.setString(2, target);

                ps.execute();

                ps.close();


                connection.close();
                if (sender != null)
                    sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.SetMoney3")
                            .replaceAll("%target", target)
                            .replaceAll("%amount", String.valueOf(amount))
                    );


            } catch (SQLException e) {
                e.printStackTrace();
                if (sender != null)
                    sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Error"));

            }
        } else {
            if (sender != null)
                sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.PlayerDontExist"));
        }

    }


    public static void setYen(Player sender, String target, double amount) {
        if (hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `" + MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Money1") + "` = ? WHERE `Player` = ?");

                ps.setDouble(1, amount);
                ps.setString(2, target);

                ps.execute();

                ps.close();
                connection.close();
                if (sender != null)
                    sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.SetMoney1")
                            .replaceAll("%target", target)
                            .replaceAll("%amount", String.valueOf(amount))
                    );


            } catch (SQLException e) {
                e.printStackTrace();
                if (sender != null)
                    sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Error"));

            }
        } else {
            if (sender != null)
                sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.PlayerDontExist"));
        }

    }


    public static void setSagesse(Player sender, String target, double amount) {
        if (hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `" + MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Money2") + "` = ? WHERE `Player` = ?");

                ps.setDouble(1, amount);
                ps.setString(2, target);

                ps.execute();


                ps.close();

                connection.close();
                if (sender != null)
                    sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.SetMoney2")
                            .replaceAll("%target", target)
                            .replaceAll("%amount", String.valueOf(amount))
                    );


            } catch (SQLException e) {
                e.printStackTrace();
                if (sender != null)
                    sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Error"));

            }
        } else {
            if (sender != null)
                sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.PlayerDontExist"));
        }

    }


    /**
     * GETTER
     */

    public static String getDateFromTimeStamp(Long ts) {

        Date aujourdhui = new Date(ts * 1000);
        SimpleDateFormat formater = null;

        formater = new SimpleDateFormat("dd/MM/yyyy 'à' HH:mm:ss", new Locale(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.TimestampLanguage"), MainSpigot.getPlugin().getConfig().getString("Messages.SQL.TimestampCountry")));

        return formater.format(aujourdhui);
    }


    public static String getDate() {
        Date aujourdhui = new Date();
        SimpleDateFormat formater = null;

        formater = new SimpleDateFormat("EEEEE d MMMMM yyyy 'à' HH:mm:ss", new Locale(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.TimestampLanguage"), MainSpigot.getPlugin().getConfig().getString("Messages.SQL.TimestampCountry")));

        return formater.format(aujourdhui);
    }


    public static int getPlayTime(String target, Player sender) {
        if (hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `PLAYTIME` FROM `General` WHERE `Player` = ?");
                ps.setString(1, target);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int ok = rs.getInt("PLAYTIME");

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            if (sender != null)
                sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.PlayerDontExist"));
            return 0;
        }
        return 0;
    }


    public static List<String> getFriends(String target, Player sender) {
        if (hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `VALUE-NAME` FROM `Friends` WHERE `KEY-NAME` = ?");
                ps.setString(1, target);
                ResultSet rs = ps.executeQuery();

                List<String> IPS = new ArrayList<>();
                String ok = "nothing";

                while (rs.next()) {
                    ok = rs.getString("VALUE-NAME");


                    assert false;
                    IPS.add(ok);

                }

                rs.close();
                ps.close();
                connection.close();

                return IPS;

            } catch (SQLException e) {
                e.printStackTrace();
                if (sender != null) sender.sendMessage("§dSatsuki §8| §cUne erreur de type SQL s'est produite.");

            }
        } else {
            if (sender != null) sender.sendMessage("§dSatsuki §8| §cCe joueur n'existe pas.");
        }

        return null;
    }


    public static double getCerise(String target, Player sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `" + MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Money3") + "` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {
                    double ok = rs.getInt(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Money3").toString());

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;

                }

            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Error"));

            }
        } else {
            if(sender != null) sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.PlayerDontExist"));
        }

        return 0;
    }


    public static double getYen(String target, Player sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `" + MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Money1") + "` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();


                while(rs.next()) {
                    double ok = rs.getInt(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Money1").toString());

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;
                }


            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Error"));

            }
        } else {
            if(sender != null) sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.PlayerDontExist"));
        }

        return 0;
    }

    public static double getSagesse(String target, Player sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `" + MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Money2") + "` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {


                    double ok = rs.getInt(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Money2").toString());

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;
                }



            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Error"));

            }
        } else {
            if(sender != null) sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.PlayerDontExist"));
        }

        return 0;
    }




    public static String getServer(String target, Player sender) {
        if (hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `SERVER` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {


                    String ok = rs.getString("SERVER");

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;
                }


            } catch (SQLException e) {
                e.printStackTrace();
                if (sender != null) sender.sendMessage("§dSatsuki §8| §cUne erreur de type SQL s'est produite.");

            }
        } else {
            if (sender != null) sender.sendMessage("§dSatsuki §8| §cCe joueur n'existe pas.");
        }

        return null;
    }
    


    /**
     * BOOLEAN
     */


    public static void check(String target) {

        try {

            Connection connection = io.ishynss.sweety.system.spigot.sql.DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `Sanctions` WHERE (`Player` = ?) AND (`ACTIVE` = ?)");
            ps.setString(1, target);
            ps.setInt(2, 1);
            ResultSet rs = ps.executeQuery();


            String ok1 = "by";
            long ok3 = 0;


            while (rs.next()) {

                long now = Instant.now().getEpochSecond();
                ok1 = rs.getString("SSID");
                ok3 = rs.getLong("DATE-END");

                if (now >= ok3) {
                    SQL.setInactiv(target, ok1);
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


    public static void setInactiv(String target, String ok1) {
        // UPDATE `Sanctions` SET `ACTIVE`= ? WHERE  WHERE (`Player` = ?) AND (`ACTIVE` = ?)
        try {

            Connection connection = io.ishynss.sweety.system.spigot.sql.DatabaseManager.Osaka2.getDatabaseAccess().getConnection();
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


    public static long getMute(String target) {
        List<String> mutes = SQL.getActiveMutes(target, null);


        List<Long> timedd = new ArrayList<>();


        for (String okgoogle2 : mutes) {

            Long time = SQL.SSWasSsEndLong.get(okgoogle2);
            long now = Instant.now().getEpochSecond();

            long ok = now - time;
            timedd.add(ok);


        }

        Long endgame = (Collections.max(timedd) / 60);
        return endgame;

    }


    public static Integer getActiveMuteCount(String target) {
        try {

            Connection connection = io.ishynss.sweety.system.spigot.sql.DatabaseManager.Osaka2.getDatabaseAccess().getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM `Sanctions` WHERE (`Player` = ?) AND (`ACTIVE` = ?) AND (`Type` = ?)");
            ps.setString(1, target);
            ps.setInt(2, 1);
            ps.setString(2, "Mute");
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


    public static List<String> getActiveMutes(String target, Player sender) {
        if (io.ishynss.sweety.system.spigot.Utils.SQL.hasAccountName(target, sender)) {
            try {

                Connection connection = io.ishynss.sweety.system.spigot.sql.DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT * FROM `Sanctions` WHERE (`Player` = ?) AND (`ACTIVE` = ?) AND (`Type` = ?)");
                ps.setString(1, target);
                ps.setInt(2, 1);
                ps.setString(3, "Mutes");
                ResultSet rs = ps.executeQuery();

                List<String> ALL = new ArrayList<>();

                String ok1 = "by";
                long ok3 = 0;
                String ok5 = "reason";
                String ok7 = "id";


                while (rs.next()) {

                    ok7 = rs.getString("SSID");
                    String id = ok7;

                    ok1 = rs.getString("BY");
                    ok3 = rs.getLong("DATE-END");
                    ok5 = rs.getString("REASON");

                    SSWasSSBy.put(id, ok1);
                    SSWasSsEnd.put(id, ok3);
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

            }
        }
        return null;
    }


    public static boolean hasAccountName(String target, Player sender) {
        try {
            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT `Player` FROM `General` WHERE `Player` = '" + target + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                connection.close();
                ps.close();
                rs.close();
                return true;
            } else {
                connection.close();
                ps.close();
                rs.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (sender != null) sender.sendMessage(MainSpigot.getPlugin().getConfig().getString("Messages.SQL.Error"));
            return false;
        }
    }


    public static boolean hasAccount(UUID uuid) {
        try {
            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT `Player` FROM `General` WHERE `UUID` = ?");
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                connection.close();
                ps.close();
                rs.close();
                return true;
            } else {
                connection.close();
                ps.close();
                rs.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("TON CHECK PUE LA MERDE");
        }
        return false;
    }

}

