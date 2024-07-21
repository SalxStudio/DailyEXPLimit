package cn.salx.plugins.dailyexplimit.dexp;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 管理权限组的最大经验值
 */

public class EXPMaxManager {

    private final DailyEXPLimit plugin;

    private final HashMap<String, Integer> groupExpLimitMap = new HashMap<>();

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
        plugin.getLogger().info("加载权限组经验上限配置: " + groupExpLimitMap);
    }
}
