package io.ishynss.sweety.system.spigot.Listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.ishynss.sweety.system.spigot.MainSpigot;
import io.ishynss.sweety.system.spigot.Utils.ItemBuilder;
import io.ishynss.sweety.system.spigot.sql.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class PluginMessage implements Listener, PluginMessageListener {

    private MainSpigot main;


    public PluginMessage(MainSpigot main) {
        this.main = main;
    }


    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {


        if (s.equalsIgnoreCase("GUI")) {
            try {
                ByteArrayDataInput in = ByteStreams.newDataInput(bytes);

                String subchannel = in.readUTF();


                if (subchannel.equals("Friends")) {

                    ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput(bytes);


                    String sub = byteArrayDataInput.readUTF();
                    String Reported = byteArrayDataInput.readUTF();
                    String EZ = byteArrayDataInput.readUTF();

                    Player pd = Bukkit.getPlayerExact(EZ);

                    if (pd.isOnline()) {

                        Reported = Reported.replace("[", "").replace("]", "");

                        List<String> all = new ArrayList<String>(Arrays.asList(Reported.split(", ")));


                        final Inventory friend = Bukkit.createInventory((InventoryHolder)null, 54, "§dSatsuki §8| §7Vos Amis.");



                        List<String> online = new ArrayList<>();
                        List<String> offline = new ArrayList<>();
                        Map<String, String> lastlog = new HashMap<>();
                        Map<String, String> server = new HashMap<>();

                        for (String pl : all) {


                            Connection connection = DatabaseManager.Osaka2.getDatabaseAccess().getConnection();
                            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `General` WHERE `Player` = ?");

                            ps.setString(1, String.valueOf(pl));

                            ResultSet rs = ps.executeQuery();

                            while(rs.next()) {

                                int okk = rs.getInt("ONLINE");
                                String bzz = rs.getString("LASTLOGIN");
                                String bzz2 = rs.getString("SERVER");


                                if (okk == 1) {
                                    online.add(pl);
                                    server.put(pl, bzz2);

                                } else {
                                    offline.add(pl);
                                    lastlog.put(pl, bzz);
                                }

                            }
                            rs.close();
                            ps.close();
                            connection.close();


                        }
                            int i = 0;


                            if(online.size() != 0) {
                                for (String p5 : online) {

                                    List<String> lore = new ArrayList<>();

                                    lore.add("§8[§a✔§8] §7Serveur: §d" + server.get(p5));
                                    lore.add("§d");
                                    lore.add("§7➥ Clic droit pour rejoindre");
                                    lore.add("§7➥ Clic gauche pour inviter au groupe");

                                    friend.setItem(i, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner(p5).setLoreWithList(lore).setInfinityDurability().setDisplayName("§8➟ §d " + p5).flag(ItemFlag.HIDE_UNBREAKABLE).flag(ItemFlag.HIDE_ATTRIBUTES).build());


                                    i++;
                                }


                            }

                            int i2 = 0;
                            if(online.size() != 0) {
                                int i3 = online.size() / 8;

                                if (i3 <= 1) {
                                    i2 = 9;
                                }

                                if (i3 <= 2) {
                                    if (i3 > 1) {
                                        i2 = 18;
                                    }
                                }

                                if (i3 <= 3) {
                                    if (i3 > 2) {
                                        i2 = 27;
                                    }
                                }

                                if (i3 <= 4) {
                                    if (i3 > 3) {
                                        i2 = 36;
                                    }
                                }


                                if (i3 <= 5) {
                                    if (i3 > 4) {
                                        i2 = 45;
                                    }
                                }

                            }

                            for (String p6 : offline) {

                                friend.setItem(i2, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner(p6).setInfinityDurability().lore("§8[§c✕§8] §7Dernière Connexion: §d" + lastlog.get(p6)).setDisplayName("§8➟ §d " + p6).flag(ItemFlag.HIDE_UNBREAKABLE).flag(ItemFlag.HIDE_ATTRIBUTES).build());


                                i2++;
                            }



                            pd.openInventory(friend);



                    }


                }


                // OPEN SS


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
