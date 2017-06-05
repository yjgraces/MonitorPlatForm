package com.yidao.monitor.monitorJob;


import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yidao.monitor.pojo.MonitorItems;
import com.yidao.monitor.service.MonitorContactsService;
import com.yidao.monitor.service.MonitorExceptionRecordService;
import com.yidao.monitor.service.MonitorItemsService;
import com.yidao.monitor.service.MonitorHostsService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.util.List;

@Component
public class MonitorSettings {

	private final static Logger logger = LoggerFactory.getLogger(MonitorSettings.class); 
	
	@Resource
	private MonitorItemsService monitorItemsService;
	@Resource
	private MonitorHostsService monitorHostsService;
	@Resource
	private MonitorExceptionRecordService monitorExceptionRecordService;
	@Resource
	private MonitorContactsService monitorContactsService;
	
	public static Scheduler scheduler;
	
	@PostConstruct
	public void init() throws SchedulerException {
		System.out.println("项目启动----------_-!");
		monitorItemsService.correctFlag();
		scheduler = new StdSchedulerFactory().getScheduler();
		execute();
	}

	
	public void execute() {
		logger.info("Monitor_YiDao-----开始扫描----");
		List<MonitorItems> monitorList = monitorItemsService.getMonitorItems();
		logger.info(String.format("共有%d条数据", monitorList.size()));
		if (monitorList != null && !monitorList.isEmpty()) {
			for (MonitorItems item : monitorList) {
				String jobName = String.format("yidaoRequestJob_%d",
						item.getMonitorId());
				String groupName = String.format("yidaoRequestGroup_%s",
						item.getMonitorSystem());
				String triggerName = String.format("yidaoRequestTrigger_%d",
						item.getMonitorId());

				JobKey jobKey = new JobKey(jobName, groupName);
				TriggerKey triggerKey = new TriggerKey(triggerName, groupName);

				Trigger trigger = null;
				try {
					trigger = scheduler.getTrigger(triggerKey);
				} catch (SchedulerException ex) {
					trigger = null;
				}
				JobDetail jobDetail = null;
				try {
					jobDetail = scheduler.getJobDetail(jobKey);
				} catch (SchedulerException ex) {
					jobDetail = null;
				}

				if (jobDetail != null) {
					try {
						// 如果任务已经存在，那么删除
						scheduler.unscheduleJob(triggerKey);
						scheduler.deleteJob(jobKey);
						logger.info(String.format("删除JOB成功!JObKey:%s", jobKey));
					} catch (SchedulerException ex) {
						logger.error(
								String.format(
										"删除JOB出现错误：%s ， jobName: %s ， groupName: %s ，jobKey:%s",
										ex.toString(), jobName, groupName,
										jobKey.toString()), ex);
					}
				}
				// 初始化JOB
				jobDetail = newJob(SchedulerExecution.class).withIdentity(jobName, groupName).build();
				// 设置要传递的参数
				jobDetail.getJobDataMap().put("monitorItems",item);
	            jobDetail.getJobDataMap().put("monitorHostsService",monitorHostsService);
	            jobDetail.getJobDataMap().put("monitorExceptionRecordService",monitorExceptionRecordService);
	            jobDetail.getJobDataMap().put("monitorContactsService",monitorContactsService);
	            jobDetail.getJobDataMap().put("monitorItemsService",monitorItemsService);
				// 初始化触发器
				trigger = newTrigger().withIdentity(triggerName, groupName)
						.withSchedule(cronSchedule(item.getMonitorCrontab()))
						.forJob(jobName, groupName).build();
				try {
					scheduler.scheduleJob(jobDetail, trigger);
					scheduler.start();
					logger.info(String.format("启动JOB成功!JObKey:%s", jobKey));
				} catch (Exception ex) {
					logger.error(String.format("任务运行出现错误：%s", ex.toString()),ex);
				}
			}
		}else{
			logger.info("没有可以监控的任务------");
		}
	}

}
