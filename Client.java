import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;


    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to server.");

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in);

            String message;
            String question;
            String answer;
//            while (!(message = userInput.readLine()).equals("exit")) {
//                out.println(message);
//            }
            System.out.println("ゲーム開始を待っています...");
            message = in.readLine();
            System.out.println(message);
            //問題1の処理の例
            question = in.readLine();
            System.out.print(question);
            answer = sc.nextLine();
            out.println(answer);
            out.flush();
            message = in.readLine();
            System.out.println(message);

            userInput.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}