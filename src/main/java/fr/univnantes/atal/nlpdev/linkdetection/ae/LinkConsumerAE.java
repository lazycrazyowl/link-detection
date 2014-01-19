package fr.univnantes.atal.nlpdev.linkdetection.ae;

import fr.univnantes.atal.nlpdev.linkdetection.types.Id;
import fr.univnantes.atal.nlpdev.linkdetection.types.Link;
import fr.univnantes.atal.nlpdev.linkdetection.util.Util;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * Analysis Engine that writes the result of the message linking process as a
 * text file.
 */
public class LinkConsumerAE extends JCasAnnotator_ImplBase {

    /**
     * Key of the output filename parameter.
     */
    public final static String PARAM_DEST_FILENAME = "destinationFilename";
    @ConfigurationParameter(name = PARAM_DEST_FILENAME,
    mandatory = true,
    description = "Where to write our result file.")
    private String outFilename;
    private PrintWriter pw;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        try {
            pw = new PrintWriter(outFilename);
        } catch (FileNotFoundException ex) {
            Util.abort("Couldn't open output file.", ex);
        }
    }

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        if (JCasUtil.exists(aJCas, Link.class)) {
            String id = JCasUtil.selectSingle(aJCas, Id.class).getUid(),
                    link = JCasUtil.selectSingle(aJCas, Link.class).getUid();
            pw.println(link + ":" + id);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        pw.close();
    }
}
