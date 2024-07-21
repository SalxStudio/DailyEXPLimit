

package cn.salx.plugins.dailyexplimit.command.subcommand;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import cn.salx.plugins.dailyexplimit.command.CommandHandler;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SubCommand_LimitAdjust implements CommandHandler<CommandSender> {

    private final DailyEXPLimit plugin;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
        String command = cmdArg[0];
        String playerName = cmdArg[1];
        String adjustLimit = cmdArg[2];
        if (command.equals("add")) {
            Player player = plugin.getServer().getPlayer(playerName);
            if (player == null) {
                sender.sendMessage("§c玩家不在线或不存在");
                return;
            }
            int addUserTempExpLimit = plugin.expMaxManager().addUserTempExpLimit(player, Integer.valueOf(adjustLimit));
            sender.sendMessage(String.format("§a成功添加玩家: %s 的经验上限, 当前经验上限为: %s", playerName, addUserTempExpLimit));
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
        if (cmdArg.length == 1) {
            return List.of("add");
        }
        if (cmdArg.length == 2) {
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}