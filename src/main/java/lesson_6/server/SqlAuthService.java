package lesson_6.server;

import java.sql.*;

public class SqlAuthService implements AuthService {
    private Statement statement;
    private ResultSet resultSet;
    private Connection connection;

    private final String INSERT_INTO_USER = "INSERT INTO 'users' ('nickname', 'password') VALUES ('%s', '%s')";
    private final String DROP_TABLE_USER = "DROP TABLE if exists 'users';";
    private final String CREATE_TABLE_USER = "CREATE TABLE 'users' ('nickname' text PRIMARY KEY, 'password' text);";
    private final String SELECT_USER = "SELECT * FROM 'users' WHERE nickname ='%s' AND password = '%s';";
    private final String UPDATE_USER_NICKNAME = "UPDATE users SET nickname = '%s' WHERE nickname = '%s';";

    public SqlAuthService() throws ClassNotFoundException, SQLException {
        init();
        initUsersData();
    }

    private void initUsersData() throws SQLException {
        statement.execute(String.format(INSERT_INTO_USER, "qwe", "qwe"));
        statement.execute(String.format(INSERT_INTO_USER, "asd", "asd"));
        statement.execute(String.format(INSERT_INTO_USER, "zxc", "zxc"));
    }

    private void init() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:ChatData.s2db");
        statement = connection.createStatement();
        statement.execute(DROP_TABLE_USER);
        statement.execute(CREATE_TABLE_USER);
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        if (login == null || password == null) {
            return null;
        }

        try {
            resultSet = statement.executeQuery(String.format(SELECT_USER, login, password));
            if (resultSet.next()) {
                return resultSet.getString("nickname");
            }
        } catch (SQLException ignored) {
        }

        return null;
    }

    @Override
    public boolean tryToChangeNickname(String nickname, String newNickname) {
        if (nickname == null || newNickname == null) {
            return false;
        }

        try {
            statement.execute(String.format(UPDATE_USER_NICKNAME, newNickname, nickname));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void close() throws SQLException {
        resultSet.close();
        statement.close();
        connection.close();
    }
}
