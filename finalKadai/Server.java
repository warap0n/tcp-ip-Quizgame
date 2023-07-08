package finalKadai;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
public class Server {
    private static final int PORT = 12345;
    private HashMap<String , Integer> questions = new HashMap<>();

    ArrayList<String> keyList = new ArrayList<String>(); //キーを格納する
    public void makeQuestions(){
        questions.put("1+1",2);
        questions.put("15+20",35);
        questions.put("203+305",508);
    }
    public static void main(String[] args) {
        Server server = new Server();
        server.makeQuestions();
        Random random = new Random();
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("finalKadai.Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                /*BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received from client: " + message);
                    // クライアントへの応答を返す場合はここに処理を追加
                }

                in.close();
                out.close();*/
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                //hashmapから問題の部分を取り出してListに格納する
                server.keyList.addAll(server.questions.keySet());
                int questionIndex = random.nextInt(server.keyList.size());
                String question = server.keyList.get(questionIndex); //問題をリストから取得
                long start = System.currentTimeMillis();
                oos.writeObject(question);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                int answer = (int)ois.readObject();
                long end = System.currentTimeMillis();
                if(server.questions.get(question) == answer){
                    System.out.println("正解");
                    System.out.println("解答時間は"+(end-start)+"ミリ秒です");
                }else{
                    System.out.println("不正解");
                }


                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

