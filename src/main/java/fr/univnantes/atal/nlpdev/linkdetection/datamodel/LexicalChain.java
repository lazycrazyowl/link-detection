package fr.univnantes.atal.nlpdev.linkdetection.datamodel;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Model class of a lexical chain.
 *
 * Holds a method to compare two chains with a Dice coefficient.
 */
public class LexicalChain {

    private final Set<String> words = new HashSet<>();
    private int gap = 0;

    /**
     * Constructor for empty lexical chains.
     */
    public LexicalChain() {
    }

    /**
     * Constructor to turn a set of words into a lexical chain.
     *
     * @param words
     */
    public LexicalChain(Set<String> words) {
        this.words.addAll(words);
    }

    /**
     * Setter to manage the current gap during lexical chain creation.
     */
    public void incGap() {
        gap++;
    }

    /**
     * Setter to manage the current gap during lexical chain creation.
     */
    public void resetGap() {
        gap = 0;
    }

    /**
     * Getter to manage the current gap during lexical chain creation.
     *
     * @return the current size of the gap during the lexical chain creation.
     */
    public int getGap() {
        return gap;
    }

    /**
     * Getter for the lexical chain.
     *
     * @return the set of words of the lexical chain.
     */
    public Set<String> getLexicalChain() {
        return Collections.unmodifiableSet(words);
    }

    /**
     * Setter for the lexical chain.
     *
     * Several words at a time.
     *
     * @param words the words to add the lexical chain.
     */
    public void addAll(Set<String> words) {
        words.addAll(words);
    }

    /**
     * Setter for the lexical chain.
     *
     * One word at a time.
     *
     * @param word
     */
    public void add(String word) {
        words.add(word);
    }

    /**
     * Comparison method between two chains.
     *
     * @param other the chain to be compared to.
     * @return a score between 0 and 1 indicating the similarity between the two
     * chains.
     */
    public Double compare(LexicalChain other) {
        Set<String> intersection = Sets.intersection(words, other.words);
        if (intersection.isEmpty()) {
            return 0.0;
        }
        return intersection.size() * 2d / (words.size() + other.words.size());
    }

    /**
     * String representation of a lexical chain.
     *
     * Uses the string representation of the set of underlying words.
     *
     * @return the representation of the lexical chain.
     */
    @Override
    public String toString() {
        return words.toString();
    }

    /**
     * Hashing of a lexical chain.
     *
     * Internally uses the underlying set of words.
     *
     * @return a hash of the lexical chain.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.words);
        return hash;
    }

    /**
     * Equality between two lexical chains.
     *
     * Internally compares the two underlying set of words.
     *
     * @param obj the object to be compared to.
     * @return true if the two lexical chains are equals, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LexicalChain other = (LexicalChain) obj;
        if (!Objects.equals(this.words, other.words)) {
            return false;
        }
        return true;
    }
}
