package animeinfo;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * An AnimeInfo reader.
 */
public class AnimeInfoReader {
    public static final String ANIME_INFO = "animeInfo", TITLE = "title", TITLES = "titles",
            CATEGORY = "category", LINK = "link", DATES = "dates", TAGS = "tags", SYNOPSIS = "synopsis";

    /**
     * Do not allow objects of this class to be made.
     */
    private AnimeInfoReader() {
    }

    /**
     * Loads a list of AnimeInfos from a given file location.
     *
     * @param fileLocation the file location
     * @return a list of the AnimeInfos in the file
     */
    public static Map<String, AnimeInfo> load(String fileLocation) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        Map<String, AnimeInfo> animeInfoList = new TreeMap<>();

        try {
            InputStream inputStream = new FileInputStream(fileLocation);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream, "UTF-8");

            // read the XML document
            AnimeInfo animeInfo = null;
            while(eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if(event.isStartElement()) {
                    StartElement startElement = event.asStartElement();

                    // if we have an AnimeInfo element, we create a new AnimeInfo
                    if(startElement.getName().getLocalPart().equals(ANIME_INFO)) {
                        animeInfo = new AnimeInfo();
                    }

                    // start adding fields
                    if(event.isStartElement()) {
                        if(event.asStartElement().getName().getLocalPart().equals(TITLE)) {
                            event = eventReader.nextEvent();
                            animeInfo.setTitle(getString(event));

                            continue;
                        }
                    }

                    if(event.asStartElement().getName().getLocalPart().equals(TITLES)) {
                        event = eventReader.nextEvent();
                        animeInfo.setTitles(getString(event));

                        continue;
                    }

                    if(event.asStartElement().getName().getLocalPart().equals(CATEGORY)) {
                        event = eventReader.nextEvent();
                        animeInfo.setCategory(getString(event));

                        continue;
                    }

                    if(event.isStartElement()) {
                        if(event.asStartElement().getName().getLocalPart().equals(LINK)) {
                            event = eventReader.nextEvent();
                            animeInfo.setLink(getString(event));

                            continue;
                        }
                    }

                    if(event.asStartElement().getName().getLocalPart().equals(DATES)) {
                        event = eventReader.nextEvent();
                        animeInfo.setDates(getString(event));

                        continue;
                    }

                    if(event.asStartElement().getName().getLocalPart().equals(TAGS)) {
                        event = eventReader.nextEvent();
                        animeInfo.setTags(getString(event));

                        continue;
                    }

                    if(event.asStartElement().getName().getLocalPart().equals(SYNOPSIS)) {
                        event = eventReader.nextEvent();
                        animeInfo.setSynopsis(getString(event));

                        continue;
                    }
                }

                // if we reach the end of an AnimeInfo element, we add it to the list
                if(event.isEndElement()) {
                    EndElement endElement = event.asEndElement();

                    if(endElement.getName().getLocalPart().equals(ANIME_INFO)) {
                        animeInfoList.put(animeInfo.getTitle(), animeInfo);
                    }
                }
            }

            eventReader.close();
            inputStream.close();
        }
        catch(FileNotFoundException e) {
        }
        catch(XMLStreamException e) {
        }
        catch(IOException e) {
        }

        return animeInfoList;
    }

    /**
     * Gets the string of an event.
     *
     * @param event the event
     * @return the string
     */
    private static String getString(XMLEvent event) {
        // make sure event has something in it
        if(event != null && event.isCharacters()) {
            return event.asCharacters().getData();
        }

        return "";
    }
} 
