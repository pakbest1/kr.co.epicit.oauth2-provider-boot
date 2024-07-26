package kr.co.epicit.app.oauth2.schduler.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.epicit.app.oauth2.schduler.service.SchedulerService;

@Controller
@RequestMapping("/scheduler")
public class SchedulerController {

	@Autowired
	private SchedulerService dynamicSchedulerService;

	@GetMapping("/schedule")
	public String scheduleTask(@RequestParam String cron) {
		dynamicSchedulerService.scheduleTask(cron);
		return "Task scheduled with cron: " + cron;
	}

	@GetMapping("/stop")
	public String stopTask() {
		dynamicSchedulerService.stopScheduledTask();
		return "Task stopped";
	}

}
