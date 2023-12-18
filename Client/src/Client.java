import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 8000);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("connected to server");
            String request ="Murmansk-321321";
            System.out.println("Request: "+request);
            writer.write(request+'\n');
            writer.flush();

            String response = reader.readLine();
            System.out.println("Response: "+response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}