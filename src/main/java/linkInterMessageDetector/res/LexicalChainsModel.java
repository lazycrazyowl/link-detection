package linkInterMessageDetector.res;

import java.util.Set;
import linkInterMessageDetector.datamodel.LexicalChain;

/**
 *
 * @author Hugo “Mog” Mougard <mog@crydee.eu>
 */
public interface LexicalChainsModel {
    public void addChain(String messageId, LexicalChain lexicalChain);
    public void setChains(String messageId, Set<LexicalChain> lexicalChains);
    public Set<LexicalChain> getChains(String messageId);
    public void save(String filename);
}
