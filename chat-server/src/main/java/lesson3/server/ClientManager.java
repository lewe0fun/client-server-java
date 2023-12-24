package lesson3.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable {
    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;

    public final static ArrayList<ClientManager> clients = new ArrayList<>();

    public ClientManager(Socket socket) {
        this.socket = socket;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            name = bufferedReader.readLine();
            clients.add(this);
            System.out.println(name + " подключился к чату.");
            broadcastMessage("Server: " + name + " подключился к чату.");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }


    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                broadcastMessage(bufferedReader.readLine());
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    /**
     * Ретрансляция сообщений другим
     * name - от кого
     * client.name - кому
     */
    private void broadcastMessage(String message) {
        boolean pm;
        boolean dpm;
        for (ClientManager client : clients) {
            pm = false;
            dpm = false;
            try {
                Character ch = message.split(" ")[1].charAt(0);
                if (ch.equals('@')) pm = true;
                if ((message.split(" ")[1].equals("@" + client.name))) {
                    System.out.println("Клиент: " + name + " написал в приват клиенту: " + client.name);
                    dpm = true;
                }


                if (!client.name.equals(name)&&pm)//проверка на свои сообщения
                {
                    if (dpm)//проверка на приватные сообщения типа: @name
                    {
                        client.bufferedWriter.write( message);
                        client.bufferedWriter.newLine();
                        client.bufferedWriter.flush();
                    }
                }
                else if (!client.name.equals(name)){
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        // Удаление клиента из коллекции
        removeClient();
        try {
            if (bufferedReader != null)
                bufferedReader.close();
            if (bufferedWriter != null)
                bufferedWriter.close();
            if (socket != null)
                socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeClient() {
        clients.remove(this);
        System.out.println(name + " покинул чат.");
        broadcastMessage("Server: " + name + " покинул чат.");
    }

}
