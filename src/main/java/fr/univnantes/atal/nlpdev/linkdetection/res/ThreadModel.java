package fr.univnantes.atal.nlpdev.linkdetection.res;

import java.util.Set;

public interface ThreadModel {
    public Set<String> getMessages(String threadId);
    public String getThread(String messageId);
}
