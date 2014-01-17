package linkInterMessageDetector.res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class ThreadModel_Impl implements ThreadModel, SharedResourceObject {

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
    public void load(DataResource aData) throws ResourceInitializationException {
        InputStream is = null;
        try {
            is = aData.getInputStream();
        } catch (NullPointerException ex) {
            System.err.println("Please provide a thread digest file as "
                    + "parameter to this resource. If you provided one it "
                    + "wasn't correct. Aborting.");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Couldn't open the thread digest file.");
            System.err.println("Aborting.");
            System.err.println("Exception was: " + ex.getLocalizedMessage());
            System.err.println("Trace was: " + ex.getStackTrace());
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
            System.err.println("Couldn't open the thread digest file.");
            System.err.println("Aborting.");
            System.err.println("Exception was: " + ex.getLocalizedMessage());
            System.err.println("Trace was: " + ex.getStackTrace());
        }
    }
}
