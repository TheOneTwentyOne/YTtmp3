package systems.petrichor.yttm.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Playlist {

    private String urlString;

    public Playlist(String urlString) {
        this.urlString = urlString;

    }
    
    public ArrayList<String> getUrls() throws IOException, InterruptedException {

        String[] commandStringArray = {
            ".\\lib\\yt-dlp.exe",
            "--flat-playlist",
            "--get-url",
            ("\"" + this.urlString + "\""),
        };

        Process process = Runtime.getRuntime().exec(commandStringArray);

        InputStream infoStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(infoStream));

        ArrayList<String> urlArrayList = new ArrayList<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            urlArrayList.add(line.strip());
        }
        
        return (urlArrayList);
    }

}
