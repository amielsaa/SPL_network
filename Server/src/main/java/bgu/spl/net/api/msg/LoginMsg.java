package bgu.spl.net.api.msg;

import java.util.List;

public class LoginMsg implements Message {

    //private short opCode;
    private List<String> vars;
    private short[] shorts;

    public LoginMsg(List<String> vars,short[] shorts) {
      //  this.opCode = opCode;
        this.vars = vars;
        this.shorts = shorts;
    }




    @Override
    public short getOpCode() {
        return 2;
    }

    @Override
    public List<String> getVars() {
        return vars;
    }


}
