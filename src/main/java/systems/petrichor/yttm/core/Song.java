package systems.petrichor.yttm.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Song{

    private Description description;
    private File directoryFile;

    private String urlString = null;
    private String unformattedDirectoryString = null;
    private String formattedDirectoryString = null;
    private String mp3FilenameString = null;
    private String mp3FilenameDirectoryString = null;
    private String descriptionString = null;
    private String imgFilenameString = null;
    private String imgFilenameDirectoryString = null;

    public boolean failed = false;
    














    /**
     * Constructor for a song object which represents a song auto-generated by Youtube.
     * Contains all data needed for initial download and will be processed and formatted
     * later on.
     * 
     * @param url URL of the video to be downloaded.
     * @param directory Directory where files end up.
     */
    public Song(String url, String directory) {

        this.urlString = url;

        this.directoryFile = new File(directory);
        this.unformattedDirectoryString = this.directoryFile.getAbsolutePath();

        if (!this.unformattedDirectoryString.endsWith("\\")) {
            this.formattedDirectoryString = this.unformattedDirectoryString + "\\";
        } else {
            this.formattedDirectoryString = this.unformattedDirectoryString;
        }

        try {
            Scanner scanner;

            for (String fileString : this.directoryFile.list()) {
                if (fileString.toLowerCase().endsWith(".description")) {
                    File descriptionFile = new File((this.formattedDirectoryString + fileString)); // Make sure to specify the directory
                    
                    scanner = new Scanner(descriptionFile);
                    ArrayList<String> linesArrayList = new ArrayList<>(); // Initialize the ArrayList
                    while (scanner.hasNextLine()) {
                        linesArrayList.add(scanner.nextLine()); // Read and add each line to the list
                    }
                    this.descriptionString = String.join("\n", linesArrayList); // Join lines into a single string
                }
            }

            this.description = new Description(this.descriptionString);

        } catch (IllegalArgumentException e) {
            System.err.println("Song is not of the proper format (description does not start with \"Provided to YouTube by ...\"). Song must be autogenerated by YouTube.");
            failed=true;

        } catch (FileNotFoundException e) {
            System.err.println("Song description file could not be found.");
            failed=true;

        }

    }












    /**
     * Instance method that triggers the song object to begin its download.
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public int startDownload() throws IOException, InterruptedException {

        String outputTemplate = this.formattedDirectoryString + "%(title)s.%(ext)s";
        String[] commandDownloadStringArray = {
            ".\\lib\\yt-dlp.exe",
            "--no-playlist",
            "--write-description",
            "--limit-rate", "100G",
            "--windows-filenames",
            "--ffmpeg-location", "lib\\",
            "--write-thumbnail",
            "--convert-thumbnails", "jpg",
            "--extract-audio",
            "--audio-format", "mp3",
            "--audio-quality", "0",
            "-o", outputTemplate,
            ("\"" + this.urlString + "\""),
        };

        Process downloadProcess = Runtime.getRuntime().exec(commandDownloadStringArray);

        InputStream downloadErrorStream = downloadProcess.getErrorStream();
        BufferedReader downloadBufferedReader = new BufferedReader(new InputStreamReader(downloadErrorStream));
        
        String downloadLineString;
        while ((downloadLineString = downloadBufferedReader.readLine()) != null) {
            System.err.println(downloadLineString);
        }

        int exitCode = downloadProcess.waitFor();
        return exitCode;

    }











    

    public int applyMetadata() throws IOException, InterruptedException, IllegalArgumentException {

        for (String fileString : this.directoryFile.list()) {
            if (fileString.toLowerCase().endsWith(".mp3")) {
                this.mp3FilenameString = fileString;
                this.mp3FilenameDirectoryString = this.formattedDirectoryString + this.mp3FilenameString;
            }
        }

        if (this.mp3FilenameString == null) {
            throw new IllegalArgumentException(".mp3 not found.");
        }

        new File(this.formattedDirectoryString + "\\finishedFile").mkdir();

        String outputFileNameString = this.formattedDirectoryString + "finishedFile\\" + this.mp3FilenameString;
        System.out.println(outputFileNameString);

        System.out.println(outputFileNameString);
        System.out.println(outputFileNameString);
        System.out.println(outputFileNameString);
        System.out.println(this.formattedDirectoryString);
        System.out.println(outputFileNameString);

        String[] commandApplyMetadataStringArray = {
            ".\\lib\\ffmpeg.exe",
            "-nostdin",
            "-i",
            "\"" + this.mp3FilenameDirectoryString + "\"",
            "-metadata", ("title=" + description.getTitleString()),
            "-metadata", ("artist=" + description.getAllArtistsString()),
            "-metadata", ("album_artist=" + description.getMainArtistString()),
            "-metadata", ("album=" + description.getAlbumString()),
            "-metadata", ("date=" + description.getYearString()),
            "-id3v2_version", "3",
            "-write_id3v1", "1",
            "-c", "copy",
            "\"" + outputFileNameString + "\"",
            "-y",
        };
        Process metadataProcess = Runtime.getRuntime().exec(commandApplyMetadataStringArray);

        InputStream metadataErrorStream = metadataProcess.getErrorStream();
        BufferedReader metadataBufferedReader = new BufferedReader(new InputStreamReader(metadataErrorStream));
        
        String line;
        while ((line = metadataBufferedReader.readLine()) != null) {
            System.err.println(line);
        }

        int exitCode = metadataProcess.waitFor();
        return exitCode;

    }

















    public int cropCoverArt() throws IOException, InterruptedException {

        for (String fileString : this.directoryFile.list()) {
            if (fileString.toLowerCase().endsWith(".jpg")) {
                this.imgFilenameString = fileString;
                this.imgFilenameDirectoryString = this.formattedDirectoryString + this.imgFilenameString;
            }
        }

        String[] cArtStringArray = {
            ".\\lib\\ffmpeg.exe",
            "-nostdin",
            "-i",
            "\"" + this.imgFilenameDirectoryString + "\"",
            "-vf",
            "crop='min(iw,ih):min(iw,ih)'",
            "\"" + this.imgFilenameDirectoryString + "\"",
            
        };

        Process imgProcess = Runtime.getRuntime().exec(cArtStringArray);

        InputStream imgErrorStream = imgProcess.getErrorStream();
        BufferedReader imgBufferedReader = new BufferedReader(new InputStreamReader(imgErrorStream));
        
        String line;
        while ((line = imgBufferedReader.readLine()) != null) {
            System.err.println(line);
        }

        int exitCode = imgProcess.waitFor();
        return exitCode;

    }

















    public int applyCoverArt() throws IOException, InterruptedException {

        for (String fileString : this.directoryFile.list()) {
            if (fileString.toLowerCase().endsWith(".jpg")) {
                this.imgFilenameString = fileString;
                this.imgFilenameDirectoryString = this.formattedDirectoryString + this.imgFilenameString;
            }
        }

        String[] cArtStringArray = {
            ".\\lib\\ffmpeg.exe",
            "-nostdin",
            "-i",
            "\"" + this.imgFilenameDirectoryString + "\"",
            "-vf",
            "crop='min(iw,ih):min(iw,ih)'",
            "\"" + this.imgFilenameDirectoryString + "\"",
        };

        Process imgProcess = Runtime.getRuntime().exec(cArtStringArray);

        InputStream imgErrorStream = imgProcess.getErrorStream();
        BufferedReader imgBufferedReader = new BufferedReader(new InputStreamReader(imgErrorStream));
        
        String line;
        while ((line = imgBufferedReader.readLine()) != null) {
            System.err.println(line);
        }

        int exitCode = imgProcess.waitFor();
        return exitCode;

    }
















    public static String sanitizeFilename(String input) {
        StringBuilder sanitized = new StringBuilder();
        
        for (char c : input.toCharArray()) {
            switch (c) {
                case '\\':
                    sanitized.append('⧵');
                    break;
                case '/':
                    sanitized.append('⧸');
                    break;
                case ':':
                    sanitized.append('꞉');
                    break;
                case '*':
                    sanitized.append('＊');
                    break;
                case '?':
                    sanitized.append('？');
                    break;
                case '"':
                    sanitized.append('ʺ');
                    break;
                case '<':
                    sanitized.append('‹');
                    break;
                case '>':
                    sanitized.append('›');
                    break;
                case '|':
                    sanitized.append('¦');
                    break;
                default:
                    sanitized.append(c);
            }
        }
        
        return sanitized.toString();
    }


















    public String getUrlString() {
        return urlString;
    }
}
