package cn.salx.plugins.dailyexplimit.listener;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener implements Listener {

    private final DailyEXPLimit plugin;

    public BukkitListener(DailyEXPLimit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        plugin.expGainedManager().playerJoinedServer(player);
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        plugin.expGainedManager().playerQuitServer(player);
    }

    @EventHandler
    public void onGainExp(PlayerExpChangeEvent playerExpChangeEvent) {
        Player player = playerExpChangeEvent.getPlayer();
        Triple<Integer, Integer, Integer> gainedExp = plugin.expGainedManager().addPlayerGainedExp(playerExpChangeEvent);

        Integer gained = gainedExp.getLeft();
        Integer left = gainedExp.getMiddle();
        Integer max = gainedExp.getRight();
        playerExpChangeEvent.setAmount(gained);
        if (gained != 0 && left == 0) {
            Integer totalGained = plugin.expGainedManager().getPlayerGainedExp(player);
            player.sendMessage(String.format("§c你的经验值已达到上限，今日已获取: %s, 上限：%s", max, totalGained));
        } else {
            plugin.getLogger().info(String.format("%s 获得 %s, 还可以获得 %s, 上限: %s", player.getName(), gained, left, max));
        }
    }
}
