package systems.petrichor.yttm.core;

public class Description {

    private String[] descriptionStringArray;
    private String mainArtistString;
    private String allArtistsString;
    private String titleString;
    private String yearString;
    private String albumString;






    public Description(String descriptionString) {
        if (!descriptionString.startsWith("Provided to YouTube")) {
            throw new IllegalArgumentException("Description does not match required format. Offending URL has been tracked.");
        } 

        this.descriptionStringArray = descriptionString.split("\n");

        this.mainArtistString = extractMainArtist();
        this.allArtistsString = extractAllArtists();
        this.titleString = extractTitle();
        this.yearString = extractYear();
        this.albumString = extractAlbum();
    }






    private String extractMainArtist() {
        String infoString = this.descriptionStringArray[2];
        return infoString.split(" · ")[1].strip();
    }

    private String extractAllArtists() {
        String infoString = this.descriptionStringArray[2];
        String[] infoStringArray = infoString.split(" · ");
        

        String concatenatedArtistsString = "";
        for (int i = 1; i < infoStringArray.length; i++) {
            if (concatenatedArtistsString.indexOf(infoStringArray[i].strip())==-1) {
                concatenatedArtistsString = concatenatedArtistsString.concat(infoStringArray[i].strip());
            }
            if (i!=infoStringArray.length-1) {
                concatenatedArtistsString = concatenatedArtistsString.concat("‎,‎‎ ");
            }
        }

        return concatenatedArtistsString;
    }

    private String extractTitle() {
        String infoString = this.descriptionStringArray[2];
        return infoString.split(" · ")[0].strip();
    }

    private String extractYear() {
        for (String line : descriptionStringArray) {
            if (line.startsWith("Released on: ")) {
                return line.replaceAll("Released on: ", "").substring(0, 4);
            }
        }

        return null;
    }

    private String extractAlbum() {
        String infoString = this.descriptionStringArray[4];
        return infoString.strip();
    }






    public String getMainArtistString() {
        return mainArtistString;
    }

    public String getAllArtistsString() {
        return allArtistsString;
    }

    public String getTitleString() {
        return titleString;
    }

    public String getYearString() {
        return yearString;
    }

    public String getAlbumString() {
        return albumString;
    }

}
