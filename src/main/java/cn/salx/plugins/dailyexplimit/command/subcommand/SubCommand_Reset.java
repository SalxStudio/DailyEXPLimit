

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
public class SubCommand_Reset implements CommandHandler<CommandSender> {

    private final DailyEXPLimit plugin;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
        String playerName = cmdArg[0];
        Player player = plugin.getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("§c玩家不在线或不存在");
            return;
        }
        boolean reset = plugin.expGainedManager().resetPlayerExp(player);
        player.sendMessage("§a重置 " + playerName + " 的经验" + (reset ? "成功" : "失败"));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
        return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}