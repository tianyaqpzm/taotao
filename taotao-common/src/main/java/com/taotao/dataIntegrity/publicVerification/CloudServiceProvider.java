package com.taotao.dataIntegrity.publicVerification;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
//import com.taotao.dataIntegrity.publicVerification.Verifier.Chal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taotao.dataIntegrity.db.DBOperation;
import com.taotao.dataIntegrity.file.FileOperation;
import com.taotao.dataIntegrity.findError.FindErrorBlock;
import com.taotao.dataIntegrity.publicVerification.Verifier.Chal;


public class CloudServiceProvider {	
	public static final String FILETAG="filetag";//����ļ����ǩ���ݱ�filetag
	protected PublicInfor pubInfor;	
	public CloudServiceProvider(){	
		pubInfor=new PublicInfor();
	}
	
	/**
	 * CSP��������������֤��proof
	 * @param challenges  У����ս����ţ��������
	 * @param cdata   �û���Կ
	 * @param  ti ���ݶ��۳ɵĹ�����Ϣֵ
	 * @return    ����������֤��
	 */
	public Map<String,Element> genProof(List<Chal> challenges,List<byte[]>cdata,List<Element>ti)
	{
		Element v=pubInfor.v;
		Element []us=pubInfor.us;
		Pairing pairing=pubInfor.pairing;
		int c=challenges.size();	
		Element aggreDMulTemp=pairing.getG1().newOneElement();//���ݿ��۳�
		Element aggreSum;
		//���ݿ���۳�
		for(int j=0;j<pubInfor.sectors;j++){
			aggreSum=pairing.getZr().newZeroElement();//ÿ����������һ����ʼ0Ԫ��
			for(int i=0;i<c;i++){				
				Element[]mij=sectorsPerBlock(cdata.get(i));
				//aggreSumj=sum(vi*mij)
				aggreSum=aggreSum.add(challenges.get(i).random.duplicate().mulZn(mij[j]));
			}			
			//��uj^aggreSumj
			aggreDMulTemp=aggreDMulTemp.mul(us[j].duplicate().powZn(aggreSum));
		}
		//e((��uj^aggreSumj),v)
		Element aggreDMul=pairing.pairing(aggreDMulTemp, v);

		Element aggreTMul=pairing.getG1().newOneElement();//���ǩ�۳�
		//У���ǩ���۳�
		for(int i=0;i<c;i++){			
			//mul(ti^vi)
			aggreTMul=aggreTMul.mul(ti.get(i).duplicate().powZn(challenges.get(i).random));
		}

		//ȫ�ֱ���proof������Ϣ
		Map<String,Element> proof=new HashMap<>();
		proof.put("aggreDMul", aggreDMul);
		proof.put("aggreTMul", aggreTMul);		
		return proof;

	}
/*	public Map<String,Element> genProof(Chal[] challenges){
		byte []cdata=readData(challenges);//�������ļ�		
		Element[] ti=readTags(challenges);//�����ݿ��б�ǩ
		return genProof(challenges,cdata,ti);
	}*/
	public Map<String,Element> genProof(List<Chal> challenges){
		List<byte []>cdata=readData(challenges);//�������ļ�		
		List<Element>ti=readTags(challenges);//�����ݿ��б�ǩ
		return genProof(challenges,cdata,ti);
	}

	public Map<String,Map<String,Element>> genProof(String method,List<Chal> challenges)
	{
		List<byte []>data=readData(challenges);//�������ļ�
		List<Element> tag=readTags(challenges);//�����ݿ��б�ǩ
		Map<String,Map<String,Element>> proofs=new HashMap<>();
		if("One".equals(method)){
			//��鷨����֤��			
			for(int i=0;i<challenges.size();i++){
				List<Chal> one=new ArrayList<>(1);
				List<Element>oneTag=new ArrayList<>(1);
				List<byte[]>oneBlock=new ArrayList<>(1);
				one.add(challenges.get(i));
				oneBlock.add(data.get(i));
				oneTag.add(tag.get(i));
				proofs.put(String.valueOf(one.get(0).num),genProof(one,oneBlock,oneTag));
			}
		}else if("Matrix".equals(method)){
			//��������֤�ݡ��������ȴ��
			int len=challenges.size();			
			Map<String,Integer>rowCol=FindErrorBlock.getMatrixIndex(len);
			int row = rowCol.get("row");
			int col=rowCol.get("col");			
			//��������ս
			for(int r=0;r<row;r++){
				List<Chal> rchal=new ArrayList<>();
				List<Element>rTag=new ArrayList<>();
				List<byte[]>rData=new ArrayList<>();
				for(int c=0;c<col;c++){
					int index=r*col+c;
					if(index<len)
					{
						rchal.add(challenges.get(index));
						rData.add(data.get(index));
						rTag.add(tag.get(index));
						
					}
						
				}				
				proofs.put("r"+(r+1),genProof(rchal,rData,rTag));

			}			
			//��������ս
			for(int c=0;c<col;c++){			
				List<Chal> cchal=new ArrayList<>();
				List<Element>cTag=new ArrayList<>();
				List<byte[]>cData=new ArrayList<>();
				for(int r=0;r<row;r++){
					int index=r*col+c;
					if(index<len)
					{
						cchal.add(challenges.get(index));
						cTag.add(tag.get(index));
						cData.add(data.get(index));
						
					}
						
				}				
				proofs.put("c"+(c+1),genProof(cchal,cData,cTag));

			}		

		}else if("Cube".equals(method)){
			//����������֤�ݡ���һ���Զ�ȡ�������ݣ�������
			int len=challenges.size();	
			Map<String,Integer> abc=FindErrorBlock.getCubeIndex(len);
			int a=abc.get("a");
			int b=abc.get("b");
			int c=abc.get("c");
             
			//������ս������
			//int [][][]V=new int [c][b][a];
			
			//Xά��֤��						
			for(int i=0;i<c;i++){		
				for(int j=0;j<b;j++){
					List<Chal> xchal=new ArrayList<>();
					List<byte[]>xdata=new ArrayList<>();
					List<Element>xtag=new ArrayList<>();
					for(int k=0;k<a;k++){
						int index=i*a*b+j*a+k;
						if(index<len){
							xchal.add(challenges.get(index));
							xdata.add(data.get(index));
							xtag.add(tag.get(index));
						}
					}
					proofs.put("x"+(i+1)+(j+1),
							genProof(xchal,xdata,xtag));
				}

			}
			//Yά��֤��
			//Map<String,Map<String,Element>> yproof=new HashMap<>();
			for(int i=0;i<c;i++){
				for(int k=0;k<a;k++){	
					//int [][]Y=new int[c][a];
					List<Chal> ychal=new ArrayList<>();
					List<byte[]>ydata=new ArrayList<>();
					List<Element>ytag=new ArrayList<>();
					for(int j=0;j<b;j++){
						int index=i*a*b+j*a+k;
						if(index<len){
							ychal.add(challenges.get(index));
							ydata.add(data.get(index));
							ytag.add(tag.get(index));
						}
					}
					proofs.put("y"+(i+1)+(k+1),
							genProof(ychal,ydata,ytag));
				}

			}
			
			//Zά��֤��			
			for(int j=0;j<b;j++){			
				for(int k=0;k<a;k++){	
					//int [][]Z=new int[b][a];
					List<Chal> zchal=new ArrayList<>();
					List<byte[]>zdata=new ArrayList<>();
					List<Element>ztag=new ArrayList<>();
					for(int i=0;i<c;i++){
						int index=i*a*b+j*a+k;
						if(index<len){
							zchal.add(challenges.get(index));
							zdata.add(data.get(index));
							ztag.add(tag.get(index));
						}
					}
					proofs.put("z"+(j+1)+(k+1),
							genProof(zchal,zdata,ztag));
				}

			}			
		}else{
			//�����ϲ��ķ�ʽ����֤��
		}
		return proofs;
	}
	
	
	//��ȡ����ս������ݡ�����ս������
	private List<byte[]> readData(List<Chal>challenges) {		
		int c=challenges.size();
		int []blockNums=new int[c];
		for(int i=0;i<c;i++)
			blockNums[i]=challenges.get(i).num;			
		return  FileOperation.readBlocks(pubInfor.filePath,pubInfor.blockSize,blockNums);
		
	}

	private Element[]sectorsPerBlock(byte[]oneBlock){		
		return FileOperation.preProcessBlock(pubInfor.sectors, oneBlock, pubInfor.sectorSize, pubInfor.pairing.getZr());
	}

    //��ȥ��ս���У���ǩ
	private List<Element> readTags(List<Chal>challenges){
		DBOperation dbo=new DBOperation();
		Map<String,byte[]> results=dbo.selectBatch(FILETAG, "");
		List<Element>ctags=new ArrayList<>();
		for (int i=0;i<challenges.size();i++){
			ctags.add((pubInfor.pairing.getG1().newElementFromBytes(results.get(String.valueOf(challenges.get(i).num)))));
		}
		return ctags;
	}
}
