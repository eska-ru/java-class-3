package lesson_6.server;

import lesson_6.commands.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private DataInputStream in;
    private DataOutputStream out;

    private String nickname;

    public ClientHandler(Server server, Socket socket) {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals(Command.END)) {
                                System.out.println("client want to disconnected ");
                                out.writeUTF(Command.END);
                                throw new RuntimeException("client want to disconnected");
                            }
                            else if (str.startsWith(Command.AUTH)) {
                                String[] token = str.split("\\s");
                                if (token.length != 3) {
                                    sendMsg("Ошибка при передаче логина / пароля");
                                    break;
                                }
                                String newNick = server.getAuthService()
                                        .getNicknameByLoginAndPassword(token[1], token[2]);
                                if (newNick != null) {
                                    nickname = newNick;
                                    sendMsg(Command.AUTH_OK + " " + nickname);
                                    server.subscribe(this);
                                    server.sendHistory(this);
                                    break;
                                } else {
                                    sendMsg("Неверный логин / пароль");
                                }
                            }
                            else {
                                sendMsg("Ошибка в команде");
                            }
                        }
                    }

                    //цикл работы
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {
                            if (str.equals(Command.END)) {
                                out.writeUTF(Command.END);
                                break;
                            } else if (str.startsWith(Command.PRIVATE_MSG)) {
                                String[] token = str.split("\\s", 3);
                                if (token.length != 3) {
                                    sendMsg("Ошибка в команде");
                                    continue;
                                }

                                if (!server.sendPrivateMsg(this, token[1], token[2])) {
                                    sendMsg("Ошибка при отправке личного сообщения");
                                }
                            } else if (str.startsWith(Command.CHANGE_NICKNAME)) {
                                String[] token = str.split("\\s", 2);
                                if (token.length != 2) {
                                    sendMsg("Ошибка в команде");
                                    continue;
                                }

                                server.changeNickname(this, token[1]);
                            } else {
                                sendMsg("Ошибка в команде");
                            }

                            continue;
                        }

                        server.broadcastMsg(this, str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Client disconnected");
                    server.unsubscribe(this);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
