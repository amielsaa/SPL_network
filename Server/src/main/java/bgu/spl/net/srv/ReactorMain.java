package bgu.spl.net.srv;

import bgu.spl.net.api.BGSEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

public class ReactorMain {
    public static void main(String[] args) {
        Server.reactor(Integer.parseInt(args[1]),
                       Integer.parseInt(args[0]),
                       ()->new BidiMessagingProtocolImpl(),
                       BGSEncoderDecoder::new).serve();
    }
}
