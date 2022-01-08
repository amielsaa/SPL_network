package bgu.spl.net.api.msg;

import java.util.List;

public class StatMsg implements Message {

    private List<String> vars;

    public StatMsg(List<String> vars) {
        this.vars = vars;
    }

    @Override
    public short getOpCode() {
        return 8;
    }

    @Override
    public List<String> getVars() {
        return vars;
    }
}
