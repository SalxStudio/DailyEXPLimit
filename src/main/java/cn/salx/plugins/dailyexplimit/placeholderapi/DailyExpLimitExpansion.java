package cn.salx.plugins.dailyexplimit.placeholderapi;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DailyExpLimitExpansion extends PlaceholderExpansion {

    private final DailyEXPLimit plugin;

    public DailyExpLimitExpansion(DailyEXPLimit plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "IMZCC"; //
    }

    @Override
    public String getIdentifier() {
        return "dexp"; //
    }

    @Override
    public String getVersion() {
        return "1.0.0"; // 
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return super.canRegister();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player != null) {
            if ("get".equalsIgnoreCase(params)) {
                Integer playerGainedExp = plugin.expGainedManager().getPlayerGainedExp(player);
                return String.valueOf(playerGainedExp);
            } else if ("max".equalsIgnoreCase(params)) {
                int userExpLimit = plugin.expMaxManager().getUserExpLimit(player);
                return String.valueOf(userExpLimit);
            }
        }
        return "";
    }
}