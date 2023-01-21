package ServerClient;

import UtilClases.ServerUtils;
import UtilClases.BooleanSearchEngine;
import UtilClases.SearchEngineMap;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class Main {
    public final static int PORT_NUMBER = 8989;

    public static void main(String[] args) {
        final String PDF_FOLDER = "./pdfs";
        final String STOP_WORD_TXT = "./stop-ru.txt";

        BooleanSearchEngine engine = null;
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            File stopWords = new File(STOP_WORD_TXT);
            List<String> stopWordsList = new ServerUtils().txtDeserialize(stopWords);
            File folder = new File(PDF_FOLDER);
            SearchEngineMap searchEngineMap = new SearchEngineMap();
            for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                engine = new BooleanSearchEngine(new File(fileEntry.getPath()), searchEngineMap, stopWordsList);
            }

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream())
                ) {
                    out.println("Произведено подключение!");
                    out.flush();

                    String request = in.readLine().toLowerCase();
                    if (request.isEmpty()) {
                        continue;
                    }
                    String[] requestArray = request.split(" ");
                    out.print(new ServerUtils().gson(Objects.requireNonNull(engine).search(requestArray)));
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}