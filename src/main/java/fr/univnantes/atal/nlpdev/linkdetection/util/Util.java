package fr.univnantes.atal.nlpdev.linkdetection.util;

/**
 * Holder class for misc methods.
 */
public class Util {

    /**
     * Helper method to quit on an error.
     *
     * @param ex the exception to display, null if no display wanted.
     */
    public static void abort(Exception ex) {
        System.err.println(ex.getLocalizedMessage());
        ex.printStackTrace(System.err);
        System.exit(1);
    }
}
