package com.ChatF;

import java.io.Serializable;
//상수의 집합
//JOIN, EXIT, SEND 3가지의 명령어에 따라 다른 역할 수행
enum Info {
    JOIN, EXIT, SEND
}

public class InfoDTO implements Serializable {
    private String nickName;
    private String message;
    //enum Info{}
    private Info command;

    //롬복 Getter 대체 가능
    public String getNickName() {
        return nickName;
    }

    public String getMessage() {
        return message;
    }

    public Info getCommand() {
        return command;
    }

    //롬복 Setter 대체 가능
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCommand(Info command) {
        this.command = command;
    }
}
