package fr.univnantes.atal.nlpdev.linkdetection.cr;

import common.util.SgmlTextExtractorSaxHandler;
import fr.univnantes.atal.nlpdev.linkdetection.util.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.ccil.cowan.tagsoup.Parser;
import org.openzim.ZIMTypes.DirectoryEntry;
import org.openzim.ZIMTypes.ZIMFile;
import org.openzim.ZIMTypes.ZIMReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This reader reads from a zim file.
 */
public class ZimReaderCR extends JCasCollectionReader_ImplBase {

    private static final Logger logger = Logger.getLogger(
            ZimReaderCR.class.getCanonicalName());
    /**
     * Path of the zim file whose articles should be turned into JCas.
     */
    public static final String PARAM_ZIM_SRCPATH = "zimFileSrcPath";
    @ConfigurationParameter(name = PARAM_ZIM_SRCPATH,
    mandatory = true)
    private String zimFileSrcPath;
    /**
     * Main language of the articles contained in the zim file.
     */
    public static final String PARAM_LANGUAGE = "language";
    @ConfigurationParameter(name = PARAM_LANGUAGE,
    mandatory = false,
    defaultValue = "fr")
    private String language;
    /**
     * Main encoding of the articles contained in the zim file.
     */
    public static final String PARAM_ENCODING = "encoding";
    @ConfigurationParameter(name = PARAM_ENCODING,
    mandatory = false,
    defaultValue = "utf-8")
    private String encoding;
    private ZIMFile zimFile;
    private ZIMReader zimReader;
    private List<String> articleUrlList;
    private DirectoryEntry directoryEntry;
    private int articleUrlIndex = 0;
    private int htmlIndex = 0;
    private int articleDataNotNullIndex = 0;

    /**
     * Get a list of the urls contained in the zim file.
     *
     * @param context the UIMA context object
     */
    @Override
    public void initialize(final UimaContext context)
            throws ResourceInitializationException {
        //super.initialize();

        // Get the zim file from the path
        zimFile = new ZIMFile(zimFileSrcPath);

        // Associate the Zim File with a Reader
        zimReader = new ZIMReader(zimFile);
        try {
            // Get the list of article urls
            articleUrlList = zimReader.getURLListByURL();
            // TODO Just to perform some tests
			/*List<String> tmp = new ArrayList<String>();
             int max = 10;
             for (int i = 0 ; i < max ; i++)
             tmp.add(articleUrlList.get(i));
             articleUrlList.retainAll(tmp);
             articleUrlIndex = articleUrlList.size() - max;
             */

            directoryEntry = zimReader.getDirectoryInfoAtTitlePosition(zimFile.getTitlePtrPos());
        } catch (IOException e) {
            logger.error("could not open ZIM file");
            Util.abort(e);
        }

    }

    /**
     * @see org.apache.uima.collection.CollectionReader#hasNext()
     */
    @Override
    public boolean hasNext() {
        if (articleUrlList == null) {
            return false;
        }

        if (articleUrlIndex == articleUrlList.size()) {
            logger.info(
                    getClass().getSimpleName()
                    + " read "
                    + zimFile.getArticleCount()
                    + " articles of "
                    + zimFile.getName()
                    + " but only "
                    + htmlIndex
                    + " html with "
                    + articleDataNotNullIndex
                    + "non null");
        }
        return articleUrlIndex < articleUrlList.size();
    }

    /**
     * Document getter.
     *
     * @param jCas the jCas to fill with the new document information
     * @throws IOException
     * @throws CollectionException
     */
    @Override
    public void getNext(JCas jCas) throws IOException, CollectionException {
        String sgmlDocument = "";
        String url = articleUrlList.get(articleUrlIndex);
        if (url.endsWith(".html") || url.endsWith(".HTML")) {
            ByteArrayOutputStream articleData = zimReader.getArticleData(
                    url,
                    directoryEntry.getNamespace());
            if (articleData != null) {
//                documentText = new String(articleData.toString("ISO-8859-1")
//                        .getBytes("UTF-8"));
//                documentText = new String(
//                        new String(articleData.toString("UTF-8"))
//                        .getBytes("UTF-8"));
                sgmlDocument = articleData.toString(encoding);
                articleDataNotNullIndex++;
            }
            htmlIndex++;
        }

        // get the text from the sgml document
        jCas.setDocumentText(getTextContent(sgmlDocument));

        // set language if it was explicitly specified as a configuration
        // parameter
        if (language != null) {
            jCas.setDocumentLanguage(language);
        }

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
        srcDocInfo.setDocumentSize(sgmlDocument.length());
        srcDocInfo.setLastSegment(articleUrlIndex == articleUrlList.size());
        srcDocInfo.addToIndexes();
        articleUrlIndex++;
        if (articleUrlIndex % 100 == 0) {
            logger.info("processing article " + articleUrlIndex);
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
        return new Progress[]{new ProgressImpl(
                    articleUrlIndex,
                    articleUrlList.size(),
                    Progress.ENTITIES)};
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
        return articleUrlList.size();
    }

    /**
     * Extracts the text content of a given html document.
     *
     * @param sgmlDocument the html document.
     * @return the text of the html document.
     */
    private String getTextContent(String sgmlDocument) {

        // Create an instance of Parser
        Parser tagsoupParser = new Parser();

        // Declare the sgml tags you do not want to get the text content
        Set<String> notContentTagSet = new HashSet<>();
        notContentTagSet.add("script");
        notContentTagSet.add("style");

        // Provide your own SAX2 ContentHandler
        SgmlTextExtractorSaxHandler xmlSaxHandler =
                new SgmlTextExtractorSaxHandler(notContentTagSet, true);
        tagsoupParser.setContentHandler(xmlSaxHandler);

        // Provide an InputSource referring to the HTML
        //String htmlDocPath="resources/extreme.html";
        //String htmlDocPath="resources/Manipuler-le-CSS.html";

        try {
            //tagsoupParser.parse(htmlDocPath);
            tagsoupParser.parse(new InputSource(new StringReader(
                    sgmlDocument)));
        } catch (SAXException e) {
            logger.error("could not parse the SGML document");
            Util.abort(e);
        } catch (IOException e) {
            logger.error("could not open the SGML document");
            Util.abort(e);
        }
        //System.out.printf("%s\n",xmlSaxHandler.getDetaggedText());
        //writeToFS(xmlSaxHandler.getDetaggedText(),"/tmp/detaggedText");
        return xmlSaxHandler.getDetaggedText();
    }
}