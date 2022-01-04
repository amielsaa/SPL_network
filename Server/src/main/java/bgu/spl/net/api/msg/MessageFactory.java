package bgu.spl.net.api.msg;

import java.util.List;

public class MessageFactory {

    public static Message createMessage(short opCode, List<String> vars,short[] shorts) {
        switch (opCode) {
            case 1:
                return new RegisterMsg(vars);
            case 2:
                return new LoginMsg(vars,shorts);
            case 3:
                return new LogoutMsg();
        }
        return null;
    }

}
