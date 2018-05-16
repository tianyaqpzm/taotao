/**
 * 功能描述：
 * 1.采用非对称的双线性映射
 * 2.采用带签名保护的索引哈希表
 * 3.服务器对数据标签进行累成，由服务器计算，最小化校验者的计算量
 * 4.服务器端对数据标签累成的改进版本
 * 5.批量校验
 * 6.包含对信息盲化处理
 */

package com.taotao.dataIntegrity.findError;

import java.io.IOException;
import java.util.HashMap;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taotao.dataIntegrity.file.FileOperation;
import it.unisa.dia.gas.jpbc.*;
import com.taotao.dataIntegrity.publicVerification.CloudServiceProvider;
import com.taotao.dataIntegrity.publicVerification.PublicInfor;
import com.taotao.dataIntegrity.publicVerification.TraceInfor;
import com.taotao.dataIntegrity.publicVerification.Verifier;
import com.taotao.dataIntegrity.publicVerification.Verifier.Chal;
import com.taotao.dataIntegrity.tool.GenerateRandom;

/**
 * 一次挑战
 * @author M
 * @version 2.0
 * @date  20
 * 
 */
public class FindErrorMoreRounds {
	public static final String PATH="/Users/pei/Documents/testDataIntegrity/102400000.txt";
	public static final String MATRIX="Matrix";	
	public static final String CUBE="Cube";	
	public static final String ONE="One";

	public static Map<String,Set<Integer>> testOneRound(String method,int []cBlocks){

		Verifier verifier=new Verifier();
		CloudServiceProvider csp=new CloudServiceProvider();		
		//int [] blocks=GenerateRandom.random(1, n, c);//���ѡȡc���������ս	
		Set<Integer>goodBlocks=new HashSet<>();
		Set<Integer>errorBlocks=new HashSet<>();
		Set<Integer>remainBlocks=new HashSet<>();
		for(int i:cBlocks)
			remainBlocks.add(i);
		while(remainBlocks.size()!=0)
		{	
			//������ս
			List<Chal>nextChal=verifier.challengeGen(remainBlocks);
			TraceInfor.challenges++;
			
			//����֤��
			TraceInfor.start();
			Map<String,Map<String,Element>> proofs=csp.genProof(method,nextChal);
			TraceInfor.csptime+=TraceInfor.end();
			TraceInfor.transCost+=proofs.size()*40;
			
			//��֤֤��
			TraceInfor.start();
			Map<String,Boolean>verResults=verifier.proofVerify(method, nextChal, proofs);
			TraceInfor.vertime+=TraceInfor.end();

			//�ж��㻵��
			TraceInfor.start();
			Map<String,Set<Integer>> blockStates=verifier.assertDamagedBlocks(method, nextChal, verResults);
			TraceInfor.asserttime+=TraceInfor.end();

			goodBlocks.addAll(blockStates.get("goodBlocks"));
			errorBlocks.addAll(blockStates.get("errorBlocks"));
			remainBlocks=blockStates.get("remainBlocks");
			if(nextChal.size()==remainBlocks.size())
				method=ONE;//�´���ս��ʣ������One����
		}
		Map<String,Set<Integer>> results=new HashMap<>();
		results.put("goodBlocks", goodBlocks);
		results.put("errorBlocks", errorBlocks);
		results.put("remainBlocks", remainBlocks);
		return results;
	}

	public static void testMoreRound(String method,int n,int chalLen,int errorCounts,double p)
	{
		int []all=new int[n];
		for(int i=0;i<n;i++)
			all[i]=i+1;
		TraceInfor.allErrorBlocks=errorCounts;
		Set<Integer> good=new HashSet<>();		
		Set<Integer>error=new HashSet<>();//���Ļ��鼯��	
		while((int)errorCounts*p>TraceInfor.errorBlocks){//�𻵿�ļ���ʴﵽp
			TraceInfor.round++;				
			int chal[]=GenerateRandom.random(all,chalLen,error);//ֻ�ɳ��˺ÿ�
			Map<String,Set<Integer>>results=testOneRound(method, chal);
			error.addAll(results.get("errorBlocks"));
			good.addAll(results.get("goodBlocks"));			
			TraceInfor.errorBlocks+=results.get("errorBlocks").size();
			TraceInfor.goodBlocks+=results.get("goodBlocks").size();
		}
		TraceInfor.logging(PATH);		
	}

	public static void testMoreRound(String method,int n,int chalLen ,int errorCounts,int rounds)
	{
		int []all=new int[n];
		for(int i=0;i<n;i++)
		{
			all[i]=i+1;
		}
		TraceInfor.allErrorBlocks=errorCounts;
		//���Ļ��鼯��
		Set<Integer>error=new HashSet<>();	
		while(rounds--!=0)
		{//�𻵿�ļ���ʴﵽp
			TraceInfor.round++;				
			int chal[]=GenerateRandom.random(all,chalLen,error);
			Map<String,Set<Integer>>results=testOneRound(method, chal);
			int errorBlocksNum=results.get("errorBlocks").size();
			error.addAll(results.get("errorBlocks"));
			TraceInfor.errorBlocks+=errorBlocksNum;
		}

		TraceInfor.logging(PATH);

	}
	public static void main(String[] args) throws IOException {
		PublicInfor pubInfor=new PublicInfor();	
		int n=pubInfor.n;
		//��������
		double []w={0.1,0.5,1};
		//��������
		double[] p={0.8,0.85,0.9,0.95,1.0};
		//ÿ��У�����
		int[] c={500,1000,1500,2000,2500,3000,3500,4000,4500,5000};


		for(int i=0;i<1;i++){
			int errorCounts=(int)Math.ceil(n*w[i]/100)+2;
			for(int j=0;j<1;j++){
				for(int k=0;k<1;k++){				
					int [] damagedBlocks=GenerateRandom.random(1,n, errorCounts);//��ȡ�Ŀ������𻵵Ŀ���		
					FileOperation.destoryBlocks(pubInfor.filePath,pubInfor.blockSizeK, damagedBlocks);
					TraceInfor.logging(PATH,"-----------------------------------");
					TraceInfor.logging(PATH,CUBE+"n:"+n+"\tc:"+c[k]+"\tw:"+w[i]+"\tp:"+p[j]);
					TraceInfor.logging(PATH,"-----------------------------------");
					try{
						testMoreRound(CUBE,n,c[k],errorCounts,p[j]);
					}finally{

						FileOperation.repairBlocks(pubInfor.filePath,pubInfor.blockSizeK, damagedBlocks);
					}
				}
			}
		}
	}}
