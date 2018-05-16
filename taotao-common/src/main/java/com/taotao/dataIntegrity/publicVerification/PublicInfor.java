package com.taotao.dataIntegrity.publicVerification;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.taotao.dataIntegrity.tool.PropertiesUtil;
import com.taotao.dataIntegrity.db.DBOperation;
import com.taotao.dataIntegrity.file.FileOperation;

public class PublicInfor {
	public static final String FILECONFIG="/Users/pei/program/java/IdeaProjects/taotao/taotao-common/src/main/resources/fileconfig.properties";
	public static final String PUBLIC="public";//��Ź�����Ϣ�����ݱ�
	public static final int K=1024;
	public static final boolean USEPBC=false;
	public static final String CURVEPATH="/Users/pei/program/java/IdeaProjects/taotao/taotao-common/src/main/resources/d_159.properties";
	public Pairing pairing;
	public  Element g1;
	public  Element g2;	
	public  int sectors;
	public  int sectorSize;
	public  int blockSize;//���ֽ���
	public  int blockSizeK;
	public  int n;
	public  Element []us;
	public  Element v;
	public  String filePath;//�û��ļ����·����Ҳ�Ǵ���ڷ������е��ļ�·��
	
	public PublicInfor(){
		init();
	}
	public void init(){
		try {
			Properties pro=PropertiesUtil.loadProperties();
			filePath=pro.getProperty("filePath");
			sectors=Integer.valueOf(pro.getProperty("sectors"));
			sectorSize=Integer.valueOf(pro.getProperty("sectorSize"));			
			blockSize=(sectors*sectorSize/1000)*K;
			blockSizeK=blockSize/K;
			n=FileOperation.blockNumbers(filePath, blockSizeK);
			//У���㷨ȫ�ֹ�����Ϣ
			PairingFactory.getInstance().setUsePBCWhenPossible(USEPBC);
			pairing=PairingFactory.getPairing(CURVEPATH);
			Map<String, byte[]> pubInfor=getPublicInfor(PUBLIC);
			g1=pairing.getG1().newElementFromBytes(pubInfor.get("g1"));
			g2=pairing.getG2().newElementFromBytes(pubInfor.get("g2"));
			v=pairing.getG2().newElementFromBytes(pubInfor.get("v"));			
			us=new Element[sectors];
			for (int i=0;i<sectors;i++){
				us[i]=pairing.getG1().newElementFromBytes(pubInfor.get("us"+String.valueOf(i+1)));
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	
		//��ȡ����У�������Ϣ
		public static Map<String ,byte[]> getPublicInfor(String table){
			DBOperation dbo=new DBOperation();
			return dbo.selectBatch(table, "");
			
		}
		
}
