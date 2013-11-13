package edu.missouristate.mote;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;

/**
 * Constants used by Mote.
 */
public final class Constants {

    // *************************************************************************
    // GENERAL
    // *************************************************************************
    public static final int DISPLAY_DIGITS = 7;
    public static final double EQUAL_DELTA = Math.pow(10, -(DISPLAY_DIGITS + 1));
    // *************************************************************************
    // EFFECT SIZES
    // *************************************************************************
    public static final double MIN_CONFIDENCE = 0.001;
    public static final double MAX_CONFIDENCE = 0.999;
    public static final double MIN_DF = 1;
    public static final double MIN_SS = MIN_DF + 1;
    /**
     * Minimum density (y value) that should be included in the PDF curve as
     * a fraction of the PDF maximum value
     */
    public static final double MIN_PDF_PROB = 0.01;
    /**
     * Maximum difference between the target and actual CDF values
     */
    public static final double PRECISION = 0.0000001;
    /**
     * T value (x axis) increments on the PDF curve
     */
    public static final double T_STEP = 0.05;
    /**
     * F value (x axis) increments on the PDF curve
     */
    public static final double F_STEP = 0.05;
    /**
     * X value increments on the PDF curve
     */
    public static final double X_STEP = 0.05;
    /**
     * Maximum number of iterations before converging on non-centrality
     */
    public static final int NC_MAX_ITER = 100;
    /**
     * Minimum value for R.
     */
    public static final double MIN_R = -1 + PRECISION;
    /**
     * Maximum value for R.
     */
    public static final double MAX_R = 1 - PRECISION;
    // *************************************************************************
    // APPEARANCE
    // *************************************************************************
    public static final String INPUT_CATEGORY = "Inputs";
    public static final String DERIVED_CATEGORY = "Derived";
    public static final String OUTPUT_CATEGORY = "Results";
    public static final Color NAME_BG_COLOR = new Color(240, 240, 240);
    public static final Color NAME_FG_COLOR = new Color(0, 0, 0);
    public static final Color VALUE_BG_COLOR = new Color(255, 255, 255);
    public static final Color VALUE_FG_COLOR = new Color(0, 0, 0);
    public static final Color CATEGORY_BG_COLOR = new Color(0, 0, 100);
    public static final Color CATEGORY_FG_COLOR = new Color(255, 255, 255);
    public static final Color SELECT_FG_COLOR = new Color(0, 0, 0);
    public static final Color SELECT_BG_COLOR = new Color(245, 245, 255);
    public static final Font CHART_FONT = new JPanel().getFont();
    // *************************************************************************
    // UNICODE CHARACTERS
    // *************************************************************************
    public static final String ALPHA_LOWER = "\u03B1";
    public static final String CHI_LOWER = "\u03C7";
    public static final String DELTA_LOWER = "\u03B4";
    public static final String DELTA_UPPER = "\u0394";
    public static final String ETA_LOWER = "\u03B7";
    public static final String LAMBDA_LOWER = "\u03BB";
    public static final String MU_LOWER = "\u03BC";
    public static final String OMEGA_LOWER = "\u03C9";
    public static final String OMEGA_UPPER = "\u03A9";
    public static final String PHI_UPPER = "\u03A6";
    public static final String SIGMA_LOWER = "\u03C3";
    public static final String SUPERSCRIPT2 = "\u00B2";

    // *************************************************************************
    // CONSTRUCTOR
    // *************************************************************************
    /**
     * Initialize a new instance of a Settings.
     */
    private Constants() {
    }
}