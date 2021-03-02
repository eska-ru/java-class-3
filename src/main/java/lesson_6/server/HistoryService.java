package lesson_6.server;

import java.io.IOException;
import java.util.List;

public interface HistoryService {
    void addMessage(String msg) throws IOException;
    List<String> getLastMessages(int count) throws IOException;
    void close();
}
