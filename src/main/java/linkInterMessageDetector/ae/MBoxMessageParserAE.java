package linkInterMessageDetector.ae;

import com.auxilii.msgparser.Message;
import common.util.MiscUtil;
import factory.parser.MBoxParser;
import my.types.Id;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.component.ViewCreatorAnnotator;
import org.apache.uima.jcas.JCas;

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
            MiscUtil.abort("Couldn't parse mbox.", e);
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
