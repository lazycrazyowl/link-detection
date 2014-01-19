

/* First created by JCasGen Sun Jan 19 22:52:20 CET 2014 */
package fr.univnantes.atal.nlpdev.linkdetection.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun Jan 19 22:52:20 CET 2014
 * XML source: /mnt/data/work/link-detection/src/main/resources/fr/univnantes/atal/nlpdev/linkdetection/types/LinkDetectionTS.xml
 * @generated */
public class Link extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Link.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Link() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Link(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Link(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Link(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: uid

  /** getter for uid - gets 
   * @generated */
  public String getUid() {
    if (Link_Type.featOkTst && ((Link_Type)jcasType).casFeat_uid == null)
      jcasType.jcas.throwFeatMissing("uid", "fr.univnantes.atal.nlpdev.linkdetection.types.Link");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Link_Type)jcasType).casFeatCode_uid);}
    
  /** setter for uid - sets  
   * @generated */
  public void setUid(String v) {
    if (Link_Type.featOkTst && ((Link_Type)jcasType).casFeat_uid == null)
      jcasType.jcas.throwFeatMissing("uid", "fr.univnantes.atal.nlpdev.linkdetection.types.Link");
    jcasType.ll_cas.ll_setStringValue(addr, ((Link_Type)jcasType).casFeatCode_uid, v);}    
  }

    