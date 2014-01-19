package fr.univnantes.atal.nlpdev.linkdetection.util;

/**
 * Holder class for misc methods.
 */
public class Util {

    /**
     * Helper method to quit on an error.
     *
     * @param message the custom message to print before aborting.
     * @param ex the exception to display, null if no display wanted.
     */
    public static void abort(String message, Exception ex) {
        System.err.println(message);
        System.err.println("Aborting.");
        if (ex != null) {
            System.err.println("Exception was: " + ex.getLocalizedMessage());
            System.err.println("Trace was:");
            ex.printStackTrace(System.err);
        }
        System.exit(1);
    }
}
