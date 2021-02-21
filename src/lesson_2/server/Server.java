package lesson_2.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private ServerSocket server;
    private final int PORT = 8189;
    private final List<ClientHandler> clients;
    private final AuthService authService;
    private final HistoryService historyService;
    private final WordFilter wordFilter;

    public Server() throws ClassNotFoundException, SQLException, FileNotFoundException {
        clients = new CopyOnWriteArrayList<>();
        authService = new SqlAuthService();
        historyService = new FileHistoryService();
        wordFilter = new FileWordFilter();

        try {
            server = new ServerSocket(PORT);
            System.out.println("server started");

            while (true) {
                Socket socket = server.accept();
                System.out.println("client connected" + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            authService.close();
            historyService.close();
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(ClientHandler sender, String msg) {
        msg = wordFilter.validateSentence(msg);

        String message = String.format("[ %s ] : %s", sender.getNickname(), msg);
        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }

        try {
            historyService.addMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendPrivateMsg(ClientHandler from, String nickname, String msg) {
        ClientHandler to = getClientByNickname(nickname);
        if (to == null) {
            return false;
        }

        String message = String.format("[ %s --> %s ] : %s", from.getNickname(), to.getNickname(), msg);
        from.sendMsg(message);
        to.sendMsg(message);

        return true;
    }

    private ClientHandler getClientByNickname(String nickname) {
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(nickname)) {
                return client;
            }
        }

        return null;
    }

    public void sendHistory(ClientHandler clientHandler) {
        try {
            for (String lastMessage : historyService.getLastMessages(100)) {
                clientHandler.sendMsg(lastMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(ClientHandler clientHandler){
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler){
        clients.remove(clientHandler);
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void changeNickname(ClientHandler clientHandler, String newNickname) {
        if (authService.tryToChangeNickname(clientHandler.getNickname(), newNickname)) {
            clientHandler.setNickname(newNickname);
            clientHandler.sendMsg("Nickname successfully changed. New nickname is " + newNickname + ".");
        }
    }
}
