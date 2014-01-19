/**
 *
 */
package fr.univnantes.atal.nlpdev.linkdetection.ae;

import common.types.Token;
import fr.univnantes.atal.nlpdev.linkdetection.res.CollocationNetworkModel;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

/**
 * Analysis Engine that builds a collocation network from a list of documents
 * and saves it to a file.
 */
public class CollocationNetworkBuilderAE extends JCasAnnotator_ImplBase {

    private static final Logger logger = Logger.getLogger(
            CollocationNetworkBuilderAE.class.getCanonicalName());
    /**
     * Key of the collocation network resource.
     */
    public final static String RES_KEY = "collocationNetwork";
    /**
     * Key of the destination filename parameter used to know where to put the
     * serialized resource.
     */
    public final static String PARAM_RESOURCE_DEST_FILENAME = "filename";
    /**
     * Key of the window size parameter used to know the maximum window in which
     * two words can be considered collocate.
     */
    public final static String PARAM_WINDOW_SIZE = "windowSize";
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
                logger.error("Couldn't build collocation network.");
                System.exit(1);
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
                logger.error("Couldn't build collocation network.");
                System.exit(1);
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
