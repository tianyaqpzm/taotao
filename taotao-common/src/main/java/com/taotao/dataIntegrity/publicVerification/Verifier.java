package com.taotao.dataIntegrity.publicVerification;

import it.unisa.dia.gas.jpbc.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taotao.dataIntegrity.findError.FindErrorBlock;
import com.taotao.dataIntegrity.tool.GenerateRandom;
import com.taotao.dataIntegrity.tool.SortAlg;

public class Verifier {

	public PublicInfor pubInfor;
	public Verifier(){
		pubInfor=new PublicInfor();
	}
	/**
	 * ��ս��Ϣ���ڲ���
	 */
	public  static class Chal implements Comparable<Chal>{
		public int num; //����߼����
		public Element random;//��Ӧ�������
		public Chal(int num,Element random){
			this.num=num;
			this.random=random;
		}
		public int compareTo(Chal o) {
			// TODO Auto-generated method stub
			return num-o.num;
		}
		

	}
	/**
	 * У����������ս��Ϣ
	 * @param c   			У�����
	 * @param allBlocks   	ȫ������
	 * @return				��ս��Ϣchal
	 */
	public List<Chal> challengeGen(int c,int allBlocks){
		int []ran=new int[c];
		ran=GenerateRandom.random(1,allBlocks,c); //1-allBlocks�е�c����ͬ����
		SortAlg.sort(ran, 0, c-1);
		List<Chal>challenges=new ArrayList<>(c);		
		//����ÿ���Ӧ�������vi
		for(int i=0;i<c;i++){			
			challenges.add(new Chal(ran[i],pubInfor.pairing.getZr().newRandomElement()));
		}
		return challenges;
	}

	public Chal[]challengeGen(int[]blocks){
		int len=blocks.length;
		GenerateRandom.shuffle(blocks);
		Chal[] challenges=new Chal[len];
		for(int j=0;j<len;j++)	
			challenges[j++]=new Chal(blocks[j],pubInfor.pairing.getZr().newRandomElement());
		return challenges;
	}
	
	public List<Chal> challengeGen(Set<Integer>blocks){
		int len=blocks.size();		
		List<Chal> challenges=new ArrayList<>(len);
		for(int b:blocks)	
			challenges.add(new Chal(b,pubInfor.pairing.getZr().newRandomElement()));
		
		return challenges;
	}

	/**
	 *
	 * @param challenges
	 * @param proof
	 * @return true or false
	 */
	public boolean proofVerify(List<Chal>challenges,Map<String,Element> proof){
		Element aggreTMul=proof.get("aggreTMul");
		Element aggreDMul=proof.get("aggreDMul");		
		int c=challenges.size();		
		Element aggreBlock=pubInfor.pairing.getG1().newOneElement();
		for(int i=0;i<c;i++){
			byte[] data=String.valueOf(challenges.get(i).num).getBytes();
			Element Hid=pubInfor.pairing.getG1().newElementFromHash(data,0,data.length);
			Element tmp=Hid.duplicate().powZn(challenges.get(i).random);
			aggreBlock=aggreBlock.mul(tmp);
		}	
		//e(Hchal,v)	
		Element temp1 =pubInfor.pairing.pairing(aggreBlock,pubInfor.v);	
		//e(Tp,g2)
		Element temp2 = pubInfor.pairing.pairing(aggreTMul, pubInfor.g2);
		return (aggreDMul.mul(temp1)).equals(temp2)? true :false;
	}
	//�𻵿��ж���֤���
	public Map<String,Boolean> proofVerify(String method ,List<Chal>challenges,Map<String,Map<String,Element>>proofs){
		
		Map<String,Boolean> results=new HashMap<>();
		if("One".equals(method))
		{//�����֤			
			for(Chal c:challenges)			
			{
				List<Chal>one=new ArrayList<Chal>(1);
				one.add(c);
				String num=String.valueOf(c.num);
				boolean result=proofVerify(one, proofs.get(num));
				results.put(num, result);
			}
		}
		else if("Matrix".equals(method))
		{
			int len=challenges.size();
			Map<String,Integer>indexAB=FindErrorBlock.getMatrixIndex(len);
			int row=indexAB.get("row");
			int col=indexAB.get("col");	

			//����֤���
			for(int r=0;r<row;r++){
				List<Chal> rchal=new ArrayList<>();
				for(int c=0;c<col;c++){
					int index=r*col+c;
					if(index<len)
						rchal.add(challenges.get(index));
				}
				boolean result=proofVerify(rchal,proofs.get("r"+(r+1)));
				//System.out.println(result);
				results.put("r"+(r+1), (result));
			}

			//����֤���
			for(int c=0;c<col;c++){
				List<Chal> cchal=new ArrayList<>();
				for(int r=0;r<row;r++){
					int index=r*col+c;
					if(index<len)
						cchal.add(challenges.get(index));
				}
				boolean result=proofVerify(cchal,proofs.get("c"+(c+1)));
				//System.out.println(result);
				results.put("c"+(c+1), (result));
			}
		}
		else if("Cube".equals(method))
		{
			int len=challenges.size();	
			Map<String,Integer> abc=FindErrorBlock.getCubeIndex(len);
			int a=abc.get("a");
			int b=abc.get("b");
			int c=abc.get("c");

			//int [][][]V=new int [c][b][a];

			//���Xά��֤��						
			for(int i=0;i<c;i++){		
				for(int j=0;j<b;j++){
					//int [][]X=new int[c][b];
					List<Chal> xchal=new ArrayList<>();
					for(int k=0;k<a;k++){
						int index=i*a*b+j*a+k;
						if(index<len){
							xchal.add(challenges.get(index));
						}
					}
					boolean result=proofVerify(xchal,proofs.get("x"+(i+1)+(j+1)));
					results.put("x"+(i+1)+(j+1),(result));
				}

			}
			//���Yά��֤��
			//Map<String,Map<String,Element>> yproof=new HashMap<>();
			for(int i=0;i<c;i++){
				for(int k=0;k<a;k++){	
					//int [][]Y=new int[c][a];
					List<Chal> ychal=new ArrayList<>();
					for(int j=0;j<b;j++){
						int index=i*a*b+j*a+k;
						if(index<len){
							ychal.add(challenges.get(index));
						}
					}
					boolean result=proofVerify(ychal,proofs.get("y"+(i+1)+(k+1)));
					results.put("y"+(i+1)+(k+1),(result));
				}

			}

			//���Zά��֤��			
			for(int j=0;j<b;j++){			
				for(int k=0;k<a;k++){	
					//int [][]Z=new int[b][a];
					List<Chal> zchal=new ArrayList<>();
					for(int i=0;i<c;i++){
						int index=i*a*b+j*a+k;
						if(index<len){
							zchal.add(challenges.get(index));
						}
					}
					boolean result=proofVerify(zchal,proofs.get("z"+(j+1)+(k+1)));
					results.put("z"+(j+1)+(k+1),(result));
				}
			}
		}
		else
		{

		}

		return results;
	}
	public Map<String,Set<Integer>> assertDamagedBlocks(String method,List<Chal>challenges,Map<String,Boolean>verResults)
	{
		int length=challenges.size();
		int [] blockNums=new int [length];
		for(int i=0;i<length;i++){
			blockNums[i]=challenges.get(i).num;
		}
		return assertDamagedBlocks(method, blockNums, verResults);
	}
	//�ж��𻵿�
	public Map<String,Set<Integer>> assertDamagedBlocks(String method,int[]blockNums,Map<String,Boolean>verResults){
		Map<String,Set<Integer>> results=new HashMap<>();
		if("One".equals(method))
		{//�����֤
			Set<Integer> errorBlocks=new HashSet<>();
			Set<Integer> goodBlocks=new HashSet<>();
			Set<Integer> remainBlocks=new HashSet<>();
			for(int i:blockNums)
			{
				//System.out.println(i);
				boolean verR=verResults.get(String.valueOf(i));	
				if(!verR)
					errorBlocks.add(i);
				else
					goodBlocks.add(i);
			}
			results.put("errorBlocks", errorBlocks);
			results.put("goodBlocks", goodBlocks);
			results.put("remainBlocks", remainBlocks);
		}
		//������֤
		else if("Matrix".equals(method))
		{
			results=matrixAssert(blockNums,verResults);
		}
		//������֤
		else if("Cube".equals(method))
		{
			results=cubeAssert(blockNums,verResults);
		}
		//��������
		else
		{
			System.out.println("other unimplements method��");
		}
		return results;
	}

	public  Map<String,Set<Integer>> matrixAssert(int[]blockNums,Map<String,Boolean>verResults){
		int len=blockNums.length;
		//��������
		Map<String,Integer>indexAB=FindErrorBlock.getMatrixIndex(len);
		int a=indexAB.get("row");
		int b=indexAB.get("col");

		//������֤���
		List<Boolean>row=new ArrayList<>();
		List<Boolean>col=new ArrayList<>();			
		for(int i=0;i<a;i++){
			row.add(verResults.get("r"+((i+1))));
		}
		for(int j=0;j<b;j++){
			col.add(verResults.get("c"+(j+1)));
		}

		Set<Integer> errorBlocks=new HashSet<Integer>();
		Set<Integer> goodBlocks=new HashSet<Integer>();
		Set<Integer>remainBlocks=new HashSet<Integer>();

		//���Һÿ�
		for(int i=0;i<len;i++)
		{
			boolean rResult=row.get(i/b);
			boolean cResult=col.get(i%b);
			if(rResult||cResult)//ĳ�л�ĳ����֤ͨ��
			{
				goodBlocks.add(blockNums[i]);
				blockNums[i]=0;
			}			
		}		
		//���һ��鼰δȷ�������ݿ�
		for(int i=0;i<len;i++)
		{
			boolean rResult=row.get(i/b);
			boolean cResult=col.get(i%b);
			int num=blockNums[i];
			if(num!=0)
			{
				boolean isUnique=false;
				int rowUnique=0,colUnique=0,index;
				//����i�����ڵ���:(i/b)*b+0->(i/b)*b+(b-1)
				for(int j=0;j<b;j++)
				{
					index=(i/b)*b+j;
					if(index<len)
						rowUnique^=blockNums[index];
				}
				//����i��������:0*b+i%b->(a-1)*b+i%b
				for(int k=0;k<a;k++)
				{
					index=k*b+i%b;
					if(index<len)
						colUnique^=blockNums[index];
				}
				if(rowUnique==blockNums[i]||colUnique==blockNums[i])
					isUnique=true;

				if((!rResult&&!cResult)&&isUnique)//��������֤��false��Ψһδȷ����
					errorBlocks.add(num);		
				else
					remainBlocks.add(num);
			}

		}
		Map<String,Set<Integer>> result=new HashMap<>(3);
		result.put("errorBlocks", errorBlocks);
		result.put("goodBlocks", goodBlocks);		
		result.put("remainBlocks", remainBlocks);
		return result;		
	}

	public  Map<String,Set<Integer>> cubeAssert(int[]blockNums,Map<String,Boolean>verResults)
	{
		Map<String,Set<Integer>> results=new HashMap<>();
		//����������
		int len=blockNums.length;	
		Map<String,Integer> abc=FindErrorBlock.getCubeIndex(len);
		int a=abc.get("a");
		int b=abc.get("b");
		int c=abc.get("c");

		//������ս������
		int [][][]V=new int [c][b][a];
		int index=0;
		for(int i=0;i<c;i++)
		{		
			for(int j=0;j<b;j++)
			{
				for(int k=0;k<a;k++)
				{
					if (index<blockNums.length)					
						V[i][j][k]=blockNums[index++];					
					else
						V[i][j][k]=0;//���á�0�����
				}
			}
		}

		Set<Integer> errorBlocks=new HashSet<Integer>();
		Set<Integer> goodBlocks=new HashSet<Integer>();
		Set<Integer>remainBlocks=new HashSet<Integer>();

		//�жϺÿ�
		for(int i=0;i<c;i++)
		{		
			for(int j=0;j<b;j++)
			{
				for(int k=0;k<a;k++)
				{
					int blockNum=V[i][j][k];
					boolean x=verResults.get("x"+(i+1)+(j+1));
					boolean y=verResults.get("y"+(i+1)+(k+1));
					boolean z=verResults.get("z"+(j+1)+(k+1));
					if(blockNum!=0&&(x || y || z)){					
						goodBlocks.add(blockNum);//�˿�Ϊ�ÿ�
						V[i][j][k]=0;//��Ǻÿ�

					}
				}
			}
		}

		//�жϻ��顢δȷ���Ŀ�
		for(int i=0;i<c;i++)
		{
			for(int j=0;j<b;j++)
			{
				for(int k=0;k<a;k++)
				{
					int blockNum=V[i][j][k];
					if (blockNum!=0){
						boolean x=verResults.get("x"+(i+1)+(j+1));
						boolean y=verResults.get("y"+(i+1)+(k+1));
						boolean z=verResults.get("z"+(j+1)+(k+1));

						//�Ƿ�Ϊĳ��Ψһδ��ǵĿ�
						boolean isUnique=false;
						int xunique=0,yunique=0,zunique=0;
						for(int l=0;l<a;l++)
							xunique^=V[i][j][l];
						for(int m=0;m<b;m++)
							yunique^=V[i][m][k];
						for(int n=0;n<c;n++)
							zunique^=V[n][j][k];
						if (xunique==blockNum || yunique==blockNum || zunique==blockNum)
							isUnique=true;
						if((!x || !y || !z)&&isUnique)
						{		
							errorBlocks.add(blockNum);

						}
						else if(!isUnique)
							remainBlocks.add(blockNum);	
						else
							System.out.println("Cube Error!");
					}
				}
			}
		}
		results.put("goodBlocks", goodBlocks);
		results.put("errorBlocks", errorBlocks);
		results.put("remainBlocks", remainBlocks);		
		return results;

	}
}

