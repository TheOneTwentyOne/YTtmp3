package systems.petrichor.yttm.core;

public class Yttm {

    public static void main(String[] args) throws Exception{

        Song test = new Song("https://music.youtube.com/watch?v=sqYbcSJ-fQI&si=xxaPYrX07oRCAgxc", "test\\");
        test.startDownload();
        test.applyMetadata();

    }
}
