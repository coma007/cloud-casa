package com.casa.app.util.email;

import java.time.LocalDateTime;

public class DateUtil {

    public static boolean IsOverlapping(LocalDateTime start1, LocalDateTime end1,
                                        LocalDateTime start2, LocalDateTime end2){
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
