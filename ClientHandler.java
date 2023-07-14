package tcp_ip_Quizgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ClientHandler extends Thread implements Comparable<ClientHandler>{
    private Socket clientSocket;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;

    private List<ClientHandler> clients;
    private String clientName;
    private List<ClientHandler> ranking;
    private boolean correct;
    private long answerTime;

    //server側で解答を終えたかを確認するための変数
    private boolean finished;
    String again;
    private String question;
    private String correctAnswer;
    private int digit;
    private String kind;

    public ClientHandler(Socket socket, Server server, List<ClientHandler> clients) throws IOException {
        this.clientSocket = socket;
        this.server = server;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        answerTime = 0;
        finished = false;
        again = "";
    }

    @Override
    public void run() {
        try {
            //ここでclientとやり取りする。以下はやり取りの例。
            String message;
            String answerFromClient;
            message = "名前を入力してください";
            out.println(message);
            out.flush();
            clientName = in.readLine();
            message = "GAME START";
            out.println(message);
            out.flush();
            Thread.sleep(1000);
            makeQuestion(digit,kind);
            out.println(question);
            out.flush();
            long startTime = System.currentTimeMillis();
            answerFromClient = in.readLine();
            if(answerFromClient.equals(correctAnswer)){
                long endTime = System.currentTimeMillis();
                setAnswerTime(endTime - startTime);
                correct = true;
                finished = true;
            }else{
                //不正解の時の処理は任せます
                correct = false;
                out.println("不正解です");
                out.flush();
                finished = true;
            }
            again = in.readLine();




        } catch (IOException | InterruptedException e) {
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

    public void makeQuestion(int digit, String kind){ //桁数と種類で問題を分ける
        if(digit == 1 && kind.equals("+") ) {
            int x;
            int y;
            Random random = new Random();
            x = random.nextInt(10);
            y = random.nextInt(10);
            question = x + " + " + y + " = ";
            correctAnswer = String.valueOf(x + y);
        }
        if(digit == 2 && kind.equals("+") ) {
            int x;
            int y;
            Random random = new Random();
            x = random.nextInt(100);
            y = random.nextInt(100);
            question = x + " + " + y + " = ";
            correctAnswer = String.valueOf(x + y);
        }
        if(digit == 1 && kind.equals("-") ) {
            int x;
            int y;
            Random random = new Random();
            x = random.nextInt(10);
            y = random.nextInt(10);
            if(x < y){
                int temp = x;
                x = y;
                y = temp;
            }
            question = x + " - " + y + " = ";
            correctAnswer = String.valueOf(x - y);
        }
        if(digit == 2 && kind.equals("-") ) {
            int x;
            int y;
            Random random = new Random();
            x = random.nextInt(100);
            y = random.nextInt(100);
            if(x < y){
                int temp = x;
                x = y;
                y = temp;
            }
            question = x + " - " + y + " = ";
            correctAnswer = String.valueOf(x - y);
        }
        if(digit == 1 && kind.equals("*") ) {
            int x;
            int y;
            Random random = new Random();
            x = random.nextInt(10);
            y = random.nextInt(10);
            question = x + " * " + y + " = ";
            correctAnswer = String.valueOf(x * y);
        }
        if(digit == 2 && kind.equals("*") ) {
            int x;
            int y;
            Random random = new Random();
            x = random.nextInt(100);
            y = random.nextInt(100);
            question = x + " * " + y + " = ";
            correctAnswer = String.valueOf(x * y);
        }
        if(digit == 1 && kind.equals("/") ) {
            int x;
            int y;
            Random random = new Random();
            x = random.nextInt(10);
            y = random.nextInt(10);
            if(x < y){
                int temp = x;
                x = y;
                y = temp;
            }
            question = x + " / " + y + " = ";
            correctAnswer = String.valueOf(x / y);
        }
        if(digit == 1 && kind.equals("/") ) {
            int x;
            int y;
            Random random = new Random();
            x = random.nextInt(100);
            y = random.nextInt(100);
            if(x < y){
                int temp = x;
                x = y;
                y = temp;
            }
            question = x + " + " + y + " = ";
            correctAnswer = String.valueOf(x / y);
        }

    }

    public void setDigitAndKind(int digit, String kind){  //問題の桁数と種類を指定
        this.digit = digit;
        this.kind = kind;
    }
    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }
    public String getClientName(){
        return clientName;
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
    public void setRanking(List<ClientHandler> ranking){
        this.ranking = ranking;
    }

    public int getRank(){ //自分の順位を返す
        int rank = 0;
        for(int i = 0; i < ranking.size(); i++){
            if(this.clientName.equals(ranking.get(i).clientName)){
                rank = i + 1;
            }
        }
        return rank;
    }

    public boolean getCorrect(){
        return correct;
    }

    @Override
    public int compareTo(ClientHandler another){
        long anotherTime = another.getAnswerTime();
        long comparison = answerTime-anotherTime;
        if(comparison < 0){
            return -1;
        }else if(comparison > 0){
            return 1;
        }else{
            return 0;
        }
    }
}


