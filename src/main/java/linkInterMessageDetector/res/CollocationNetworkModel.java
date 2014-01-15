/**
 *
 */
package linkInterMessageDetector.res;

import java.util.Set;

/**
 * Interface which models the Collocation Network ; Declare the methods handled
 * by the annotators to access the resource
 */
public interface CollocationNetworkModel {

    /**
     * Get the number of occurrences of a given word and its collocation
     */
    public Double get(String word, String collocation);

    /**
     * Increment the occurrence counter of a given word
     */
    public void inc(String key, String collocation);

    /**
     * Increment the occurrence counter of a given word
     */
    public void inc(String key, String collocation, Double howMuch);

    /**
     * Decrement the occurrence counter of a given word
     */
    public void dec(String key, String collocation);

    /**
     * Get the number of words
     */
    public Integer size();

    /**
     * Get the number of collocations for a word
     */
    public Integer size(String word);

    /**
     * Get a set of all the words
     */
    public Set<String> keySet();

    /**
     * Get a set of all the allocations of a word
     */
    public Set<String> keySet(String word);

    /**
     * Get the sum of all collocations occurrences
     */
    public Double getTotalCounter();

    /**
     * Get the sum of all collocations occurrences for a given word
     */
    public Double getTotalCounter(String word);

    /**
     * Display the content of the current word counter
     */
    public void echo();

    /**
     * Save the content of the resource as a CSV file a line per collocation:
     * word, co-occurring word and counter as columns with tab character as
     * separator use the MiscUtil.writeToFS(textString,filenameString)
     */
    public void save(String filename);
}
