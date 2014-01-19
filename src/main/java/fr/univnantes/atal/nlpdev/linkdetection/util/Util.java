package fr.univnantes.atal.nlpdev.linkdetection.util;

public class Util {

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
