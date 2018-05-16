/**
 * ����������
 * 1.���÷ǶԳƵ�˫����ӳ��
 * 2.���ô�ǩ��������������ϣ��
 * 3.�����������ݱ�ǩ�����۳ɣ��ɷ��������㣬��С��У���ߵļ�����
 * 4.�������˶����ݱ�ǩ�۳ɵĸĽ��汾
 * 5.����У��
 * 6.��������Ϣä������
 */

package com.taotao.dataIntegrity.findError;

import java.io.IOException;
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
 * 一次坏块检测
 * @author
 * @version
 * @date
 * 
 */
public class FindErrorOneRound {
	public static final String PATH="/Users/pei/Documents/testDataIntegrity/1G.txt";
	public static final String MATRIX="Matrix";	
	public static final String CUBE="Cube";	
	public static final String ONE="One";
	public static void testOneRound(String method,int []cBlocks,boolean isOne){

		Verifier verifier=new Verifier();
		CloudServiceProvider csp=new CloudServiceProvider();		
		//int [] blocks=GenerateRandom.random(1, n, c);// 随机选出cge块进行挑战
		Set<Integer>goodBlocks=new HashSet<>();
		Set<Integer>errorBlocks=new HashSet<>();
		Set<Integer>remainBlocks=new HashSet<>();
		for(int i:cBlocks)
			remainBlocks.add(i);
		while(remainBlocks.size()!=0)
		{	
			
			// 产生挑战
			List<Chal>nextChal=verifier.challengeGen(remainBlocks);
			TraceInfor.challenges++;
			
			// 计算证据
			TraceInfor.start();
			Map<String,Map<String,Element>> proofs=csp.genProof(method,nextChal);
			TraceInfor.csptime+=TraceInfor.end();
			TraceInfor.transCost+=proofs.size()*40;
			
			// 验证证据
			TraceInfor.start();
			Map<String,Boolean>verResults=verifier.proofVerify(method, nextChal, proofs);
			TraceInfor.vertime+=TraceInfor.end();

			// 判定坏块
			TraceInfor.start();
			Map<String,Set<Integer>> blockStates=verifier.assertDamagedBlocks(method, nextChal, verResults);
			TraceInfor.asserttime+=TraceInfor.end();

			goodBlocks.addAll(blockStates.get("goodBlocks"));
			errorBlocks.addAll(blockStates.get("errorBlocks"));
			remainBlocks=blockStates.get("remainBlocks");
			if(nextChal.size()==remainBlocks.size()){
				System.out.println(nextChal.size());
				if(isOne)
					method=ONE;//下次挑战对剩余块采用One 方法
				else 
					break;
			}
		}
		TraceInfor.errorBlocks=errorBlocks.size();
		TraceInfor.goodBlocks=goodBlocks.size();
		TraceInfor.remainBlocks=remainBlocks.size();
		
		TraceInfor.logging(PATH);
		
	}

	public static void main(String[] args) throws IOException {
		PublicInfor pubInfor=new PublicInfor();	
		int n=pubInfor.n;
		int[] c={500,1000,1500,2000,2500,3000,3500,4000,4500,5000};
		double []w={0.2,0.5,1};
		for(int i=0;i<1;i++)
		{
			for(int j=0;j<1;j++){
				int errorCounts=(int)(w[i]*c[j]/100);
				int []cBlocks=GenerateRandom.random(1, n, c[j]);// 一轮中抽取对块数
				int [] damagedBlocks=GenerateRandom.random(cBlocks, errorCounts);// 抽取对块数中损失对块数
				FileOperation.destoryBlocks(pubInfor.filePath,pubInfor.blockSizeK, damagedBlocks);
				TraceInfor.allErrorBlocks=errorCounts;
				TraceInfor.round=1;
				TraceInfor.logging(PATH,"-----------------------------------");
				TraceInfor.logging(PATH,CUBE+"\tc:"+c[j]+"\tw:"+w[i]);
				TraceInfor.logging(PATH,"-----------------------------------");
				testOneRound(CUBE,cBlocks,true);

				FileOperation.repairBlocks(pubInfor.filePath,pubInfor.blockSizeK, damagedBlocks);
			}
		}
	}
}
