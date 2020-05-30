package tv.twitch.riespanda.paydays;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public final class Paydays extends JavaPlugin {
    private Economy econ = null;

    @Override
    public void onEnable() {

        if (!setupEconomy()) {
            System.out.println("No economy plugin found, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        System.out.println("Paydays by RiesPanda has started up!");
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        int time = getConfig().getInt("time-between-paydays");
        long minTime = time * 20;
        double amount = getConfig().getDouble("amount-paid");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (amount < 0) {
                System.out.println("Amount - Paid in config.yml is less than 0.");
                return;
            }
             else {
                for (Player player : Bukkit.getOnlinePlayers())
                    if (player != null) {
                            player.sendMessage(ChatColor.GREEN + (ChatColor.BOLD + "You have been paid $" + (ChatColor.GOLD + "" + amount)));
                            econ.depositPlayer(player, amount);
                    }
                else
                    {
                        return;
                    }
            }
        }, minTime, minTime);
        }



    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}