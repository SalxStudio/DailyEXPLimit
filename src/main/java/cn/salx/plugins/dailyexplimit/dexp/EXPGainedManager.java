package cn.salx.plugins.dailyexplimit.dexp;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理权限组的最大经验值
 */

public class EXPGainedManager {

    private final DailyEXPLimit plugin;

    private final Map<UUID, Integer> gainedExpMap = new ConcurrentHashMap<>();


    public EXPGainedManager(DailyEXPLimit plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
    }

    public void playerJoinedServer(UUID player) {
        Integer playerGainedExp = plugin.database().getDatabaseHelper().getPlayerGainedExp(player).join();
        gainedExpMap.putIfAbsent(player, playerGainedExp == null ? 0 : playerGainedExp);
    }

    public void playerQuitServer(UUID player) {
        Integer result = plugin.database().getDatabaseHelper().updatePlayerGainedExp(player, gainedExpMap.get(player)).join();
        if (result.compareTo(1) == 0) {
            plugin.getLogger().info("玩家 " + player + " 的经验值已保存");
            gainedExpMap.remove(player);
        }
    }

    public Triple<Integer, Integer, Integer> addPlayerGainedExp(PlayerExpChangeEvent playerExpChangeEvent) {
        // 给予玩家经验值，返回此次获得的经验 和 还可以获得多少经验
        Player player = playerExpChangeEvent.getPlayer();
        int exp = playerExpChangeEvent.getAmount();
        int userExpLimit = plugin.expMaxManager().getUserExpLimit(player);
        Integer alreadyGained = gainedExpMap.getOrDefault(player.getUniqueId(), 0);
        Triple<Integer, Integer, Integer> result;
        int total = alreadyGained + exp;
        if (total <= userExpLimit) {
            result = Triple.of(exp, userExpLimit - total, userExpLimit);
        } else {
            result = Triple.of(userExpLimit - alreadyGained, 0, userExpLimit);
        }
        gainedExpMap.put(player.getUniqueId(), alreadyGained + result.getLeft());
        return result;
    }

    public boolean resetPlayerExp(Player player) {
        return gainedExpMap.put(player.getUniqueId(), 0) != null;
    }

    public void resetAllPlayerExp() {
        gainedExpMap.clear();
    }

    public void flush() {
        gainedExpMap.forEach((key, value) -> {
            plugin.database().getDatabaseHelper().updatePlayerGainedExp(key, value).join();
        });
    }
}
