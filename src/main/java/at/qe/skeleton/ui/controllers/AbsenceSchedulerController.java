package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.configs.SchedulerTaskConfig;
import at.qe.skeleton.model.Absence;
import at.qe.skeleton.model.AbsenceReason;
import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.services.AbsenceSchedulerService;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Objects;


@Component
@Profile("!test")
@Scope("view")
public class AbsenceSchedulerController {

    @Autowired
    private UserService userService;

    @Autowired
    SessionInfoBean sessionInfoBean;

    @Autowired
    SchedulerTaskConfig schedulerTaskConfig;

    @Autowired
    AbsenceSchedulerService absenceSchedulerService;

    @Autowired
    AuditLogService auditLogService;

    public void setServices(UserService userService, SessionInfoBean sessionInfoBean,
                            SchedulerTaskConfig schedulerTaskConfig,
                            AbsenceSchedulerService absenceSchedulerService,
                            AuditLogService auditLogService){
        this.userService = userService;
        this.sessionInfoBean = sessionInfoBean;
        this.schedulerTaskConfig = schedulerTaskConfig;
        this.absenceSchedulerService = absenceSchedulerService;
        this.auditLogService = auditLogService;
    }


    private Absence absence;

    private AuditLog auditLog;

    /**Current user getter*/
    public Users getCurrentUser() {
        return sessionInfoBean.getCurrentUser();
    }

    public Absence getAbsence() {
        return absence;
    }

    public void setAbsence(Absence absence) {
        this.absence = absence;
    }

    /**Methods implementation for the user scheduler
     * */
    /************************************************/
    private ScheduleModel eventModel;

    private String userStatus;

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    @PostConstruct
    void init(){
        eventModel = new DefaultScheduleModel();

        for (Absence eventAbsence : absenceSchedulerService.getAbsencesByUser(getCurrentUser())) {
            event = DefaultScheduleEvent.builder()
                    .id(Integer.toString(eventAbsence.getAbsenceId()))
                    .startDate(eventAbsence.getStartDate())
                    .endDate(eventAbsence.getEndDate())
                    .title(eventAbsence.getTitle())
                    .build();
            eventModel.addEvent(event);
        }

    }

    private ScheduleEvent<?> event = new DefaultScheduleEvent<>();

    public ScheduleEvent<?> getEvent() {

        return event;
    }

    public void setEvent(ScheduleEvent<?> event) {

        this.event = event;
    }


    /**Method to add an event in calender and also save it in repository
     *
     */
    public void addEvent() {
        auditLog = new AuditLog();

        if(!Objects.equals(userStatus, "") && event.getId() == null){
            event = DefaultScheduleEvent.builder()
                    .startDate(event.getStartDate())
                    .endDate(event.getStartDate().plusHours(1))
                    .title(event.getTitle())
                    .build();
        }

        if (event.isAllDay() && event.getStartDate().toLocalDate().equals(event.getEndDate().toLocalDate())) {
            event.setEndDate(event.getEndDate().plusDays(1));
        }

        /**If the event does not exist, create new one
         *
         */
        if (event.getId() == null) {
            this.absence = new Absence();
            this.absence.setUser(getCurrentUser());
            this.absence.setTitle(event.getTitle());
            this.absence.setStartDate(event.getStartDate());
            this.absence.setEndDate(event.getEndDate());
            this.absence.setAbsenceReason(AbsenceReason.valueOf(event.getTitle()));
            getCurrentUser().getAbsences().add(absence);

            eventModel.addEvent(event);


            auditLog.setEvent("New absence created");
        }
        /**If the event exist, update it
         *
         */
        else {
            eventModel.updateEvent(event);
            this.absence.setTitle(event.getTitle());
            this.absence.setAbsenceReason(AbsenceReason.valueOf(event.getTitle()));
            this.absence.setStartDate(event.getStartDate());
            this.absence.setEndDate(event.getEndDate());

            auditLog.setEvent("Absence (ID: " + this.absence.getAbsenceId() + ") edited");
        }

        absenceSchedulerService.saveAbsence(absence);
        event.setId(Integer.toString(absence.getAbsenceId()));

        auditLogService.saveAuditLog(auditLog);

        event = new DefaultScheduleEvent<>();
    }

    /**On event select, set the absence to the equal of the event
     *
     * @param selectEvent
     */
    public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) {
        schedulerTaskConfig.startAbsenceTaskTimer();
        event = selectEvent.getObject();
        eventModel.updateEvent(event);
        this.absence = absenceSchedulerService.loadAbsence(Integer.parseInt(event.getId()));
    }

    /**By clicking on an empty slot on calender
     *
     * @param selectEvent
     */
    public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
        setUserStatus("");
        event = DefaultScheduleEvent.builder()
                .startDate(selectEvent.getObject())
                .endDate(selectEvent.getObject().plusHours(1))
                .build();
    }

    /**Delete an existing event
     *
     */
    public void onEventDelete() {
        auditLog = new AuditLog();

        if (event.getId() != null) {
            ScheduleEvent<?> absenceEvent = eventModel.getEvent(getEvent().getId());
            absenceSchedulerService.deleteAbsence(absence);

            auditLog.setEvent("Absence (ID: " + this.absence.getAbsenceId() + ") has been deleted");
            auditLogService.saveAuditLog(auditLog);
            eventModel.deleteEvent(absenceEvent);
        }
    }

    /**On event move update absence
     *
     * @param event
     */
    public void onEventMove(ScheduleEntryMoveEvent event) {
        eventModel.updateEvent(event.getScheduleEvent());
        this.event = event.getScheduleEvent();
        this.absence = absenceSchedulerService.loadAbsence(Integer.parseInt(event.getScheduleEvent().getId()));
        addEvent();
    }
}
