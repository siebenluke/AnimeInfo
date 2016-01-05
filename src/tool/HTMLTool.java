package tool;

import java.util.List;

/**
 * General purpose class to create HTML tags.
 */
public final class HTMLTool {
    public static final String SEPARATOR = System.getProperty("line.separator");
    public static String SPACING = "    ";
    public static String DOUBLE_SPACING = SPACING + SPACING;
    public static String BR_TAG = "<br>";

    /**
     * Do not allow objects of this class to be made.
     */
    private HTMLTool() {
    }

    /**
     * Gets an a tag.
     *
     * @param href the href of the a tag
     * @param text the text of the a tag
     * @return the a tag
     */
    public static String getATag(String href, String text) {
        return "<a href=\"" + href + "\">" + text + "</a>";
    }

    /**
     * Gets an a tag that is useful for bookmarking purposes.
     *
     * @param text the id, href, and text of the a tag
     * @return the a tag
     */
    public static String getATag(String id, String href, String text) {
        return "<a id=\"" + id + "\" href=\"" + href + "\">" + text + "</a>";
    }

    /**
     * Gets a div tag.
     *
     * @param text the text of the div tag
     * @return the div tag
     */
    public static String getDivTag(String text) {
        return "<div>" + text + "</div>";
    }

    /**
     * Gets a div tag.
     *
     * @param divClass the class of the div tag
     * @param text     the text of the div tag
     * @return the div tag
     */
    public static String getDivTag(String divClass, String text) {
        return "<div class=\"" + divClass + "\">" + text + "</div>";
    }

    /**
     * Gets a h1 tag.
     *
     * @param text the text of the h1 tag
     * @return the h1 tag
     */
    public static String getH1Tag(String text) {
        return "<h1>" + text + "</h1>";
    }

    /**
     * Gets an img tag.
     *
     * @param src the source of the img tag
     * @return the img tag
     */
    public static String getImgTag(String src) {
        return "<img src=\"" + src + "\">";
    }

    /**
     * Gets an img tag.
     *
     * @param imgClass the class of the img tag
     * @param href     the href of the img tag
     * @param title    the title of the img tag
     * @param src      the source of the img tag
     * @return the img tag
     */
    public static String getIMGTag(String imgClass, String href, String title, String src) {
        if(href.equals("")) {
            return "<a class=\"" + imgClass + "\"><img src=\"" + src + "\" alt=\"" + title + "\" title=\"" + title + "\"></a>";
        }
        else {
            return "<a class=\"" + imgClass + "\" href=\"" + href + "\"><img src=\"" + src + "\" alt=\"" + title + "\" title=\"" + title + "\"></a>";
        }
    }

    /**
     * Gets a list.
     *
     * @param listTagStart the list's starting tag
     * @param listTagEnd   the list's ending tag
     * @param list         the list of items to put in the list
     * @return the list
     */
    public static String getList(String listTagStart, String listTagEnd, List<String> list) {
        String l = listTagStart + SEPARATOR;

        for(String temp : list) {
            l += SPACING + "<li>" + temp + "</li>" + SEPARATOR;
        }

        l += listTagEnd;

        return l;
    }

    /**
     * Gets an ordered list.
     *
     * @param orderedListClass the ordered list's class
     * @param list             the list of items to put in the list
     * @return the list
     */
    public static String getOrderedList(String orderedListClass, List<String> list) {
        return getList("<ol class=\"" + orderedListClass + "\">", "</ol>", list);
    }

    /**
     * Gets an ordered list.
     *
     * @param list the list of items to put in the list
     * @return the list
     */
    public static String getOrderedList(List<String> list) {
        return getList("<ol>", "</ol>", list);
    }

    /**
     * Gets a p tag.
     *
     * @param text the text of the p tag
     * @return the p tag
     */
    public static String getPTag(String text) {
        return "<p>" + text + "</p>";
    }

    /**
     * Gets a pre tag.
     *
     * @param text the text of the pre tag
     * @return the pre tag
     */
    public static String getPreTag(String text) {
        return "<pre>" + text + "</pre>";
    }

    /**
     * Gets a span tag.
     *
     * @param text the text of the span tag
     * @return the span tag
     */
    public static String getSpanTag(String text) {
        return "<span>" + text + "</span>";
    }

    /**
     * Gets a strong tag.
     *
     * @param text the text of the strong tag
     * @return the strong tag
     */
    public static String getStrongTag(String text) {
        return "<strong>" + text + "</strong>";
    }

    /**
     * Gets a sup tag.
     *
     * @param text the text of the sup tag
     * @return the sup tag
     */
    public static String getSupTag(String text) {
        return "<sup>" + text + "</sup>";
    }

    /**
     * Gets a table.
     *
     * @param tableClass the table's class
     * @param title      the title of the table
     * @param headers    the headers of the table
     * @param elements   the elements in each row
     * @return the table
     */
    public static String getTable(String tableClass, String title, List<String> headers, List<List<String>> elements) {
        String columnHeader = "";

        if(headers != null) {
            columnHeader = SPACING + "<thead>" + SEPARATOR +
                    SPACING + "<tr>" + SEPARATOR;

            for(String header : headers) {
                columnHeader += DOUBLE_SPACING + "<th>" + header + "</th>" + SEPARATOR;
            }

            columnHeader += SPACING + "</tr>" + SEPARATOR +
                    SPACING + "</thead>" + SEPARATOR;
        }

        String elementRow = "";
        for(List<String> list : elements) {
            String row = SPACING + "<tr>" + SEPARATOR;

            for(String element : list) {
                row += DOUBLE_SPACING + "<td>" + element + "</td>" + SEPARATOR;
            }

            row += SPACING + "</tr>" + SEPARATOR;
            elementRow += row;
        }

        String caption = "";
        if(!title.equals("")) {
            caption = SPACING + "<caption>" + title + "</caption>" + SEPARATOR;
        }

        return "<table class=\"" + tableClass + "\">" + SEPARATOR +
                caption +
                columnHeader +
                SPACING + "<tbody>" + SEPARATOR +
                elementRow +
                SPACING + "</tbody>" + SEPARATOR +
                "</table>";
    }

    /**
     * Gets a title tag.
     *
     * @param text the text of the title tag
     * @return the title tag
     */
    public static String getTitleTag(String text) {
        return "<title>" + text + "</title>";
    }

    /**
     * Gets an unordered list.
     *
     * @param unorderedListClass the unordered list's class
     * @param list               the list of items to put in the list
     * @return the list
     */
    public static String getUnorderedList(String unorderedListClass, List<String> list) {
        return getList("<ul class=\"" + unorderedListClass + "\">", "</ul>", list);
    }

    /**
     * Gets an unordered list.
     *
     * @param list the list of items to put in the list
     * @return the list
     */
    public static String getUnorderedList(List<String> list) {
        return getList("<ul>", "</ul>", list);
    }
}