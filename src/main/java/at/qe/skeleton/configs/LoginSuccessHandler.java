package at.qe.skeleton.configs;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


/**
 * This handler is triggered after a login is performed.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 *
 */
@Component
@Scope("request")
public class LoginSuccessHandler implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Autowired
    AuditLogService auditLogService;

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEvent("Logged-in");
        auditLogService.saveAuditLog(auditLog);
    }

}
