package gui;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.*;
import java.io.*;
import java.util.Map;

/**
 * The Color writer.
 */
public class ColorWriter {
    public static final String SEPARATOR = System.getProperty("line.separator"), SPACING = "    ";
    public static final String COLORS = "colors", COLOR = "color", NAME = "name", RGB = "rgb";

    /**
     * Do not allow objects of this class to be made.
     */
    private ColorWriter() {
    }

    /**
     * Saves a theme to a given file location.
     *
     * @param fileLocation the file location
     * @return true on success
     */
    public static boolean save(String fileLocation, Map<String, Color> nameToColorMap) {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        try {
            OutputStream outputStream = new FileOutputStream(fileLocation);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(bufferedOutputStream, "UTF-8");

            streamWriter.writeStartDocument("UTF-8", "1.0");
            streamWriter.writeDTD(SEPARATOR);

            streamWriter.writeStartElement(COLORS);
            streamWriter.writeCharacters(SEPARATOR);

            for(Map.Entry<String, Color> colorEntry : nameToColorMap.entrySet()) {
                createNode(streamWriter, colorEntry);
            }

            streamWriter.writeEndElement();
            streamWriter.writeCharacters(SEPARATOR);

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
     * @param colorEntry   the colorEntry
     *                     throws XMLStreamException on XMLStreamException
     */
    private static void createNode(XMLStreamWriter streamWriter, Map.Entry<String, Color> colorEntry) throws XMLStreamException {
        streamWriter.writeCharacters(SPACING);
        streamWriter.writeStartElement(COLOR);
        streamWriter.writeCharacters(SEPARATOR);

        createCharacterNode(streamWriter, NAME, colorEntry.getKey(), SPACING);
        Color color = colorEntry.getValue();
        createCharacterNode(streamWriter, RGB, getColorAsText(colorEntry.getValue()), SPACING);

        streamWriter.writeCharacters(SPACING);
        streamWriter.writeEndElement();
        streamWriter.writeCharacters(SEPARATOR);
    }

    /**
     * Gets a color as text (the RGB value separated by commas).
     *
     * @param color the color
     * @return the color as text
     */
    public static String getColorAsText(Color color) {
        return color.getRed() + ", " + color.getGreen() + ", " + color.getBlue();
    }

    /**
     * Creates a character node.
     *
     * @param streamWriter the stream writer to create the node on
     * @param localName    the node's localName
     * @param text         the node's text
     * @throws XMLStreamException on XMLStreamException
     */
    private static void createCharacterNode(XMLStreamWriter streamWriter, String localName, String text, String spacing) throws XMLStreamException {
        streamWriter.writeCharacters(SPACING + spacing);
        streamWriter.writeStartElement(localName);

        streamWriter.writeCharacters(text);

        streamWriter.writeEndElement();
        streamWriter.writeCharacters(SEPARATOR);
    }
}
