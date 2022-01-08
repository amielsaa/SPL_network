package bgu.spl.net.api.msg;

import java.util.List;

public class ErrorMsg implements Message {

    private short msgOpCode;

    public ErrorMsg(short msgOpCode) {
        this.msgOpCode = msgOpCode;
    }

    @Override
    public short getOpCode() {
        return 11;
    }

    public short getMsgOpCode() { return msgOpCode; }

    @Override
    public List<String> getVars() {
        return null;
    }


}
