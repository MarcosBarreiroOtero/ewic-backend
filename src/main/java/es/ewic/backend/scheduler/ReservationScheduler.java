package es.ewic.backend.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import es.ewic.backend.service.reservationService.ReservationService;

@Component
public class ReservationScheduler implements SchedulingConfigurer {

	@Autowired
	private ReservationService reservationService;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addTriggerTask(new Runnable() {

			@Override
			public void run() {
				reservationService.reservationScheduledTask();
			}

		}, new Trigger() {

			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				Calendar nextExecutionTime = new GregorianCalendar();
				Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
				nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
				nextExecutionTime.add(Calendar.MILLISECOND, 10000);
				return nextExecutionTime.getTime();
			}
		});
	}

}
