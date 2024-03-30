package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MyServerSocket {
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;

    public static void main(String[] args) throws IOException{
        server = new ServerSocket(port);
        RequestHandler handler = new RequestHandler();
        while(true){
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            final int maxMessageLenght = 1024;
            byte[] messageBytes = new byte[maxMessageLenght];

            int bytesRead = 0;
            while(bytesRead < messageBytes.length){
                int currRead = in.read(messageBytes, bytesRead, messageBytes.length-bytesRead);

                if (currRead > 0){
                    System.out.println("Message Received: " + new String(messageBytes, bytesRead, currRead, StandardCharsets.UTF_8));
                    messageBytes[bytesRead+currRead] = " ".getBytes()[0];
                    StatusCode resResult = handler.Response(messageBytes,bytesRead, currRead+1, out);
                    if (resResult.equals(StatusCode.EXIT))
                        break;
                    bytesRead += currRead;
                } else{
                    break;
                }
            }

            in.close();
            out.close();
            socket.close();
        }
    }
}
