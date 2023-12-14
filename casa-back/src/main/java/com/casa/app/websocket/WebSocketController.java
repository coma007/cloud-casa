package com.casa.app.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public boolean sendMessage(SocketMessage message) {
        if (message.getMessage() != null) {
            String topic = message.getTopic();
            if (topic != null && !topic.equals("")) {
                topic += "/";
            }
            if (message.getToId() != null && !message.getToId().equals("")) {
                this.simpMessagingTemplate.convertAndSend("/topic/" + topic + message.getToId(), message);
            } else {
                this.simpMessagingTemplate.convertAndSend("/topic/" + topic, message);
            }
            System.err.println("Message sent! Message: " + message + " topic " + topic + message.getToId());
            return true;
        }

        System.err.println("Message not sent!");
        return false;
    }


    @MessageMapping("/send/message")
    public Map<String, String> sendMessage(String message) {
        //TODO: Change socket endpoint logic
        System.err.println(message);

        Map<String, String> messageConverted = parseMessage(message);

        if (messageConverted != null) {
            if (messageConverted.containsKey("toId") && messageConverted.get("toId") != null
                    && !messageConverted.get("toId").equals("")) {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher" + messageConverted.get("toId"),
                        messageConverted);
                this.simpMessagingTemplate.convertAndSend("/socket-publisher" + messageConverted.get("fromId"),
                        messageConverted);
            } else {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/hello", messageConverted);
            }
        }

        return messageConverted;
    }

    private Map<String, String> parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> retVal;
        try {
            retVal = mapper.readValue(message, Map.class); // parsiranje JSON stringa
        } catch (IOException e) {
            retVal = null;
        }

        return retVal;
    }
}