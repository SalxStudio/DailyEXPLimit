

package cn.salx.plugins.dailyexplimit.command.subcommand;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import cn.salx.plugins.dailyexplimit.command.CommandHandler;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SubCommand_Reload implements CommandHandler<CommandSender> {

    private final DailyEXPLimit plugin;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
        plugin.reloadConfig();
        plugin.expMaxManager().init();
    }
}