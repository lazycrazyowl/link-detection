package fr.univnantes.atal.nlpdev.linkdetection;

import fr.univnantes.atal.nlpdev.linkdetection.ae.LinkAE;
import fr.univnantes.atal.nlpdev.linkdetection.ae.LinkConsumerAE;
import fr.univnantes.atal.nlpdev.linkdetection.ae.MBoxMessageParserAE;
import fr.univnantes.atal.nlpdev.linkdetection.cr.MboxReaderCR;
import fr.univnantes.atal.nlpdev.linkdetection.res.LexicalChainsModel_Impl;
import fr.univnantes.atal.nlpdev.linkdetection.res.ThreadModel_Impl;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;

/**
 * Workflow that detects links between messages when we already have lexical
 * chains information about them and about the way they are threaded.
 */
public class LinksDetectionWF {

    /**
     * Entry point
     *
     * @param args arguments passed to the program.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        ExternalResourceDescription threadResourceDesc =
                createExternalResourceDescription(
                ThreadModel_Impl.class,
                "file:data/thread-messageId.digest");

        ExternalResourceDescription chainsResourceDesc =
                createExternalResourceDescription(
                LexicalChainsModel_Impl.class,
                "file:output/lc.txt");

        AnalysisEngineDescription parserAe = createEngineDescription(
                MBoxMessageParserAE.class);

        AnalysisEngineDescription linkAe = createEngineDescription(
                LinkAE.class,
                LinkAE.RES_THREADS_KEY, threadResourceDesc,
                LinkAE.RES_CHAINS_KEY, chainsResourceDesc);

        AnalysisEngineDescription linkConsumerAe = createEngineDescription(
                LinkConsumerAE.class,
                LinkConsumerAE.PARAM_DEST_FILENAME, "output/result.txt");

        CollectionReaderDescription crd = createReaderDescription(
                MboxReaderCR.class,
                MboxReaderCR.PARAM_MBOX_SRCPATH, "data/ubuntu-fr.mbox",
                MboxReaderCR.PARAM_LANGUAGE, "fr",
                MboxReaderCR.PARAM_ENCODING, "iso-8859-1");

        AggregateBuilder builder = new AggregateBuilder();
        builder.add(parserAe);
        builder.add(linkAe, CAS.NAME_DEFAULT_SOFA, "parsed");
        builder.add(linkConsumerAe, CAS.NAME_DEFAULT_SOFA, "parsed");
        SimplePipeline.runPipeline(
                crd,
                builder.createAggregateDescription());
    }
}