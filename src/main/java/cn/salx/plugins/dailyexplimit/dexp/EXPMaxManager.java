package cn.salx.plugins.dailyexplimit.dexp;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 管理权限组的最大经验值
 */

public class EXPMaxManager {

    private final DailyEXPLimit plugin;

    private final HashMap<String, Integer> groupExpLimitMap = new HashMap<>();
    private final HashMap<String, Integer> tempExpLimitMap = new HashMap<>();
    private final String defaultEXPLimitKey = "dexp.limit.default";

    public EXPMaxManager(DailyEXPLimit plugin) {
        this.plugin = plugin;
        init();
    }

    public void init() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("dexp.limit");
        assert section != null;

        Set<String> groupKeys = section.getKeys(false);
        Map<String, Integer> limitMap = groupKeys.stream().map(s -> Pair.of(String.format("%s.%s", section.getCurrentPath(), s), section.getInt(s)))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
        groupExpLimitMap.putAll(limitMap);
        if (!groupExpLimitMap.containsKey(defaultEXPLimitKey)) {
            throw new IllegalArgumentException("未设置默认权限组经验上限");
        }
        plugin.getLogger().info("加载权限组经验上限配置: " + groupExpLimitMap);
    }

    private Integer getDefaultEXPLimit() {
        return groupExpLimitMap.get(defaultEXPLimitKey);
    }

    public int getUserExpLimit(Player player) {
        Integer groupExpLimit = groupExpLimitMap.keySet().stream()
                .filter(player::hasPermission)
                .map(groupExpLimitMap::get)
                .sorted().findFirst().orElse(getDefaultEXPLimit());
        Integer tempExpLimit = tempExpLimitMap.getOrDefault(player.getName(), 0);
        return groupExpLimit + tempExpLimit;
    }

    public int addUserTempExpLimit(Player player, Integer limit) {
        tempExpLimitMap.compute(player.getName(), (k, v) -> v == null ? limit : v + limit);
        return getUserExpLimit(player);
    }

    public void clearTempExpLimit() {
        tempExpLimitMap.clear();
    }
}
