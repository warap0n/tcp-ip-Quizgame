package tcp_ip_Quizgame;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private List<ClientHandler> clients;
    private List<ClientHandler> corrects; //正解者だけのリスト

    public Server() {
        clients = new ArrayList<>();
        corrects = new ArrayList<>();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            Scanner scanner = new Scanner(System.in);
            System.out.println("参加人数を指定してください");
            int requiredNumber = scanner.nextInt();

            while (true) {

                System.out.println("待機中　現在　" + clients.size() + "　人入室しています。");
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket, this, clients);
                clients.add(clientHandler);
                System.out.println("New Client connected! 現在　" + clients.size() + "　人入室しています。");


                //指定の人数以上入室したらゲーム開始
                if(clients.size() >= requiredNumber) {
                    gamestart();
                    clients.clear();  //参加者リセット
                    corrects.clear(); //正解者リセット
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gamestart() {
        int digit = 2; //1なら一桁、2なら二桁
        String kind = "+" ; //四則演算の種類
        for(ClientHandler client : clients) {
            client.setDigitAndKind(digit,kind);
            client.start();
        }
        System.out.println("GAME START");
        //全員答え終わるまで待機
            while (true) {
                if (allFinished()) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            for (ClientHandler client : clients) { //正解してるものだけをcorrectsに追加
                if (client.getCorrect()) {
                    corrects.add(client);
                }
            }
            Collections.sort(corrects); //解答時間で昇順にソートしている
            sendResult();
            showRanking();
    }

    public boolean allFinished() {
        int count = 0;
        for(ClientHandler client : clients) {
            if(client.getIsFinished()) count++;
        }
        return count == clients.size();
    }
    public void showRanking(){
        for(ClientHandler client : corrects){
            System.out.println(client.getRank()+"位"+" : "+client.getClientName());
        }
    }


    public void sendResult() {
        for(int i = 0; i < corrects.size(); i++){
            corrects.get(i).setRanking(corrects);
        }
        for(ClientHandler client: corrects){
            client.sendMessage(client.getClientName()+"は"+client.getRank()+"位です。");
        }
    }
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}

