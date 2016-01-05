package animeinfo;

import java.util.Map;
import java.util.TreeMap;

public class AnimeInfoManager {
    private static Map<String, AnimeInfo> titleToAnimeInfoMap;
    private static boolean changed;

    // static initializer
    static {
        titleToAnimeInfoMap = new TreeMap<>();
        changed = false;
    }

    /**
     * Do not allow objects of this class to be made.
     */
    private AnimeInfoManager() {
    }

    /**
     * Attempts to setup the anime info map.
     * @param fileLocation the location of the Theme file
     */
    public static void setup(String fileLocation) {
        titleToAnimeInfoMap = AnimeInfoReader.load(fileLocation);
    }

    /**
     * Gets an AnimeInfo object.
     * @param title the title
     * @return the AnimeInfo object
     */
    public static AnimeInfo getAnimeInfo(String title) {
        AnimeInfo animeInfo = titleToAnimeInfoMap.get(title);
        // see if title was found, or if the found AnimeInfo is missing fields
        if(animeInfo == null || animeInfo.isMissingFields()) {
            animeInfo = new AnimeInfo(title);
            titleToAnimeInfoMap.put(animeInfo.getTitle(), animeInfo);
            changed = true;
        }

        return animeInfo;
    }

    /**
     * Adds an AnimeInfo object to the manager.
     * @param animeInfo the AnimeInfo object
     */
    public static void add(AnimeInfo animeInfo) {
        titleToAnimeInfoMap.put(animeInfo.getTitle(), animeInfo);
        changed = true;
    }

    /**
     * Saves the AnimeInfo to a given file location.
     * @param fileLocation  the file location
     */
    public static void save(String fileLocation) {
        if(changed) {
            AnimeInfoWriter.save(fileLocation, titleToAnimeInfoMap);
            changed = false;
        }
    }
}
