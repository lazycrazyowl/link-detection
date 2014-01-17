package linkInterMessageDetector.res;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import linkInterMessageDetector.datamodel.LexicalChain;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 *
 * @author Hugo “Mog” Mougard <mog@crydee.eu>
 */
public class LexicalChainsModel_Impl
        implements LexicalChainsModel, SharedResourceObject {

    private final Map<String, Set<LexicalChain>> chains = new HashMap<>();

    @Override
    public void load(DataResource aData)
            throws ResourceInitializationException {
    }

    @Override
    public void addChain(String messageId, LexicalChain lexicalChain) {
        if (!chains.containsKey(messageId)) {
            chains.put(messageId, new HashSet<LexicalChain>());
        }
        chains.get(messageId).add(lexicalChain);
    }

    @Override
    public void setChains(String messageId, Set<LexicalChain> lexicalChains) {
        chains.put(messageId, new HashSet<>(lexicalChains));
    }

    @Override
    public Set<LexicalChain> getChains(String messageId) {
        Set<LexicalChain> result = chains.get(messageId);
        if (result == null) {
            result = new HashSet<>();
        }
        return Collections.unmodifiableSet(result);
    }
}
