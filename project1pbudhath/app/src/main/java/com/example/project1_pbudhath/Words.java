package com.example.project1_pbudhath;
import java.util.Random;
public class Words {
    private static final int NUM_WORDS = 10;
    private static final String [] wordlist = {"CIDER","QUITS","ZEBRA","GNOME","WHISK",
            "LEVER","EXTRA","JOKER","FIRST","PRAYS"};


    public String get_word() {
        int ran = new Random().nextInt(NUM_WORDS);
        return wordlist[ran];
    }
}
