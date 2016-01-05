package animeinfo;

import season.Season;
import tool.DataTool;
import tool.HTMLTool;
import tool.LinkTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * General purpose class to get synopsis and tags from www.animenewsnetwork.com
 */
public class AnimeInfo {
    public static final String SEPARATOR = System.getProperty("line.separator");
    public static final String PIPE = " | ";
    public static final Comparator<String> LONGEST_FIRST_COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String str1, String str2) {
            return str2.length() - str1.length();
        }
    };
    private String title, titles, category, link, dates, tags, synopsis;
    private String page;

    /**
     * Default constructor.
     */
    public AnimeInfo() {
        title = "";
        titles = "";
        category = "";
        link = "";
        dates = "";
        tags = "";
        synopsis = "";
    }

    /**
     * Creates a new AnimeInfo object.
     *
     * @param title the title of the anime
     */
    public AnimeInfo(String title) {
        this.title = title;

        link = getANNLink();
        page = LinkTool.loadAsString(link);

        List<String> titleList = new ArrayList<>();

        if(page != null) {
            String heading = getHeading();

            // get titles
            String mainTitle = getMainTitle(heading);
            if(!mainTitle.equals("")) { // don't add empty string as a title
                titleList.add(mainTitle);
            }
            titleList.addAll(getMisc("Alternative title"));
            titles = DataTool.getListAsString(titleList, PIPE);

            category = getType(heading);

            dates = DataTool.getListAsString(getMisc("Vintage"), PIPE);

            tags = getTagsFromPage();

            // get synopsis
            synopsis = DataTool.getListAsString(getMisc("Plot Summary"));
        }
        else {
            titles = "";
            category = "";
            dates = "";
            tags = "";
            synopsis = "";
        }
    }

    /**
     * Checks if the AnimeInfo is missing any fields.
     *
     * @return true if it is missing any fields
     */
    public boolean isMissingFields() {
        return titles.equals("") || category.equals("") || link.equals("") || dates.equals("") ||
                tags.equals("") || synopsis.equals("");
    }

    /**
     * Gets the link.
     *
     * @return the link
     */
    private String getANNLink() {
        String searchTerm = title + " anime news network";
        String siteLinkStart = "http://www.animenewsnetwork.com/encyclopedia/anime.php";

        // try google
        String googleLink = LinkTool.getGoogleSearchResultLink(searchTerm, siteLinkStart);
        if(googleLink != null) {
            return googleLink;
        }

        // try bing
        String bingLink = LinkTool.getBingSearchResultLink(searchTerm, siteLinkStart);
        if(bingLink != null) {
            return bingLink;
        }

        return "";
    }

    /**
     * Gets the tags.
     */
    private String getTagsFromPage() {
        List<String> genreList = getTagList("Genres");
        List<String> themeList = getTagList("Themes");

        genreList.addAll(themeList);

        return DataTool.getListAsString(genreList, ", ");
    }

    /**
     * Gets the tag.
     *
     * @param type the type of tag to get
     *             Can either be "Genres" or "Themes"
     * @return the tags
     */
    private List<String> getTagList(String type) {
        List<String> tagList = new ArrayList<>();

        int tagsIndex = page.indexOf("<strong>" + type + ":</strong>");
        if(tagsIndex == -1) {
            return tagList;
        }

        String span = "<span>";
        int tagsStartIndex = page.indexOf(span, tagsIndex);
        if(tagsStartIndex == -1) {
            return tagList;
        }

        int tagsEndIndex = page.indexOf("</div>", tagsStartIndex);
        if(tagsEndIndex == -1) {
            return tagList;
        }

        String tagContent = page.substring(tagsStartIndex + span.length(), tagsEndIndex);

        for(String tag : tagContent.split(SEPARATOR)) {
            // end of tag
            int aSpanIndex = tag.lastIndexOf("</a></span>");
            if(aSpanIndex != -1) {
                // start of tag
                String closeTag = ">";
                int tagIndex = tag.lastIndexOf(closeTag, aSpanIndex);
                if(tagIndex != -1) {
                    tagList.add(tag.substring(tagIndex + closeTag.length(), aSpanIndex));
                }
            }
        }

        return tagList;
    }

    /**
     * Gets the first date a show aired.
     *
     * @return the date
     */
    public String getDate() {
        List<String> dateList = DataTool.getStringAsList(dates, PIPE);

        // return "?" as the season if we are given an empty dateList
        if(dateList.size() == 0) {
            return new Season("").SEASON;
        }

        String date = dateList.get(0);

        String space = " ";
        int spaceIndex = date.indexOf(space);
        if(spaceIndex != -1) {
            date = date.substring(0, spaceIndex);
        }

        return date;
    }

    /**
     * Gets the synopsis table with a continue reading tag after the first paragraph.
     *
     * @param tableClass         the table class
     * @param continueReadingTag the continue reading tag
     * @return the synopsis table
     */
    public String getSynopsisTable(String tableClass, String continueReadingTag) {
        String synopsis = this.synopsis;
        if(!synopsis.contains(SEPARATOR)) {
            synopsis = synopsis.replace("\n", SEPARATOR); // XML always uses a \n instead of the OS's line separator
        }

        // set up continue reading tag
        if(!synopsis.equals("")) {
            if(synopsis.indexOf(SEPARATOR) == -1) { // single paragraph synopsis
                synopsis += continueReadingTag;
            }
            else { // multi paragraph synopsis
                synopsis = synopsis.replaceFirst(SEPARATOR, continueReadingTag + SEPARATOR);
                // replace single separators with two separators
                if(!synopsis.contains(SEPARATOR + SEPARATOR)) {
                    synopsis = synopsis.replace(SEPARATOR, SEPARATOR + SEPARATOR);
                }
            }
        }
        else { // no synopsis
            synopsis = continueReadingTag;
        }

        // build table
        List<String> element = new ArrayList<>();
        element.add(synopsis);

        List<List<String>> elements = new ArrayList<>();
        elements.add(element);

        return HTMLTool.getTable(tableClass, "Synopsis for: " + getGoodTitles() + " (" + getSeason() + ")", null, elements);
    }

    /**
     * Gets the good titles in the titles.
     *
     * @return the good titles
     */
    public String getGoodTitles() {
        String goodTitles = title.replace(": ", " - ");

        String cleanedTitles = titles.replace(": ", " - ");
        List<String> titleList = DataTool.getStringAsList(cleanedTitles, PIPE);
        Collections.sort(titleList, LONGEST_FIRST_COMPARATOR);

        for(String temp : titleList) {
            String parenEnd = ")";
            int parenEndIndex = temp.lastIndexOf(parenEnd);

            String spaceAndParenStart = " (";
            int spaceAndParenStartIndex = temp.lastIndexOf(spaceAndParenStart, parenEndIndex);

            // title does not have a language associated with it and is in English
            if(parenEndIndex == -1 && spaceAndParenStartIndex == -1 &&
                    temp.matches(".*\\w.*")) {
                goodTitles = DataTool.addNonDuplicateIgnoresCase(goodTitles, temp, PIPE);
            }
            else if(parenEndIndex != -1 && spaceAndParenStartIndex != -1) { // title has a language associated with it
                String language = temp.substring(spaceAndParenStartIndex + spaceAndParenStart.length(), parenEndIndex);
                String tempTitle = temp.substring(0, spaceAndParenStartIndex);

                // do not allow Japanese characters in filename
                if(language.equals("English") || (language.equals("Japanese") &&
                        !tempTitle.matches(".*[\\p{IsHiragana}\\p{IsKatakana}\\p{IsHan}].+"))) {
                    goodTitles = DataTool.addNonDuplicateIgnoresCase(goodTitles, tempTitle, PIPE);
                }
            }
        }

        return goodTitles;
    }

    public String getSeason() {
        return new Season(getDate()).SEASON;
    }

    /**
     * Gets the type of medium an anime is.
     *
     * @param heading the heading
     * @return the type
     */
    private String getType(String heading) {
        String parenEnd = ")";
        int parenEndIndex = heading.lastIndexOf(parenEnd);
        if(parenEndIndex == -1) {
            return "";
        }

        String parenStart = "(";
        int parenStartIndex = heading.lastIndexOf(parenStart, parenEndIndex);
        if(parenStartIndex == -1) {
            return "";
        }

        return heading.substring(parenStartIndex + parenStart.length(), parenEndIndex);
    }

    /**
     * Gets the alternative titles of an anime.
     *
     * @param heading the heading
     * @return the alternative titles
     */
    private String getMainTitle(String heading) {
        String spaceAndParenStart = " (";
        int spacenAndParenStartIndex = heading.lastIndexOf(spaceAndParenStart);
        if(spacenAndParenStartIndex == -1) {
            return "";
        }

        return heading.substring(0, spacenAndParenStartIndex);
    }

    /**
     * Gets the heading of an anime.
     *
     * @return the heading
     */
    private String getHeading() {
        String header = "<h1 id=\"page_header\">";
        int headerStartIndex = page.indexOf(header);
        if(headerStartIndex == -1) {
            return "";
        }

        int headerEndIndex = page.indexOf("</h1>", headerStartIndex + header.length());
        if(headerEndIndex == -1) {
            return "";
        }

        return page.substring(headerStartIndex + header.length(), headerEndIndex);
    }

    /**
     * Gets the misc data of an anime that appears in a listed ordered.
     *
     * @param type the type of misc data
     *             Can be "Vintage", "Alternative title", "Plot Summary"
     * @return the misc data list
     */
    private List<String> getMisc(String type) {
        List<String> miscList = new ArrayList<>();
        String miscStart = "<strong>" + type + ":</strong>";
        int miscStartIndex = page.indexOf(miscStart);
        if(miscStartIndex == -1) {
            return miscList;
        }

        int miscEndIndex = page.indexOf("<div id=\"infotype-", miscStartIndex + miscStart.length());
        if(miscEndIndex == -1) {
            // try javascript misc end for titles that do not have further info blocks
            miscEndIndex = page.indexOf("<script type=\"text/javascript\">", miscStartIndex + miscStart.length());
            if(miscEndIndex == -1) {
                return miscList;
            }
        }

        String miscContent = page.substring(miscStartIndex + miscStart.length(), miscEndIndex);

        String spanStart = "<span>";
        int spanStartIndex = miscContent.indexOf(spanStart);
        if(spanStartIndex == -1) { // multiple miscs
            for(String line : miscContent.split(SEPARATOR)) {
                // start of misc
                String divStart = "<div class=\"tab\">";
                int divStartIndex = line.lastIndexOf(divStart);
                if(divStartIndex != -1) {
                    // end of content
                    String divEnd = "<";
                    int divEndIndex = line.indexOf(divEnd, divStartIndex + divStart.length());
                    if(divEndIndex != -1) {
                        miscList.add(line.substring(divStartIndex + divStart.length(), divEndIndex));
                    }
                }
            }

            return miscList;
        }
        else { // single misc
            int spanEndIndex = miscContent.indexOf("</span>", spanStartIndex + spanStart.length());
            if(spanEndIndex == -1) {
                return miscList;
            }

            miscList.add(miscContent.substring(spanStartIndex + spanStart.length(), spanEndIndex));

            return miscList;
        }
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    /**
     * Gets the hash code.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;

        return prime * synopsis.length();
    }

    /**
     * Checks if this object is equal to another object.
     *
     * @param object the other object
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object object) {
        // reflexive test
        if(this == object) {
            return true;
        }

        // null test
        if(object == null) {
            return false;
        }

        // symmetry test
        if(getClass() != object.getClass()) {
            return false;
        }

        AnimeInfo animeInfo = (AnimeInfo) object;
        return title.equals(animeInfo.title) && titles.equals(animeInfo.titles) &&
                category.equals(animeInfo.category) && link.equals(animeInfo.link) &&
                dates.equals(animeInfo.dates) && tags.equals(animeInfo.tags) &&
                synopsis.equals(animeInfo.synopsis);
    }

    /**
     * Gets a string representation of this object.
     *
     * @return the string representation of this object
     */
    @Override
    public String toString() {
        return "AnimeInfo [title=" + title + ", link=" + link + ", dates=" + dates + ", tags=" + tags +
                ", category=" + category + ", titles=" + titles + ", synopsis=" + synopsis + "]";
    }
}