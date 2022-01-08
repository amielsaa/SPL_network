package bgu.spl.net.api.msg;

import java.util.List;

public class BlockMsg implements Message {

    private List<String> vars;

    public BlockMsg(List<String> vars) {
        this.vars = vars;
    }

    @Override
    public short getOpCode() {
        return 12;
    }

    @Override
    public List<String> getVars() {
        return vars;
    }
}
