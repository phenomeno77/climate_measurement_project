package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("view")
public class AuditLogListController {

    @Autowired
    AuditLogService auditLogService;

    public List<AuditLog> getAllAuditLog(){
        return auditLogService.getAllAuditLogs();
    }

    public List<AuditLog> getAllAuditLogByDepartmentManager(){
        return auditLogService.getAllAuditLogsByDepartment();
    }

}
