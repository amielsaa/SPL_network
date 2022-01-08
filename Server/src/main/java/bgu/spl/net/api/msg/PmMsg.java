package bgu.spl.net.api.msg;

import java.util.List;

public class PmMsg implements Message {

    List<String> vars;

    public PmMsg(List<String> vars) {
        this.vars = vars;
    }

    @Override
    public short getOpCode() {
        return 6;
    }

    @Override
    public List<String> getVars() {
        //0-recipient
        //1-content
        //2-date
        return vars;
    }
}
