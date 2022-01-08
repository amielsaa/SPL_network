package bgu.spl.net.api.msg;

import java.util.List;

public class LogstatMsg implements Message {

    private List<String> vars;

    public LogstatMsg(List<String> vars) {
        this.vars = vars;
    }

    @Override
    public short getOpCode() {
        return 7;
    }

    @Override
    public List<String> getVars() {
        return vars;
    }
}
