package cn.salx.plugins.dailyexplimit.job;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyCronJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 这里编写你的任务逻辑
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        DailyEXPLimit plugin = (DailyEXPLimit) dataMap.get("plugin");
        plugin.expGainedManager().clearAll();
        plugin.getLogger().info("reset job execute");
    }
}