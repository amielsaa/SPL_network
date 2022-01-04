package bgu.spl.net.api.msg;

import java.util.List;

public class PostMsg implements Message {

    private List<String> vars;

    public PostMsg(List<String> vars) {
        this.vars = vars;
    }

    @Override
    public short getOpCode() {
        return 5;
    }

    @Override
    public List<String> getVars() {
        return vars;
    }
}
