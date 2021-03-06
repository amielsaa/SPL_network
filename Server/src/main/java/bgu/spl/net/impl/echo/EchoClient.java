package bgu.spl.net.impl.echo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    public static void main(String[] args) throws IOException, InterruptedException {

        if (args.length == 0) {
            args = new String[]{"127.0.0.1", "hello"};
        }

        if (args.length < 2) {
            System.out.println("you must supply two arguments: host, message");
            System.exit(1);
        }
        Scanner sc = new Scanner(System.in);


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
