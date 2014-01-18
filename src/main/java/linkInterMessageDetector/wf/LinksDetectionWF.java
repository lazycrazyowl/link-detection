package linkInterMessageDetector.wf;

import linkInterMessageDetector.ae.LinkAE;
import linkInterMessageDetector.ae.LinkConsumerAE;
import linkInterMessageDetector.ae.MBoxMessageParserAE;
import linkInterMessageDetector.cr.MboxReaderCR;
import linkInterMessageDetector.res.LexicalChainsModel_Impl;
import linkInterMessageDetector.res.ThreadModel_Impl;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;

public class LinksDetectionWF {

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
                MboxReaderCR.PARAM_MBOX_SRCPATH, "data/ubuntu-fr-utf8.mbox",
                MboxReaderCR.PARAM_LANGUAGE, "fr",
                MboxReaderCR.PARAM_ENCODING, "utf-8");

        AggregateBuilder builder = new AggregateBuilder();
        builder.add(parserAe);
        builder.add(linkAe, CAS.NAME_DEFAULT_SOFA, "parsed");
        builder.add(linkConsumerAe, CAS.NAME_DEFAULT_SOFA, "parsed");
        SimplePipeline.runPipeline(
                crd,
                builder.createAggregateDescription());
    }
}