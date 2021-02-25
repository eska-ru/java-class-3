package lesson_4.chat.server;

import java.sql.SQLException;

public interface AuthService {
    String getNicknameByLoginAndPassword(String login, String password);
    boolean tryToChangeNickname(String nickname, String newNickname);
    void close() throws SQLException;
}
