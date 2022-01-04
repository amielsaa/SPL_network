package bgu.spl.net.api.msg;

import java.util.List;

public class RegisterMsg implements Message {

    //private short opCode;
    private List<String> vars;

    public RegisterMsg( List<String> vars) {
        //this.opCode=opCode;
        this.vars=vars;
    }

    @Override
    public short getOpCode() {
        return 1;
    }

    @Override
    public List<String> getVars() {
        return vars;
    }


}
