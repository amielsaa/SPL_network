package bgu.spl.net.impl.echo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    public static void main(String[] args) throws IOException, InterruptedException {

        if (args.length == 0) {
            args = new String[]{"localhost", "hello"};
        }

        if (args.length < 2) {
            System.out.println("you must supply two arguments: host, message");
            System.exit(1);
        }
        Scanner sc = new Scanner(System.in);
//        EchoClientTest echo = new EchoClientTest(args[0],7777);
//        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
//        Thread sending = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("enter: ");
//                String ms = sc.nextLine();
//                try {
//                    echo.send(ms);
//                } catch(IOException e) {}
//            }
//        });
//        Thread receiving = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    echo.receive();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        receiving.start();
//        sending.start();
//        while(echo.shouldRun()) {}
//        sending.join();
//        receiving.join();

        try (EchoClientTest echo = new EchoClientTest(args[0],7777)) {
            while(echo.shouldRun()){
                //
                System.out.println("enter: ");
                String ms = sc.nextLine();
                echo.send(ms);
                echo.receive();
                //echo.receive();
            }

        }

    }
}
