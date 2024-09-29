package systems.petrichor.yttm.core;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Yttm {

    public static void main(String[] args) throws Exception {
        final int noOfThreads = 32;

        // Make an array list of URLs
        ArrayList<String> urls = new ArrayList<>();

        // Adding some URLs to the queue (you can add as many as needed)
        urls.add("https://music.youtube.com/watch?v=sqYbcSJ-fQI&si=xxaPYrX07oRCAgxc");
        urls.add("https://music.youtube.com/watch?v=sqYbcSJ-fQI&si=xxaPYrX07oRCAgxc");
        urls.add("https://music.youtube.com/watch?v=sqYbcSJ-fQI&si=xxaPYrX07oRCAgxc");

        ArrayList<String> songUrlArrayList = new Playlist("https://music.youtube.com/playlist?list=PLLtfsNRMIOUd44d4IT870Qef71f3rckP9&si=-fR0Y-CPvTLuRh0M").getUrls();
        for (String SongUrlString : songUrlArrayList) {
            urls.add(SongUrlString);
        }

        // Create an array list of songs
        ArrayList<Song> songs = IntStream.range(0, urls.size())
                .mapToObj(i -> new Song(urls.get(i), "subdir_" + i + "\\"))
                .collect(Collectors.toCollection(ArrayList::new));

        // Set the number of platform threads available for scheduling virtual threads
        // System.setProperty("jdk.virtualThreadScheduler.parallelism", "4"); // Keep this the default


        if (noOfThreads > 0) {
            // Set the number of platform threads available for scheduling virtual threads
            System.setProperty("jdk.virtualThreadScheduler.parallelism", Integer.toString(noOfThreads));
        } // else keep the default value (Very big number)

        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
            // Submit tasks to the pool
            songs.forEach(pool::execute);

            pool.shutdown();  // Shutdown the pool after tasks are submitted
        }
    }
}
