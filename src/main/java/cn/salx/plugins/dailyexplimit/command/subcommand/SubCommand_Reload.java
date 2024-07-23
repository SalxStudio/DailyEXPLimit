package cn.salx.plugins.dailyexplimit.command.subcommand;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import cn.salx.plugins.dailyexplimit.command.CommandHandler;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class SubCommand_Reload implements CommandHandler<CommandSender> {

    private final DailyEXPLimit plugin;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
        plugin.reloadConfig();
        plugin.expMaxManager().init();
    }
}