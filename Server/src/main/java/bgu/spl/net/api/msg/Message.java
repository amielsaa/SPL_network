package bgu.spl.net.api.msg;


import java.util.List;

public class Message {

    private short opCode;
    private List<String> vars;

    public Message(short opCode, List<String> vars) {
        this.opCode=opCode;
        this.vars=vars;
    }

    public short getOpCode() {
        return opCode;
    }

    public List<String> getVars() {
        return vars;
    }

    public byte[] getBytes() {
        String stringBytes = ">";
        if(opCode==10)
            stringBytes+="ACK";
        else
            stringBytes+="ERROR";
        for(int i=0;i<vars.size();i++) {
            stringBytes=stringBytes +" "+vars.get(i);
        }
        stringBytes+=';';
        return stringBytes.getBytes();
    }


}
