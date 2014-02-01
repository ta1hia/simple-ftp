package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
        System.out.println("FTP Server Started on Port Number 6000");
        while (true) {
            System.out.println("Waiting for Connection ...");
            FTPHandler t = new FTPHandler(soc.accept());

        }
    }
}

class FTPHandler extends Thread {
    int[] data_connections = new int[10];
    String REMOTE_HOME = "remotedir/";
    Socket clientsocket;

    DataInputStream cin;
    DataOutputStream cout;

    FTPHandler(Socket soc) {
        try {
            clientsocket = soc;
            cin = new DataInputStream(clientsocket.getInputStream());
            cout = new DataOutputStream(clientsocket.getOutputStream());
            System.out.println("FTP Client Connected ...");
            start();
        } catch (Exception ex) {
        }
    }

    void SendFile() throws Exception {
        Integer nextport = -1;
        String filename = cin.readUTF();
        System.out.println("Client filename and path: " + REMOTE_HOME + filename);

        File f = new File(REMOTE_HOME + filename);

        /* Send client confirmation if client was found */
        if (f.exists()) {
            System.out.println("File found");
            cout.writeUTF("FOUND");
        } else {
            System.out.println("Could not find file");
            cout.writeUTF("NOTFOUND");
            return;
        }

        /* Send client port # for data connection */
        nextport = getAvailableDataConnection();
        if (nextport == -1) {
            System.out.println("No data port available");
            cout.writeUTF(Integer.toString(nextport));
            return;
        }

        /* Open and prepare data connection */
        System.out.println("Openning data connection for file transfer on port " + (6010 + nextport));
        cout.writeUTF(Integer.toString(nextport));
        ServerSocket dsock = new ServerSocket(6010 + nextport);
        Socket client_dsock = dsock.accept();
        DataOutputStream dout = new DataOutputStream(client_dsock.getOutputStream());

        /* File transfer */
        FileInputStream fin = new FileInputStream(f);
        int len = 1024;
        byte[] buffer;
        while (len == 1024) {
            buffer = new byte[1024];
            len = fin.read(buffer);
            dout.write(buffer, 0, len);
        }
        fin.close();
        System.out.println("File transfer to client finished");
        data_connections[nextport] = 0;
    }

    void ReceiveFile() throws Exception {
        String filename = cin.readUTF();
        if (filename.compareTo("") == 0) {
            return;
        }
        File f = new File(filename);

        if (f.exists()) {
        } else {
        }


    }

    Integer getAvailableDataConnection() {

        int nextport = -1;
        for (int i = 0; i < 10; i++) {
            if (data_connections[i] == 0) {
                nextport = i;
                break;
            }
        }
        return nextport;
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for command");
                String command = cin.readUTF();
                System.out.println("Command is: " + command);

                if (command.compareToIgnoreCase("RETR") == 0) {
                    System.out.println("Client command: RETR");
                    SendFile();
                } else if (command.compareToIgnoreCase("STOR") == 0) {
                    System.out.println("Client command: STOR");
                    ReceiveFile();
                } else if (command.compareToIgnoreCase("QUIT") == 0) {
                    System.out.println("Client command: QUIT");
                    clientsocket.close();
                    return;
                } else {
                /* couldn't recognize command */
                    System.out.println("Client command: invalid");
                }

            } catch (Exception ex) {
            }
        }
    }
}

