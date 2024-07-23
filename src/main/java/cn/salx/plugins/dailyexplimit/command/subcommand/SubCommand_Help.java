package cn.salx.plugins.dailyexplimit.command.subcommand;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import cn.salx.plugins.dailyexplimit.command.CommandContainer;
import cn.salx.plugins.dailyexplimit.command.CommandHandler;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class SubCommand_Help implements CommandHandler<CommandSender> {

    private final DailyEXPLimit plugin;

    @Override
    public void onCommand(
            @NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
        sendHelp(sender, commandLabel);
    }


    private void sendHelp(@NotNull CommandSender s, @NotNull String commandLabel) {
        s.sendMessage("§a DailyEXPLimit");
        commandPrintingLoop:
        for (CommandContainer container : plugin.commandManager().getRegisteredCommands()) {
            if (!container.isHidden()) {
                boolean passed = false;
                //selectivePermissions
                final List<String> selectivePermissions = container.getSelectivePermissions();
                if (selectivePermissions != null && !selectivePermissions.isEmpty()) {
                    for (String selectivePermission : container.getSelectivePermissions()) {
                        if (selectivePermission != null && !selectivePermission.isEmpty()) {
                            if (s.hasPermission(selectivePermission)) {
                                passed = true;
                                break;
                            }
                        }
                    }
                }
                //requirePermissions
                final List<String> requirePermissions = container.getPermissions();
                if (requirePermissions != null && !requirePermissions.isEmpty()) {
                    for (String requirePermission : requirePermissions) {
                        if (requirePermission != null && !requirePermission.isEmpty() && !s.hasPermission(requirePermission)) {
                            continue commandPrintingLoop;
                        }
                    }
                    passed = true;
                }
                if (!passed) {
                    continue;
                }

                String commandDesc = "帮助命令";
                if (container.getDescription() != null) {
                    commandDesc = container.getDescription();
                    //noinspection ConstantValue
                    if (commandDesc == null) {
                        commandDesc = "子命令 " + container.getPrefix() + " # " + container.getClass().getCanonicalName() + " 未注册有效帮助描述信息";
                    }
                }
                if (container.isDisabled() || (container.getDisabledSupplier() != null && container.getDisabledSupplier().get())) {
                    if (s.hasPermission("kooksrv.showdisabled")) {
                        s.sendMessage(String.format("/%s %s - %s", commandLabel, container.getPrefix(), commandDesc));
                    }
                } else {
                    s.sendMessage(String.format("/%s %s - %s", commandLabel, container.getPrefix(), commandDesc));
                }
            }
        }
    }

}