package edu.missouristate.mote;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 * Dialog box that gives information about this application.
 */
public class AboutDialog extends JDialog {

    // *************************************************************************
    // PRIVATE FIELDS
    // *************************************************************************
    private transient final JPanel buttonPanel;
    private transient final JLabel contactLabel;
    private transient final JLabel emailLabel;
    private transient final JButton homeButton;
    private transient final JButton licenseButton;
    private transient final ImagePanel logoPanel;
    private transient final JLabel nameLabel;
    private transient final JButton sourceButton;
    private transient final JLabel subtitleLabel;
    private transient final JLabel titleLabel;
    private transient final JLabel versionLabel;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of an AboutDialog.
     * @param parent parent frame
     */
    public AboutDialog(final Frame parent) {
        super(parent, true);
        buttonPanel = new JPanel();
        contactLabel = new JLabel();
        emailLabel = new JLabel();
        homeButton = new JButton();
        licenseButton = new JButton();
        logoPanel = new ImagePanel();
        nameLabel = new JLabel();
        sourceButton = new JButton();
        subtitleLabel = new JLabel();
        titleLabel = new JLabel();
        versionLabel = new JLabel();
        setup();
        getContentPane().setBackground(Color.white);
        setLocation(parent.getX() + parent.getWidth() / 2 - getWidth() / 2,
                parent.getY() + parent.getHeight() / 2 - getHeight() / 2);
        setResizable(false);
    }

    // *************************************************************************
    // PRIVATE STATIC METHODS
    // *************************************************************************
    private static final void setupButton(final JButton button,
            final String text, final String url) {
        button.setBackground(Constants.NAME_BG_COLOR);
        button.setText("<html><a href=''>" + text + "</a></html>");
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                DesktopHandler.openWebPage(url);
            }
        });
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    /**
     * Close the dialog window.
     */
    private void closeDialog() {
        setVisible(false);
        dispose();
    }
    
    private void setup() {
        setupButtons();
        setupLabels();
        setupLogo();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent evt) {
                closeDialog();
            }
        });

        final GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(logoPanel, 220, 220, 220)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                .add(emailLabel, 240, 240, 240)
                .add(GroupLayout.CENTER, nameLabel)
                .add(GroupLayout.CENTER, contactLabel)
                .add(GroupLayout.CENTER, versionLabel)
                .add(GroupLayout.CENTER, subtitleLabel)
                .add(GroupLayout.CENTER, titleLabel)))
                .add(GroupLayout.TRAILING, buttonPanel));
        layout.setVerticalGroup(
                layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                .add(46, 46, 46)
                .add(titleLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(subtitleLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(versionLabel)
                .add(18, 18, 18)
                .add(contactLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(nameLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(emailLabel))
                .add(logoPanel, 220, 220, 220))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(buttonPanel)));
        pack();
    }

    private void setupButtons() {
        // Formatting
        buttonPanel.setBackground(Constants.NAME_BG_COLOR);
        setupButton(homeButton, "Home Page", "http://www.aggieerin.com/mote");
        homeButton.setHorizontalAlignment(SwingConstants.LEADING);
        setupButton(sourceButton, "Source Code", "https://www.github.com/larmer01/mote");
        sourceButton.setHorizontalAlignment(SwingConstants.CENTER);
        setupButton(licenseButton, "License", "https://github.com/larmer01/Mote/blob/master/LICENSE.md");
        licenseButton.setHorizontalAlignment(SwingConstants.TRAILING);
        // Layout
        final GroupLayout layout = new GroupLayout(buttonPanel);
        buttonPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(homeButton)
                .add(sourceButton)
                .add(licenseButton)
                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                .add(homeButton)
                .add(sourceButton)
                .add(licenseButton))
                .addContainerGap()));
    }

    private void setupLabels() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setText("MOTE");

        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setText("Measure of the Effect");

        versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        versionLabel.setText(Constants.VERSION
            + (Constants.VERSION < 1 ? " (beta)" : ""));

        contactLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contactLabel.setText("Contact:");

        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setText("Erin Buchanan");

        emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emailLabel.setText("erinbuchanan@missouristate.edu");
    }

    private void setupLogo() {
        logoPanel.setBackgroundImage("/MoteLogo.png");
    }
}