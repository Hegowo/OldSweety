package io.ishynss.sweety.system.spigot.Listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.ishynss.sweety.system.spigot.MainSpigot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class Inventory implements Listener {

    private MainSpigot main;


    public Inventory(MainSpigot main) {
        this.main = main;
    }


    @EventHandler
    public void onInv(InventoryClickEvent e) {

        if(e.getCurrentItem() == null) return;

        if(!e.getCurrentItem().hasItemMeta()) return;

        if(e.getClickedInventory().getName().equals("§dSatsuki §8| §7Vos Amis.")) {
            e.setCancelled(true);

            String ps = e.getCurrentItem().getItemMeta().getDisplayName().replace("§8➟ §d ", "");
            List<String> ps1 = e.getCurrentItem().getItemMeta().getLore();
            String ps2 = ps1.toString().replaceAll(", ", " ");
            if(ps2.contains("§7Serveur:")) {



                String ps4 = ps2
                        .replace("[§8[§a✔§8] §7Serveur: §d", "")
                        .replaceAll("§d", "")
                        .replace("§7➥ Clic-droit pour rejoindre le serveur", "")
                        .replace("§7➥ Clic-gauche pour inviter au groupe", "")
                        .replace("]", "")
                        .replaceAll(" ", "");


                if (e.getClick().isLeftClick()) {

                }


                if (e.getClick().isRightClick()) {

                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF(ps4);

                    // If you don't care about the player
                    // Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                    // Else, specify them
                    Player player = (Player) e.getWhoClicked();

                    player.sendPluginMessage(MainSpigot.getPlugin(), "BungeeCord", out.toByteArray());


                }
            }



        }


    }

}
