package fr.univnantes.atal.nlpdev.linkdetection.res;

import fr.univnantes.atal.nlpdev.linkdetection.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 * Implementation of ThreadModel based on two maps.
 */
public class ThreadModel_Impl implements ThreadModel, SharedResourceObject {

    private static final Logger logger = Logger.getLogger(
            ThreadModel_Impl.class.getCanonicalName());
    private final Map<String, Set<String>> byThreads = new HashMap<>();
    private final Map<String, String> byMessages = new HashMap<>();

    @Override
    public Set<String> getMessages(String threadId) {
        return Collections.unmodifiableSet(byThreads.get(threadId));
    }

    @Override
    public String getThread(String messageId) {
        return byMessages.get(messageId);
    }

    @Override
    public void load(DataResource aData)
            throws ResourceInitializationException {
        logger.info("> starting to load thread info");
        InputStream is = null;
        try {
            is = aData.getInputStream();
        } catch (NullPointerException ex) {
            logger.error("please provide a thread digest file as parameter to "
                    + "this resource. If you provided one it wasn't correct");
            System.exit(1);
        } catch (IOException ex) {
            logger.error("couldn't open the thread digest file");
            Util.abort(ex);
        }
        try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(is))) {
            String line;
            String threadId = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("# messages")) {
                    threadId = null;
                } else if (line.startsWith("# threads")) {
                    break;
                } else {
                    if (threadId == null) {
                        threadId = line.trim();
                    }
                    String messageId = line.trim();
                    if (!byThreads.containsKey(threadId)) {
                        byThreads.put(threadId, new HashSet<String>());
                    }
                    byThreads.get(threadId).add(messageId);
                    byMessages.put(messageId, threadId);
                }
            }
        } catch (IOException ex) {
            logger.error("couldn't open the thread digest file");
            Util.abort(ex);
        }
        logger.info("> done loading thread info");
    }
}
