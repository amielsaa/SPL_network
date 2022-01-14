package bgu.spl.net.api;

import bgu.spl.net.api.msg.AckMsg;
import bgu.spl.net.api.msg.ErrorMsg;
import bgu.spl.net.api.msg.Message;

import java.util.List;

public class Encoder {

    short opCode;

    public Encoder() {

    }

    public void setOpCode(short opCode) {
        this.opCode = opCode;
    }

    public void clean() {
        opCode=-1;
    }


    public byte[] encode(Message message) {
        //notification
        if(message.getOpCode()==9){
            return encodeNotification(message);
        }//ack
        else if(message.getOpCode()==10){
            return encodeACK(message);
        }//error
        else if(message.getOpCode()==11){
            return encodeError(message);
        }
        return null;
    }

    private byte[] encodeError(Message message) {
        byte[] opCodeArray = shortToBytes(opCode);
        byte[] msgOpCodeArray = shortToBytes(((ErrorMsg)message).getMsgOpCode());
        byte[] bytesArray = new byte[5];
        System.arraycopy(opCodeArray,0,bytesArray,0,2);
        System.arraycopy(msgOpCodeArray,0,bytesArray,2,2);
        bytesArray[bytesArray.length-1] = ';';
        return bytesArray;

    }

    private byte[] encodeNotification(Message message) {
        List<String> vars = message.getVars();
        String username = vars.get(0);
        String content = vars.get(1);
        String type = vars.get(2);
        byte[] bytesArray = new byte[username.length()+content.length()+6];
        byte[] contentArray = content.getBytes();
        byte[] usernameArray = username.getBytes();
        byte[] opCodeArray = shortToBytes(opCode);
        byte typeByte = '\0';
        if(type.equals("Public"))
            typeByte='\1';
        System.arraycopy(opCodeArray,0,bytesArray,0,2);
        bytesArray[2] = typeByte;
        System.arraycopy(usernameArray,0,bytesArray,3,usernameArray.length);
        bytesArray[usernameArray.length+3] = '\0';
        System.arraycopy(contentArray,0,bytesArray,4+usernameArray.length,contentArray.length);
        bytesArray[bytesArray.length-2] = '\0';
        bytesArray[bytesArray.length-1] = ';';
        return bytesArray;
    }



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
            bytesArray = new byte[4+message.getVars().size()*9+1];
            System.arraycopy(opCodeArray,0,bytesArray,0,2);
            System.arraycopy(msgOpCodeArray,0,bytesArray,2,2);
            int arrayCopyIndex = 4;
            for(int i=0;i<message.getVars().size();i++) {
                String[] splitted = message.getVars().get(i).split(" ");
                for(int j=0;j<splitted.length;j++) {
                    byte[] currentShortBytes = shortToBytes(Short.parseShort(splitted[j]));
                    System.arraycopy(currentShortBytes,0,bytesArray,arrayCopyIndex,2);
                    arrayCopyIndex+=2;
                }
                bytesArray[arrayCopyIndex] = '\0';
                arrayCopyIndex++;
            }

        } else{//need to change
            bytesArray = new byte[5];
            System.arraycopy(opCodeArray,0,bytesArray,0,2);
            System.arraycopy(msgOpCodeArray,0,bytesArray,2,2);
            //System.arraycopy(stringBytes,0,bytesArray,4,stringBytes.length);
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
