package fr.univnantes.atal.nlpdev.linkdetection.res;

/**
 * Interface which models a simple set of stop words.
 */
public interface StopWordModel {

    /**
     * Test to know whether a word is a stop-word or not.
     *
     * @param key the word to test.
     * @return true if the word is a stop-word, false otherwise.
     */
    public Boolean contains(String key);
}