package season;

import java.util.Comparator;

/**
 * String comparator for sorting seasons.
 */
public class SeasonComparator implements Comparator<String> {
    public int compare(String str1, String str2) {
        Season season1 = new Season(str1);
        Season season2 = new Season(str2);

        if(season1.YEAR > season2.YEAR) {
            return 1;
        }
        else if(season1.YEAR < season2.YEAR) {
            return -1;
        }
        else if(season1.MONTH > season2.MONTH) {
            return 1;
        }
        else if(season1.MONTH < season2.MONTH) {
            return -1;
        }
        else if(season1.DAY > season2.DAY) {
            return 1;
        }
        else if(season1.DAY < season2.DAY) {
            return -1;
        }
        else {
            return 0;
        }
    }
}