package lesson_4.chat.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileHistoryService implements HistoryService {
    private final String FILENAME = "history.log";
    RandomAccessFile file;

    public FileHistoryService() throws FileNotFoundException {
        file = new RandomAccessFile(new File(FILENAME), "rw");
    }

    @Override
    public void addMessage(String msg) throws IOException {
        file.seek(file.length());
        file.writeUTF(msg + "\n");
        file.getFD().sync();
    }

    @Override
    public List<String> getLastMessages(int count) throws IOException {
        long start = file.length();
        long end = file.length();
        long seek = file.length();
        List<String> list = new ArrayList<>(count);
        while (list.size() < count && start > 0) {
            char symbol = ' ';
            while (symbol != '\n' && seek > 0) {
                file.seek(seek);
                symbol = (char)file.read();
                --seek;
            }
            start = seek;
            String str = file.readLine();
            if (str == null || str.isEmpty()) {
                continue;
            }

            str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
            list.add(str);
            end = start - 1;
        }

        return list;
    }

    @Override
    public void close() {
        if (file != null) {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
