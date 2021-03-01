package lesson_4.chat.client;

import lesson_4.chat.commands.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static final int PORT = 8189;
    private static final String IP_ADDRESS = "localhost";
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner inConsole;
    private Thread receivingThread;
    private Thread sendingThread;

    public void start() throws IOException, InterruptedException {
        try {
            connect();
            init();
            tryToAuth();
            startSending();
            startReceiving();

            receivingThread.join();
            sendingThread.join();

        } finally {
            if (inConsole != null) inConsole.close();
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        }
    }

    private void startReceiving() {
        receivingThread = new Thread(() -> {
            while (true) {
                String str;
                try {
                    str = in.readUTF();
                } catch (NoSuchElementException | IOException e) {
                    System.out.println("Socket closed");
                    throw new RuntimeException("The server disconnected us");
                }
                if (str.equals(Command.END)) {
                    System.out.println("Server disconnected");
                    System.exit(0);
                }
                System.out.println(str);
            }
        });
        receivingThread.start();
    }

    private void startSending() {
        sendingThread = new Thread(() -> {
            while (true) {
                String str = inConsole.nextLine();
                try {
                    out.writeUTF(str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (str.equals(Command.END)) {
                    System.out.println("Client closed");
                    System.exit(0);
                }
            }
        });
        sendingThread.start();
    }

    private void init() {
        inConsole = new Scanner(System.in);
    }

    private void connect() throws IOException {
        socket = new Socket(IP_ADDRESS, PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        System.out.println("Connected to the server");
    }

    private void tryToAuth() throws IOException {
        System.out.println("Please, enter your nickname");
        String nickname = inConsole.nextLine().trim();

        System.out.println("Please, enter your password (one word)");
        String pass = inConsole.nextLine().trim();

        String msg = String.format("%s %s %s", Command.AUTH, nickname, pass);
        out.writeUTF(msg);

        String answer = in.readUTF();
        if (!answer.startsWith(Command.AUTH_OK)) {
            System.out.println("Something wrong. Please, try again.");
            tryToAuth();
        } else {
            System.out.println("Right nickname and password!");
            System.out.println("The chat is started!");
        }
    }
}
