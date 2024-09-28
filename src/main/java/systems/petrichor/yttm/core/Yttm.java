package systems.petrichor.yttm.core;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class Yttm {






        static class SongWorker implements Runnable {
        private BlockingQueue<String> urlQueue;  // Queue of URLs
        private String path;                     // Unique path for this thread

        public SongWorker(BlockingQueue<String> urlQueue, String path) {
            this.urlQueue = urlQueue;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                // Continuously process URLs from the queue
                while (true) {
                    String url = urlQueue.take();  // Take a URL from the queue (blocking call)
                    if (url.equals("POISON_PILL")) {
                        break;  // Stop thread when poison pill is received
                    }
                    // Create and process Song object
                    Song song = new Song(url, path);
                    song.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restore interrupted state
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final int noOfThreads = 32;
        BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();

        // Adding some URLs to the queue (you can add as many as needed)
        urlQueue.add("https://music.youtube.com/watch?v=sqYbcSJ-fQI&si=xxaPYrX07oRCAgxc");
        urlQueue.add("https://music.youtube.com/watch?v=example2");
        urlQueue.add("https://music.youtube.com/watch?v=example3");
        // Add more URLs as needed...

        // Poison pills to stop the threads when work is done
        for (int i = 0; i < noOfThreads; i++) {
            urlQueue.add("POISON_PILL");
        }

        // Initialize a thread pool
        ExecutorService pool = Executors.newFixedThreadPool(noOfThreads);

        // Assign each thread a unique path (subdirectory)
        for (int i = 0; i < noOfThreads; i++) {
            String path = "subdir_" + i + "\\";  // Unique path for each thread
            pool.execute(new SongWorker(urlQueue, path));
        }

        pool.shutdown();  // Shutdown the pool after tasks are submitted
    }




    //public static void main(String[] args) throws Exception {

    //    Song test = new Song("https://music.youtube.com/watch?v=sqYbcSJ-fQI&si=xxaPYrX07oRCAgxc", "test\\");

    //    ArrayList<Song> downlode = new ArrayList<>();
    //    downlode.add(test);
    //    final int noOfThreads = 16;
    //    try (ExecutorService pool = Executors.newFixedThreadPool(noOfThreads)) {
    //        downlode.forEach(pool::execute);
    //    }
    //}
}
