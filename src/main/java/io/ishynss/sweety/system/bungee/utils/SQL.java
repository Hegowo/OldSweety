package io.ishynss.sweety.system.bungee.utils;

import io.ishynss.sweety.system.bungee.MainBungee;
import io.ishynss.sweety.system.bungee.sql.DatabaseManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SQL {

    public static Map<String, String> WasSSBy = new HashMap();
    public static Map<String, String> WasSsWhy = new HashMap();
    public static Map<String, Long> WasSsStart = new HashMap();
    public static Map<String, Long> WasSsEnd = new HashMap();
    public static Map<String, String> WasSsOnServer = new HashMap();
    public static Map<String, Integer> WasSsActive = new HashMap();
    public static Map<String, String> WasSsType = new HashMap();




    public static void createTable() {
        try {

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `General` ( `UUID` varchar(50) , `Player` varchar(17) , `RANK` varchar(15) , `PREFIX` varchar(22) , `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money3") + "` DOUBLE NOT NULL , `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money1") + "` DOUBLE NOT NULL , `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money2") + "` DOUBLE NOT NULL , `REG-IP` varchar(16) , `REG-DATE` varchar(55), `PLAYEDGAMES` varchar(55), `LASTLOGIN` varchar(55), `PLAYTIME` INT NOT NULL, `ONLINE` INT NOT NULL, `SERVER` varchar(55))");
            PreparedStatement ps2 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `IPS` ( `UUID` varchar(50) , `Player` varchar(17) , `IP` varchar(16))");
            PreparedStatement ps3 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Friends` ( `KEY-UUID` varchar(60) , `KEY-NAME` varchar(16), `VALUE-UUID` varchar(60), `VALUE-NAME` varchar(16))");
            PreparedStatement ps4 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Sanctions` ( `SSID` varchar(32) , `Type` varchar(7) , `UUID` varchar(60) , `Player` varchar(16), `BY` varchar(60), `DATE-APPLY` LONG NOT NULL, `REASON` varchar(30), `DATE-END` LONG NOT NULL, `SERVER` varchar(20), `ACTIVE` INT NOT NULL)");

            ps2.execute();
            ps3.execute();
            ps4.execute();
            ps.execute();

            ps.close();
            ps2.close();
            ps4.close();
            ps3.close();


            connection.close();
            System.out.println("§a[SweetySystem] SQL - Tables created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("§c[SweetySystem] SQL - Error during tables creation.");

        }

    }





    public static void createPlayerAccount(ProxiedPlayer p, UUID pid) {
        try {

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `General`(`UUID`, `Player`, `RANK`, `PREFIX`, `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money3") + "`, `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money1") + "`, `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money2") + "`, `REG-IP`, `REG-DATE`, `PLAYEDGAMES`, `LASTLOGIN`, `PLAYTIME`, `ONLINE`, `SERVER`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");


            ps.setString(1, pid.toString());
            ps.setString(2, p.getName());
            ps.setString(3, "Non Défini");
            ps.setString(4, "§8Non Défini §8");
            ps.setDouble(5, MainBungee.getINSTANCE().theConfig.getDouble("Messages.SQL.DefaultValueMoney3"));
            ps.setDouble(6, MainBungee.getINSTANCE().theConfig.getDouble("Messages.SQL.DefaultValueMoney1"));
            ps.setDouble(7, MainBungee.getINSTANCE().theConfig.getDouble("Messages.SQL.DefaultValueMoney2"));
            ps.setString(8, String.valueOf(p.getAddress().getAddress()).replaceAll("/", ""));
            ps.setString(9, getDate());
            ps.setInt(10, 0);
            ps.setString(11, getDate());
            ps.setInt(12, 1);
            ps.setInt(13, 1);
            ps.setString(14, "none");


            ps.execute();
            ps.close();
            connection.close();


        } catch (SQLException e) {

            e.printStackTrace();
        }
        addNewIP(p);

    }




    public static void addNewIP(ProxiedPlayer p) {
        try {

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT `IP` FROM `IPS` WHERE `IP` = ?");
            ps.setString(1, String.valueOf(p.getAddress().getAddress()).replaceAll("/", ""));
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                ps.close();
                rs.close();

            }else {
                ps.close();
                rs.close();

                PreparedStatement ps2 = connection.prepareStatement("INSERT INTO `IPS`(`UUID`, `Player`, `IP`) VALUES (?,?,?)");
                ps2.setString(1, p.getUniqueId().toString());
                ps2.setString(2, p.getName());
                ps2.setString(3, String.valueOf(p.getAddress().getAddress()).replaceAll("/" , ""));

                ps2.execute();
                ps2.close();
            }



            connection.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public static void updateLastLogin(ProxiedPlayer p) {
        if(hasAccount(p.getUniqueId())) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `LASTLOGIN`= ? WHERE `UUID` = ?");


                ps.setString(1, getDate());
                ps.setString(2, p.getUniqueId().toString());

                ps.execute();

                ps.close();

                connection.close();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            p.disconnect(TextComponent.fromLegacyText("§dSatsuki §8| §cVotre compte ne semble pas exister."));
        }
    }




     public static void updateName(ProxiedPlayer p) {
        if(hasAccount(p.getUniqueId())) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `Player`= ? WHERE `UUID` = ?");

                PreparedStatement ps2 = connection.prepareStatement("UPDATE `IPS` SET `Player`= ? WHERE `UUID` = ?");
                PreparedStatement ps3 = connection.prepareStatement("UPDATE `Friends` SET `KEY-NAME`= ? WHERE `KEY-UUID` = ?");
                PreparedStatement ps4 = connection.prepareStatement("UPDATE `Friends` SET `VALUE-NAME`= ? WHERE `VALUE-UUID` = ?");
                PreparedStatement ps5 = connection.prepareStatement("UPDATE `Sanctions` SET `Player`= ? WHERE `UUID` = ?");

                ps5.setString(1, p.getName());
                ps5.setString(2, p.getUniqueId().toString());

                ps2.setString(1, p.getName());
                ps2.setString(2, p.getUniqueId().toString());

                ps.setString(1, p.getName());
                ps.setString(2, p.getUniqueId().toString());

                ps3.setString(1, p.getName());
                ps3.setString(2, p.getUniqueId().toString());

                ps4.setString(1, p.getName());
                ps4.setString(2, p.getUniqueId().toString());

                ps.execute();
                ps2.execute();
                ps3.execute();
                ps4.execute();

                ps.close();
                ps2.close();
                ps3.close();
                ps4.close();

                connection.close();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            p.disconnect(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.YourAccountNotExist")));
        }
    }




    public static void updateAllOnline(boolean b) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `ONLINE`= ?");

                if(b) {
                    ps.setInt(1, 1);
                } else {
                    ps.setInt(1, 0);
                }

                ps.execute();

                ps.close();

                connection.close();


            } catch (SQLException e) {
                e.printStackTrace();
            }
    }




    public static void updateOnline(ProxiedPlayer p, boolean b) {
        if(hasAccount(p.getUniqueId())) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `ONLINE`= ? WHERE `UUID` = ?");

                if(b) {
                    ps.setInt(1, 1);
                } else {
                    ps.setInt(1, 0);
                }

                ps.setString(2, p.getUniqueId().toString());

                ps.execute();

                ps.close();

                connection.close();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            p.disconnect(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.YourAccountNotExist")));
        }
    }






    /**
     *
     *
     *
     *  SETTER
     *
     *
     *
     */



    public static void setServer(ProxiedPlayer sender, String target, String amount) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `SERVER` = ? WHERE `Player` = ?");

                ps.setString(1, amount);
                ps.setString(2, target);

                ps.execute();


                connection.close();


            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

    }





    public static void setCerise(ProxiedPlayer sender, String target, double amount) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money3") + "` = ? WHERE `Player` = ?");

                ps.setDouble(1, amount);
                ps.setString(2, target);

                ps.execute();


                connection.close();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.SetMoney3")
                        .replaceAll("%target", target)
                        .replaceAll("%amount", String.valueOf(amount))
                ));


            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

    }






    public static void setYen(ProxiedPlayer sender, String target, double amount) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money1") + "` = ? WHERE `Player` = ?");

                ps.setDouble(1, amount);
                ps.setString(2, target);

                ps.execute();


                connection.close();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.SetMoney1")
                        .replaceAll("%target", target)
                        .replaceAll("%amount", String.valueOf(amount))
                ));



            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

    }






    public static void setSagesse(ProxiedPlayer sender, String target, double amount) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `" +  MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money2") + "` = ? WHERE `Player` = ?");

                ps.setDouble(1, amount);
                ps.setString(2, target);

                ps.execute();


                connection.close();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.SetMoney2")
                        .replaceAll("%target", target)
                        .replaceAll("%amount", String.valueOf(amount))
                ));


            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

    }






    public static void setPlayTime(ProxiedPlayer sender, Integer pt) {
        try {

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("UPDATE `General` SET `PLAYTIME` = ? WHERE `Player` = ?");

            ps.setInt(1, pt);
            ps.setString(2, sender.getName());

            ps.execute();


            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

        }

    }





    public static boolean addFriends(UUID keyuuid, String keyname,  UUID valueuuid, String valuename) {
        try {

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("INSERT INTO `Friends`(`KEY-UUID`, `KEY-NAME`, `VALUE-UUID`, `VALUE-NAME`) VALUES (?,?,?,?)");
            ps.setString(1, keyuuid.toString());
            ps.setString(2, keyname);
            ps.setString(3, valueuuid.toString());
            ps.setString(4, valuename);

            ps.execute();
            ps.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }







    public static boolean removeFriends(String keyname, String valuename) {
        try {

            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("DELETE FROM `Friends` WHERE `KEY-NAME` = ? AND `VALUE-NAME` = ?");

            ps.setString(1, keyname);
            ps.setString(2, valuename);

            ps.execute();
            ps.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    /**
     *
     *
     *
     *  GETTER
     *
     *
     *
     */


    public static String getDateFromTimeStamp(Long ts) {

        Date aujourdhui = new Date(ts*1000);
        SimpleDateFormat formater = null;

        formater = new SimpleDateFormat("dd/MM/yyyy 'à' HH:mm:ss", new Locale(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.TimestampLanguage"),MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.TimestampCountry")));

        return formater.format(aujourdhui);
    }



    public static String getDate() {
        Date aujourdhui = new Date();
        SimpleDateFormat formater = null;

        formater = new SimpleDateFormat("EEEEE d MMMMM yyyy 'à' HH:mm:ss", new Locale(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.TimestampLanguage"),MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.TimestampCountry")));

        return formater.format(aujourdhui);
    }



    public static int getPlayTime(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `PLAYTIME` FROM `General` WHERE `Player` = ?");
                ps.setString(1, target);
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {
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
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
            return 0;
        }
        return 0;
    }




    public static List<String> getAlts(String target, ProxiedPlayer sender) {
        if(hasAccountIP(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `Player` FROM `IPS` WHERE `IP` = ?");
                ps.setString(1, target);
                ResultSet rs = ps.executeQuery();

                List<String> IPS = new ArrayList<>();
                String ok = "nothing";

                while(rs.next()) {
                    ok = rs.getString("Player");


                    assert false;
                    IPS.add(ok);

                }

                rs.close();
                ps.close();
                connection.close();

                return IPS;

            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.IPDontExist")));
        }

        return null;
    }




    public static List<String> getFriends(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `VALUE-NAME` FROM `Friends` WHERE `KEY-NAME` = ?");
                ps.setString(1, target);
                ResultSet rs = ps.executeQuery();

                List<String> IPS = new ArrayList<>();
                String ok = "nothing";

                while(rs.next()) {
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
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return null;
    }


    public static List<String> getStaffs(ProxiedPlayer sender) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `Player` FROM `General` WHERE (`RANK` = ? OR `RANK` = ? OR `RANK` = ? OR `RANK` = ? OR `RANK` = ? OR `RANK` = ? OR `RANK` = ? OR `RANK` = ? OR `RANK` = ?)");
                ps.setString(1, MainBungee.getINSTANCE().theConfig.getString("Staff.1"));
                ps.setString(2, MainBungee.getINSTANCE().theConfig.getString("Staff.2"));
                ps.setString(3, MainBungee.getINSTANCE().theConfig.getString("Staff.3"));
                ps.setString(4, MainBungee.getINSTANCE().theConfig.getString("Staff.4"));
                ps.setString(5, MainBungee.getINSTANCE().theConfig.getString("Staff.5"));
                ps.setString(6, MainBungee.getINSTANCE().theConfig.getString("Staff.6"));
                ps.setString(7, MainBungee.getINSTANCE().theConfig.getString("Staff.7"));
                ps.setString(8, MainBungee.getINSTANCE().theConfig.getString("Staff.8"));
                ps.setString(9, MainBungee.getINSTANCE().theConfig.getString("Staff.9"));
                ResultSet rs = ps.executeQuery();

                List<String> IPS = new ArrayList<>();
                String ok = "Empty";

                while(rs.next()) {
                    ok = rs.getString("Player");


                    assert false;
                    IPS.add(ok);

                }

                rs.close();
                ps.close();
                connection.close();

                return IPS;

            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }

        return null;
    }


    public static List<String> getSanctions(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT * FROM `Sanctions` WHERE `Player` = ?");
                ps.setString(1, target);
                ResultSet rs = ps.executeQuery();

                List<String> ALL = new ArrayList<>();

                String ok1 = "by";
                String ok2 = "server";
                long ok3 = 0;
                long ok4 = 0;
                String ok5 = "reason";
                int ok6 = 1;
                String ok7 = "reason";
                String ok8 = "reason";

                while(rs.next()) {

                    ok7 = rs.getString("SSID");
                    String id = ok7;

                    ok1 = rs.getString("BY");
                    ok2 = rs.getString("SERVER");
                    ok3 = rs.getLong("DATE-END");
                    ok4 = rs.getLong("DATE-APPLY");
                    ok5 = rs.getString("REASON");
                    ok6 = rs.getInt("ACTIVE");
                    ok8 = rs.getString("Type");

                    WasSSBy.put(id, ok1);
                    WasSsOnServer.put(id, ok2);
                    WasSsEnd.put(id, ok3);
                    WasSsStart.put(id, ok4);
                    WasSsWhy.put(id, ok5);

                    WasSsActive.put(id, ok6);
                    WasSsType.put(id, ok8);




                    assert false;
                    ALL.add(id);

                }

                rs.close();
                ps.close();
                connection.close();

                return ALL;

            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return null;
    }





    public static List<String> getIp(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `IP` FROM `IPS` WHERE `Player` = ?");
                ps.setString(1, target);
                ResultSet rs = ps.executeQuery();

                List<String> IPS = new ArrayList<>();
                String ok = "nothing";
                
                while(rs.next()) {
                    ok = rs.getString("IP");


                    assert false;
                    IPS.add(ok);

                }

                rs.close();
                ps.close();
                connection.close();

                return IPS;

            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return null;
    }
    




    public static double getCerise(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money3") + "` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {
                    double ok = rs.getInt(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money3").toString());

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;

                }

            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return 0;
    }

    public static String getPrefix(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `PREFIX` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();


                while(rs.next()) {
                    String ok = rs.getString("PREFIX");

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;
                }


            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return "N/A";
    }





    public static String getRank(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `RANK` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();


                while(rs.next()) {
                    String ok = rs.getString("RANK");

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;
                }


            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return "N/A";
    }



    public static double getYen(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money1") + "` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();


                while(rs.next()) {
                    double ok = rs.getInt(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money1").toString());

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;
                }


            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return 0;
    }







    public static String getServer(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `SERVER` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {


                    String ok = rs.getString("SERVER");

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;
                }



            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return null;
    }



    public static String getUuid(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `UUID` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {


                    String ok = rs.getString("UUID");

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;
                }



            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return null;
    }









    public static double getSagesse(String target, ProxiedPlayer sender) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT `" + MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money2") + "` FROM `General` WHERE `Player` = '" + target + "'");
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {


                    double ok = rs.getInt(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money2").toString());

                    rs.close();
                    ps.close();
                    connection.close();

                    return ok;
                }



            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return 0;
    }






    public static void getInfos(ProxiedPlayer sender, String target) {
        if(hasAccountName(target, sender)) {
            try {

                Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT * FROM `General` WHERE `Player` = ?");
                ps.setString(1, target);
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {
                    String rank = rs.getString("RANK");
                    Double cerise = rs.getDouble(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money3").toString());
                    String uuid = rs.getString("UUID");
                    String prefix = rs.getString("PREFIX");
                    Double sagesse = rs.getDouble(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money2").toString());
                    String regip = rs.getString("REG-IP");
                    String playtime = rs.getString("PLAYTIME");
                    String regdate = rs.getString("REG-DATE");
                    Double yen = rs.getDouble(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Money1").toString());
                    String played = rs.getString("PLAYEDGAMES");
                    Integer onlines = rs.getInt("ONLINE");
                    String lastlogin = rs.getString("LASTLOGIN");

                    sender.sendMessage(TextComponent.fromLegacyText("§d"));
                    DecimalFormat df = new DecimalFormat("###.##");
                    sender.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §7Profil de §d" + target));
                    if(onlines == 1) {
                        sender.sendMessage(TextComponent.fromLegacyText("§5- §7Connecté: §aOui"));
                    } else {
                        sender.sendMessage(TextComponent.fromLegacyText("§5- §7Connecté: §cNon"));

                    }
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7Rang: §d" + rank));
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7Prefix: §r" + prefix));
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7Cerise: §d" + df.format(cerise)));
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7Yen: §d" + df.format(yen)));
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7Sagesse: §d" + df.format(sagesse)));
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7Parties Jouées: §d" + played));
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7IP d'Enregistrement: §d" + regip.replaceAll("/", "")));
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7Date d'Enregistrement: §d" + regdate));
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7Dernière Connexion: §d" + lastlogin));
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7Temps de Jeu: §d" + playtime + "§7 minute(s)"));
                    sender.sendMessage(TextComponent.fromLegacyText("§5- §7UUID: §d" + uuid));

                    TextComponent am = new TextComponent(ChatColor.LIGHT_PURPLE + "[?]");
                    am.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adm friends " + target));
                    am.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.WHITE + "Voir les amis de " + target + "?").create())));

                    TextComponent ss = new TextComponent(ChatColor.LIGHT_PURPLE + "[?]");
                    ss.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adm ss " + target));
                    ss.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.WHITE + "Voir les sanctions de " + target + "?").create())));

                    TextComponent ip = new TextComponent(ChatColor.LIGHT_PURPLE + "[?]");
                    ip.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adm ip " + target));
                    ip.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.WHITE + "Voir les IP's de " + target + "?").create())));


                    ((ProxiedPlayer) sender).sendMessage(new TextComponent("§5- §7Sanctions: §d"), ((BaseComponent) ss));
                    ((ProxiedPlayer) sender).sendMessage(new TextComponent("§5- §7Amis: §d"), ((BaseComponent) am));
                    ((ProxiedPlayer) sender).sendMessage(new TextComponent("§5- §7IPs: §d"), ((BaseComponent) ip));

                    sender.sendMessage(TextComponent.fromLegacyText("§d"));
                    rs.close();
                    ps.close();
                    connection.close();
                    return;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));

            }
        } else {
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.PlayerDontExist")));
        }

        return;
    }



    /**
     *
     *
     *
     *  BOOLEAN
     *
     *
     *
     */

    public static boolean hasAccountIP(String target, ProxiedPlayer sender) {
        try {
            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT `IP` FROM `IPS` WHERE `IP` = ?");
            ps.setString(1, target);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                connection.close();
                ps.close();
                rs.close();
                return true;
            }else {
                connection.close();
                ps.close();
                rs.close();
                return false;
            }
        }catch(SQLException e) {
            e.printStackTrace();
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));
            return false;
        }
    }





    public static boolean hasAccountName(String target, ProxiedPlayer sender) {
        try {
            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT `Player` FROM `General` WHERE `Player` = '" + target + "'");
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                connection.close();
                ps.close();
                rs.close();
                return true;
            }else {
                connection.close();
                ps.close();
                rs.close();
                return false;
            }
        }catch(SQLException e) {
            e.printStackTrace();
            if(sender != null) sender.sendMessage(TextComponent.fromLegacyText(MainBungee.getINSTANCE().theConfig.getString("Messages.SQL.Error")));
            return false;
        }
    }





    public static boolean hasAccount(UUID uuid) {
        try {
            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT `Player` FROM `General` WHERE `UUID` = ?");
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                connection.close();
                ps.close();
                rs.close();
                return true;
            }else {
                connection.close();
                ps.close();
                rs.close();
                return false;
            }
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("TON CHECK PUE LA MERDE");
        }
        return false;
    }


    public static boolean getIfPlayerAcceptFriendRequest(ProxiedPlayer p) { // TODO: GET SEETTINGs
        return true;
    }
}
