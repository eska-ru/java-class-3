package lesson_6.server;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private final int PORT = 8189;
    private final List<ClientHandler> clients;
    private final AuthService authService;
    private final HistoryService historyService;
    private final WordFilter wordFilter;

    private static final Logger log = Logger.getLogger(Server.class);
    private static final String NULL_PARAMETER = "Server function %s received null '%s' parameter";

    public Server() throws ClassNotFoundException, SQLException, FileNotFoundException {
        clients = new CopyOnWriteArrayList<>();
        authService = new SqlAuthService();
        historyService = new FileHistoryService();
        wordFilter = new FileWordFilter();
        log.info("Server initialized");

        try (ServerSocket server  = new ServerSocket(PORT)) {
            log.info("Server started");

            while (true) {
                Socket socket = server.accept();
                log.info("Client connected " + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
                log.info("Client handler started");
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Server failed to start", e);
        } finally {
            authService.close();
            historyService.close();
        }
        log.info("Server successfully closed");
    }

    public void broadcastMsg(ClientHandler sender, String msg) {
        final String METHOD_NAME = "broadcastMsg";
        if (sender == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "sender"));
            return;
        }

        if (msg == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "msg"));
            return;
        }

        msg = wordFilter.validateSentence(msg);

        String message = String.format("[ %s ] : %s", sender.getNickname(), msg);
        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }
        log.info("Server broadcast message: " + message);

        try {
            historyService.addMessage(message);
            log.info("Server store history: " + message);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Server failed to store history: " + message);
        }
    }

    public boolean sendPrivateMsg(ClientHandler from, String nickname, String msg) {
        final String METHOD_NAME = "sendPrivateMsg";
        if (from == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "from"));
            return false;
        }

        if (nickname == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "nickname"));
            return false;
        }

        if (msg == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "msg"));
            return false;
        }
        ClientHandler to = getClientByNickname(nickname);
        if (to == null) {
            log.error("Server function sendPrivateMsg ");
            return false;
        }

        String message = String.format("[ %s --> %s ] : %s", from.getNickname(), to.getNickname(), msg);
        from.sendMsg(message);
        to.sendMsg(message);
        log.info("Server private message: " + message);

        return true;
    }

    private ClientHandler getClientByNickname(String nickname) {
        final String METHOD_NAME = "getClientByNickname";
        if (nickname == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "nickname"));
            return null;
        }

        for (ClientHandler client : clients) {
            if (client.getNickname().equals(nickname)) {
                return client;
            }
        }

        return null;
    }

    public void sendHistory(ClientHandler clientHandler) {
        final String METHOD_NAME = "sendHistory";
        if (clientHandler == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "clientHandler"));
            return;
        }

        try {
            for (String lastMessage : historyService.getLastMessages(100)) {
                clientHandler.sendMsg(lastMessage);
            }
            log.info("Server send history to " + clientHandler.getNickname());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Server can't send history to " + clientHandler.getNickname());
        }
    }

    public void subscribe(ClientHandler clientHandler){
        final String METHOD_NAME = "subscribe";
        if (clientHandler == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "clientHandler"));
            return;
        }

        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler){
        final String METHOD_NAME = "unsubscribe";
        if (clientHandler == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "clientHandler"));
            return;
        }

        clients.remove(clientHandler);
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void changeNickname(ClientHandler clientHandler, String newNickname) {
        final String METHOD_NAME = "changeNickname";
        if (clientHandler == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "clientHandler"));
            return;
        }

        if (newNickname == null) {
            log.error(String.format(NULL_PARAMETER, METHOD_NAME, "newNickname"));
            return;
        }

        if (authService.tryToChangeNickname(clientHandler.getNickname(), newNickname)) {
            clientHandler.setNickname(newNickname);
            clientHandler.sendMsg("Nickname successfully changed. New nickname is " + newNickname + ".");
            log.info("Server successfully changed nickname to " + clientHandler.getNickname());
        } else {
            log.error("Server can't change nickname " + clientHandler.getNickname());
        }
    }
}
