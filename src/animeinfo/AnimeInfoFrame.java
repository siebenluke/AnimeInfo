package animeinfo;

import gui.ColorManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class AnimeInfoFrame {
    public static void main(String[] args) {
        // if any args are given, use the command line version
        if(args.length != 0) {
            String fileLocation = "AnimeInfo.xml";
            System.out.println("Loading data from: " + fileLocation);
            AnimeInfoManager.setup(fileLocation);
            System.out.println("Loaded data from: " + fileLocation);
            System.out.println();

            Scanner scanner = new Scanner(System.in);
            while(true) {
                System.out.print("Enter an anime title (or nothing to quit): ");
                String searchTitle = scanner.nextLine();
                if(searchTitle.equals("")) {
                    System.out.println("Quitting program");
                    break;
                }

                System.out.println("searching for: " + searchTitle);
                AnimeInfo animeInfo = AnimeInfoManager.getAnimeInfo(searchTitle);
                System.out.println(animeInfo);
            }

            System.out.println();
            System.out.println("Saving data to: " + fileLocation);
            AnimeInfoManager.save(fileLocation);
            System.out.println("Saved data to: " + fileLocation);
        }
        else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGui(args);
                }
            });
        }
    }

    /**
     * Initializes the GUI.
     */
    private static void createAndShowGui(String[] args) {
        // try to load data
        final String colorsLocation = "Colors.xml";
        ColorManager.setup(colorsLocation);

        final String fileLocation = "AnimeInfo.xml";
        AnimeInfoManager.setup(fileLocation);

        final JFrame frame = new JFrame("AnimeInfo 2016-01-05");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(new AnimeInfoPanel());

        // set size
        Dimension dimension = new Dimension(815, 666);
        frame.setSize(dimension);
        frame.setMinimumSize(dimension);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // window listener stuff
        frame.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                ColorManager.refresh(colorsLocation);
                SwingUtilities.updateComponentTreeUI(frame);
            }

            public void windowClosing(WindowEvent e) {
                AnimeInfoManager.save(fileLocation);
            }
        });
    }
}
