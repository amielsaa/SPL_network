package bgu.spl.net.api.msg;

import java.util.List;

public class LogoutMsg implements Message {

    public LogoutMsg() {

    }

    @Override
    public short getOpCode() {
        return 3;
    }

    @Override
    public List<String> getVars() {
        return null;
    }
}
