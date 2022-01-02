package bgu.spl.net.api;

import bgu.spl.net.api.msg.Message;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BGSEncoderDecoder implements MessageEncoderDecoder<Message> {

    private byte[] bytes = new byte[1 << 10];
    private byte[] opBytes = new byte[2];
    private int len = 0;
    private short opCode = -1;
    private int shortcount = 0;
    private List<String> vars = new ArrayList<>();

    @Override
    public Message decodeNextByte(byte nextByte) {
        if(opCode==-1) {
            pushByteToShort(nextByte);
            if(shortcount==2){
                popShort();
            }
        }
        else if(nextByte =='\0'){
            vars.add(popString());
        }
        else if (nextByte == ';') {
            if(len>0)
                vars.add(popString());
            return popFinalResult();
        } else {
            pushByte(nextByte);
        }

        return null;
    }

    @Override
    public byte[] encode(Message message) {
        return message.getBytes();
    }

    private void pushByteToShort(byte nextByte) {
        bytes[shortcount++] = nextByte;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private void popShort() {
        short result = (short)((opBytes[0] & 0xff) << 8);
        result += (short)(opBytes[1] & 0xff);
        opCode = result;
    }

    private String popString() {
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private Message popFinalResult() {
        Message finalResult = new Message(opCode,vars);
        vars = new ArrayList<>();
        opBytes = new byte[2];
        opCode = -1;
        shortcount = 0;
        len = 0;

        return finalResult;
    }


}
