package at.qe.skeleton.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "auditLog")
public class AuditLog implements Persistable<String>, Serializable{

    @Id
    @SequenceGenerator(
            name = "audit_sequence",
            sequenceName = "audit_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "audit_sequence"
    )

    @Column(nullable = false,updatable = false)
    protected int auditLogId;
    @Column(nullable = false,updatable = false)
    protected String user;
    @Column(nullable = false,updatable = false)
    protected String event;
    @Column(nullable = false,updatable = false)
    protected Date timeStamp;

    public int getAuditLogId() {
        return auditLogId;
    }

    public void setAuditLogId(int auditLogId) {
        this.auditLogId = auditLogId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Override
    public String getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }

}
