package fr.univnantes.atal.nlpdev.linkdetection;

import fr.univnantes.atal.nlpdev.linkdetection.ae.CollocationNetworkBuilderAE;
import fr.univnantes.atal.nlpdev.linkdetection.ae.WordSegmenterAE;
import fr.univnantes.atal.nlpdev.linkdetection.cr.ZimReaderCR;
import fr.univnantes.atal.nlpdev.linkdetection.res.CollocationNetworkModel_Impl;
import fr.univnantes.atal.nlpdev.linkdetection.res.StopWordModel_Impl;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;

/**
 * Workflow that builds a collocation network from a ZIM file.
 */
public class CollocationNetworkBuilderWF {

    /**
     * Entry point.
     *
     * @param args arguments passed to the program.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ExternalResourceDescription stopWordsResourceDesc =
                createExternalResourceDescription(
                StopWordModel_Impl.class,
                "file:resourceManagement/data/stopwords-fr.txt");

        ExternalResourceDescription collocationNetworkResourceDesc =
                createExternalResourceDescription(
                CollocationNetworkModel_Impl.class,
                "");

        AnalysisEngineDescription segmenterAe = createEngineDescription(
                WordSegmenterAE.class,
                WordSegmenterAE.RES_KEY,
                stopWordsResourceDesc);

        AnalysisEngineDescription cnAe = createEngineDescription(
                CollocationNetworkBuilderAE.class,
                CollocationNetworkBuilderAE.RES_KEY,
                collocationNetworkResourceDesc,
                CollocationNetworkBuilderAE.PARAM_RESOURCE_DEST_FILENAME,
                "output/cn.csv",
                CollocationNetworkBuilderAE.PARAM_WINDOW_SIZE,
                3);

        CollectionReaderDescription crd = createReaderDescription(ZimReaderCR.class,
                ZimReaderCR.PARAM_ZIM_SRCPATH, "data/ubuntudoc_fr_01_2009.zim",
                ZimReaderCR.PARAM_LANGUAGE, "fr",
                ZimReaderCR.PARAM_ENCODING, "utf-8");
        SimplePipeline.runPipeline(crd, segmenterAe, cnAe);
    }
}
