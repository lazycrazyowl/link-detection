package fr.univnantes.atal.nlpdev.linkdetection.res;

import fr.univnantes.atal.nlpdev.linkdetection.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
 * Implementation of CollocationNetworkModel based on a queue to go through the
 * collocation window.
 */
public final class CollocationNetworkModel_Impl
        implements CollocationNetworkModel, SharedResourceObject {

    private static final Logger logger = Logger.getLogger(
            CollocationNetworkModel_Impl.class.getCanonicalName());
    private final Map<String, Map<String, Double>> collocationNetworkMap =
            new HashMap<>();
    private final static double initialValue = 0.;
    private final static int incrementValue = 1;

    @Override
    public void load(DataResource aData)
            throws ResourceInitializationException {
        logger.info("> starting to load collocation network");
        InputStream is = null;
        try {
            is = aData.getInputStream();
        } catch (NullPointerException ex) {
            logger.info("| nothing to load");
            logger.info("< done loading collocation network");
            return;
        } catch (IOException ex) {
            logger.error("couldn't process the collocation network file");
            Util.abort(ex);
        }
        try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] fields = line.split("\\t");
                if (fields.length != 3) {
                    continue;
                }
                final String word1 = fields[0],
                        word2 = fields[1];
                if (word1.equals(word2)) {
                    inc(fields[0],
                            fields[1],
                            Double.parseDouble(fields[2]));
                } else {
                    inc(fields[0],
                            fields[1],
                            Double.parseDouble(fields[2]) / 2);
                }
            }
        } catch (IOException ex) {
            logger.error("couldn't process the collocation network file");
            Util.abort(ex);
        }
        logger.info("< done loading collocation network");
    }

    @Override
    public Double get(String word, String collocation) {
        if (collocationNetworkMap.containsKey(word)) {
            Map<String, Double> wordMap = collocationNetworkMap.get(word);
            if (wordMap.containsKey(collocation)) {
                return wordMap.get(collocation);
            }
        }
        return initialValue;
    }

    @Override
    public synchronized void inc(String key, String collocation) {
        if (!collocationNetworkMap.containsKey(key)) {
            collocationNetworkMap.put(key, new HashMap<String, Double>());
        }
        Map<String, Double> wordMap = collocationNetworkMap.get(key);
        if (!wordMap.containsKey(collocation)) {
            wordMap.put(collocation, initialValue);
        }
        wordMap.put(collocation, wordMap.get(collocation) + incrementValue);
        if (!key.equals(collocation)) {
            if (!collocationNetworkMap.containsKey(collocation)) {
                collocationNetworkMap.put(
                        collocation,
                        new HashMap<String, Double>());
            }
            wordMap = collocationNetworkMap.get(collocation);
            if (!wordMap.containsKey(key)) {
                wordMap.put(key, initialValue);
            }
            wordMap.put(key, wordMap.get(key) + incrementValue);
        }
    }

    @Override
    public synchronized void inc(
            String key,
            String collocation,
            Double howMuch) {
        if (!collocationNetworkMap.containsKey(key)) {
            collocationNetworkMap.put(key, new HashMap<String, Double>());
        }
        Map<String, Double> wordMap = collocationNetworkMap.get(key);
        if (!wordMap.containsKey(collocation)) {
            wordMap.put(collocation, initialValue);
        }
        wordMap.put(collocation, wordMap.get(collocation) + howMuch);
        if (!key.equals(collocation)) {
            if (!collocationNetworkMap.containsKey(collocation)) {
                collocationNetworkMap.put(
                        collocation,
                        new HashMap<String, Double>());
            }
            wordMap = collocationNetworkMap.get(collocation);
            if (!wordMap.containsKey(key)) {
                wordMap.put(key, initialValue);
            }
            wordMap.put(key, wordMap.get(key) + howMuch);
        }
    }

    @Override
    public synchronized void dec(String key, String collocation) {
        if (!collocationNetworkMap.containsKey(key)) {
            collocationNetworkMap.put(key, new HashMap<String, Double>());
        }
        Map<String, Double> wordMap = collocationNetworkMap.get(key);
        if (!wordMap.containsKey(collocation)) {
            wordMap.put(collocation, initialValue);
        }
        wordMap.put(
                collocation,
                Math.max(initialValue,
                wordMap.get(collocation) - incrementValue));
        if (!key.equals(collocation)) {
            if (!collocationNetworkMap.containsKey(collocation)) {
                collocationNetworkMap.put(
                        collocation,
                        new HashMap<String, Double>());
            }
            wordMap = collocationNetworkMap.get(collocation);
            if (!wordMap.containsKey(key)) {
                wordMap.put(key, initialValue);
            }
            wordMap.put(
                    collocation,
                    Math.max(initialValue,
                    wordMap.get(collocation) - incrementValue));
        }
    }

    @Override
    public Integer size() {
        return collocationNetworkMap.size();
    }

    @Override
    public Integer size(String word) {
        if (!collocationNetworkMap.containsKey(word)) {
            return 0;
        }
        return collocationNetworkMap.get(word).size();
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(collocationNetworkMap.keySet());
    }

    @Override
    public Set<String> keySet(String word) {
        if (!collocationNetworkMap.containsKey(word)) {
            return Collections.unmodifiableSet(new HashSet<String>());
        }
        return Collections.unmodifiableSet(collocationNetworkMap.get(word).keySet());
    }

    @Override
    public Double getTotalCounter() {
        double total = 0;
        for (String word : collocationNetworkMap.keySet()) {
            for (Double value : collocationNetworkMap.get(word).values()) {
                total += value;
            }
        }
        return total;
    }

    @Override
    public Double getTotalCounter(String word) {
        if (!collocationNetworkMap.containsKey(word)) {
            return initialValue;
        }
        double total = 0;
        for (Double value : collocationNetworkMap.get(word).values()) {
            total += value;
        }
        return total;
    }

    /**
     * Returns a representation of the collocation network that uses a line per
     * collocation.
     *
     * The format is word\tcollocation\tscore\n
     *
     * @return the string representation of the collocation network.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String word : collocationNetworkMap.keySet()) {
            for (String collocation : collocationNetworkMap
                    .get(word)
                    .keySet()) {
                sb.append(word)
                        .append('\t')
                        .append(collocation)
                        .append('\t')
                        .append(
                        collocationNetworkMap.get(word).get(collocation))
                        .append('\n');
            }
        }
        return sb.toString();
    }

    @Override
    public void echo() {
        System.out.println(this);
    }

    @Override
    public void save(String filename) {
        try (PrintWriter pw = new PrintWriter(filename)) {
            for (String word : collocationNetworkMap.keySet()) {
                for (String collocation : collocationNetworkMap
                        .get(word)
                        .keySet()) {
                    pw.println(
                            word
                            + '\t'
                            + collocation
                            + '\t'
                            + collocationNetworkMap.get(word).get(collocation));
                }
            }
        } catch (IOException e) {
            logger.error("couldn't open output file “" + filename + "”");
            Util.abort(e);
        }
    }
}
