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
        int byteIndex = 2;
        byte[] shortBytes = shortToBytes((short)0);
        bytesArray[0] = shortBytes[0];
        bytesArray[1] = shortBytes[1];
        String[] splitted = message.split(" ");
        if(splitted[0].equals("REGISTER")){
            for(int i=1;i<splitted.length;i++) {
                for(int j=0;j<splitted[i].length();j++) {
                    bytesArray[byteIndex++] = (byte)splitted[i].charAt(j);
                }
                bytesArray[byteIndex++]='\0';
            }
        }
        bytesArray[byteIndex] = ';';
        return bytesArray;


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
