import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 12345;
    private List<ClientHandler> clients;

    public Server() {
        clients = new ArrayList<>();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket, this, clients);
                clients.add(clientHandler);
                System.out.println("New Client connected! 現在　" + clients.size() + "　人入室しています。");


                //指定の人数以上入室したらゲーム開始
                int requiredNumber = 1;
                if(clients.size() >= requiredNumber) {
                    gamestart();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gamestart() {
        for(ClientHandler client : clients) {
            client.start();
        }
        System.out.println("GAME START");
        //全員答え終わるまで待機
        while(true) {
            if(allFinished()){
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        int winnerIndex = judgeWinner();
        sendResult(winnerIndex);
    }

    public boolean allFinished() {
        int count = 0;
        for(ClientHandler client : clients) {
            if(client.getIsFinished()) count++;
        }
        return count == clients.size();
    }

    public int judgeWinner() {
        long[] resultTime = new long[clients.size()];
        long min = clients.get(0).getAnswerTime();
        int winnerIndex = 0;
        int i;
        for(i = 1; i < clients.size(); i++){
            resultTime[i] = clients.get(i).getAnswerTime();
            if(min > resultTime[i]){
                min = resultTime[i];
                winnerIndex = i;
            }
        }
        return winnerIndex;
    }

    public void sendResult(int winnerIndex) {
        for(int i = 0; i < clients.size(); i++) {
            if(winnerIndex == i) {
                clients.get(i).sendMessage("あなたの勝利です");
            } else {
                clients.get(i).sendMessage("あなたの負けです");
            }
        }
    }
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}

