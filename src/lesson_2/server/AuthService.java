package lesson_2.server;

public interface AuthService {
    String getNicknameByLoginAndPassword(String login, String password);
    boolean tryToChangeNickname(String nickname, String newNickname);
}
