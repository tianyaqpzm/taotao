package com.taotao.dataIntegrity.publicVerification;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.taotao.dataIntegrity.db.DBOperation;
import it.unisa.dia.gas.jpbc.Element;

public class MerkleTree {
	  public static final String FILETAG="filetag";//����ļ����ǩ���ݱ�filetag
	  protected PublicInfor pubInfor;	
	  // transaction List
//	  List<String> txList;
	  Map<String,byte[]> txList;
	  // Merkle Root
	  String root;
	  
	  /**
	   * constructor
	   * @param txList transaction List 
	   */
	  public MerkleTree(Map<String,byte[]> txList) {
	    this.txList = txList;
	    root = "";
//	    pubInfor=new PublicInfor();
	  }
	   
	  /**
	   * execute merkle_tree and set root.
	   */
	  public void merkle_tree() {
	    
	    List<String> tempTxList = new ArrayList<String>();
	    
	    for (int i = 1; i <= this.txList.size(); i++) {
	    	byte[] temp = txList.get(" "+Integer.toString(i));

			StringBuilder sb = new StringBuilder(2 * temp.length);
			for(byte b: temp) {
			  sb.append(String.format("%02x", b&0xff) );
			}
	        tempTxList.add(sb.toString());
	    }
	   
	    List<String> newTxList = getNewTxList(tempTxList);
	   
	    while (newTxList.size() != 1) {
	      newTxList = getNewTxList(newTxList);
	    }
	    
	    this.root = newTxList.get(0);
	  }
	  
	  /**
	   * return Node Hash List.
	   * @param tempTxList
	   * @return
	   */
	  private List<String> getNewTxList(List<String> tempTxList) {
	    
	    List<String> newTxList = new ArrayList<String>();
	    int index = 0;
	    while (index < tempTxList.size()) {
	      // left
	      String left = tempTxList.get(index);
	      index++;

	      // right
	      String right = "";
	      if (index != tempTxList.size()) {
	        right = tempTxList.get(index);
	      }

	      // sha2 hex value
	      String sha2HexValue = getSHA2HexValue(left + right);
	      newTxList.add(sha2HexValue);
	      index++;
	      
	    }
	    
	    return newTxList;
	  }
	  
	  /**
	   * Return hex string
	   * @param str
	   * @return
	   */
	  public String getSHA2HexValue(String str) {
	        byte[] cipher_byte;
	        try{
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            md.update(str.getBytes());
	            cipher_byte = md.digest();
	            StringBuilder sb = new StringBuilder(2 * cipher_byte.length);
	            for(byte b: cipher_byte) {
	              sb.append(String.format("%02x", b&0xff) );
	            }
	            return sb.toString();
	        } catch (Exception e) {
	                e.printStackTrace();
	        }
	        
	        return "";
	  }
	  
	  /**
	   * Get Root
	   * @return
	   */
	  public String getRoot() {
	    return this.root;
	  }
	  
	    //��ȥ��ս���У���ǩ  ���� map
		private Map<String,byte[]> readTags(){
			DBOperation dbo=new DBOperation();
			Map<String,byte[]> results=dbo.selectBatch(FILETAG, "");
			List<Element>ctags=new ArrayList<>();
//			for (int i=0;i<challenges.size();i++){
//				ctags.add((pubInfor.pairing.getG1().newElementFromBytes(results.get(String.valueOf(challenges.get(i).num)))));
//			}
//			return ctags;
			return results;
		}
	    
	}
