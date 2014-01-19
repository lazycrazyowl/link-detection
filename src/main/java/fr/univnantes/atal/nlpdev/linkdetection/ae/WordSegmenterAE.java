package fr.univnantes.atal.nlpdev.linkdetection.ae;

import common.types.Token;
import fr.univnantes.atal.nlpdev.linkdetection.res.StopWordModel;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

/**
 * Annotator that segments the text into words.
 *
 * Filters the stop-words by using a resource. Keeps only the words of length 2
 * or plus and made of letters only.
 */
public class WordSegmenterAE extends JCasAnnotator_ImplBase {

    final static private String WORD_SEPARATOR_PATTERN =
            "[^\\s\\p{Punct}\\d]+", //"[^\\s\\.:,'\\(\\)!]+";
            WORD_PATTERN = "\\p{L}{2,}";
    /**
     * Key of the resource containing the stop words to filter out.
     */
    public final static String RES_KEY = "stopwords";
    @ExternalResource(key = RES_KEY)
    private StopWordModel stopWords;

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        Pattern wordSeparatorPattern = Pattern.compile(WORD_SEPARATOR_PATTERN),
                wordPattern = Pattern.compile(WORD_PATTERN);
        Matcher matcher = wordSeparatorPattern.matcher(aJCas.getDocumentText());
        while (matcher.find()) {
            String word = matcher.group().toLowerCase(Locale.FRENCH);
            if (!stopWords.contains(word)
                    && wordPattern.matcher(word).matches()) {
                new Token(aJCas, matcher.start(), matcher.end()).addToIndexes();
            }
        }
    }
}
