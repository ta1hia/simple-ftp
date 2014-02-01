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
        System.out.println("Connecting to FTP server on port 6000");
        FTPClient ftpt = new FTPClient(soc);
        ftpt.run();

    }
}

class FTPClient {
    String DIR_HOME = "dir/";
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

    void SendFile(String filename) throws Exception {

    }

    void ReceiveFile(String filename) throws Exception {

        dout.writeUTF(filename);
        String confirm = din.readUTF();

        if (confirm.compareToIgnoreCase("FOUND") == 0) {
            System.out.println("File found. Beginning file transfer.");
        } else if (confirm.compareToIgnoreCase("NOTFOUND") == 0) {
            System.out.println("File could not be found on remote host");
            return;
        } else {
            System.out.println("Error finding file");
            return;
        }

        File f = new File(DIR_HOME + filename);
        FileOutputStream fos = new FileOutputStream(f);

        byte[] buffer;
        int len = 1024;
        while (len == 1024) {
            buffer = new byte[1024];
            len = din.read(buffer);
            fos.write(buffer, 0, len);
        }
        fos.close();
        System.out.println("File transfer from remote host finished");


    }

    public void run() throws Exception {
        while (true) {
            System.out.print("> ");
            String command = br.readLine();
            String[] tokens = command.split(" ");
            command = tokens[0];

            if (command.compareToIgnoreCase("RETR") == 0) {
                dout.writeUTF(tokens[0]);
                ReceiveFile(tokens[1]);
            } else if (command.compareToIgnoreCase("STOR") == 0) {
                dout.writeUTF(tokens[0]);
                SendFile(tokens[1]);
            } else if (command.compareToIgnoreCase("QUIT") == 0) {
                dout.writeUTF(tokens[0]);
                return;
            } else {
                /* couldn't recognize command */
                System.out.println("Client command: invalid");
            }

        }
    }
}