package systems.petrichor.yttm.core;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.nio.file.*;

public class Yttm {

    public static void main(String[] args) throws Exception {
        final int noOfThreads = 1;  
        ArrayList<String> urls = new ArrayList<>();

        ArrayList<String> songUrlArrayList = new Playlist("https://music.youtube.com/playlist?list=PLLtfsNRMIOUd44d4IT870Qef71f3rckP9&si=-fR0Y-CPvTLuRh0M").getUrls();
        urls.addAll(songUrlArrayList);

        Queue<String> urlQueue = new ConcurrentLinkedQueue<>(urls);
        
        ExecutorService pool = Executors.newFixedThreadPool(noOfThreads);

        Path[] threadFolders = new Path[noOfThreads];
        for (int i = 0; i < noOfThreads; i++) {
            threadFolders[i] = Paths.get("ACTIVE_WORKER_" + (i + 1));
            if (Files.notExists(threadFolders[i])) {
                Files.createDirectories(threadFolders[i]);
            }
        }

        for (int i = 0; i < noOfThreads; i++) {
            final Path assignedFolder = threadFolders[i];  // Each thread gets one folder
            pool.execute(() -> {
                while (!urlQueue.isEmpty()) {
                    String url = urlQueue.poll();
                    if (url != null) {
                        try {
                            // Create and run the Song object, using the same folder for each thread
                            Song song = new Song(url, assignedFolder.toAbsolutePath().toString());
                            song.run();  // Assuming `Song` has its run logic properly implemented
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        // Shutdown the pool after all tasks are finished
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

}




/* 
    public static void main(String[] args) throws Exception {
        final int noOfThreads = 4;  // Set the number of threads
        ArrayList<String> urls = new ArrayList<>();

        // Populate URLs (simulating a playlist fetch)
        ArrayList<String> songUrlArrayList = new Playlist("https://music.youtube.com/playlist?list=PLLtfsNRMIOUd44d4IT870Qef71f3rckP9&si=-fR0Y-CPvTLuRh0M").getUrls();
        urls.addAll(songUrlArrayList);

        // Shared queue for URLs
        Queue<String> urlQueue = new ConcurrentLinkedQueue<>(urls);
        
        // Atomic counter for assigning folders dynamically
        AtomicInteger folderCounter = new AtomicInteger(1);

        // Executor service with fixed number of threads
        ExecutorService pool = Executors.newFixedThreadPool(noOfThreads);

        // Define a task for each thread
        for (int i = 0; i < noOfThreads; i++) {
            pool.execute(() -> {
                while (!urlQueue.isEmpty()) {
                    String url = urlQueue.poll();
                    if (url != null) {
                        // Assign a folder based on the thread
                        int folderNumber = folderCounter.getAndIncrement();
                        Path argPath = Paths.get("ACTIVE_WORKER_" + folderNumber);
                        try {
                            // Ensure the directory is created
                            if (Files.notExists(argPath)) {
                                Files.createDirectories(argPath);
                            }
                            // Purge the folder (if needed)
                            purgeDirectory(argPath);

                            // Create and run the Song object
                            Song song = new Song(url, argPath.toAbsolutePath().toString());
                            song.run();  // Assuming `Song` has its run logic properly implemented
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        // Shutdown the pool after all tasks are finished
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
*/






/*
    public static void main(String[] args) throws Exception {
        final int noOfThreads = 1;

        // Make an array list of URLs
        ArrayList<String> urls = new ArrayList<>();

        ArrayList<String> songUrlArrayList = new Playlist("https://music.youtube.com/playlist?list=PLLtfsNRMIOUd44d4IT870Qef71f3rckP9&si=-fR0Y-CPvTLuRh0M").getUrls();
        for (String SongUrlString : songUrlArrayList) {
            urls.add(SongUrlString);
        }


        ArrayList<Song> songArrayList = new ArrayList<>();
        Path argPath;
        int offsetInt = 0;
        for (int i = 0; i < urls.size(); i++) {
            argPath = Paths.get("ACTIVE_WORKER_" + (i + offsetInt) + "\\");
            if (Files.exists(argPath)) {
                offsetInt++;
                argPath = Paths.get("ACTIVE_WORKER_" + (i + offsetInt) + "\\");
            }
            Files.createDirectories(argPath);
            Song argSong = new Song(urls.get(i), argPath.toAbsolutePath().toString());
            songArrayList.add(argSong);
        }

        //ArrayList<Song> songs = IntStream.range(0, urls.size())
        //        .mapToObj(i -> new Song(urls.get(i), "subdir_" + i + "\\"))
        //        .collect(Collectors.toCollection(ArrayList::new));

        // Set the number of platform threads available for scheduling virtual threads
        // System.setProperty("jdk.virtualThreadScheduler.parallelism", "4"); // Keep this the default


        //if (noOfThreads > 0) {
        //    // Set the number of platform threads available for scheduling virtual threads
        //    System.setProperty("jdk.virtualThreadScheduler.parallelism", Integer.toString(noOfThreads));
        //} // else keep the default value (Very big number)

        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
            // Submit tasks to the pool
            songArrayList.forEach(pool::execute);

            pool.shutdown();  // Shutdown the pool after tasks are submitted
        }
    }
*/