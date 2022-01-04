package bgu.spl.net.api.msg;

import java.util.List;

public class FollowMsg implements Message{


    private List<String> vars;

    public FollowMsg(List<String> vars) {
        this.vars = vars;
    }

    @Override
    public short getOpCode() {
        return 4;
    }

    @Override
    public List<String> getVars() {
        return vars;
    }
}
