package lesson_6.server;

import org.sqlite.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileWordFilter implements WordFilter {
    private final String FILENAME_PROFANITY = "profanityWords.txt";
    private List<String> profanityWords;

    private final String FILENAME_TYPO = "typoWords.txt";
    private Map<String, String> typoWords;

    public FileWordFilter() {
        try {
            profanityWords = Files.readAllLines(Path.of(FILENAME_PROFANITY));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            List<String> typoLines = Files.readAllLines(Path.of(FILENAME_TYPO));
            typoWords = new HashMap<>();
            for (String typoLine : typoLines) {
                String[] arr = typoLine.split(" ", 2);
                if (arr.length < 2) {
                    continue;
                }
                typoWords.put(arr[0].toLowerCase(), arr[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String validateSentence(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        String[] arr = str.split(" ");

        for (int i = 0; i < arr.length; i++) {
            if (isProfanityWord(arr[i])) {
                arr[i] = ("%").repeat(arr[i].length());
            } else {
                arr[i] = correctTypo(arr[i]);
            }
        }

        return StringUtils.join(Arrays.asList(arr), " ");
    }

    @Override
    public Boolean isProfanityWord(String str) {
        if (str == null) {
            return false;
        }
        return profanityWords.contains(str.toLowerCase());
    }

    @Override
    public String correctTypo(String str) {
        if (str == null) {
            return "";
        }

        if (typoWords.containsKey(str.toLowerCase())) {
            return typoWords.get(str.toLowerCase());
        }

        return str;
    }

    @Override
    public void addProfanityWord(String str) {
        System.out.println("Not implementing yet");
    }

    @Override
    public void addRightWord(String str) {
        System.out.println("Not implementing yet");
    }
}
