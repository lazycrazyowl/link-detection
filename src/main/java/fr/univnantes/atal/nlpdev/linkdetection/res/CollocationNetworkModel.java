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
     */
    public Double get(String word, String collocation);

    /**
     * Increments the occurrence counter of a given word.
     */
    public void inc(String key, String collocation);

    /**
     * Increments the occurrence counter of a given word.
     */
    public void inc(String key, String collocation, Double howMuch);

    /**
     * Decrements the occurrence counter of a given word.
     */
    public void dec(String key, String collocation);

    /**
     * Gets the number of words.
     */
    public Integer size();

    /**
     * Gets the number of collocations for a word.
     */
    public Integer size(String word);

    /**
     * Gets a set of all the words.
     */
    public Set<String> keySet();

    /**
     * Gets a set of all the allocations of a word.
     */
    public Set<String> keySet(String word);

    /**
     * Gets the sum of all collocations occurrences.
     */
    public Double getTotalCounter();

    /**
     * Gets the sum of all collocations occurrences for a given word.
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
     */
    public void save(String filename);
}
