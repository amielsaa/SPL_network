package bgu.spl.net.api;

import bgu.spl.net.api.msg.AckMsg;
import bgu.spl.net.api.msg.Message;
import bgu.spl.net.api.msg.MessageFactory;

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
    private Decoder decoder = new Decoder();

    @Override
    public Message decodeNextByte(byte nextByte) {
        //add if not a character but a number

        if(opCode==-1) {
            pushByteToShort(nextByte);
            if(shortcount==2){
                popShort();
                decoder.setOpCode(opCode);
            }
        }
//        else if(nextByte =='\0'){
//            vars.add(popString());
//        }
        else if (nextByte == ';') {
//            if(len>0)
//                vars.add(popString());
            Message msg = decoder.popMessage();
            decoder.clean();
            //opBytes = new byte[2];
            opCode = -1;
            shortcount = 0;
            return msg;
        } else {
            decoder.decode(nextByte);
            //pushByte(nextByte);
        }

        return null;
    }

    @Override
    public byte[] encode(Message message) {
        //notification
        if(message.getOpCode()==9){

        }//ack
        else if(message.getOpCode()==10){
            return encodeACK(message);
        }//error
        else if(message.getOpCode()==11){

        }//block
        else if(message.getOpCode()==12){

        }
        return null;
    }

    private void pushByteToShort(byte nextByte) {
        opBytes[shortcount++] = nextByte;
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

//    private Message popFinalResult() {
//        Message finalResult = MessageFactory.createMessage(opCode,vars);
//        System.out.println(vars.toString());
//        vars = new ArrayList<>();
//        opBytes = new byte[2];
//        opCode = -1;
//        shortcount = 0;
//        len = 0;
//
//        return finalResult;
//    }

    private byte[] encodeACK(Message message) {
        byte[] bytesArray = new byte[0];
        short msgOpCode = ((AckMsg)message).getMsgOpCode();
        byte[] opCodeArray = shortToBytes((short)10);
        byte[] msgOpCodeArray = shortToBytes(msgOpCode);
        byte[] stringBytes = "ACK".getBytes();
        //follow case
        if(msgOpCode==4){
            int bytesArrayLength = message.getVars().get(1).length()+opCodeArray.length+msgOpCodeArray.length+2;
            bytesArray = new byte[bytesArrayLength];
            byte[] usernameBytes = message.getVars().get(1).getBytes();
            System.arraycopy(opCodeArray,0,bytesArray,0,2);
            System.arraycopy(msgOpCodeArray,0,bytesArray,2,2);
            System.arraycopy(usernameBytes,0,bytesArray,4,usernameBytes.length);
            bytesArray[bytesArrayLength-2] = '\0';
        }//logstat/stat case
        else if(msgOpCode==7 | msgOpCode==8) {

        } else{//need to change
            bytesArray = new byte[stringBytes.length+5];
            System.arraycopy(opCodeArray,0,bytesArray,0,2);
            System.arraycopy(msgOpCodeArray,0,bytesArray,2,2);
            System.arraycopy(stringBytes,0,bytesArray,4,stringBytes.length);
        }
        bytesArray[bytesArray.length-1] = ';';
        return bytesArray;
    }

    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }


}
