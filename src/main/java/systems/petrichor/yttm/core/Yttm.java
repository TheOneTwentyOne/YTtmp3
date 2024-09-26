package src.main.java.systems.petrichor.yttm.core;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Yttm {

    public static void main(String[] args) throws Exception {

        Song test = new Song("https://music.youtube.com/watch?v=sqYbcSJ-fQI&si=xxaPYrX07oRCAgxc", "test\\");

        ArrayList<Song> downlode = new ArrayList<>();
        downlode.add(test);
        final int noOfThreads = 16;
        try (ExecutorService pool = Executors.newFixedThreadPool(noOfThreads)) {
            downlode.forEach(pool::execute);
        }
    }
}
