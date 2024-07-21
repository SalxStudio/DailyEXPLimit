package cn.salx.plugins.dailyexplimit.database;

import cc.carm.lib.easysql.hikari.HikariConfig;
import org.bukkit.configuration.ConfigurationSection;

public class HikariUtil {
    private HikariUtil() {
    }

    public static HikariConfig createHikariConfig(ConfigurationSection section) {
        HikariConfig config = new HikariConfig();
        if (section == null) {
            throw new IllegalArgumentException("database.properties section in configuration not found");
        }
        for (String key : section.getKeys(false)) {
            config.addDataSourceProperty(key, section.getString(key));
        }
        return config;
    }

    public static HikariConfig createHikariConfig() {
        return new HikariConfig();
    }
}
