package fr.univnantes.atal.nlpdev.linkdetection.ae;

import com.auxilii.msgparser.Message;
import factory.parser.MBoxParser;
import fr.univnantes.atal.nlpdev.linkdetection.types.Id;
import fr.univnantes.atal.nlpdev.linkdetection.util.Util;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.component.ViewCreatorAnnotator;
import org.apache.uima.jcas.JCas;

/**
 * Analysis Engine that extracts body text, ID and date from messages.
 * 
 * Creates a new view called “parsed” to store the body text as document text.
 * Stores the date and ID as annotations in this view as well.
 */
public class MBoxMessageParserAE extends JCasAnnotator_ImplBase {

    @Override
    public void process(JCas aJCas)
            throws AnalysisEngineProcessException {
        JCas parsed = ViewCreatorAnnotator.createViewSafely(aJCas, "parsed");
        MBoxParser mboxParser = new MBoxParser();
        Message message = null;
        try {
            message = mboxParser.parse(aJCas.getDocumentText());
        } catch (Exception e) {
            Util.abort("Couldn't parse mbox.", e);
        }
        String body = message.getBodyText();
        parsed.setDocumentText(body);
        String uid = message.getMessageId();
        if (uid == null) {
            uid = "NULL";
        }
        uid = uid.substring(1, uid.length() - 1);
        Id id = new Id(parsed, 0, body.length() - 1);
        id.setUid(uid);
        id.addToIndexes();
    }
}
