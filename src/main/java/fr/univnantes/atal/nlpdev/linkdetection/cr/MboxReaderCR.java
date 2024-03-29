package fr.univnantes.atal.nlpdev.linkdetection.cr;

import fr.univnantes.atal.nlpdev.linkdetection.util.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.mboxiterator.CharBufferWrapper;
import org.apache.james.mime4j.mboxiterator.MboxIterator;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;

/**
 * This reader reads messages from a mbox file.
 */
public class MboxReaderCR extends JCasCollectionReader_ImplBase {

    private static final Logger logger = Logger.getLogger(
            MboxReaderCR.class.getCanonicalName());
    /**
     * Path of the mbox file whose messages should be turned into JCas.
     */
    public static final String PARAM_MBOX_SRCPATH = "mboxSrcPath";
    @ConfigurationParameter(name = PARAM_MBOX_SRCPATH,
    mandatory = true)
    private String mboxSrcPath;
    /**
     * Main language of the messages contained in the mbox file.
     */
    public static final String PARAM_LANGUAGE = "language";
    @ConfigurationParameter(name = PARAM_LANGUAGE,
    mandatory = false,
    defaultValue = "fr")
    private String language;
    /**
     * Main encoding of the messages contained in the mbox file.
     */
    public static final String PARAM_ENCODING = "encoding";
    @ConfigurationParameter(name = PARAM_ENCODING,
    mandatory = false,
    defaultValue = "utf-8")
    private String encoding;
    private static CharsetEncoder CHARSET_ENCODER;
    private Iterator<CharBufferWrapper> mboxIterator;
    private int messageIndex = 0;

    /**
     * Gets an iterator on the mbox.
     *
     * @param context the UIMA context object
     */
    @Override
    public void initialize(final UimaContext context)
            throws ResourceInitializationException {
        //super.initialize();

        CHARSET_ENCODER = Charset.forName(encoding).newEncoder();

        // Get the mbox file from the path
        final File mbox = new File(mboxSrcPath);
        try {
            mboxIterator = MboxIterator.fromFile(mbox)
                    .charset(CHARSET_ENCODER.charset())
                    .build()
                    .iterator();
        } catch (FileNotFoundException e) {
            logger.error("could not find the mbox file");
            Util.abort(e);
        } catch (IOException e) {
            logger.error("could not process the mbox file");
            Util.abort(e);
        }

    }

    /**
     * @see org.apache.uima.collection.CollectionReader#hasNext()
     */
    @Override
    public boolean hasNext() {
        return mboxIterator.hasNext();
    }

    /**
     * Message getter.
     *
     * @param jCas the jCas to fill with the new message information
     * @throws IOException
     * @throws CollectionException
     */
    @Override
    public void getNext(JCas jCas) throws IOException, CollectionException {
        CharBufferWrapper message = mboxIterator.next();

        StringWriter writer = new StringWriter();
        IOUtils.copy(message.asInputStream(CHARSET_ENCODER.charset()), writer);
        String messageContent = writer.toString(),
                documentText = new String(messageContent.getBytes("UTF-8"));

        jCas.setDocumentText(documentText);

        // set language if it was explicitly specified as a configuration
        // parameter.
        if (language != null) {
            jCas.setDocumentLanguage(language);
        }

        //Message-Id: ou Message-ID:
        String messageId = "Message-I";
        int start = documentText.indexOf(messageId) + messageId.length() + 2;
        int end = documentText.indexOf("\n", start);
        String url = documentText.substring(start, end).trim();
        // to remove < and >
        url = url.substring(1, url.length() - 1);

        // Also store location of source document in CAS. This information is
        // critical if CAS Consumers will need to know where the original
        // document contents are located.
        // For example, the Semantic Search CAS Indexer writes this information
        // into the search index that it creates, which allows applications that
        // use the search index to locate the documents that satisfy their
        // semantic queries.
        SourceDocumentInformation srcDocInfo =
                new SourceDocumentInformation(jCas);
        srcDocInfo.setUri(url);
        srcDocInfo.setOffsetInSource(0);
        srcDocInfo.setDocumentSize(documentText.length());
        srcDocInfo.setLastSegment(false);
        // TODO cannot know when the last segment is encountered with an iterator
        srcDocInfo.addToIndexes();
        messageIndex++;
        if (messageIndex % 100 == 0) {
            logger.info("processing message " + messageIndex);
        }
    }

    /**
     * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#close()
     */
    @Override
    public void close() throws IOException {
    }

    /**
     * @see
     * org.apache.uima.collection.base_cpm.BaseCollectionReader#getProgress()
     */
    @Override
    public Progress[] getProgress() {
        return new Progress[messageIndex];
    }

    /**
     * Gets the total number of documents that will be returned by this
     * collection reader.
     *
     * This is not part of the general collection reader interface.
     *
     * @return the number of documents in the collection
     */
    public int getNumberOfDocuments() {
        return messageIndex;
    }
}