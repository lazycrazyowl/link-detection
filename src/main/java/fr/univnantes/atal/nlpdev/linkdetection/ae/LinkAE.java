package fr.univnantes.atal.nlpdev.linkdetection.ae;

import fr.univnantes.atal.nlpdev.linkdetection.datamodel.LexicalChain;
import fr.univnantes.atal.nlpdev.linkdetection.res.LexicalChainsModel;
import fr.univnantes.atal.nlpdev.linkdetection.res.ThreadModel;
import fr.univnantes.atal.nlpdev.linkdetection.types.Id;
import fr.univnantes.atal.nlpdev.linkdetection.types.Link;
import java.util.HashSet;
import java.util.Set;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

/**
 * Analysis Engine that links the current message to its most probable parent.
 *
 * Requires the lexical chains model and the thread info to be loaded.
 */
public class LinkAE extends JCasAnnotator_ImplBase {

    /**
     * Key of the threads info resource.
     */
    public final static String RES_THREADS_KEY = "threads";
    /**
     * Key of the lexical chains resource.
     */
    public final static String RES_CHAINS_KEY = "chains";
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
            if (message.equals(id)) {
                continue;
            }
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
        Set<LexicalChain> biggest, smallest;
        if (chains.size() > otherChains.size()) {
            biggest = chains;
            smallest = otherChains;
        } else {
            biggest = otherChains;
            smallest = chains;
        }
        Set<LexicalChain> o = new HashSet<>(biggest);
        for (LexicalChain chain1 : smallest) {
            LexicalChain best = null;
            double bestScore = Double.NEGATIVE_INFINITY;
            for (LexicalChain chain2 : o) {
                Double comparison = chain1.compare(chain2);
                if (comparison > bestScore) {
                    best = chain2;
                    bestScore = comparison;
                }
            }
            o.remove(best);
            score += bestScore;
        }
        return score / smallest.size();
    }
}
