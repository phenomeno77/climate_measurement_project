package at.qe.skeleton.configs;

import at.qe.skeleton.Tasks.UserSettingsTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Configuration
public class SchedulerTaskConfig {

    @Autowired
    UserSettingsTask userSettingsTask;

    /**Scheduler to run every time of period to check the absences
     * and set the user status
     *
     */
    public void startAbsenceTaskTimer() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                userSettingsTask.doCheckUserAbsence();
            }
        };
        Timer timer = new Timer("Timer");
        long period = 30L * 60L * 1000L ; //30 minutes
        timer.schedule(repeatedTask, 0, period);
    }

    /**This method will run every day at the same time.
     * Trigger the method to send email
     */
    public void startEmailTaskTimer() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,12);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);

        Date timeToRun = cal.getTime();

        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                userSettingsTask.doUserMailSend();
            }
        };

        Timer timer = new Timer("Timer");
        timer.schedule(repeatedTask,timeToRun);
    }
}
