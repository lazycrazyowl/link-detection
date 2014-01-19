package fr.univnantes.atal.nlpdev.linkdetection.res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 * Implementation of StopWordModel.
 */
public final class StopWordModel_Impl
        implements StopWordModel, SharedResourceObject {

    private static final Logger logger = Logger.getLogger(
            StopWordModel_Impl.class.getCanonicalName());
    private Set<String> stopWordSet;

    private synchronized Set<String> getStopWords() {
        return stopWordSet;
    }

    private synchronized void add(String key) {
        getStopWords().add(key);
    }

    @Override
    public Boolean contains(String key) {
        return getStopWords().contains(key);
    }

    @Override
    public synchronized void load(DataResource aData)
            throws ResourceInitializationException {
        logger.info("> starting to load stop words");
        if (stopWordSet == null) {
            stopWordSet = new HashSet<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                            aData.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith("#")) {
                        add(line.trim());
                    }
                }
            } catch (IOException e) {
                throw new ResourceInitializationException(e);
            }
        }
        logger.info("< done loading stop words");
    }
}