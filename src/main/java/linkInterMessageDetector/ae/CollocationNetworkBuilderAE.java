/**
 *
 */
package linkInterMessageDetector.ae;

import common.types.Token;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import linkInterMessageDetector.res.CollocationNetworkModel;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

/**
 * Annotator that increments the occurrence counter of word forms
 */
public class CollocationNetworkBuilderAE extends JCasAnnotator_ImplBase {

    public final static String RES_KEY = "CollocationNetworkBuilderKey1",
            PARAM_RESOURCE_DEST_FILENAME = "CollocationNetworkBuilderKey2",
            PARAM_WINDOW_SIZE = "CollocationNetworkBuilderKey3";
    @ExternalResource(key = RES_KEY)
    private CollocationNetworkModel collocationNetwork;
    @ConfigurationParameter(name = PARAM_RESOURCE_DEST_FILENAME,
    mandatory = true,
    description = "Where to write our csv")
    private String rFilename;
    @ConfigurationParameter(name = PARAM_WINDOW_SIZE,
    mandatory = true,
    description = "Size of the window in which words are considered colocated")
    private int windowSize;

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        Deque<String> queue = new LinkedList<>();
        Iterator<Token> tokens = JCasUtil.select(aJCas, Token.class).iterator();
        for (int i = 1; i < windowSize; i++) {
            if (tokens.hasNext()) {
                queue.addLast(tokens
                        .next()
                        .getCoveredText()
                        .toLowerCase(Locale.FRENCH));
            }
        }
        while (tokens.hasNext()) {
            String word =
                    tokens.next().getCoveredText().toLowerCase(Locale.FRENCH);
            queue.addLast(word);
            Iterator<String> it = queue.iterator();
            if (!it.hasNext()) {
                System.err.println("Couldn't build collocation network.");
                System.err.println("Aborting.");
                System.err.println("Reason: empty window at a wrong point.");
            } else {
                String current = it.next();
                while (it.hasNext()) {
                    collocationNetwork.inc(current, it.next());
                }
            }
            queue.removeFirst();
        }
        while (!queue.isEmpty()) {
            Iterator<String> it = queue.iterator();
            if (!it.hasNext()) {
                System.err.println("Couldn't build collocation network.");
                System.err.println("Aborting.");
                System.err.println("Reason: empty window at a wrong point.");
            } else {
                String current = it.next();
                while (it.hasNext()) {
                    collocationNetwork.inc(current, it.next());
                }
            }
            queue.removeFirst();
        }
    }

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        collocationNetwork.save(rFilename);
    }
}
