/**
 * 
 */
package linkInterMessageDetector.res;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

import common.util.MiscUtil;

/**
 * Implementation of the resource model for collocation network
 * No resource actually loaded 
 */
public final class CollocationNetworkModel_Impl implements CollocationNetworkModel, SharedResourceObject {
	private Map<String,Map<String,Double>> collocationNetwork;

	final static private double nullValue = 0.0;
	final static private double initialValue = 1.0;
	final static private double incrementValue = 1.0;

	private Boolean isSaved = false;
	private Boolean alreadyLoaded = false;
	@Override
	public void load(DataResource aData) throws ResourceInitializationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Boolean contains(String word) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Set<String> getCollocated(String word) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void inc(String word1, String word2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Double getScore(String word1, String word2) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer getCollocatedSize(String word1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer size() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void echo() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void save(String filename) {
		// TODO Auto-generated method stub
		
	}



}
