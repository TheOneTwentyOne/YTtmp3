package systems.petrichor.yttm.core;



public class Description {

    private final String mainArtistString;
    private final String allArtistsString;
    private final String titleString;
    private final String yearString;
    private final String albumString;


    public Description(String descriptionString) {

        if (descriptionString == null) {
            throw new IllegalArgumentException("Description cannot be equal to 'null'");
        } 
        
        if (!descriptionString.startsWith("Provided to YouTube")) {
            throw new IllegalArgumentException("Description does not match required format. Offending URL has been tracked.");
        } 


        String[] descriptionStringArray = descriptionString.split("\n");
        String[] split = descriptionStringArray[2].split(" · ");

        this.mainArtistString = split[1].strip();
        this.allArtistsString = extractAllArtists(descriptionStringArray);
        this.titleString = split[0].strip();
        this.yearString = extractYear(descriptionStringArray);
        this.albumString = descriptionStringArray[4].strip();
    }


    private String extractAllArtists(String[] descriptionStringArray) {
        String infoString = descriptionStringArray[2];
        String[] infoStringArray = infoString.split(" · ");


        StringBuilder concatenatedArtistsString = new StringBuilder();
        for (int i = 1; i < infoStringArray.length; i++) {
            if (concatenatedArtistsString.indexOf(infoStringArray[i].strip()) == -1) {
                concatenatedArtistsString.append(infoStringArray[i].strip());
            }
            if (i != infoStringArray.length - 1) {
                concatenatedArtistsString.append("‎,‎‎ ");
            }
        }

        return concatenatedArtistsString.toString();
    }


    private String extractYear(String[] descriptionStringArray) {
        for (String line : descriptionStringArray) {
            if (line.startsWith("Released on: ")) {
                return line.replaceAll("Released on: ", "").substring(0, 4);
            }
        }

        return null;
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
