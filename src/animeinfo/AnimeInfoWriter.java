package animeinfo;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.Map;

/**
 * An AnimeInfo writer.
 */
public class AnimeInfoWriter {
    public static final String SEPARATOR = System.getProperty("line.separator"), SPACING = "    ";
    public static final String ANIME_INFOS = "animeInfos", ANIME_INFO = "animeInfo", TITLE = "title", TITLES = "titles",
            CATEGORY = "category", LINK = "link", DATES = "dates", TAGS = "tags", SYNOPSIS = "synopsis";

    /**
     * Do not allow objects of this class to be made.
     */
    private AnimeInfoWriter() {
    }

    /**
     * Saves a list of AnimeInfos to a given file location.
     *
     * @param fileLocation  the file location
     * @param animeInfoList the list of AnimeInfos
     * @return true on success
     */
    public static boolean save(String fileLocation, Map<String, AnimeInfo> animeInfoList) {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        try {
            OutputStream outputStream = new FileOutputStream(fileLocation);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(bufferedOutputStream, "UTF-8");

            streamWriter.writeStartDocument("UTF-8", "1.0");
            streamWriter.writeDTD(SEPARATOR);

            streamWriter.writeStartElement(ANIME_INFOS);
            streamWriter.writeDTD(SEPARATOR);

            for(AnimeInfo animeInfo : animeInfoList.values()) {
                createNode(streamWriter, animeInfo);
            }

            streamWriter.writeEndElement();
            streamWriter.writeDTD(SEPARATOR);

            streamWriter.writeEndDocument();

            streamWriter.close();
            bufferedOutputStream.close();
            outputStream.close();

            return true;
        }
        catch(FileNotFoundException e) {
        }
        catch(XMLStreamException e) {
        }
        catch(IOException e) {
        }

        return false;
    }

    /**
     * Creates a node.
     *
     * @param streamWriter the stream writer to create the node on
     * @param animeInfo    the animeInfo
     *                     throws XMLStreamException on XMLStreamException
     */
    private static void createNode(XMLStreamWriter streamWriter, AnimeInfo animeInfo) throws XMLStreamException {
        streamWriter.writeCharacters(SPACING);
        streamWriter.writeStartElement(ANIME_INFO);
        streamWriter.writeCharacters(SEPARATOR);

        createCDataNode(streamWriter, TITLE, animeInfo.getTitle());
        createCDataNode(streamWriter, TITLES, animeInfo.getTitles());
        createCDataNode(streamWriter, CATEGORY, animeInfo.getCategory());
        createCDataNode(streamWriter, LINK, animeInfo.getLink());
        createCDataNode(streamWriter, DATES, animeInfo.getDates());
        createCDataNode(streamWriter, TAGS, animeInfo.getTags());
        createCDataNode(streamWriter, SYNOPSIS, animeInfo.getSynopsis());

        streamWriter.writeCharacters(SPACING);
        streamWriter.writeEndElement();
        streamWriter.writeCharacters(SEPARATOR);
    }

    /**
     * Creates a CData node.
     *
     * @param streamWriter the stream writer to create the node on
     * @param localName    the node's localName
     * @param data         the node's data
     * @throws XMLStreamException on XMLStreamException
     */
    private static void createCDataNode(XMLStreamWriter streamWriter, String localName, String data) throws XMLStreamException {
        streamWriter.writeCharacters(SPACING + SPACING);
        streamWriter.writeStartElement(localName);

        streamWriter.writeCData(data);

        streamWriter.writeEndElement();
        streamWriter.writeCharacters(SEPARATOR);
    }
}
