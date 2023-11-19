package com.casa.app.device;


import org.springframework.stereotype.Service;

@Service
public class DeviceStatusService {

    public void pingHandler(String message) {
        System.out.println(message);
    }
}
