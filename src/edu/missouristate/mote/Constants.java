package edu.missouristate.mote;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Constants used by Mote.
 */
public final class Constants {

    // *************************************************************************
    // GENERAL
    // *************************************************************************

    /** Program version. */
    public static final String VERSION = "0.7.1";

    // *************************************************************************
    // EFFECT SIZES
    // *************************************************************************

    /** Minimum value for 1 - alpha. */
    public static final double MIN_CONFIDENCE = 0.001;

    /** Maximum value for 1 - alpha. */
    public static final double MAX_CONFIDENCE = 0.999;

    /** Minimum degrees of freedom. */
    public static final double MIN_DF = 1;

    /** Minimum sample size. */
    public static final double MIN_SS = MIN_DF + 1;

    /**
     * Minimum density (y value) that should be included in the PDF curve as
     * a fraction of the PDF maximum value.
     */
    public static final double MIN_PDF_PROB = 0.01;

    /** Maximum difference between the target and actual CDF values. */
    public static final double PRECISION = 0.0000001;

    /** T value (x axis) increments on the PDF curve. */
    public static final double T_STEP = 0.05;

    /** F value (x axis) increments on the PDF curve. */
    public static final double F_STEP = 0.05;

    /** X value increments on the PDF curve. */
    public static final double X_STEP = 0.05;

    /** Maximum number of iterations before converging on non-centrality. */
    public static final int NC_MAX_ITER = 100;

    /** Minimum value for R. */
    public static final double MIN_R = -1 + PRECISION;

    /** Maximum value for R. */
    public static final double MAX_R = 1 - PRECISION;

    // *************************************************************************
    // APPEARANCE
    // *************************************************************************

    /** Data grid min height. */
    public static final int GRID_MIN_HEIGHT = 600;

    /** Data grid min width. */
    public static final int GRID_MIN_WIDTH = 320;

    /** Property grid column names. */
    public static final String[] GRID_COL_NAMES = new String[]{"Name", "Value"};

    /** Percent of the property grid's width taken up by the name column. */
    public static final double GRID_NAME_PCT = 0.6;

    /** Insets for the cells in the property grid. */
    public static final Insets CELL_INSETS = new Insets(1, 5, 1, 1);
    
    /** Height of the cells in the property grid. */
    public static final int CELL_HEIGHT = 20;

    /** Property grid help area min height. */
    public static final int HELP_MIN_HEIGHT = 110;

    /** Decimal format for property grid numbers. */
    public static final String DECIMAL_FORMAT = "#,##0.0######";

    /** Background color for the graph. */
    public static final Color GRAPH_BG_COLOR =
            UIManager.getColor("TextField.background");

    /** Default graph height. */
    public static final int GRAPH_HEIGHT = 550;

    /** Default graph width. */
    public static final int GRAPH_WIDTH = 680;

    /** Graph gridline color. */
    public static final Color GRAPH_GRID_COLOR =
            UIManager.getColor("Table.gridColor");

    /** Background color for the form. */
    public static final Color FORM_BG_COLOR =
            UIManager.getColor("TextField.background");

    /** Name of the property grid input category. */
    public static final String INPUT_CATEGORY = "Inputs";

    /** Name of the property grid derived inputs category. */
    public static final String DERIVED_CATEGORY = "Derived";

    /** Name of the property grid results category. */
    public static final String OUTPUT_CATEGORY = "Results";

    /** Background color for the property grid name fields. */
    public static final Color NAME_BG_COLOR =
            UIManager.getColor("TableHeader.background");

    /** Foreground color for the property grid name fields. */
    public static final Color NAME_FG_COLOR =
            UIManager.getColor("TableHeader.foreground");

    /** Background color for the property grid value fields. */
    public static final Color VALUE_BG_COLOR =
            UIManager.getColor("Table.background");

    /** Foreground color for the property grid value fields. */
    public static final Color VALUE_FG_COLOR =
            UIManager.getColor("Table.foreground");

    /** Background color for the property grid category fields. */
    public static final Color CATEGORY_BG_COLOR =
            UIManager.getColor("Table.dropLineColor");

    /** Foreground color for the property grid category fields. */
    public static final Color CATEGORY_FG_COLOR =
            UIManager.getColor("Table.background");

    /** Background color for the property grid selected field. */
    public static final Color SELECT_BG_COLOR =
            UIManager.getColor("Table.selectionBackground");

    /** Foreground color for the property grid selected field. */
    public static final Color SELECT_FG_COLOR =
            UIManager.getColor("Table.selectionForeground");

    /** Chart font. */
    public static final Font CHART_FONT = new JPanel().getFont();

    // *************************************************************************
    // UNICODE CHARACTERS
    // *************************************************************************

    /** Greek letter lowercase alpha. */
    public static final String ALPHA_LOWER = "\u03B1";

    /** Greek letter lowercase chi. */
    public static final String CHI_LOWER = "\u03C7";

    /** Greek letter lowercase delta. */
    public static final String DELTA_LOWER = "\u03B4";

    /** Greek letter uppercase delta. */
    public static final String DELTA_UPPER = "\u0394";

    /** Lowercase e with an accent. */
    public static final String E_ACCENT_LOWER = "\u00E9";

    /** Greek letter lowercase eta. */
    public static final String ETA_LOWER = "\u03B7";

    /** Greek letter lowercase lambda. */
    public static final String LAMBDA_LOWER = "\u03BB";

    /** Greek letter lowercase mu. */
    public static final String MU_LOWER = "\u03BC";

    /** Greek letter lowercase omega. */
    public static final String OMEGA_LOWER = "\u03C9";

    /** Greek letter uppercase omega. */
    public static final String OMEGA_UPPER = "\u03A9";

    /** Greek letter uppercase phi. */
    public static final String PHI_UPPER = "\u03A6";

    /** Greek letter lowercase sigma. */
    public static final String SIGMA_LOWER = "\u03C3";

    /** Superscripted "2". */
    public static final String SUPERSCRIPT2 = "\u00B2";

    // *************************************************************************
    // CONSTRUCTOR
    // *************************************************************************
    /**
     * Initialize a new instance of a Constants.
     */
    private Constants() {
    }
}
