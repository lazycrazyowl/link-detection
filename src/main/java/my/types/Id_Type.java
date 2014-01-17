
/* First created by JCasGen Fri Jan 17 13:04:54 CET 2014 */
package my.types;

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
 * Updated by JCasGen Fri Jan 17 13:04:54 CET 2014
 * @generated */
public class Id_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Id_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Id_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Id(addr, Id_Type.this);
  			   Id_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Id(addr, Id_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Id.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("my.types.Id");
 
  /** @generated */
  final Feature casFeat_uid;
  /** @generated */
  final int     casFeatCode_uid;
  /** @generated */ 
  public String getUid(int addr) {
        if (featOkTst && casFeat_uid == null)
      jcas.throwFeatMissing("uid", "my.types.Id");
    return ll_cas.ll_getStringValue(addr, casFeatCode_uid);
  }
  /** @generated */    
  public void setUid(int addr, String v) {
        if (featOkTst && casFeat_uid == null)
      jcas.throwFeatMissing("uid", "my.types.Id");
    ll_cas.ll_setStringValue(addr, casFeatCode_uid, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Id_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_uid = jcas.getRequiredFeatureDE(casType, "uid", "uima.cas.String", featOkTst);
    casFeatCode_uid  = (null == casFeat_uid) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_uid).getCode();

  }
}



    