package cn.salx.plugins.dailyexplimit.listener;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class BukkitListener implements Listener {

    private final DailyEXPLimit plugin;

    public BukkitListener(DailyEXPLimit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent playerJoinEvent) {
        UUID uuid = playerJoinEvent.getPlayer().getUniqueId();
        plugin.expGainedManager().playerJoinedServer(uuid);
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent playerQuitEvent) {
        UUID uuid = playerQuitEvent.getPlayer().getUniqueId();
        plugin.expGainedManager().playerQuitServer(uuid);
    }

    @EventHandler
    public void onGainExp(PlayerExpChangeEvent playerExpChangeEvent) {
        Player player = playerExpChangeEvent.getPlayer();
        Triple<Integer, Integer, Integer> gainedExp = plugin.expGainedManager().addPlayerGainedExp(playerExpChangeEvent);
        Integer gained = gainedExp.getLeft();
        Integer left = gainedExp.getMiddle();
        Integer max = gainedExp.getRight();
        playerExpChangeEvent.setAmount(gained);
        if (gained == 0) {
            player.sendMessage(String.format("§c你的经验值已达到上限: %s", max));
        } else {
//            playerExpChangeEvent.getPlayer().sendMessage("获得了 " + gained + " 经验值, 还可以获得 " + left + " 经验值");
            plugin.getLogger().info(String.format("%s 获得了 %s 经验值, 还可以获得 %s 经验值", player.getName(), gained, left));
        }
    }
}
