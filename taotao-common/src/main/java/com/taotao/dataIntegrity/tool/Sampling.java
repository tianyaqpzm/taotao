package com.taotao.dataIntegrity.tool;

public class Sampling {
	/**
	 * ��ò�������
	 * @param n �ܿ���
	 * @param e ������
	 * @param P ̽����
	 * @return	��������
	 */
	 public static int getSampleBlocks(int n,int e,double P){
		 double pb=(double)e/n;
		 return (int)(Math.log(1-P)/Math.log(1-pb));
	 }
	 public static int getSampleBlocks(double pe,double P){
		
		 return (int)(Math.log(1-P)/Math.log(1-pe));
	 }
	 /**
	  * ��ò�������
	  * @param n �ܿ���
	  * @param e ����
	  * @param P ̽����
	  * @return ��������
	  */
	 public static double getSamplingRatio(int n,int e,double P){
		 double pb=(double)e/n;		
		 double w=(Math.log(1-P)/Math.log(1-pb))/n;
		 return w;
	 } 
	 /**
	  * ����ʵ���У��Ƶ��
	  * @param n	�ܿ���
	  * @param e	����
	  * @param t	��������
	  * @param Pt	�����ڵ�̽����
	  * @param T	У�����ڣ��졢�ܡ�������
	  * @return		У��Ƶ��
	  */
	 public static int getVerifyingFrequent(int n,double e,int t,double Pt,int T){
		 return (int)Math.ceil((Math.log(1-Pt)/(t*T*(Math.log(1-e/n)))));
		 
	 }
	 
	 /**
	  * �ֶ�����µĲ�������
	  * @param n	�ܿ��� 
	  * @param P	̽����
	  * @param s	ÿ�����
	  * @param es	�𻵵Ķ���
	  * @return		��������
	  */
	 public static int getSampleBlocks(int n,double P,int s,int es){
		 double Ps=(double)es/(n*s);
		 //StdOut.println(Ps);
		 return (int)(Math.log(1-P)/(s*Math.log(1-Ps)));
	 }
	 
	 public static double getSamplingRatio(int n,double P,int s,int es){
		 double Ps=(double)es/(n*s);
		 //w=log(1-P)/nslog(1-Ps)	  
		 return(Math.log(1-P)/(s*n*Math.log(1-Ps)));
	 } 
	 
	 public static int getVerifyingFrequent(int n,int t,double Pt,int T,int s,int es){
		double Ps=(double)es/(n*s);
		 return (int)Math.ceil((Math.log(1-Pt)/(t*T*(Math.log(1-Ps)))));
		 
	 }
	 public static void main(String[] args){
		 int c=getSampleBlocks(10000, 100, 0.999);
		 System.out.println(c);
		 
		 int cs=getSampleBlocks(10000,0.999,200,100);
		 System.out.println(cs);
	 }
}
