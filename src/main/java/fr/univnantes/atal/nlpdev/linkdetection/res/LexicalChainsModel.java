package fr.univnantes.atal.nlpdev.linkdetection.res;

import fr.univnantes.atal.nlpdev.linkdetection.datamodel.LexicalChain;
import java.util.Set;

public interface LexicalChainsModel {
    public void addChain(String messageId, LexicalChain lexicalChain);
    public void setChains(String messageId, Set<LexicalChain> lexicalChains);
    public Set<LexicalChain> getChains(String messageId);
    public void save(String filename);
}
