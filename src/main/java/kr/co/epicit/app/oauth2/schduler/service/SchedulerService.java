package kr.co.epicit.app.oauth2.schduler.service;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;

	private ScheduledFuture<?> scheduledFuture;

	public void scheduleTask(String cronExpression) {
		Runnable task = () -> System.out.println("Executing task at " + new Date());

		if (scheduledFuture != null) {
			scheduledFuture.cancel(false);
		}

		scheduledFuture = taskScheduler.schedule(task, new CronTrigger(cronExpression));
	}

	public void stopScheduledTask() {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(false);
		}
	}
}
