package fr.univnantes.atal.nlpdev.linkdetection.ae;

import common.types.Token;
import fr.univnantes.atal.nlpdev.linkdetection.datamodel.LexicalChain;
import fr.univnantes.atal.nlpdev.linkdetection.res.CollocationNetworkModel;
import fr.univnantes.atal.nlpdev.linkdetection.res.LexicalChainsModel;
import fr.univnantes.atal.nlpdev.linkdetection.types.Id;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

/**
 * Analysis Engine that builds a lexical chains resource mapping messages ids to
 * the lexical chains they contain based on a collocation network.
 */
public class LexicalChainsAE extends JCasAnnotator_ImplBase {

    private static final Logger logger = Logger.getLogger(
            LexicalChainsAE.class.getCanonicalName());
    /**
     * Key of the collocation network resource.
     */
    public final static String RES_CN_KEY = "collocationNetwork";
    /**
     * Key of the lexical chains resource.
     */
    public final static String RES_CHAINS_KEY = "lexicalChains";
    /**
     * Key of the gap parameter used to limit the distance between two words.
     */
    public final static String PARAM_GAP_SIZE = "gap";
    /**
     * Key of the threshold parameter used to keep or discard words couples.
     */
    public final static String PARAM_SCORE_THRESHOLD = "threshold";
    /**
     * Key of the minimum length parameter used to filter out lexical chains
     * that are too short.
     */
    public final static String PARAM_MINIMUM_LENGTH = "minimumLength";
    /**
     * Key of the destination filename parameter used to know where to put the
     * serialized resource.
     */
    public final static String PARAM_RESOURCE_DEST_FILENAME = "filename";
    @ExternalResource(key = RES_CN_KEY)
    private CollocationNetworkModel collocationNetwork;
    @ExternalResource(key = RES_CHAINS_KEY)
    private LexicalChainsModel lexicalChains;
    @ConfigurationParameter(name = PARAM_GAP_SIZE,
    mandatory = true,
    description = "Size of the maximum gap in lexical chains")
    private Integer gapSize;
    @ConfigurationParameter(name = PARAM_SCORE_THRESHOLD,
    mandatory = true,
    description = "Minimum collocation score to consider two words related")
    private int threshold;
    @ConfigurationParameter(name = PARAM_MINIMUM_LENGTH,
    mandatory = true,
    description = "Minimum collocation score to consider two words related")
    private int minimumLength;
    @ConfigurationParameter(name = PARAM_RESOURCE_DEST_FILENAME,
    mandatory = false,
    description = "Where to save the lexical chains. Not present = not saved")
    private String outFilename;
    static int counter = 1;

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        Set<LexicalChain> result = new HashSet<>();
        Set<LexicalChain> lexicalChainsSet = new HashSet<>();
        for (Token token : JCasUtil.select(aJCas, Token.class)) {
            String word = token.getCoveredText().toLowerCase(Locale.FRENCH);
            boolean used = false;
            Set<LexicalChain> toRemove = new HashSet<>();
            for (LexicalChain lexicalChain : lexicalChainsSet) {
                Integer gap = lexicalChain.getGap();
                if (gap > gapSize) {
                    if (lexicalChain.getLexicalChain().size()
                            >= minimumLength) {
                        result.add(lexicalChain);
                    }
                    toRemove.add(lexicalChain);
                }
                boolean shouldAdd = false;
                words:
                for (String otherWord : lexicalChain.getLexicalChain()) {
                    if (collocationNetwork.get(word, otherWord) >= threshold) {
                        used = true;
                        shouldAdd = true;
                        break words;
                    }
                }
                if (shouldAdd) {
                    lexicalChain.add(word);
                    lexicalChain.resetGap();
                } else {
                    lexicalChain.incGap();
                }
            }
            for (LexicalChain lexicalChain : toRemove) {
                lexicalChainsSet.remove(lexicalChain);
            }
            if (!used) {
                LexicalChain newLexicalChain = new LexicalChain();
                newLexicalChain.add(word);
                lexicalChainsSet.add(newLexicalChain);
            }
        }
        for (LexicalChain lexicalChain : lexicalChainsSet) {
            if (lexicalChain.getLexicalChain().size()
                    >= minimumLength) {
                result.add(lexicalChain);
            }
        }
        String id = JCasUtil.selectSingle(aJCas, Id.class).getUid();
        logger.debug(
                "processed message "
                + id
                + ": made "
                + counter
                + " lexical chains.");
        lexicalChains.setChains(id, result);
    }

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        if (outFilename != null) {
            lexicalChains.save(outFilename);
        }
    }
}
