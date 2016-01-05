package season;

/**
 * Stores info about a season.
 */
public class Season {
    public static final String[] SEASONS = {"Winter", "Spring", "Summer", "Fall"};
    public final String SEASON, TERM;
    public final int YEAR, MONTH, DAY;

    /**
     * Makes a Season object.
     *
     * @param season the season in TERM YEAR or YEAR-MONTH-DAY format
     *               on format failure the SEASON and TERM fields will be "?"
     *               and the YEAR and SEASON_VALUE will be -1
     */
    public Season(String season) {
        if(isTermYearSeason(season)) {
            String space = " ";
            int spaceIndex = season.indexOf(space);
            TERM = season.substring(0, spaceIndex);
            YEAR = Integer.parseInt(season.substring(spaceIndex + space.length()));

            SEASON = season;

            MONTH = getFirstMonthForTerm(TERM);
            DAY = 1;
        }
        else if(isYearMonthDaySeason(season)) {
            String[] dateArr = season.split("-");

            DAY = Integer.parseInt(dateArr[2]);

            MONTH = Integer.parseInt(dateArr[1]);

            TERM = getTerm(MONTH);
            YEAR = Integer.parseInt(dateArr[0]);

            SEASON = TERM + " " + YEAR;
        }
        else {
            SEASON = "?";

            DAY = -1;

            MONTH = -1;

            TERM = "?";
            YEAR = -1;
        }
    }

    /**
     * Checks if a str matches the pattern for a season.
     *
     * @param season the season in TERM YEAR format.
     * @return true if season matches the above format
     */
    private boolean isTermYearSeason(String season) {
        if(season == null) {
            return false;
        }

        // match any case type
        season = season.toLowerCase();

        for(String temp : SEASONS) {
            // see if the season has "term #"
            if(season.matches("^" + temp.toLowerCase() + " [0-9]+$")) {
                return true;
            }
        }

        return false;
    }

    private boolean isYearMonthDaySeason(String season) {
        if(season == null) {
            return false;
        }

        return season.matches("^[0-9]+\\-0[1-9]\\-[0-9]{2}$") ||
                season.matches("^[0-9]+\\-1[0-2]\\-[0-9]{2}$");
    }

    /**
     * Gets the term a month is in.
     *
     * @param month the month
     * @return the term
     */
    private String getTerm(int month) {
        if(month >= 1 && month <= 3) {
            return SEASONS[0];
        }
        else if(month >= 4 && month <= 6) {
            return SEASONS[1];
        }
        else if(month >= 7 && month <= 9) {
            return SEASONS[2];
        }
        else {
            return SEASONS[3];
        }
    }

    /**
     * Gets the seasons value for sorting purposes.
     *
     * @param term the term
     * @return the season's value
     */
    private int getFirstMonthForTerm(String term) {
        if(term == null) {
            return -1;
        }

        int[] firstMonthForTermArr = {1, 4, 7, 10};

        // match any case type
        term = term.toLowerCase();

        for(int i = 0; i < SEASONS.length; i++) {
            if(term.equals(SEASONS[i].toLowerCase())) {
                return firstMonthForTermArr[i];
            }
        }

        // could not find a match
        return -1;
    }

    public int hashCode() {
        final int prime = 31;

        return prime * YEAR * MONTH * DAY;
    }

    public boolean equals(Object object) {
        if(object == null) {
            return false;
        }
        else if(!(object instanceof Season)) {
            return false;
        }

        Season season = (Season) object;

        return YEAR == season.YEAR && MONTH == season.MONTH && DAY == season.DAY;
    }
}