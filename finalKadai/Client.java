package finalKadai;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to server.");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String qustion = (String)ois.readObject();
            System.out.println("問題は " +qustion+"\sです");
            System.out.println("解答を入力してください");
            Scanner sc = new Scanner(System.in);
            int answer = sc.nextInt();

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(answer);
            oos.flush();

            /*BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = userInput.readLine()) != null) {
                out.println(message);
            }

            userInput.close();
            out.close();*/
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
