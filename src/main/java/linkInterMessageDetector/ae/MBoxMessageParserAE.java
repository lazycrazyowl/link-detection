/**
 *
 */
package linkInterMessageDetector.ae;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;
import factory.parser.MBoxParser;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;

/**
 * Annotator that parse the content of a JCas assuming it is an MBox message
 */
public class MBoxMessageParserAE extends JCasAnnotator_ImplBase {

    public static final String PARAM_DEST_DIR = "DestDir";
    @ConfigurationParameter(name = PARAM_DEST_DIR, mandatory = false, defaultValue = "/tmp")
    private String destDir;

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        // Prints the instance ID to the console - this proves the same instance
        // of the SharedModel is used in both Annotator instances.
        //System.out.println(getClass().getSimpleName() + ": " + wordCounter);


        MBoxParser mboxParser = new MBoxParser();
        Message message = null;
        try {
            message = mboxParser.parse(aJCas.getDocumentText());
        } catch (Exception e) {
            System.err.println("Couldn't parse mbox.");
            System.err.println("Aborting.");
            System.err.println("Exception: " + e.getMessage());
            System.err.println("Trace: " + e.getStackTrace());
            System.exit(1);
        }

        System.out.println(messageSummary(message));


    }

    private static String messageSummary(Message message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Subject ")
                .append(message.getSubject())
                .append('\n')
                .append("Date ")
                .append(message.getDate())
                .append('\n')
                .append("MessageId ")
                .append(message.getMessageId())
                .append('\n')
                .append("From name ")
                .append(message.getFromName())
                .append(" email ")
                .append(message.getFromEmail())
                .append('\n')
                .append("To name ")
                .append(message.getToName())
                .append(" email ")
                .append(message.getToEmail())
                .append('\n')
                .append("Recipients ");
        for (RecipientEntry r : message.getRecipients()) {
            sb.append(r.getToName())
                    .append(" ")
                    .append(r.getToEmail())
                    .append(", ");
        }
        sb.append("\n")
                .append("DisplayTo ")
                .append(message.getDisplayTo())
                .append('\n')
                .append("DisplayCc ")
                .append(message.getDisplayCc())
                .append('\n')
                .append("DisplayBcc ")
                .append(message.getDisplayBcc())
                .append('\n');
        for (String p : message.getProperties()) {
            sb.append("Property ")
                    .append(p)
                    .append(":")
                    .append(message.getProperty(p))
                    .append('\n');
        }
        sb.append('\n');
        return sb.toString();
    }
}
