package com.casa.app.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocketMessage<T> {

    private String topic;
    private String message;
    private String fromId;
    private String toId;
    private T attachment;


}