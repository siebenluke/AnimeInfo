package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Color manager.
 */
public class ColorManager {
    public static final String LOOK_AND_FEEL = "Nimbus", BACKGROUND = "background", TEXT = "text", HIGHLIGHT = "highlight",
            SELECTED_TEXT = "selectedText", SUCCESS_TEXT = "successText", WARNING_TEXT = "warningText", ERROR_TEXT = "errorText";
    private static Map<String, Color> nameToColorMap;
    private static boolean firstRun;

    // static initializer
    static {
        nameToColorMap = new TreeMap<>();
        nameToColorMap.put(BACKGROUND, new Color(30, 30, 30));
        nameToColorMap.put(HIGHLIGHT, new Color(19, 63, 110));
        nameToColorMap.put(TEXT, new Color(200, 200, 200));
        nameToColorMap.put(SELECTED_TEXT, new Color(200, 200, 200));
        nameToColorMap.put(SUCCESS_TEXT, new Color(128, 255, 128));
        nameToColorMap.put(WARNING_TEXT, new Color(255, 255, 128));
        nameToColorMap.put(ERROR_TEXT, new Color(255, 128, 128));
        firstRun = true;
    }

    /**
     * Do not allow objects of this class to be made.
     */
    private ColorManager() {
    }

    /**
     * Attempts to setup the theme color and saves the default color mappings if we failed to load the theme file.
     *
     * @return true on success
     */
    public static boolean setup() {
        return setup("");
    }

    /**
     * Attempts to setup the theme color and saves the default color mappings if we failed to load the theme file.
     *
     * @param fileLocation the location of the Theme file
     * @return true on success
     */
    public static boolean setup(String fileLocation) {
        try {
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if(info.getName().equals(LOOK_AND_FEEL)) {
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }
        }
        catch(Exception e) {
        }

        firstRun = true;
        return refresh(fileLocation);
    }

    /**
     * Saves the colors to a given file location.
     * @param fileLocation  the file location
     */
    public static boolean save(String fileLocation) {
        return ColorWriter.save(fileLocation, nameToColorMap);
    }

    /**
     * Attempts to refresh the theme.
     *
     * @return true on success
     */
    public static boolean refresh(String fileLocation) {
        Map<String, Color> newNameToColorMap = ColorReader.load(fileLocation);
        if(firstRun) {
            firstRun = false;
        }
        else {
            if(nameToColorMap.equals(newNameToColorMap)) {
                return false;
            }
        }

        nameToColorMap.putAll(newNameToColorMap);
        setNimbusColors();

        return true;
    }

    /**
     * Puts a color with a name into the Color Manager.
     * @param name the name associated with the color
     * @param color the color
     * @return true if name and color are not null
     */
    public static boolean put(String name, Color color) {
        if(name == null || color == null) {
            return false;
        }

        nameToColorMap.put(name, color);

        return true;
    }

    /**
     * Sets the Nimbus colors of the GUI using the colors in the Theme class.
     */
    private static void setNimbusColors() {
        UIManager.put("control", nameToColorMap.get(BACKGROUND)); // empty (aka background of JPanels) component color
        UIManager.put("info", nameToColorMap.get(BACKGROUND)); // tooltip background color

        UIManager.put("nimbusBase", nameToColorMap.get(BACKGROUND)); // color of radio buttons dots/first item of combo box background

        UIManager.put("nimbusFocus", nameToColorMap.get(HIGHLIGHT)); // color around focused component

        UIManager.put("nimbusLightBackground", nameToColorMap.get(BACKGROUND)); // text area background

        UIManager.put("nimbusOrange", nameToColorMap.get(HIGHLIGHT)); // progress bar background color

        UIManager.put("nimbusSelectedText", nameToColorMap.get(SELECTED_TEXT)); // selected text color
        UIManager.put("nimbusSelectionBackground", nameToColorMap.get(HIGHLIGHT)); // selected text's background color
        UIManager.put("text", nameToColorMap.get(TEXT)); // text color

        UIManager.put("nimbusBlueGrey", nameToColorMap.get(BACKGROUND)); // general background color of components

        UIManager.put("TitledBorder.titleColor", nameToColorMap.get(TEXT)); // titled border color
    }

    /**
     * Gets a color given a color name.
     *
     * @param colorName the color name
     * @return the color
     */
    public static Color getColor(String colorName) {
        return nameToColorMap.get(colorName);
    }
}
