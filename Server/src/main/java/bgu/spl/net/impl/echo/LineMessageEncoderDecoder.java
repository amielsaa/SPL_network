package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessageEncoderDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LineMessageEncoderDecoder implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public String decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison

        if (nextByte == ';') {
            return popString();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(String message) {
        return getZeroBytes(message); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }
    private byte[] getZeroBytes(String message){
        byte[] bytesArray = new byte[1<<10];
        int byteIndex = 0;


        String[] splitted = message.split(" ");
        short num=getOpCode(splitted[0]);
        byte[] shortBytes = shortToBytes(num);
        byte[] stringBytes;
        byte[] merged = new byte[1];
        if(num == 1) {
            stringBytes = getStringBytes(splitted,1, splitted.length);
            merged = new byte[shortBytes.length+stringBytes.length+1];
            System.arraycopy(shortBytes,0,merged,0,2);
            System.arraycopy(stringBytes,0,merged,2,stringBytes.length);
        } else if(num == 2) {
            stringBytes = getStringBytes(splitted,1, splitted.length-1);
            merged = new byte[shortBytes.length+stringBytes.length+2];
            System.arraycopy(shortBytes,0,merged,0,2);
            System.arraycopy(stringBytes,0,merged,2,stringBytes.length);
            if(splitted[splitted.length-1].equals("1"))
                merged[merged.length-2]='\1';
            else
                merged[merged.length-2]='\0';
        } else if(num==3) {
            merged = new byte[shortBytes.length+1];
            System.arraycopy(shortBytes,0,merged,0,2);
        }

        merged[merged.length-1] = ';';

        return merged;


    }

    private byte[] getStringBytes(String[] splitted,int startIndex,int endIndex) {
        int byteArrayLength = 0;
        for(int i=startIndex;i<endIndex;i++) {
            byteArrayLength=byteArrayLength+splitted[i].length()+1;
        }
        int byteIndex = 0;
        byte[] bytesArray = new byte[byteArrayLength];

        for(int i=1;i<endIndex;i++) {
            for(int j=0;j<splitted[i].length();j++) {
                bytesArray[byteIndex++] = (byte)splitted[i].charAt(j);
            }
            bytesArray[byteIndex++]='\0';
        }

        return bytesArray;
    }


    private short getOpCode(String op) {
        switch (op) {
            case "REGISTER":
                return 1;
            case "LOGIN":
                return 2;
            case "LOGOUT":
                return 3;
        }
        return 0;
    }


    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
}
