package animeinfo;

import animeinfo.AnimeInfo;
import animeinfo.AnimeInfoManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An AnimeInfoPanel.
 */
public class AnimeInfoPanel extends JPanel {
    public static final String SEPARATOR = System.getProperty("line.separator");

    public AnimeInfoPanel() {
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(0, 2));
        JTextField searchTextField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchTextField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        JTextArea resultsArea = new JTextArea();
        resultsArea.setLineWrap(true);
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Results Area"));
        add(scrollPane, BorderLayout.CENTER);

        JProgressBar searchProgressBar = new JProgressBar();
        searchProgressBar.setStringPainted(true);
        searchProgressBar.setString("Search status");
        add(searchProgressBar, BorderLayout.SOUTH);

        // add listener
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Boolean, Void>() {
                    public Boolean doInBackground() {
                        String searchTitle = searchTextField.getText();
                        searchProgressBar.setString("Searching for: " + searchTitle);
                        searchProgressBar.setIndeterminate(true);

                        AnimeInfo animeInfo = AnimeInfoManager.getAnimeInfo(searchTitle);

                        String text = animeInfo + SEPARATOR + SEPARATOR +  resultsArea.getText();
                        resultsArea.setText(text);

                        searchProgressBar.setString("Added data for: " + searchTitle);
                        searchProgressBar.setIndeterminate(false);

                        return true;
                    }

                    public void done() {
                    }
                }.execute();
            }
        };
        searchTextField.addActionListener(actionListener);
        searchButton.addActionListener(actionListener);
    }
}
