package com.casa.app.notifications;

import com.casa.app.user.User;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void makeNotification(User user, String text){
        Notification newNotification = new Notification();
        newNotification.setUser(user);
        newNotification.setText(text);
        notificationRepository.save(newNotification);
    }
}
