package fr.univnantes.atal.nlpdev.linkdetection.datamodel;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LexicalChain {

    private final Set<String> words = new HashSet<>();
    public final Double THRESHOLD = 0.5;
    private int gap = 0;

    public LexicalChain() {
    }

    public LexicalChain(Set<String> words) {
        this.words.addAll(words);
    }

    public void incGap() {
        gap++;
    }

    public void resetGap() {
        gap = 0;
    }

    public int getGap() {
        return gap;
    }

    public Set<String> getLexicalChain() {
        return Collections.unmodifiableSet(words);
    }

    public void addAll(Set<String> lexicalChains) {
        words.addAll(lexicalChains);
    }

    public void add(String item) {
        words.add(item);
    }

    public Double compare(LexicalChain other) {
        Set<String> intersection = Sets.intersection(words, other.words);
        if (intersection.isEmpty()) {
            return 0.0;
        }
        return intersection.size() * 2d / (words.size() + other.words.size());
    }

    @Override
    public String toString() {
        return words.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.words);
        hash = 83 * hash + Objects.hashCode(this.THRESHOLD);
        return hash;
    }

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
        if (!Objects.equals(this.THRESHOLD, other.THRESHOLD)) {
            return false;
        }
        return true;
    }
}
