package tool;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;

/**
 * General purpose class to deal with links.
 */
public final class LinkTool {
    public static final String SEPARATOR = System.getProperty("line.separator");

    /**
     * Do not allow objects of this class to be made.
     */
    private LinkTool() {
    }

    /**
     * Gets a bing search of non UTF8 encoded text.
     *
     * @param searchTerm the search term
     * @return the bing search result or null
     */
    public static String getBingSearchResult(String searchTerm) {
        return loadAsString("http://www.bing.com/search?q=" + getUTF8Encode(searchTerm));
    }

    /**
     * Gets the first link of a site after doing a bing search on the given search term.
     *
     * @param searchTerm    the search term
     * @param siteLinkStart the site we want to get a link from
     * @return the site link or null
     */
    public static String getBingSearchResultLink(String searchTerm, String siteLinkStart) {
        String bingResult = getBingSearchResult(searchTerm);

        if(bingResult == null) {
            return null;
        }

        int bingLinkStartIndex = bingResult.indexOf(siteLinkStart);
        if(bingLinkStartIndex == -1) { // could not find start of link
            return null;
        }

        int bingLinkEndIndex = bingResult.indexOf("\"", bingLinkStartIndex);
        if(bingLinkEndIndex == -1) { // could not find end of link
            return null;
        }

        return bingResult.substring(bingLinkStartIndex, bingLinkEndIndex);
    }

    /**
     * Gets a google search of non UTF8 encoded text.
     *
     * @param searchTerm the search term
     * @return the google search result or null
     */
    public static String getGoogleSearchResult(String searchTerm) {
        return loadAsString("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=" + getUTF8Encode(searchTerm));
    }

    /**
     * Gets the first link of a site after doing a google search on the given search term.
     *
     * @param searchTerm    the search term
     * @param siteLinkStart the site we want to get a link from
     * @return the site link or null
     */
    public static String getGoogleSearchResultLink(String searchTerm, String siteLinkStart) {
        String googleResult = getGoogleSearchResult(searchTerm);

        if(googleResult == null) {
            return null;
        }

        String urlStart = "\"url\":\"";
        int googleLinkStartIndex = googleResult.indexOf(urlStart + siteLinkStart);
        if(googleLinkStartIndex == -1) {
            return null;
        }

        String urlEnd = "\",\"visibleUrl\"";
        int googleLinkEndIndex = googleResult.indexOf(urlEnd, googleLinkStartIndex);
        if(googleLinkEndIndex == -1) {
            return null;
        }

        return getUTF8Decode(googleResult.substring(googleLinkStartIndex + urlStart.length(), googleLinkEndIndex));
    }

    /*
     * Gets the size of a page in bytes.
     * @param link the link
     * @return the size of a page in bytes, otherwise -1
     */
    public static int getSize(String link) {
        if(link == null) {
            return -1;
        }

        try {
            return new URL(link).openConnection().getContentLength();
        }
        catch(MalformedURLException e) {
        }
        catch(IOException e) {
        }

        return -1;
    }

    /**
     * Gets the UTF8 decode of the given text.
     *
     * @param text the text
     * @return the UTF8 encode of the given text, otherwise null
     */
    public static String getUTF8Decode(String text) {
        if(text == null) {
            return null;
        }

        try {
            return URLDecoder.decode(text, "UTF-8");
        }
        catch(UnsupportedEncodingException e) {
        }

        return null;
    }

    /**
     * Gets the UTF8 encode of the given text.
     *
     * @param text the text
     * @return the UTF8 encode of the given text, otherwise null
     */
    public static String getUTF8Encode(String text) {
        if(text == null) {
            return null;
        }

        try {
            return URLEncoder.encode(text, "UTF-8");
        }
        catch(UnsupportedEncodingException e) {
        }

        return null;
    }

    /**
     * Gets data from a link.
     *
     * @param link the link
     * @return the contents of the link as a list, otherwise null
     */
    public static List<String> loadAsList(String link) {
        String data = loadAsString(link);
        if(data == null) {
            return null;
        }

        return Arrays.asList(data.split(SEPARATOR));
    }

    /**
     * Gets data from a link.
     *
     * @param link the link
     * @return the contents of the link as a string, otherwise null
     */
    public static String loadAsString(String link) {
        if(link == null) {
            return null;
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"))) {

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null) {
                sb.append(line);
                sb.append(SEPARATOR);

                line = br.readLine();
            }

            // remove last separator if there is one
            String data = sb.toString();
            int lastSeparatorIndex = data.lastIndexOf(SEPARATOR);
            if(lastSeparatorIndex != -1) {
                data = data.substring(0, lastSeparatorIndex);
            }

            return data;
        }
        catch(IOException e) {
        }

        return null;
    }

    /**
     * Attempts to open a given link in the user's default browser.
     *
     * @param link the link
     * @return true on success, otherwise false
     */
    public static boolean openLink(String link) {
        if(link == null) {
            return false;
        }

        try {
            if(Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(link));

                return true;
            }
        }
        catch(IOException e) {
        }

        return false;
    }

    /**
     * Sends data to a link in order to get a response.
     *
     * @param link     the link
     * @param postData the data to post
     * @return the response, otherwise null
     */
    public static String post(String link, String postData) {
        if(link == null || postData == null) {
            return null;
        }

        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();

            // send request
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(postData);
            dataOutputStream.flush();
            dataOutputStream.close();

            // get response
            String data = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String temp = bufferedReader.readLine();
            while(temp != null) {
                data += temp + SEPARATOR;
                temp = bufferedReader.readLine();
            }
            bufferedReader.close();

            // remove last separator
            int lastSeparatorIndex = data.lastIndexOf(SEPARATOR);
            if(lastSeparatorIndex != -1) {
                data = data.substring(0, lastSeparatorIndex);
            }

            return data;
        }
        catch(MalformedURLException e) {
        }
        catch(IOException e) {
        }

        return null;
    }

    /**
     * Attempts to save a given link into a given file.
     *
     * @param link     the link
     * @param filename the filename
     *                 return true on success, otherwise false
     */
    public static boolean save(String filename, String link) {
        if(filename == null || link == null) {
            return false;
        }

        // don't waste time downloading an image the user already has
        File file = new File(filename);
        if(file.exists()) {
            return true;
        }

        try(InputStream is = new URL(link).openStream();
            OutputStream os = new FileOutputStream(filename)) {
            byte[] b = new byte[1024];

            int length;
            while((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            return true;
        }
        catch(IOException e) {
        }

        return false;
    }
}