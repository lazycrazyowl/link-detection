package linkInterMessageDetector.ae;

import common.types.Token;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import linkInterMessageDetector.datamodel.LexicalChain;
import linkInterMessageDetector.res.CollocationNetworkModel;
import linkInterMessageDetector.res.LexicalChainsModel;
import my.types.Id;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

public class LexicalChainsAE extends JCasAnnotator_ImplBase {

    public final static String RES_CN_KEY = "LexicalChainsKey1",
            RES_CHAINS_KEY = "LexicalChainsKey2",
            PARAM_GAP_SIZE = "LexicalChainsKey3",
            PARAM_SCORE_THRESHOLD = "LexicalChainsKey4",
            PARAM_MINIMUM_LENGTH = "LexicalChainsKey5";
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
        System.out.println("\r" + id + ": " + counter++ + ": " + result.size());
        lexicalChains.setChains(id, result);
    }
}
