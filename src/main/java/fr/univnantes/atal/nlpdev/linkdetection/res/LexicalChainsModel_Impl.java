package fr.univnantes.atal.nlpdev.linkdetection.res;

import fr.univnantes.atal.nlpdev.linkdetection.datamodel.LexicalChain;
import fr.univnantes.atal.nlpdev.linkdetection.util.Util;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 * Implementation of LexicalChainModel.
 */
public class LexicalChainsModel_Impl
        implements LexicalChainsModel, SharedResourceObject {

    private static final Logger logger = Logger.getLogger(
            LexicalChainsModel_Impl.class.getCanonicalName());
    private final Map<String, Set<LexicalChain>> chains = new HashMap<>();

    @Override
    public void load(DataResource aData)
            throws ResourceInitializationException {
        logger.info("> starting to load lexical chains");
        InputStream is = null;
        try {
            is = aData.getInputStream();
        } catch (NullPointerException ex) {
            logger.info("| nothing to load");
            logger.info("< done loading lexical chains");
            return;
        } catch (IOException ex) {
            logger.error("couldn't load the lexical chain models");
            Util.abort(ex);
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
            logger.error("couldn't load the lexical chain models");
            Util.abort(ex);
        }
        logger.info("< done loading lexical chains");
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
            logger.error("couldn't open output file “" + filename + "”");
            Util.abort(e);
        }
    }
}
