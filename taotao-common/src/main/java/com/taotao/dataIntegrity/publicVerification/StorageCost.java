package com.taotao.dataIntegrity.publicVerification;

import com.taotao.dataIntegrity.file.FileOperation;
import com.taotao.dataIntegrity.tool.DataFilter;
import com.taotao.dataIntegrity.tool.StdOut;

/**
 * �������д洢���ģ�ǩ�����������ļ�Ԫ��Ϣ
 * @author MichaelSun
 *
 */
public class StorageCost {
	public static final int K=1024;
	public static final int Byte=8;
	/**
	 * CSP�г����ݵĶ���洢����
	 * @param n		�ܿ���
	 * @param s		ÿ�����
	 * @param p		��������20B��
	 * @return 		����KB
	 */
	public static double IHTPADDStorageCost(int n,int s,int p){
		double usLength=s*p;
		double ihtLength=IHTStorageCost(n,p);
		double tagLength=n*p;
		double total=usLength+ihtLength+tagLength;
		return DataFilter.roundDouble(total/K,3);
	}
	/**
	 * CSP�ж���洢����ռ���ݵı���
	 * @param n		�ܿ���
	 * @param s		ÿ�����
	 * @param p		��������20B��
	 * @return 		�ٷֱ�
	 */
	public static double IHTPADDStorageCostRatio(int n,int s,int p){
		double additionStorageCost=IHTPADDStorageCost(n,s,p);
		double total=n*s*p/K;
		return DataFilter.roundDouble(additionStorageCost/total, 3)*100;
	}


	/**
	 * IHT��Ĵ洢����
	 * (index,B,V,R,Hmac)
	 * @param n		�ļ�����
	 * @param p		ǩ������
	 * @return		��������ֽ���(B)
	 */
	public static double IHTStorageCost(int n,int p){		
		return n*(2*p+2*Math.log(n)/Byte);
	}
	
	public static double tagStorageCost(int n,int p){
		return n*p/Byte;
	}
	//�������ĵ�KB
	public static double MHTStorgeCost(int n,int s,int p){
		//double result=n*Math.log(n)+3*p+s*p;
		double result=n*p+3*p+s*p;
		return DataFilter.roundDouble(result/K,2);
	}

	public static void main(String[] args) {

		//�ļ���СΪ2M
		//int M=1024*1024;
		
		int []files={2};
		int []s={50,100,150,200,250,300,350,400,450,500};				
		int p=20;//160bit

		for(int f=0;f<files.length;f++){
			StdOut.println("�ļ���С"+files[f]+"M");
			StdOut.println("========IHT�洢����=========");
			for(int i=0;i<s.length;i++){
				int blockNums=FileOperation.blockNumbers(files[f]*K,s[i],p);
				StdOut.println(s[i]+"\t"+blockNums+"\t"+
								DataFilter.roundDouble(IHTStorageCost(blockNums,p)/K,3)+"\t"+
								DataFilter.roundDouble(tagStorageCost(blockNums,p)/K,3)+"\t"+
								DataFilter.roundDouble(s[i]*p/1000,3));
			}
			/*StdOut.println("========MHT�洢����=========");
			for(int i=0;i<s.length;i++){
				int blockNums=fileBlocks(files[f]*M,s[i],p);
				StdOut.println(MHTStorgeCost(blockNums,s[i],p));
			}*/
		}

	}

}
