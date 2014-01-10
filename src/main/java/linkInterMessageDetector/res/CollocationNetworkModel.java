/**
 * 
 */
package linkInterMessageDetector.res;

import java.util.Set;

/**
 * Interface which models the collection networks ; 
 * Declare the methods handled by the annotators to access the resource 
 */
public interface CollocationNetworkModel {



	/** Test if a given word has somme collocated */
	public Boolean contains (String word);
	
	/** Get the collocated words of a given word*/
	public Set<String> getCollocated (String word);
	
	/** Increment the collocation score between two words*/
	public  void inc (String word1, String word2);
	
	/** Get the collocation score between two words */
	public Double getScore(String word1, String word2);

	/** Get the number of collocated of a given word*/
	public Integer getCollocatedSize(String word1);
		
	/** Get the number of words */
	public Integer size();

	/** Display the content of the current collocation network */
	public void echo();

	/**
	 * Save the content of the resource as a CSV file
	 * a line per word, word and counter as columns with tab character as separator
	 * use the MiscUtil.writeToFS(textString,filenameString) DONE */
	public void save(String filename);

}