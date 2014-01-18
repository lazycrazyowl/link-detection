package linkInterMessageDetector.res;

import common.util.MiscUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import linkInterMessageDetector.datamodel.LexicalChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class LexicalChainsModel_Impl
        implements LexicalChainsModel, SharedResourceObject {

    private final Map<String, Set<LexicalChain>> chains = new HashMap<>();

    @Override
    public void load(DataResource aData)
            throws ResourceInitializationException {
        InputStream is = null;
        try {
            is = aData.getInputStream();
        } catch (NullPointerException ex) {
            // We didn't find an input stream to open
            // Nothing to load
            return;
        } catch (IOException ex) {
            MiscUtil.abort("Couldn't load the lexical chain models.", ex);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        is))) {
            String line, currentMessageId = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("# messageId: ")) {
                    currentMessageId = line.replace("# messageId: ", "");
                    chains.put(currentMessageId, new HashSet<LexicalChain>());
                } else {
                    chains.get(currentMessageId).add(new LexicalChain(
                            new HashSet<>(Arrays.asList(line.split(" ")))));
                }
            }
        } catch (IOException ex) {
            MiscUtil.abort("Couldn't load the lexical chain models.", ex);
        }
    }

    @Override
    public void addChain(String messageId, LexicalChain lexicalChain) {
        if (!chains.containsKey(messageId)) {
            chains.put(messageId, new HashSet<LexicalChain>());
        }
        chains.get(messageId).add(lexicalChain);
    }

    @Override
    public void setChains(String messageId, Set<LexicalChain> lexicalChains) {
        chains.put(messageId, new HashSet<>(lexicalChains));
    }

    @Override
    public Set<LexicalChain> getChains(String messageId) {
        Set<LexicalChain> result = chains.get(messageId);
        if (result == null) {
            result = new HashSet<>();
        }
        return Collections.unmodifiableSet(result);
    }

    @Override
    public void save(String filename) {
        try (PrintWriter pw = new PrintWriter(filename)) {
            for (String messageId : chains.keySet()) {
                pw.println("# messageId: " + messageId);
                for (LexicalChain chain : chains.get(messageId)) {
                    pw.println(StringUtils.join(chain.getLexicalChain(), " "));
                }
            }
        } catch (IOException e) {
            MiscUtil.abort("Couldn't open output file “" + filename + "”.", e);
        }
    }
}
