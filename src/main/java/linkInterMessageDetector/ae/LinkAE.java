package linkInterMessageDetector.ae;

import java.util.Set;
import linkInterMessageDetector.datamodel.LexicalChain;
import linkInterMessageDetector.res.LexicalChainsModel;
import linkInterMessageDetector.res.ThreadModel;
import my.types.Id;
import my.types.Link;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

public class LinkAE extends JCasAnnotator_ImplBase {

    public final static String RES_THREADS_KEY = "LinkKey1",
            RES_CHAINS_KEY = "LinkKey2";
    @ExternalResource(key = RES_THREADS_KEY)
    private ThreadModel threads;
    @ExternalResource(key = RES_CHAINS_KEY)
    private LexicalChainsModel lexicalChains;

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        String id = JCasUtil.selectSingle(aJCas, Id.class).getUid(),
                threadId = threads.getThread(id);
        if (threadId == null) {
            return;
        }
        Set<LexicalChain> chains = lexicalChains.getChains(id);
        if (chains.isEmpty()) {
            return;
        }
        Set<String> messages = threads.getMessages(threadId);
        if (messages.isEmpty()) {
            return;
        }
        String bestId = null;
        Double bestScore = Double.NEGATIVE_INFINITY;
        for (String message : messages) {
            Set<LexicalChain> otherChains = lexicalChains.getChains(message);
            if (otherChains.isEmpty()) {
                continue;
            }
            Double score = compareChains(chains, otherChains);
            if (score > bestScore) {
                bestId = message;
            }
        }
        Link link = new Link(aJCas, 0, aJCas.getDocumentText().length() - 1);
        link.setUid(bestId);
        link.addToIndexes();
    }

    private Double compareChains(
            Set<LexicalChain> chains,
            Set<LexicalChain> otherChains) {
        double score = 0d;
        for (LexicalChain chain1 : chains) {
            for (LexicalChain chain2 : otherChains) {
                score += chain1.compare(chain2);
            }
        }
        return score / (chains.size() * otherChains.size());
    }
}
