/**
 *
 */
package linkInterMessageDetector.ae;

import common.types.Token;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import linkInterMessageDetector.res.StopWordModel;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

/**
 * Annotator that segments the text into words and filter the one present in a
 * stop word set
 */
public class WordSegmenterAE extends JCasAnnotator_ImplBase {

    final static String WORD_SEPARATOR_PATTERN = "[^\\s\\p{Punct}\\d]+", //"[^\\s\\.:,'\\(\\)!]+";
            WORD_PATTERN = "\\p{L}{2,}";
    // TODO add http://www.w3.org/TR/html-markup/elements.html
    public final static String RES_KEY = "aKey";
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
