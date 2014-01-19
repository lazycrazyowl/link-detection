package fr.univnantes.atal.nlpdev.linkdetection.res;

import fr.univnantes.atal.nlpdev.linkdetection.datamodel.LexicalChain;
import java.util.Set;

/**
 * Interface which models a mapping between messages identifiers and their
 * lexical chains.
 */
public interface LexicalChainsModel {

    /**
     * Adds a chain to resource for a given messageId.
     *
     * @param messageId the identifier of the message we're interested in.
     * @param lexicalChain the lexical chain to add.
     */
    public void addChain(String messageId, LexicalChain lexicalChain);

    /**
     * Sets a mapping from a messageId to a set a lexicalChains in the resource.
     *
     * @param messageId the identifier of the message we're interested in.
     * @param lexicalChains the lexical chains to set.
     */
    public void setChains(String messageId, Set<LexicalChain> lexicalChains);

    /**
     * Getter for the lexical chains of a message.
     *
     * @param messageId the identifier of the message we're interested in.
     * @return the set of lexical chains of the message whose ID is messageId.
     */
    public Set<LexicalChain> getChains(String messageId);

    /**
     * Serialization method.
     *
     * @param filename where to save the serialized resource.
     */
    public void save(String filename);
}
