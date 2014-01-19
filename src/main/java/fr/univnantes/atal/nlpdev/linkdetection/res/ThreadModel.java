package fr.univnantes.atal.nlpdev.linkdetection.res;

import java.util.Set;

/**
 * Interface which models the information available about threads and messages.
 */
public interface ThreadModel {

    /**
     * Getter for message identifiers from thread identifiers.
     *
     * @param threadId the identifier of a thread.
     * @return the identifiers of the messages of the thread.
     */
    public Set<String> getMessages(String threadId);

    /**
     * Getter for thread identifiers from message identifiers.
     *
     * @param messageId the identifier of a message.
     * @return the identifier of the thread of the message.
     */
    public String getThread(String messageId);
}
