package systems.petrichor.yttm.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Playlist {

    private String urlString;

    public Playlist(String urlString) {
        this.urlString = urlString;

    }
    
    public String[] getUrls() throws IOException, InterruptedException {

        String[] commandStringArray = {
            ".\\lib\\yt-dlp.exe",
            "--flat-playlist",
            "--get-url",
            ("\"" + this.urlString + "\""),
        };

        Process process = Runtime.getRuntime().exec(commandStringArray);

        InputStream infoStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(infoStream));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        
        return commandStringArray;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("BEEP");

        Playlist playlist = new Playlist("https://music.youtube.com/playlist?list=PLLtfsNRMIOUd44d4IT870Qef71f3rckP9&si=-fR0Y-CPvTLuRh0M");
        playlist.getUrls();

        System.out.println("BEEP");
    }

}
