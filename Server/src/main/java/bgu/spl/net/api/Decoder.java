package bgu.spl.net.api;

import bgu.spl.net.api.msg.LoginMsg;
import bgu.spl.net.api.msg.Message;
import bgu.spl.net.api.msg.MessageFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Decoder {


    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private Message message;
    private short opCode;
    private List<String> vars;
    private byte currentByte;
    private short[] shorts = new short[0];

    public Decoder() {
        vars = new ArrayList<>();
    }

    public void setOpCode(short opCode){
        message = MessageFactory.createMessage(opCode,vars,shorts);
        this.opCode = opCode;
    }

    public void decode(byte nextByte) {
        this.currentByte=nextByte;
        actByOpCode();
    }

    public void clean() {
        vars = new ArrayList<>();
        shorts = new short[0];
        opCode = -1;
        len = 0;
    }

    public Message popMessage() {
        return message;
    }

    private void actByOpCode() {
        switch (opCode){
            case 1:
                registerOp();
                break;
            case 2:
                loginOp();
                break;
            case 3:
                logoutOp();
                break;

        }
    }


    private void registerOp() {
        if(currentByte=='\0'){
            vars.add(popString());
        }else {
            pushByte(currentByte);
        }
    }

    private void loginOp() {
        if(vars.size()==2){
            if(currentByte=='\1')
                vars.add("1");
            else
                vars.add("0");
        } else if(currentByte=='\0') {
            vars.add(popString());
        } else {
            pushByte(currentByte);
        }
    }

    private void logoutOp() {

    }

    private String popString() {
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }


}