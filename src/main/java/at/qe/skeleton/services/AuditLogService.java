package at.qe.skeleton.services;

import at.qe.skeleton.model.AuditLog;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.repositories.AuditLogRepository;
import at.qe.skeleton.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    @Autowired
    AuditLogRepository auditLogRepository;

    @Autowired
    UserRepository userRepository;

    public void saveAuditLog(AuditLog auditLog){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Date dateNow = new Date();
        auditLog.setUser(auth.getName());
        auditLog.setTimeStamp(dateNow);
       auditLogRepository.save(auditLog);
    }

    public void saveAuditLogLogout(AuditLog auditLog,String userName){
        Date dateNow = new Date();
        auditLog.setUser(userName);
        auditLog.setTimeStamp(dateNow);
        auditLogRepository.save(auditLog);
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FACILITY_MANAGER')")
    public List<AuditLog> getAllAuditLogs() {
       return auditLogRepository.findAll().stream().sorted(Comparator.comparing(AuditLog::getTimeStamp).reversed())
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('FACILITY_MANAGER') or hasAuthority('ADMIN')")
    public List<AuditLog> getAllAuditLogsByDepartment() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users manager =  userRepository.findFirstByUsername(auth.getName());
        List<AuditLog> auditLogs = new ArrayList<>();

        for(Users users : manager.getOffice().getEmployees()){
           for(AuditLog auditLog : auditLogRepository.findAll()){
                if(auditLog.getUser().equals(users.getUsername())){
                    auditLogs.add(auditLog);
                }
            }
        }


        return auditLogs.stream().sorted(Comparator.comparing(AuditLog::getTimeStamp).reversed())
                .collect(Collectors.toList());
    }


}
