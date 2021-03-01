package lesson_4.chat.client;

import java.io.IOException;

public class StartClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        new Client().start();
    }
}
