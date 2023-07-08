import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;

    private List<ClientHandler> clients;

    private long answerTime;

    //server側で解答を終えたかを確認するための変数
    private boolean finished;

    public ClientHandler(Socket socket, Server server, List<ClientHandler> clients) throws IOException {
        this.clientSocket = socket;
        this.server = server;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        answerTime = 0;
        finished = false;
    }

    @Override
    public void run() {
        try {
            //ここでclientとやり取りする。以下はやり取りの例。
            String message;
            String question;
            String answerFromClient;
            String correctAnswer;

            message = "GAME START";
            out.println(message);
            out.flush();
            Thread.sleep(1000);
            question = "100^2 = ";
            correctAnswer = "10000";
            out.println(question);
            out.flush();
            long startTime = System.currentTimeMillis();
            answerFromClient = in.readLine();
            if(answerFromClient.equals(correctAnswer)){
                long endTime = System.currentTimeMillis();
                setAnswerTime(endTime - startTime);
                finished = true;
            } //不正解の時の処理は任せます

            //問題2

            answerFromClient = in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public void setFinished(boolean bool) {
        this.finished = bool;
    }

    public boolean getIsFinished() {
        return finished;
    }

    public void setAnswerTime(long answerTime) {
        this.answerTime = answerTime;
    }
    public long getAnswerTime() {
        return answerTime;
    }
}

