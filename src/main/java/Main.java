import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {

    public static final int PORT = 8989;

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));


        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Запуск сервера " + PORT + "...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    String word = in.readLine();
                    if (word.length() == 0) {
                        throw new IllegalStateException("Не корректный ввод");
                    }
                    Gson gson = new Gson();
                    List<PageEntry> result = engine.search(word);
                    out.println(gson.toJson(result));
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}