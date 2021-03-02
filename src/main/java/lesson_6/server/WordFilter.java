package lesson_6.server;

public interface WordFilter {
    String validateSentence(String str);
    Boolean isProfanityWord(String str);
    String correctTypo(String str);

    void addProfanityWord(String str);
    void addRightWord(String str);
}
