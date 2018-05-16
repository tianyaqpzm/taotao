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
 * @author MichaelSun
 * @version 2.0
 * @date  2015.6.5
 * 
 */
public class FindErrorOneChallenge {

	public static final String PATH="/Users/pei/Documents/testDataIntegrity/102400000.txt";
	public static void main(String[] args) throws IOException {
		PublicInfor pubInfor=new PublicInfor();	
		Verifier verifier=new Verifier();
		CloudServiceProvider csp=new CloudServiceProvider();
		TraceInfor time=new TraceInfor();
		int n=pubInfor.n;//�ܿ���
		int c=100;//��ս����
		int errorCounts=10;
		String method="Cube";
		//�ƻ����ݿ�
		int [] damagedBlocks=GenerateRandom.random(1, n, errorCounts);		
		FileOperation.destoryBlocks(pubInfor.filePath,pubInfor.blockSizeK, damagedBlocks);

		List<Chal>challenges=verifier.challengeGen(c, n);

		//��������������֤��
		TraceInfor.start();
		Map<String,Map<String,Element>> proofs=csp.genProof(method,challenges);
		TraceInfor.csptime+=TraceInfor.end();

		//��֤֤��
		TraceInfor.start();
		Map<String,Boolean>verResults=verifier.proofVerify(method, challenges, proofs);
		TraceInfor.vertime+=TraceInfor.end();

		//�ж��𻵿�
		TraceInfor.start();
		Map<String,Set<Integer>> blockStates=verifier.assertDamagedBlocks(method, challenges, verResults);
		TraceInfor.vertime+=TraceInfor.end();

		TraceInfor.goodBlocks=blockStates.get("goodBlocks").size();
		TraceInfor.errorBlocks=blockStates.get("errorBlocks").size();
		TraceInfor.remainBlocks=blockStates.get("remainBlocks").size();
		TraceInfor.logging(PATH);
		//�ָ����ݿ�
		FileOperation.repairBlocks(pubInfor.filePath,pubInfor.blockSizeK, damagedBlocks);
	}



}
