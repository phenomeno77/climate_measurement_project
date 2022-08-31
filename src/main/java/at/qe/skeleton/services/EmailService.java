package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.ui.beans.AverageCalculatorReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class EmailService {

    @Autowired
    private  JavaMailSender javaMailSender;
    @Autowired
    private  TemplateEngine templateEngine;
    @Autowired
    private  UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private  MeasurementService measurementService;
    @Autowired
    private  AverageCalculatorReport averageCalculatorReport;



    public void setServices(UserService userService, TemplateEngine templateEngine,
                        JavaMailSender javaMailSender, PasswordEncoder passwordEncoder,
                        MeasurementService measurementService, AverageCalculatorReport averageCalculatorReport) {
        this.userService = userService;
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.measurementService = measurementService;
        this.averageCalculatorReport = averageCalculatorReport;
    }



    @Async
    public String sendMailNotification(Users users) {
        List<Measurement> measurementListOffice = new ArrayList<>(measurementService.getEmployeeOfficeReport(users, ReportFrequency.valueOf(users.getMailSettings().name())));

        Context context = new Context();
        context.setVariable("office",users.getMailSettings().name() + " Measurement Data for " + users.getOffice().getRoomName());

        averageCalculatorReport.calculateAverages(measurementListOffice);
        context.setVariable("decibelAverage","DECIBEL AVERAGE MEASUREMENT: " + averageCalculatorReport.getDecibel());
        context.setVariable("humidityAverage","HUMIDITY AVERAGE MEASUREMENT: " + averageCalculatorReport.getHumidity());
        context.setVariable("luxAverage","LUX AVERAGE MEASUREMENT: " + averageCalculatorReport.getLux());
        context.setVariable("gasAverage","GAS AVERAGE MEASUREMENT: " + averageCalculatorReport.getGas());
        context.setVariable("pressureAverage","PRESSURE AVERAGE MEASUREMENT: " + averageCalculatorReport.getPressure());
        context.setVariable("temperatureAverage","TEMPERATURE AVERAGE MEASUREMENT: " + averageCalculatorReport.getTemperature());


        Map<Room,List<Measurement>> measurementMap = new HashMap<>(measurementService.getEmployeePublicRoomReport(users,ReportFrequency.valueOf(users.getMailSettings().name())));

        for (Map.Entry<Room, List<Measurement>> entry : measurementMap.entrySet()) {
            context.setVariable(entry.getKey().getRoomType().name(),users.getMailSettings().name() + " Measurement Data for " + entry.getKey().getRoomName());
            averageCalculatorReport.calculateAverages(entry.getValue());
                String roomType = entry.getKey().getRoomType().name();
                context.setVariable("decibelAverage"+roomType,"DECIBEL AVERAGE MEASUREMENT: " + averageCalculatorReport.getDecibel());
                context.setVariable("humidityAverage"+roomType,"HUMIDITY AVERAGE MEASUREMENT: " + averageCalculatorReport.getHumidity());
                context.setVariable("luxAverage"+roomType,"LUX AVERAGE MEASUREMENT: " + averageCalculatorReport.getLux());
                context.setVariable("gasAverage"+roomType,"GAS AVERAGE MEASUREMENT: " + averageCalculatorReport.getGas());
                context.setVariable("pressureAverage"+roomType,"PRESSURE AVERAGE MEASUREMENT: " + averageCalculatorReport.getPressure());
                context.setVariable("temperatureAverage"+roomType,"TEMPERATURE AVERAGE MEASUREMENT: " + averageCalculatorReport.getTemperature());
        }


        String body = templateEngine.process("notification", context);
        MimeMessage message = javaMailSender.createMimeMessage();

        if (users.getEmail() != null) {
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(users.getEmail());
                helper.setSubject("Room Climate Monitoring");
                helper.setText(body, true);
                javaMailSender.send(message);
                return "Email sent successfully!";
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Async
    public String sendMailUserAdded(Users users, String rawPassword) {
        if (users != null) {
            String title = "Welcome dear user " + users.getUsername();
            Context context = new Context();
            context.setVariable("welcomeUser", title);
            context.setVariable("login", users.getUsername());
            context.setVariable("pass", rawPassword);

            String body = templateEngine.process("userAddEmail", context);


            MimeMessage message = javaMailSender.createMimeMessage();

            if (users.getEmail() != null) {
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(users.getEmail());
                    helper.setSubject("New User");
                    helper.setText(body, true);
                    javaMailSender.send(message);
                    return "User added. Email sent successfully";
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Async
    public String systemStartNotification() {

        for(Users users : userService.getUsers()) {
            if (users.getRoles() == UserRole.FACILITY_MANAGER) {
                Context context = new Context();
                context.setVariable("notify", "Hardware Check");
                context.setVariable("message","Dear " + users.getUsername() + "!" +
                        "\nThe system has been started." +
                        "\nPlease check the hardware system to ensure that everything is running OK!" );
                String body = templateEngine.process("reportNotification", context);
                MimeMessage message = javaMailSender.createMimeMessage();

                if (users.getEmail() != null) {
                    try {
                        MimeMessageHelper helper = new MimeMessageHelper(message, true);
                        helper.setTo(users.getEmail());
                        helper.setSubject("System Started!");
                        helper.setText(body, true);
                        javaMailSender.send(message);
                        return "Notification sent";
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        return "";
    }


}


