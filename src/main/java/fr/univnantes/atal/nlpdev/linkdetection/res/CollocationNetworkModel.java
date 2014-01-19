package fr.univnantes.atal.nlpdev.linkdetection.res;

import java.util.Set;

/**
 * Interface which models the Collocation Network.
 *
 * Declares the methods handled by the annotators to access the resource.
 */
public interface CollocationNetworkModel {

    /**
     * Gets the number of occurrences of a given word and its collocation.
     *
     * @param word first word of the collocation.
     * @param collocation second word of the collocation.
     * @return the collocation score between word and collocation.
     */
    public Double get(String word, String collocation);

    /**
     * Increments the occurrence counter of a given collocation.
     *
     * @param key first word of the collocation.
     * @param collocation second word of the collocation.
     */
    public void inc(String key, String collocation);

    /**
     * Increments the occurrence counter of a given collocation.
     *
     * @param key first word of the collocation.
     * @param collocation second word of the collocation.
     * @param howMuch how much to increase the collocation between word and
     * collocation.
     */
    public void inc(String key, String collocation, Double howMuch);

    /**
     * Decrements the occurrence counter of a given word.
     *
     * @param key first word of the collocation.
     * @param collocation second word of the collocation.
     */
    public void dec(String key, String collocation);

    /**
     * Gets the number of words.
     *
     * @return the number of words in the collocation network.
     */
    public Integer size();

    /**
     * Gets the number of collocations for a word.
     *
     * @param word the word for which we want the number of collocations.
     * @return the number of collocations of word in the collocation network.
     */
    public Integer size(String word);

    /**
     * Gets a set of all the words.
     *
     * @return a set of all the words in the collocation network.
     */
    public Set<String> keySet();

    /**
     * Gets a set of all the collocations of a word.
     *
     * @param word the word for which we want the set of collocations.
     * @return a set of all the collocations of word in the collocation network.
     */
    public Set<String> keySet(String word);

    /**
     * Gets the sum of all collocations occurrences.
     *
     * @return the sum of all collocation scores.
     */
    public Double getTotalCounter();

    /**
     * Gets the sum of all collocations occurrences for a given word.
     *
     * @param word the word for which we want the total of scores.
     * @return the sum of all collocation scores involving word.
     */
    public Double getTotalCounter(String word);

    /**
     * Displays the content of the current word counter.
     */
    public void echo();

    /**
     * Saves the content of the resource as a CSV file.
     *
     * One line per collocation: word, co-occurring word and counter as columns
     * with tab character as separator.
     *
     * @param filename the name of the file used to save the collocation
     * network.
     */
    public void save(String filename);
}
