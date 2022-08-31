package at.qe.skeleton.ui.controllers.rest;

import at.qe.skeleton.services.EmailService;
import at.qe.skeleton.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/noti")
public class EmailNotificationController {
    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;



}
