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

        String chosenPathString = "C:\\Users\\ephem\\Desktop\\SONGSTESTER\\";
        
        final int noOfThreads = 32;  

        ArrayList<String> urls = new ArrayList<>();

        ArrayList<String> songUrlArrayList = new Playlist("https://music.youtube.com/playlist?list=PLLtfsNRMIOUd44d4IT870Qef71f3rckP9&si=-fR0Y-CPvTLuRh0M").getUrls();
        urls.addAll(songUrlArrayList);

        Queue<String> urlQueue = new ConcurrentLinkedQueue<>(urls);
        
        ExecutorService pool = Executors.newFixedThreadPool(noOfThreads);

        Path[] threadFolders = new Path[noOfThreads];
        for (int i = 0; i < noOfThreads; i++) {
            threadFolders[i] = Paths.get(chosenPathString + "ACTIVE_WORKER_" + (i + 1));
            if (Files.notExists(threadFolders[i])) {
                Files.createDirectories(threadFolders[i]);
            }
        }

        for (int i = 0; i < noOfThreads; i++) {
            final Path assignedFolder = threadFolders[i];  
            pool.execute(() -> {
                while (!urlQueue.isEmpty()) {
                    String url = urlQueue.poll();
                    if (url != null) {
                        try {
                            Song song = new Song(url, assignedFolder.toAbsolutePath().toString());
                            song.run();  
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }



        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        for (Path pathFromArray : threadFolders) {
            System.out.println(pathFromArray.toAbsolutePath().toString());
            Files.deleteIfExists(pathFromArray.toAbsolutePath());
        }
    }

}