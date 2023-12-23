package lesson3.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите имя:");
            String name = scanner.nextLine();
            Socket socket = new Socket("localhost", 1440);

            InetAddress inetAddress = socket.getInetAddress();
            System.out.println("InetAddress: " + inetAddress +
                    "\nRemote IP: " + inetAddress.getHostAddress() +
                    "\nLocalPort:" + socket.getLocalPort()+
                    "\nType @ before name for private message");

            Client client = new Client(socket, name);
            client.listenForMessage();
            client.sendMessage();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("хост отсутствует");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}