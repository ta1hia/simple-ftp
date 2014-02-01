package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: shawli
 * Date: 01/02/14
 * Time: 11:12 AM
 */


public class Client {
    public static void main(String args[]) throws Exception {
        Socket soc = new Socket("127.0.0.1", 6000);
        System.out.println("> Connecting to FTP server on port 6000");
        FTPClient ftpt = new FTPClient(soc);
        ftpt.run();

    }
}

class FTPClient {
    Socket ClientSoc;

    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;

    FTPClient(Socket soc) {
        try {
            ClientSoc = soc;
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception ex) {
        }
    }

    void SendFile() throws Exception {

    }

    void ReceiveFile() throws Exception {

    }

    public void run() throws Exception {
        while (true) {

        }
    }
}