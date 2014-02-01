package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: shawli
 * Date: 01/02/14
 * Time: 11:12 AM
 */
public class FTPServer {
    public static void main(String args[]) throws Exception {
        ServerSocket soc = new ServerSocket(6000);
        System.out.println("> FTP Server Started on Port Number 6000");
        while (true) {
            System.out.println("> Waiting for Connection ...");
            FTPHandler t = new FTPHandler(soc.accept());

        }
    }
}

class FTPHandler extends Thread {
    Socket clientsocket;

    DataInputStream din;
    DataOutputStream dout;

    FTPHandler(Socket soc) {
        try {
            clientsocket = soc;
            din = new DataInputStream(clientsocket.getInputStream());
            dout = new DataOutputStream(clientsocket.getOutputStream());
            System.out.println("> FTP Client Connected ...");
            start();

        } catch (Exception ex) {
        }
    }

    void SendFile() throws Exception {
        String filename = din.readUTF();
        File f = new File(filename);

    }

    void ReceiveFile() throws Exception {
        String filename = din.readUTF();
        if (filename.compareTo("") == 0) {
            return;
        }
        File f = new File(filename);

        if (f.exists()) {
        } else {
        }



    }


    public void run() {
        while (true) {
            try {

            } catch (Exception ex) {
            }
        }
    }
}

