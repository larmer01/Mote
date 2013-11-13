MOTE - Measure of the Effect
============================

Overview
--------
Mote is an effect size program that calculates various effect sizes and their
confidence intervals. It's designed to be easy to use for everyone from
students to established researchers.

The program calculates the following measures based on the normal and
non-central t distributions. These have been tested, but are still considered
beta quality:
* Cohen’s d
  * Z
  * Single Sample t
  * Dependent t (Averages)
  * Dependent t (SD Diff)
  * Independent t
  * Pearson’s r
* Hedges’s g
  * Independent t
* Glass’s Delta
  * Independent t

The following 1-way ANOVA tests based on the non-central F distribution are
currently under development and should be considered alpha quality work:
* R-squared
* Delta R-squared
* Eta-squared
* Partial eta-squared
* Omega-squared
* Partial omega-squared
* Intraclass correlation

Additionally, confidence intervals can be found for:
* Odds ratio

Installation
------------
Download the JAR file and save it to a location of your choice. Then just
double-click the file to run it. Download location:
    http://aggieerin.com/mote/Mote.1.1.jar

Note that you must have at least Java 6 installed. If you do not have a Java
runtime installed, you can download one from Oracle:
    http://java.com/inc/BrowserRedirect1.jsp

The full user’s guide is available via the _Help --> Users’s Guide_ menu.

Known Issues
------------
* Large sample sizes can cause the PDF for t distributions to flatten out
  making the graph un-viewable. The numeric values are still correct.
* Validation is still underway for the ANOVA tests and the results should be
  considered alpha quality.
* When using the mouse to select cells for entry, the cursor can sometimes
  become “stuck” and Mote will be unresponsive to key presses. Clicking on the
  _Name_ header will free the cursor.

Contact
-------
Website: http://aggieerin.com/mote/

E-mail: Tim Larmer (larmer01@gmail.com) or Erin Bunchanan (erinbuchanan@missouristate.edu)
