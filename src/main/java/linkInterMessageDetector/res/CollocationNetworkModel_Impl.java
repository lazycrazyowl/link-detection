/**
 *
 */
package linkInterMessageDetector.res;

import common.util.MiscUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 * Implementation of the resource model for Word Counters No resource actually
 * loaded
 */
public final class CollocationNetworkModel_Impl
        implements CollocationNetworkModel, SharedResourceObject {

    private final Map<String, Map<String, Double>> collocationNetworkMap =
            new HashMap<>();
    private final static double initialValue = 0.;
    private final static int incrementValue = 1;

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
            Logger.getLogger(CollocationNetworkModel_Impl.class.getName())
                    .log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CollocationNetworkModel_Impl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
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
        MiscUtil.writeToFS(this.toString(), filename);
    }
}
