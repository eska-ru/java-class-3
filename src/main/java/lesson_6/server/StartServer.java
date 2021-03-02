package lesson_6.server;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class StartServer {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException {
        new Server();
    }
}
