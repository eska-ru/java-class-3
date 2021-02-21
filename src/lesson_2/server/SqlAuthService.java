package lesson_2.server;

import java.sql.*;

public class SqlAuthService implements AuthService {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    public SqlAuthService() throws ClassNotFoundException, SQLException {
        init();
        initUsersData();
    }

    private void initUsersData() throws SQLException {
        statement.execute("INSERT INTO 'users' ('nickname', 'password') VALUES ('qwe', 'qwe')");
        statement.execute("INSERT INTO 'users' ('nickname', 'password') VALUES ('asd', 'asd')");
        statement.execute("INSERT INTO 'users' ('nickname', 'password') VALUES ('zxc', 'zxc')");
    }

    private void init() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:ChatData.s2db");
        statement = connection.createStatement();
        statement.execute("DROP TABLE 'users';");
        statement.execute(
                "CREATE TABLE 'users'" +
                        "('nickname' text PRIMARY KEY, 'password' text);");
        statement.execute(
                "CREATE TABLE if not exists 'users_new'" +
                        "('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            resultSet = statement.executeQuery(
                    "SELECT * FROM 'users' WHERE nickname ='" + login + "' AND password = '"+ password +"';"
            );
            if (resultSet.next()) {
                return resultSet.getString("nickname");
            }
        } catch (SQLException throwables) {
        }

        return null;
    }

    @Override
    public boolean tryToChangeNickname(String nickname, String newNickname) {
        try {
            statement.execute(
                    "UPDATE users SET nickname = '"+newNickname+"' WHERE nickname = '"+nickname+"';");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
}
