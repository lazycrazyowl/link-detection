package linkInterMessageDetector.wf;

import linkInterMessageDetector.ae.LexicalChainsAE;
import linkInterMessageDetector.ae.MBoxMessageParserAE;
import linkInterMessageDetector.ae.WordSegmenterAE;
import linkInterMessageDetector.cr.MboxReaderCR;
import linkInterMessageDetector.res.CollocationNetworkModel_Impl;
import linkInterMessageDetector.res.LexicalChainsModel_Impl;
import linkInterMessageDetector.res.StopWordModel_Impl;
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

public class LexicalChainsBuilderWF {

    public static void main(String[] args) throws Exception {

        ExternalResourceDescription stopWordsResourceDesc =
                createExternalResourceDescription(
                StopWordModel_Impl.class,
                "file:resourceManagement/data/stopwords-fr.txt");

        ExternalResourceDescription threadResourceDesc =
                createExternalResourceDescription(
                ThreadModel_Impl.class,
                "file:data/thread-messageId.digest");

        ExternalResourceDescription chainsResourceDesc =
                createExternalResourceDescription(
                LexicalChainsModel_Impl.class,
                "");

        ExternalResourceDescription collocationNetworkResourceDesc =
                createExternalResourceDescription(
                CollocationNetworkModel_Impl.class,
                "file:output/cn.csv");

        AnalysisEngineDescription parserAe = createEngineDescription(
                MBoxMessageParserAE.class);

        AnalysisEngineDescription segmenterAe = createEngineDescription(
                WordSegmenterAE.class,
                WordSegmenterAE.RES_KEY, stopWordsResourceDesc);

        AnalysisEngineDescription chainsAe = createEngineDescription(
                LexicalChainsAE.class,
                LexicalChainsAE.RES_CN_KEY, collocationNetworkResourceDesc,
                LexicalChainsAE.RES_CHAINS_KEY, chainsResourceDesc,
                LexicalChainsAE.PARAM_GAP_SIZE, 20,
                LexicalChainsAE.PARAM_SCORE_THRESHOLD, 10,
                LexicalChainsAE.PARAM_MINIMUM_LENGTH, 2,
                LexicalChainsAE.PARAM_RESOURCE_DEST_FILENAME, "output/lc.txt");

        CollectionReaderDescription crd = createReaderDescription(
                MboxReaderCR.class,
                MboxReaderCR.PARAM_MBOX_SRCPATH, "data/ubuntu-fr-utf8.mbox",
                MboxReaderCR.PARAM_LANGUAGE, "fr",
                MboxReaderCR.PARAM_ENCODING, "utf-8");

        AggregateBuilder builder = new AggregateBuilder();
        builder.add(parserAe);
        builder.add(segmenterAe, CAS.NAME_DEFAULT_SOFA, "parsed");
        builder.add(chainsAe, CAS.NAME_DEFAULT_SOFA, "parsed");
        SimplePipeline.runPipeline(
                crd,
                builder.createAggregateDescription());
    }
}
