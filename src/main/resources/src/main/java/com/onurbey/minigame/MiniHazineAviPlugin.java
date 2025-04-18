package com.onurbey.minigame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class MiniHazineAvıPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MiniHazineAvı Plugin yüklendi.");
    }

    @Override
    public void onDisable() {
        getLogger().info("MiniHazineAvı Plugin kapandı.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("startgame") && sender.hasPermission("minigame.start")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location loc = player.getLocation();
                player.sendMessage("§aHazine avı başladı! §e10 altın külçesini toplayın.");

                // 10 altın külçesini rastgele yayıyoruz
                Random rnd = new Random();
                for (int i = 0; i < 10; i++) {
                    double dx = rnd.nextDouble() * 20 - 10;
                    double dz = rnd.nextDouble() * 20 - 10;
                    Location dropLoc = loc.clone().add(dx, 1, dz);
                    Item dropped = player.getWorld().dropItem(dropLoc, new ItemStack(Material.GOLD_INGOT));
                    dropped.setPickupDelay(0);
                }

                // 60 saniye sonra oyunu bitirip kontrol edelim
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        int count = 0;
                        for (ItemStack stack : player.getInventory().getContents()) {
                            if (stack != null && stack.getType() == Material.GOLD_INGOT) {
                                count += stack.getAmount();
                            }
                        }
                        if (count >= 10) {
                            player.sendMessage("§6Tebrikler! §a10 altın külçesini topladın.");
                        } else {
                            player.sendMessage("§cSüre doldu! Sadece " + count + " külçe topladın.");
                        }
                    }
                }.runTaskLater(this, 20 * 60);

                return true;
            }
        }
        return false;
    }
}
