/**
 *
 */
package linkInterMessageDetector.datamodel;

import java.util.HashSet;
import java.util.Set;

/**
 * @author hernandez
 *
 */
public class LexicalChain {

    private Set<String> lexicalChains;
    public Double THRESHOLD = 0.5;

    public LexicalChain() {
        super();
        this.lexicalChains = new HashSet<>();
    }

    public LexicalChain(Set<String> lexicalChains) {
        super();
        this.lexicalChains = new HashSet<>(lexicalChains);
    }

    public Set<String> getLexicalChain() {
        return this.lexicalChains;
    }

    public void setLexicalChain(Set<String> lexicalChains) {
        getLexicalChain().addAll(lexicalChains);
    }

    public void addItem(String item) {
        getLexicalChain().add(item);
    }

    public Double compare(LexicalChain otherLexicalChain) {
        Set<String> lexicalChainIntersection = new HashSet<>(getLexicalChain());
        lexicalChainIntersection.retainAll(otherLexicalChain.getLexicalChain());
        if (lexicalChainIntersection.isEmpty()) {
            return 0.0;
        }
        return (double) ((double) lexicalChainIntersection.size()
                / (double) ((getLexicalChain().size()
                + otherLexicalChain.getLexicalChain().size())
                / 2));
    }

    public Boolean isSimilar(LexicalChain otherLexicalChain, Double threshold) {

        if (this.compare(otherLexicalChain) > threshold) {
            return true;
        }
        return false;
    }

    public Boolean isSimilar(LexicalChain otherLexicalChain) {

        return isSimilar(otherLexicalChain, THRESHOLD);
    }

    @Override
    public String toString() {
        return getLexicalChain().toString();
    }
}
