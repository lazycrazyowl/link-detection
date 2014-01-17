/**
 *
 */
package linkInterMessageDetector.wf;

import linkInterMessageDetector.ae.CollocationNetworkBuilderAE;
import linkInterMessageDetector.ae.WordSegmenterAE;
import linkInterMessageDetector.cr.ZimReaderCR;
import linkInterMessageDetector.res.CollocationNetworkModel_Impl;
import linkInterMessageDetector.res.StopWordModel_Impl;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;

public class CollocationNetworkBuilderWF {

    public static void main(String[] args) throws Exception {
        ExternalResourceDescription stopWordsResourceDesc =
                createExternalResourceDescription(
                StopWordModel_Impl.class,
                "file:resourceManagement/data/stopwords-fr.txt");

        ExternalResourceDescription collocationNetworkResourceDesc =
                createExternalResourceDescription(
                CollocationNetworkModel_Impl.class,
                "");

        AnalysisEngineDescription aed0 = createEngineDescription(
                WordSegmenterAE.class,
                WordSegmenterAE.RES_KEY,
                stopWordsResourceDesc);

        AnalysisEngineDescription aed3 = createEngineDescription(
                CollocationNetworkBuilderAE.class,
                CollocationNetworkBuilderAE.RES_KEY,
                collocationNetworkResourceDesc,
                CollocationNetworkBuilderAE.PARAM_RESOURCE_DEST_FILENAME,
                "output/cn.csv",
                CollocationNetworkBuilderAE.PARAM_WINDOW_SIZE,
                3);

        AnalysisEngineDescription aaed = createEngineDescription(aed0, aed3);

        CollectionReaderDescription crd = createReaderDescription(ZimReaderCR.class,
                ZimReaderCR.PARAM_ZIM_SRCPATH, "data/ubuntudoc_fr_01_2009.zim",
                ZimReaderCR.PARAM_LANGUAGE, "fr",
                ZimReaderCR.PARAM_ENCODING, "utf-8");
        SimplePipeline.runPipeline(crd, aaed);
    }
}
