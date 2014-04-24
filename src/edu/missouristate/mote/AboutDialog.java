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
    // PRIVATE CONSTANTS
    // *************************************************************************

    /** Space above the contact label. */
    private static final int CONTACT_SPACE = 18;

    /** Width of the email label. */
    private static final int EMAIL_LABEL_SIZE = 240;

    /** Width and height of the logo panel. */
    private static final int LOGO_PANEL_SIZE = 220;

    /** Space above the title label. */
    private static final int TITLE_SPACE = 46;

    // *************************************************************************
    // PRIVATE FIELDS
    // *************************************************************************

    /** Panel for holding the home, source, and license buttons. */
    private final transient JPanel buttonPanel;

    /** Label for the contact. */
    private final transient JLabel contactLabel;

    /** Label for the contact email addresses(s). */
    private final transient JLabel emailLabel;

    /** Button/link for the Mote home page. */
    private final transient JButton homeButton;

    /** Button/link for the Mote license. */
    private final transient JButton licenseButton;

    /** Panel containing the Mote logo. */
    private final transient ImagePanel logoPanel;

    /** Label for the contact name(s). */
    private final transient JLabel nameLabel;

    /** Button/link for the Mote source repository. */
    private final transient JButton sourceButton;

    /** Mote acronym definition. */
    private final transient JLabel subtitleLabel;

    /** Mote!. */
    private final transient JLabel titleLabel;

    /** Current program version. */
    private final transient JLabel versionLabel;

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
    /**
     * Setup a link button.
     * @param button button reference
     * @param text button text
     * @param url link associated with the button text
     */
    private static void setupButton(final JButton button,
            final String text, final String url) {
        button.setBackground(Constants.NAME_BG_COLOR);
        button.setText("<html><a href=''>" + text + "</a></html>");
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(new ActionListener() {
            @Override
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

    /**
     * Setup this dialog.
     */
    private void setup() {
        setupButtons();
        setupLabels();
        setupLogo();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent evt) {
                closeDialog();
            }
        });

        final GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                .add(logoPanel, LOGO_PANEL_SIZE, LOGO_PANEL_SIZE,
                        LOGO_PANEL_SIZE)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                .add(emailLabel, EMAIL_LABEL_SIZE, EMAIL_LABEL_SIZE,
                        EMAIL_LABEL_SIZE)
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
                .add(TITLE_SPACE, TITLE_SPACE, TITLE_SPACE)
                .add(titleLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(subtitleLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(versionLabel)
                .add(CONTACT_SPACE, CONTACT_SPACE, CONTACT_SPACE)
                .add(contactLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(nameLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(emailLabel))
                .add(logoPanel, LOGO_PANEL_SIZE, LOGO_PANEL_SIZE,
                        LOGO_PANEL_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(buttonPanel)));
        pack();
    }

    /**
     * Setup the buttons on this dialog.
     */
    private void setupButtons() {
        // Formatting
        buttonPanel.setBackground(Constants.NAME_BG_COLOR);
        setupButton(homeButton, "Home Page", "http://www.aggieerin.com/mote");
        homeButton.setHorizontalAlignment(SwingConstants.LEADING);
        setupButton(sourceButton, "Source Code",
                "https://www.github.com/larmer01/mote");
        sourceButton.setHorizontalAlignment(SwingConstants.CENTER);
        setupButton(licenseButton, "License",
                "https://github.com/larmer01/Mote/blob/master/LICENSE.md");
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

    /**
     * Setup the labels on this dialog.
     */
    private void setupLabels() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setText("MOTE");

        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setText("Measure of the Effect");

        if (Constants.VERSION.startsWith("0.")) {
            versionLabel.setText(Constants.VERSION + " (beta)");
        } else {
            versionLabel.setText(Constants.VERSION);
        }
        versionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        contactLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contactLabel.setText("Contact:");

        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setText("Erin Buchanan");

        emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emailLabel.setText("erinbuchanan@missouristate.edu");
    }

    /**
     * Setup the logo image on this dialog.
     */
    private void setupLogo() {
        logoPanel.setBackgroundImage("/MoteLogo.png");
    }
}
