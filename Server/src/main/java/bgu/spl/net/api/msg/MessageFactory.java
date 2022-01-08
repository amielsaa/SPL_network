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
            case 4:
                return new FollowMsg(vars);
            case 5:
                return new PostMsg(vars);
            case 6:
                return new PmMsg(vars);
            case 7:
                return new LogstatMsg(vars);
            case 8:
                return new StatMsg(vars);
            case 12:
                return new BlockMsg(vars);
        }
        return null;
    }

}
