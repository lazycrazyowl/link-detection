
/* First created by JCasGen Sun Jan 19 11:34:16 CET 2014 */
package fr.univnantes.atal.nlpdev.linkdetection.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Sun Jan 19 11:34:16 CET 2014
 * @generated */
public class Link_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Link_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Link_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Link(addr, Link_Type.this);
  			   Link_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Link(addr, Link_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Link.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("fr.univnantes.atal.nlpdev.linkdetection.types.Link");
 
  /** @generated */
  final Feature casFeat_uid;
  /** @generated */
  final int     casFeatCode_uid;
  /** @generated */ 
  public String getUid(int addr) {
        if (featOkTst && casFeat_uid == null)
      jcas.throwFeatMissing("uid", "fr.univnantes.atal.nlpdev.linkdetection.types.Link");
    return ll_cas.ll_getStringValue(addr, casFeatCode_uid);
  }
  /** @generated */    
  public void setUid(int addr, String v) {
        if (featOkTst && casFeat_uid == null)
      jcas.throwFeatMissing("uid", "fr.univnantes.atal.nlpdev.linkdetection.types.Link");
    ll_cas.ll_setStringValue(addr, casFeatCode_uid, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Link_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_uid = jcas.getRequiredFeatureDE(casType, "uid", "uima.cas.String", featOkTst);
    casFeatCode_uid  = (null == casFeat_uid) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_uid).getCode();

  }
}



    