package cn.salx.plugins.dailyexplimit;

import cn.salx.plugins.dailyexplimit.command.SimpleCommandManager;
import cn.salx.plugins.dailyexplimit.database.DatabaseManager;
import cn.salx.plugins.dailyexplimit.dexp.EXPGainedManager;
import cn.salx.plugins.dailyexplimit.dexp.EXPMaxManager;
import cn.salx.plugins.dailyexplimit.job.JobManager;
import cn.salx.plugins.dailyexplimit.listener.BukkitListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DailyEXPLimit extends JavaPlugin {

    private DatabaseManager databaseManager;
    private EXPMaxManager expMaxManager;
    private EXPGainedManager expGainedManager;
    private SimpleCommandManager commandManager;
    private JobManager jobManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new BukkitListener(this), this);

        databaseManager = new DatabaseManager(this);
        expMaxManager = new EXPMaxManager(this);
        expGainedManager = new EXPGainedManager(this);
        commandManager = new SimpleCommandManager(this);
        PluginCommand pluginCommand = getCommand("dexp");
        assert pluginCommand != null;
        pluginCommand.setExecutor(this.commandManager);
        pluginCommand.setTabCompleter(this.commandManager);

        jobManager = new JobManager(this);
    }

    @Override
    public void onDisable() {
        expGainedManager.flush();
    }

    public DatabaseManager database() {
        return databaseManager;
    }

    public EXPMaxManager expMaxManager() {
        return expMaxManager;
    }

    public EXPGainedManager expGainedManager() {
        return expGainedManager;
    }

    public SimpleCommandManager commandManager() {
        return commandManager;
    }
}
