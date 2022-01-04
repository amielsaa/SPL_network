package bgu.spl.net.api.msg;

import java.util.List;

public class AckMsg implements Message{

    private short msgOpCode;
    private List<String> vars;
    //private List<Short> shortVars;

    public AckMsg(short msgOpCode) {
        this.msgOpCode=msgOpCode;
    }

    public AckMsg(short msgOpCode,List<String> vars) {
        this.msgOpCode=msgOpCode;
        this.vars=vars;
        //this.shortVars=shortVars;
    }

    public short getMsgOpCode() {
        return msgOpCode;
    }

    @Override
    public short getOpCode() {
        return 10;
    }

    @Override
    public List<String> getVars() {
        return vars;
    }

//    @Override
//    public byte[] getBytes() {
//        byte[] arrayBytes;
//        String stringBytes = "ACK";
//        //follow case
//        if(msgOpCode==4){
//
//        }//logstat/stat case
//        else if(msgOpCode==7 | msgOpCode==8) {
//
//        } else{
//
//        }
//        return null;
//
//    }
}
