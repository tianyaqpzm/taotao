package com.taotao.dataIntegrity.findError;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.taotao.dataIntegrity.tool.GenerateRandom;
import com.taotao.dataIntegrity.tool.SortAlg;
import com.taotao.dataIntegrity.tool.StdOut;

public class FindErrorBlock {
	public static List<Integer>errorNumBinary;		
	private Map<String,Integer> findErrorByBinaryOnce(int[] chal,int[] error)
	{	
		boolean lowError=false;
		boolean highError=false;
		int mid=chal.length/2;
		//左半段有错？？
		for(int i=0;i<mid;i++)
		{
			if(binaryfind(chal[i], error)!=-1)
			{
				lowError=true;
				break;
			}

		}
		//又半段有错？
		for(int j=mid;j<chal.length;j++)
		{
			if(binaryfind(chal[j], error)!=-1)
			{
				highError=true;
				break;
			}

		}

		int  goodBlocks=(lowError?0:mid)+(highError?0:chal.length-mid);
		int remainBlocks=chal.length-goodBlocks;
		Map<String,Integer> result=new HashMap<>(2);
		result.put("goodBlocks", goodBlocks);
		result.put("remainBlocks", remainBlocks);
		return result;
	}


	/**
	 * 查找指定范围内的错误块的编号————二分
	 * @param chal 		所有挑战的数据块
	 * @param s			起始下标
	 * @param e			终止下标
	 * @param error		给定的可能的错误编号集合
	 * @return			找错的挑战次数+1
	 */
	public  int findErrorByBinary(int []chal,int s,int e,int[]error){

		int half=(e-s+1)/2;		
		if(s==e){
			if(binaryfind(chal[s],error)!=-1){				
				errorNumBinary.add(chal[s]);
			}
			return 1;
		}
		boolean left=chalIncludeError(chal,s,s+half-1,error);
		boolean right=chalIncludeError(chal,s+half,e,error);		
		return (left?findErrorByBinary(chal,s,s+half-1,error):1)+
				(right?findErrorByBinary(chal,s+half,e,error):1)+1;

	}
	
	public  Map<String,Set<Integer>> findErrorByOneBlock(List<Integer> chal,int[]error){
		
		Set<Integer> chalSet=new HashSet<>();
		for(int i:chal){
			chalSet.add(i);
		}
		return findErrorByOneBlock(chalSet, error);
	}

	public  Map<String ,Set<Integer>> findErrorByOneBlock(Set<Integer> chal,int[]error){
		Set<Integer> errorBlocks=new HashSet<Integer>();
		Set<Integer> goodBlocks=new HashSet<Integer>();		
		int []temp=error.clone();//��¡�����������
		SortAlg.sort(temp,0,temp.length-1);//�������������
		/**
		 * ������������ս����֤
		 * */

		for(int i:chal)			
			if(binaryfind(i,temp)!=-1)
				errorBlocks.add(i);
			else goodBlocks.add(i);
		Map<String,Set<Integer>> results=new HashMap<>();
		results.put("errorBlocks", errorBlocks);
		results.put("goodBlocks", goodBlocks);
		//System.out.println("e"+errorBlocks.size());
		return results;
	}

	public  Map<String,Set<Integer>>findErrorByMatrix(Set<Integer>chal,int[] error){
		int[] chalArray=new int[chal.size()];
		int j=0;		
		for (int i:chal)
			chalArray[j++]=i;
		shuffleArray(chalArray);
		return this.findErrorByMatrix(chalArray, error);
	}

	//Ĭ�����Խ���Ϊ����chal��col�ָ���ľ���Mab
	public Map<String,Set<Integer>>findErrorByMatrix(int chal[],int[] error){
		//SortAlg.sort(error, 0, error.length-1);	
		Map<String,int[]>rcResult=getRowColResult(chal,error);
		int []rowResult=rcResult.get("rowResult");
		int []colResult=rcResult.get("colResult");	
		return findErrorByMatrix2(chal,rowResult,colResult);
	}
	
	

	/**
	 * ���þ���ķ�ʽ��λ���ݿ飬Ŀǰֻ��������
	 * ��һ��ȥ���󲿷ֺõĿ飬���»���ͻ�����ȷ���Ŀ�,�ڶ��β��á���鷨���ٴη�����ս,
	 * @param row	����ս����ĸ�����֤���,0����1��ȷ
	 * @param col	����ս����ĸ�����֤���
	 * @param chal	�����ı��1-n������������
	 * @return		�����ı�š���ս����������ս��������
	 */
	private  Map<String,Set<Integer>> findErrorByMatrix(int []chal,int[] row,int []col){

		int a=row.length;
		int b=col.length;	
		
		Set<Integer> errorBlocks=new HashSet<Integer>();
		Set<Integer> goodBlocks=new HashSet<Integer>();
		for(int i: chal)
			goodBlocks.add(i);		
		Set<Integer>remainBlocks=new HashSet<Integer>();
		int rError=isOneError(row);
		int cError=isOneError(col);
		boolean oneBlockError=(rError!=-1&&cError!=-1)?true:false;//ֻ��һ�����

		if(oneBlockError){//ֻ��һ�����			
			for(int j=0;j<b;j++){
				if(col[j]==0){
					errorBlocks.add(chal[rError*b+j]);					
					goodBlocks.remove(chal[rError*b+j]);
				}
			}

		}
		if(!oneBlockError&&rError!=-1){//һ����֤����
			for(int j=0;j<b;j++){
				if(col[j]==0){
					errorBlocks.add(chal[rError*b+j]);
					goodBlocks.remove(chal[rError*b+j]);
				}
			}
		}
		if(!oneBlockError&&cError!=-1){//һ����֤����
			for(int k=0;k<a;k++){
				if(row[k]==0){
					errorBlocks.add(chal[k*b+cError]);
					goodBlocks.remove(chal[k*b+cError]);
				}
			}


		}

		//����ȷ���κ�һ���������ݿ飬�������ų��󲿷ֺÿ�
		if(rError==-1&&cError==-1){//���ж��д���,��������ȷ���Ŀ�			
			for(int i=0;i<chal.length;i++){
				int r=i/b;
				int c=i-r*b;
				if(row[r]==0&&col[c]==0){
					remainBlocks.add(chal[i]);
					goodBlocks.remove(chal[i]);

				}				
			}

		}
		
		Map<String,Set<Integer>> result=new HashMap<>(3);
		result.put("errorBlocks", errorBlocks);
		result.put("goodBlocks", goodBlocks);		
		result.put("remainBlocks", remainBlocks);
		return result;

	}
	private  Map<String,Set<Integer>> findErrorByMatrix2(int []blockNums,int[] row,int []col){

		int a=row.length;
		int b=col.length;	
		int len=blockNums.length;
		Set<Integer> errorBlocks=new HashSet<Integer>();
		Set<Integer> goodBlocks=new HashSet<Integer>();			
		Set<Integer>remainBlocks=new HashSet<Integer>();
		//���Һÿ�
				for(int i=0;i<len;i++)
				{
					boolean rResult=(row[i/b]==1?true:false);
					boolean cResult=(col[i%b]==1?true:false);
					if(rResult||cResult)//ĳ�л�ĳ����֤ͨ��
					{
						goodBlocks.add(blockNums[i]);
						blockNums[i]=0;
					}			
				}		
				//���һ��鼰δȷ�������ݿ�
				for(int i=0;i<len;i++)
				{
					boolean rResult=(row[i/b]==1?true:false);
					boolean cResult=(col[i%b]==1?true:false);
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


	//��װ��
	public  Map<String,Set<Integer>>findErrorByCube(Set<Integer>chal,int[] error){
		int[] chalArray=new int[chal.size()];
		int j=0;		
		for (int i:chal)
			chalArray[j++]=i;
		shuffleArray(chalArray);
		return findErrorByCube(chalArray, error);
	}

	
	/**
	 * �����������ʽ������ս
	 * @param chal
	 * @param error
	 * @return
	 */
	public  Map<String,Set<Integer>>findErrorByCube(int chal[],int[] error){
		Map<String,Integer> cubeIndex=getCubeIndex(chal.length);
		//��ó����壨�����壩�ı�a��b��c
		int a=cubeIndex.get("a"),b=cubeIndex.get("b"),c=cubeIndex.get("c");		
		Set<Integer> errorBlocks=new HashSet<>();
		Set<Integer> goodBlocks=new HashSet<>();	
		Set<Integer> remainBlocks=new HashSet<>();
		int [][][]V=new int [c][b][a];
		int [][]X=new int[c][b];
		int [][]Y=new int[c][a];		
		int [][]Z=new int[b][a];
		int index=0;		
		//���㳤����������ս��ֵ0��Ϊ��ȷ��1������
		for(int i=0;i<c;i++){		
			for(int j=0;j<b;j++){
				for(int k=0;k<a;k++){
					if (index<chal.length){
						int blockNum=chal[index++];						
						V[i][j][k]=blockNum;
						if(binaryfind(blockNum, error)!=-1){//�ÿ�Ϊ����
							X[i][j]=1;
							Y[i][k]=1;
							Z[j][k]=1;							
						}						
					}
					else
						V[i][j][k]=0;//���á�0�����


				}
			}

		}
		//ȷ���ÿ�
		for(int i=0;i<c;i++){
			for(int j=0;j<b;j++){
				for(int k=0;k<a;k++){
					int blockNum=V[i][j][k];					
					if(blockNum!=0&&(X[i][j]==0||Y[i][k]==0 || Z[j][k]==0)){					
						goodBlocks.add(blockNum);//�˿�Ϊ�ÿ�
						V[i][j][k]=0;//��Ǻÿ�
					}
				}
			}
		}		
		//System.out.println("goodblock:"+goodBlocks.size());
		//���һ��顢δ�ж��Ŀ�
		
		for(int i=0;i<c;i++){
			for(int j=0;j<b;j++){
				for(int k=0;k<a;k++){
					int blockNum=V[i][j][k];
					if (blockNum!=0){
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
						if((Y[i][k]==1 || X[i][j]==1 || Z[j][k]==1)&&isUnique)
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

		Map<String,Set<Integer>> result=new HashMap<>();			
		result.put("goodBlocks", goodBlocks);
		result.put("errorBlocks", errorBlocks);
		result.put("remainBlocks", remainBlocks);		
		return result;
	}

	public static void shuffleArray(int []a){
		Random ran=new Random();
		int len=a.length;
		for(int k=0;k<len;k++)
		{
			//����a[k]��a[rand]
			int rand=k+ran.nextInt(len)%(len-k);
			int t=a[k];
			a[k]=a[rand];
			a[rand]=t;          
		}
	}
	 public static boolean isdup(int [] chal){
		  Set<Integer> a=new HashSet<>();
		  for (int i :chal){
				a.add(i);
			}
		  return a.size()!=chal.length;
	  }

	

	/**
	 * �ж�a���Ƿ�ֻ��һ��0Ԫ�أ����������±�
	 * @param a
	 * @return	0Ԫ�ص�λ��
	 */
	private  int isOneError(int []a){	//������һ����һ�д��󣬷��ش����λ��
		int position=-1;
		int count=0;
		for(int i=0;i<a.length;i++){
			if(a[i]==0){//0:��ʾ��֤����
				position=i;
				count++;
			}
		}
		return count==1?position:-1;
	}
	/**
	 * ������ս���鳤���ҳ����ֵľ���������������a<=b)
	 * @param arrayLength
	 * @return ������С���
	 */
	public static Map<String,Integer> getMatrixIndex(int arrayLength){
		Map<String,Integer> mab=new HashMap<String,Integer>(2);
		double sqrt=Math.sqrt(arrayLength);
		int up=(int)Math.ceil(sqrt);
		int down=(int)Math.floor(sqrt);
		if(up*down>=arrayLength){
			mab.put("row", down);			
		}else {
			mab.put("row", up);		
		}
		mab.put("col", up);
		return mab;
	}
	public static Map<String,Integer> getCubeIndex(int arrayLength)
	{
		Map<String,Integer> cube=new HashMap<>();
		double cbrt=Math.cbrt(arrayLength);
		int down=(int)Math.floor(cbrt);
		int b=down,c=down,a=down;
		//1.abc=len;2.a+1*b*c>len;3.a+1*b+1*c>len;4.a+1*b+1*c+1
		if(down*down*down==arrayLength)
		{
			a=down;b=down;c=down;
		}
		else if((down+1)*down*down>=arrayLength)
		{
			a=down+1;b=down;c=down;
		}
		else if((down+1)*(down+1)*down>=arrayLength)
		{
			a=down+1;
			b=down+1;
			c=down;
		}
		else if((down+1)*(down+1)*(down+1)>=arrayLength)
		{
			a=down+1;
			b=down+1;
			c=down+1;
		}


		cube.put("a", a);
		cube.put("b", b);
		cube.put("c", c);
		return cube;
	}
	/**
	 * ������ս��֤���
	 * @param chal	
	 * @param error	��������
	 * @return
	 */
	private  Map<String,int[]> getRowColResult(int[] chal,int[] error){
		int len=chal.length;
		Map<String,Integer>indexAB=getMatrixIndex(len);
		int a=indexAB.get("row");
		int b=indexAB.get("col");
		int [] row=new int[a];		
		int [] col=new int[b];	
		initIntArray(row,1);
		initIntArray(col,1);
		for(int i=0;i<len;i++){
			if(binaryfind(chal[i],error)!=-1){//�ô���
				row[i/b]=0;
				col[i%b]=0;
			}
		}
		Map<String,int[]>result=new HashMap<String,int[]>(2);
		result.put("rowResult", row);
		result.put("colResult", col);
		return result;
	}
	public static void initIntArray(int[] a,int initValue){
		int len=a.length;
		for(int i=0;i<len;i++){
			a[i]=initValue;
		}
	}
	/**
	 * ��ս�����Ƿ��������Ŀ顪�����������Ƿ�����ͬԪ��
	 * @param chal		��ս��������
	 * @param error		��������
	 * @return
	 */
	public static boolean chalIncludeError(int[] chal,int []error){
		//sort(error,0,error.length-1);
		for(int i=0;i<chal.length;i++){

			//���ҵ��򷵻�true,��һ������ȫ����ֻҪ֪��chal�д���鼴��
			if(binaryfind(chal[i],error)!=-1)return true;
		}
		return false;
	}

	/**
	 * ������ָ����Χ���Ƿ��������
	 * @param chal		��ս����
	 * @param s			��ʼ�߽�
	 * @param e			�����߽�
	 * @param error 	�������������
	 * @return		
	 */
	public static boolean chalIncludeError(int[] chal,int s,int e,int []error){
		int c=e-s+1;		
		for(int i=0;i<c;i++){
			//���ҵ��򷵻�true,��һ������ȫ����ֻҪ֪��chal�д���鼴��
			if(binaryfind(chal[s+i],error)!=-1)return true;
		}
		return false;
	}

	/**
	 * ���Ҹ�����ֵ�������ַ�
	 * @param key	������ֵ
	 * @param a		��������
	 * @return		���������е�λ��
	 */
	public static int binaryfind(int key,int []a){
		int lo=0;
		int hi=a.length-1;
		while(lo<=hi){
			int mid=lo+(hi-lo)/2;
			if(key<a[mid]) 		hi=mid-1;
			else if(key>a[mid]) lo=mid+1;
			else 				return mid;
		}
		return -1;
	}

	public static void main(String[] args){
		int [] chal={1,2,3,4,5,6,7};
		int []error={2,4,5,1,6,7};

//		int [] chal=GenerateRandom.random(1, 10000,2000,17);
		System.out.println(chal.length);

//		int[] error=GenerateRandom.random(chal,20,17);
		FindErrorBlock feb=new FindErrorBlock();
		SortAlg.sort(error, 0, error.length-1);

		Map<String,Integer> bOnce=feb.findErrorByBinaryOnce(chal,error);
		StdOut.println(bOnce.get("goodBlocks")+" "+bOnce.get("remainBlocks"));
/*
		//逐块法
		Map<String,Object> one=feb.findErrorByOneBlock(chal,error);
		StdOut.println(chal.length+" "+chal.length+" "+one.get("errorNum"));

		// 二分法
		Map<String,Object>binary=feb.findErrorByBinary(chal,error);
		StdOut.println("Binary: "+binary.get("chalCount")+" "+
				binary.get("chalBlocks")+" "+
				binary.get("errorNum"));

		// 矩阵法
		Map<String,Object>matrixResult=feb.findErrorByMatrix(chal,error);
		StdOut.println("Matrix: "+matrixResult.get("chalCount")+" "+
				matrixResult.get("chalBlocks")+" "+
				matrixResult.get("errorNum"));
*/
		//立方法
		Map<String,Set<Integer>>cubeResult=feb.findErrorByCube(chal, error);
		StdOut.println(cubeResult.get("remainBlocks"));
		StdOut.println(cubeResult.get("errorBlocks"));
		StdOut.println(cubeResult.get("goodBlocks"));
		StdOut.println(cubeResult.get("remainBlocks").size());
		StdOut.println(cubeResult.get("errorBlocks").size());
		StdOut.println(cubeResult.get("goodBlocks").size());

		//立方索引
		for(int i=0;i<10;i++)
		{
			int n=new Random().nextInt(1000);
			//	System.out.println(n);
			getCubeIndex(n);
		}
	}
}
