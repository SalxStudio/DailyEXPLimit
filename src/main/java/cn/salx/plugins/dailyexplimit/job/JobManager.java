package cn.salx.plugins.dailyexplimit.job;

import cn.salx.plugins.dailyexplimit.DailyEXPLimit;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class JobManager {

    private final DailyEXPLimit plugin;

    public JobManager(DailyEXPLimit plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
        try {
            // 创建Scheduler
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDataMap m = new JobDataMap();
            m.put("plugin", plugin);
            // 创建JobDetail
            JobDetail jobDetail = JobBuilder.newJob(MyCronJob.class)
                    .usingJobData(m)
                    .withIdentity("myCronJob")
                    .build();

            // 创建Trigger
            String cronExpression = plugin.getConfig().getString("dexp.reset.cron");
            assert cronExpression != null;
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myCronTrigger")
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();
            plugin.getLogger().info(String.format("reset job: %s", cronExpression));
            // 绑定Job和Trigger
            scheduler.scheduleJob(jobDetail, trigger);

            // 启动Scheduler
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
