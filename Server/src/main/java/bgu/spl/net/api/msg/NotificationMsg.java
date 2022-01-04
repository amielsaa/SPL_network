package bgu.spl.net.api.msg;

import java.util.List;

public class NotificationMsg implements Message {

    private List<String> vars;

    public NotificationMsg(List<String> vars) {
        this.vars = vars;
    }


    @Override
    public short getOpCode() {
        return 9;
    }

    @Override
    public List<String> getVars() {
        //0-sender
        //1-content
        //2-type
        return vars;
    }
}
