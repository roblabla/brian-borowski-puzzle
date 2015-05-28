package me.roblabla;

/**
 * File: MessageBox.java
 * Author: Brian Borowski
 * Date created: May 1999
 * Date last modified: March 17, 2013
 */
import java.applet.AudioClip;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MessageBox extends JDialog {
    public final static int EXCLAIM = 0, INFORM = 1;
    private static final long serialVersionUID = 1L;
    private final static String[] imageFilename =
        {"images/exclaim.png", "images/information.png"};

    public MessageBox(final JFrame parent, final String title,
                      final String messageString, final int imageType) {

        super(parent, title, true);
        final ImagePanel imagePanel =
            new ImagePanel(imageFilename[imageType]);

        final JLabel messageLabel = new JLabel(messageString);

        final JPanel imageMessagePanel = new JPanel();
        imageMessagePanel.setLayout(new FlowLayout());
        imageMessagePanel.add(imagePanel);
        imageMessagePanel.add(messageLabel);

        final JButton okButton = new JButton("OK");
        okButton.setMnemonic('O');
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                setVisible(false);
                dispose();
            }
        });
        okButton.addKeyListener(new KeyListener() {
            public void keyPressed(final KeyEvent ke) {
                if ((KeyEvent.getKeyText(ke.getKeyCode()).equals("Enter")) ||
                    (KeyEvent.getKeyText(ke.getKeyCode()).equals("Escape"))) {
                    setVisible(false);
                    dispose();
                }
            }
            public void keyReleased(final KeyEvent ke) { }
            public void keyTyped(final KeyEvent ke) { }
        });

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(okButton);

        setLayout(new GridLayout(2, 1));
        add(imageMessagePanel);
        add(buttonPanel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                e.getWindow().setVisible(false);
                e.getWindow().dispose();
            }
        });

        setResizable(false);
        pack();

        setLocationRelativeTo(parent);
        play("audio/notification.wav");
        setVisible(true);
    }

    private static void play(final String filename) {
        URL url = null;

        url = MessageBox.class.getResource(filename);
        if (url == null) {
            try {
                final File file = new File(filename);
                if (file.canRead()) url = file.toURI().toURL();
            } catch (final IllegalArgumentException iae) {
                // URL is not absolute.
            } catch (final MalformedURLException murle) {
                // Not a URL.
            }
        }

        if (url == null) {
            System.err.println("Audio file " + filename + " not found.");
            return;
        }
        final AudioClip clip = JApplet.newAudioClip(url);
        clip.play();
    }
}

