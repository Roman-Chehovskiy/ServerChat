import com.sun.deploy.util.SessionState;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    public ServerSocket server;
    public String input;
    public ArrayList<Client> clientList = new ArrayList<>();

    public Server() {
        try {
            // создаем серверный сокет на порту 1234
            server = new ServerSocket(1234);
            System.out.println("Waiting...");
        } catch (IOException e) {
            System.out.println(e.fillInStackTrace());
        }
    }



    class Client extends Thread {
        Socket socket;
        Scanner in;
        PrintStream out;


        public Client() {
            try {
                this.socket = server.accept();
                System.out.println("Waiting...");
                System.out.println("Client connected!");

                // получаем потоки ввода и вывода
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                // создаем удобные средства ввода и вывода
                in = new Scanner(is);
                out = new PrintStream(os);
                out.println("Welcome to chat!");
            } catch (IOException e) {
                System.out.println(e.fillInStackTrace());
            }
            this.start();
        }

        void print(String input) {
            out.println(input);
        }

        @Override
        public void run() {
            try {
                // читаем из сети и пишем в сети
                 input = in.nextLine();
                while (!input.equals("bye")) {
                    for (Client client : clientList) {
                        if (client.equals(this)) continue;
                        client.print(input);
                    }
                    input = in.nextLine();
                }
                socket.close();
            } catch (IOException e) {
                System.out.println(e.fillInStackTrace());
            }
        }
    }

    public void starting() {
        while (true) {
            clientList.add(new Client());
        }
    }

    public static void main(String[] args) {
        new Server().starting();

    }
}

