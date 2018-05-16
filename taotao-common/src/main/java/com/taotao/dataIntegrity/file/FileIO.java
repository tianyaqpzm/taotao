package com.taotao.dataIntegrity.file;

import it.unisa.dia.gas.jpbc.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileIO {
	public static final int K=1024;
	public static final int M=1024*1024;
	/**
	 * ���ֽ�Ϊ��λ��ȡ�ļ��������ڶ��������ļ�����ͼƬ��������Ӱ����ļ���
	 * @param filePath �ļ�����
	 */
	public static void readFileByBytes(String filePath)throws Exception{
		File file = new File(filePath);
		InputStream in = null;			
		in = new FileInputStream(file);
		while(in.read() != -1){	
			//�Զ��������ݽ��д���
		}		
		in.close();

	}
	/**
	 * ���ֽ�Ϊ��λ��ȡ�ļ���һ�ζ����ֽ�
	 * @param filePath �ļ�·��
	 */
	public static void readFileByMulBytes(String filePath)throws Exception{
		InputStream in = null;
		byte[] tempbytes = new byte[100];
		in = new FileInputStream(filePath);	
		while ((in.read(tempbytes)) != -1){
			//�Զ��������ݽ��д���
		}		
		if (in != null)				
			in.close();			



	}
	/**
	 * ���ַ�Ϊ��λ��ȡ�ļ��������ڶ��ı������ֵ����͵��ļ�
	 * @param filePath �ļ���
	 */
	public static void readFileByChars(String filePath)throws Exception{
		File file = new File(filePath);
		Reader reader = null;

		// һ�ζ�һ���ַ�
		reader = new InputStreamReader(new FileInputStream(file));
		int tempchar;
		while ((tempchar = reader.read()) != -1){
			//����windows�£�rn�������ַ���һ��ʱ����ʾһ�����С�
			//������������ַ��ֿ���ʾʱ���ỻ�����С�
			//��ˣ����ε�r����������n�����򣬽������ܶ���С�
			if (((char)tempchar) != 'r'){
				System.out.print((char)tempchar);
			}
		}
		reader.close();


	}

	public static void readFileByMulChars(String filePath)throws Exception{
		Reader reader = null;
		try {
			System.out.println("���ַ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�����ֽڣ�");
			//һ�ζ�����ַ�
			char[] tempchars = new char[30];
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(filePath));

			while ((charread = reader.read(tempchars))!=-1){
				//ͬ�����ε�r����ʾ
				if ((charread == tempchars.length)&&(tempchars[tempchars.length-1] != 'r')){
					System.out.print(tempchars);
				}else{
					for (int i=0; i<charread; i++){
						if(tempchars[i] == 'r'){
							continue;
						}else{
							System.out.print(tempchars[i]);
						}
					}
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}


	/**
	 * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
	 * @param filePath �ļ���
	 */
	public static void readFileByLines(String filePath)throws Exception{
		File file = new File(filePath);
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(file));
		String tempString = null;
		int line = 1;
		//һ�ζ���һ�У�ֱ������nullΪ�ļ�����
		while ((tempString = reader.readLine()) != null){
			//handle tempString
			line=line+1;
		}
		reader.close();

	}

	//ͨ��ɨ���ļ������У���ȡ��Ҫ�Ĳ�����
	//lineNum ����
	public static List<String> readFileByLines(String filePath,int []lineNums)throws Exception{
		List<String> wantedLines=new ArrayList<String>(10);
		File file = new File(filePath);
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(file));
		String tempString = null;
		int line = 1;
		int i=0;
		//һ�ζ���һ�У�ֱ������nullΪ�ļ�����
		while ((tempString = reader.readLine()) != null&&i<lineNums.length){
			if(line==lineNums[i]){
				i++;
				wantedLines.add(tempString);
			}
			line++;
		}
		reader.close();
		return wantedLines;

	}



	/**
	 * �����ȡ�ļ���һ�ζ�ȡn��ָ�����ȣ�������	
	 * @param filePath
	 * @param beginIndex
	 * @param length
	 * @param n
	 */
	public static void readFileByRandomAccess(String filePath,int beginIndex,int length,int n)throws Exception{
		RandomAccessFile randomFile = null;				
		randomFile = new RandomAccessFile(filePath, "r");
		long fileLength = randomFile.length();	
		int randomPosit;
		for (int i = 0; i < n; i++) {
			//��λ���λ��
			randomPosit=new Random().nextInt((int)(fileLength-length));			
			randomFile.seek(randomPosit);	
			int byteread = 0;
			while(byteread<length){
				randomFile.readByte();
				byteread++;
			}	
		}
		if (randomFile != null)				
			randomFile.close();


	}

	/**
	 * �����ȡ�ļ���һ�ζ�ȡһ��ָ������
	 * @param filePath
	 * @param beginIndex
	 * @param blockSizeK ��Ĵ�С
	 * @param length	50��������
	 */
	public static void readFileByRandomAccess(String filePath,long beginIndex,long length)throws Exception{
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(filePath, "r");
			long fileLength = randomFile.length();			
			randomFile.seek(beginIndex);			
			if(length>(fileLength-beginIndex))//���ļ�ĩβ�ĳ��Ȳ���length
				length=(fileLength-beginIndex);
			int byteread = 0;
			while(byteread<length){//����ֽڶ�ȡlength����
				randomFile.readByte();
				byteread++;
			}	

		} catch (IOException e){
			e.printStackTrace();
		} finally {
			if (randomFile != null){
				try {

					randomFile.close();
				} catch (IOException e1) {
				}
			}
		}
	}


	/**
	 * �����ȡ�ļ�����n��	
	 * @param filePath
	 * @param beginIndex
	 * @param length ÿ�г���
	 * @param n			
	 */
	public static List<String> readSequentMulLine(String filePath,int beginIndex,int length,int n)throws Exception{
		List<String> readData=new ArrayList<String>();
		RandomAccessFile randomFile = new RandomAccessFile(filePath, "r");
		randomFile.seek(beginIndex);
		for (int i = 0; i < n; i++) {
			//��λ���λ��			
			String data=randomFile.readLine();
			readData.add(data);			

		}
		if (randomFile != null)				
			randomFile.close();
		return readData;

	}
	//�ļ���¼���Ƕ������޷�ʵ�� 
	public static List<String> readRandomMulLine(String filePath,int [] nBeginIndex,int length,int n)throws Exception{
		List<String> readData=new ArrayList<String>();
		RandomAccessFile randomFile = new RandomAccessFile(filePath, "r");
		for (int i = 0; i < n; i++) {
			randomFile.seek(nBeginIndex[i]*length);
			//��λ���λ��			
			String data=randomFile.readLine();
			readData.add(data);			

		}
		if (randomFile != null)				
			randomFile.close();
		return readData;

	}

	/**
	 * ��ȡ���ļ������д�����MappedByteBuffer���л���
	 * @param fileName
	 * @param fileLength
	 * @param buffSize
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void readLargeFile(String fileName,long fileLength,int buffSize) throws FileNotFoundException, IOException {  	       

		File file = new File(fileName);  
		//fileLength = file.length();  
		@SuppressWarnings("resource")
		FileChannel fc=new RandomAccessFile(file, "r").getChannel();
		MappedByteBuffer inputBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0,	fileLength);// ��ȡ���ļ�  

		byte[] dst = new byte[buffSize];// ÿ�ζ���3M������  

		for (int offset = 0; offset < fileLength; offset += buffSize) {  
			if (fileLength - offset >= buffSize) { 				
				inputBuffer.get(dst);  
			} else {  //���һ�鲻��buffsize��С�����⴦��
				for (int i = 0; i < fileLength - offset; i++)  
					dst[i] = inputBuffer.get(offset + i);  
			}  
			// ���õ���3M���ݽ��м��㣻  
		}
		fc.close();

	}  
	
	/**
	 * A����׷���ļ���ʹ��RandomAccessFile
	 *
	 * @param fileName		 �ļ���
	 *           
	 * @param content		׷�ӵ�����
	 * @throws IOException 
	 *            
	 */
	public static void appendString(String fileName, String content) throws IOException {

		// ��һ����������ļ���������д��ʽ
		RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
		// �ļ����ȣ��ֽ���
		long fileLength = randomFile.length();
		// ��д�ļ�ָ���Ƶ��ļ�β��
		randomFile.seek(fileLength);
		randomFile.writeBytes(content);
		randomFile.close();

	}

	public static void appendByteArray(String fileName, byte[] content) throws IOException {

		// ��һ����������ļ���������д��ʽ
		RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
		// �ļ����ȣ��ֽ���
		long fileLength = randomFile.length();
		// ��д�ļ�ָ���Ƶ��ļ�β��
		randomFile.seek(fileLength);
		randomFile.write(content);
		randomFile.close();
	}

	public static void appendElementArray(String fileName, Element[] content) throws IOException {
		// ��һ����������ļ���������д��ʽ
		RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");

		// ��д�ļ�ָ���Ƶ��ļ�β��
		long fileLength = randomFile.length();		
		randomFile.seek(fileLength);

		for(int i=0;i<content.length;i++){
			randomFile.write(content[i].toBytes());
			randomFile.writeBytes("\n\r");
		}
		randomFile.close();
	}

	public static void appendListElement(String fileName, List<List<Element>> content) throws IOException {
		int size=content.size();

		// ��һ����������ļ���������д��ʽ
		RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");

		// ��д�ļ�ָ���Ƶ��ļ�β��
		long fileLength = randomFile.length();		
		randomFile.seek(fileLength);

		for(int i=0;i<size;i++){
			List<Element> data=content.get(i);
			int length=data.size();
			for(int j=0;j<length;j++){
				randomFile.write(data.get(j).toBytes());
			}
			randomFile.writeBytes("\n\r");
		}
		randomFile.close();
	}

	public static void appendListElementArray(String fileName, List<Element[]> content) throws IOException {
		int size=content.size();

		// ��һ����������ļ���������д��ʽ
		RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");

		// ��д�ļ�ָ���Ƶ��ļ�β��
		long fileLength = randomFile.length();		
		randomFile.seek(fileLength);
		for(int i=0;i<size;i++){
			Element[] data=content.get(i);
			int length=data.length;
			for(int j=0;j<length;j++){
				randomFile.write(data[i].toBytes());
			}
			randomFile.writeBytes("\n\r");
		}
		randomFile.close();
	}
	/**
	 * B����׷���ļ���ʹ��FileWriter
	 *
	 * @param fileName
	 * @param content
	 * @throws IOException 
	 */
	public static void appendMethodB(String fileName, String content) throws IOException {

		// ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
		FileWriter writer = new FileWriter(fileName, true);
		writer.write(content);
		writer.close();

	}

	/**
	 * �������ָ����С�ļ�
	 * @param fileSzieM ��MΪ��λ
	 * @throws IOException
	 */
	public static void genRandomFile( String filePath,int fileSzieM) throws IOException{
		String str = "0123456789vasdjhklsadfqwiurewopt"; //�Լ���ȫ��ĸ������,����ַ�������Ϊ���ȡֵ��Դ
		PrintWriter pw = new PrintWriter(new FileWriter(filePath));
		int len = str.length();		
		for (int i = 0; i < K; i++)
		{
			StringBuilder s = new StringBuilder();
			for (int j = 0; j < (fileSzieM*K); j++)
			{
				s.append(str.charAt((int)(Math.random()*len)));
			}
			pw.print(s.toString());
		}
		pw.close();
	}

	public static void writeElementArray(String fileName, Element[] content) throws IOException {
		// ��һ����������ļ���������д��ʽ
		RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
		for(int i=0;i<content.length;i++){
			randomFile.write(content[i].toBytes());
			randomFile.writeBytes("\n");
		}
		randomFile.close();
	}
	//
	public static void writeListListElement(String fileName, List<List<Element>> content) throws IOException {
		int size=content.size();
		// ��һ����������ļ���������д��ʽ
		RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
		for(int i=0;i<size;i++){
			List<Element> data=content.get(i);
			int length=data.size();
			for(int j=0;j<length;j++){
				randomFile.write(data.get(j).toBytes());
			}
		}
		randomFile.close();
	}

	public static void writeListElementArray(String fileName, List<Element[]> content) throws IOException {
		int size=content.size();
		// ��һ����������ļ���������д��ʽ
		RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
		for(int i=0;i<size;i++){
			Element[] data=content.get(i);
			int length=data.length;
			for(int j=0;j<length;j++){
				randomFile.write(data[j].toBytes());
			}
		}
		randomFile.close();
	}


	/**
	 * д���ļ������ڴ�ӳ���ļ�
	 * @param fileName	
	 * @param length	
	 * @throws Exception
	 */
	public static void writeLargeFile(String fileName,int length)throws Exception {
		@SuppressWarnings("resource")
		FileChannel fc = new RandomAccessFile(fileName, "rw").getChannel();  
		//ע�⣬�ļ�ͨ���Ŀɶ���дҪ�������ļ�������ɶ�д�Ļ���֮��  
		MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, length);  
		//д128M������  
		for (int i = 0; i < length; i++) {  
			out.put((byte) 'x');  
		}  
		System.out.println("Finished writing"); 

		fc.close();  
	}  



	//���߳�д�ļ��������Կ����ǰ�������
	public static void FileWriteMulThread(String fileName,int nThread,int [] nSkip,String[] content) throws Exception{
		// Ԥ�����ļ���ռ�Ĵ��̿ռ䣬�����лᴴ��һ��ָ����С���ļ�  
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");  
		raf.setLength(1*M); // Ԥ���� 1M ���ļ��ռ�  
		raf.close();  		
		// ���ö��߳�ͬʱд��һ���ļ�  
		for(int i=0;i<nThread;i++){	
			new FileWriteThread(fileName,nSkip[i],content[i].getBytes()).start(); // ���ļ���1024�ֽ�֮��ʼд������  

		}	 
	}  

	// �����߳����ļ���ָ��λ��д��ָ������  
	static class FileWriteThread extends Thread{  
		private int skip;  
		private byte[] content;  
		private String fileName;
		public FileWriteThread(String fileName,int skip,byte[] content){  
			this.skip = skip;  
			this.content = content;  
		}  

		public void run(){  
			RandomAccessFile raf = null;  
			try {  
				raf = new RandomAccessFile(fileName, "rw");  
				raf.seek(skip);  
				raf.write(content);  
			} catch (FileNotFoundException e) {  
				e.printStackTrace();  
			} catch (IOException e) { 				
				e.printStackTrace();  
			} finally {  
				try {  
					raf.close();  
				} catch (Exception e) {  
				}  
			}  
		}  

	}

	static class FileReadThread extends Thread{
		private int skip;
		private int length;
		private String fileName;
		public FileReadThread(String fileName,int skip,int length) {
			this.skip=skip;
			this.fileName=fileName;
			this.length=length;
		}
		public void run(){  
			RandomAccessFile raf = null;  
			byte[] readTemp=new byte[length];
			try {  
				raf = new RandomAccessFile(fileName, "rw");  
				raf.seek(skip); 
				raf.read(readTemp);      
			} catch (FileNotFoundException e) {  
				e.printStackTrace();  
			} catch (IOException e) {  				
				e.printStackTrace();  
			} finally {  
				try {  
					raf.close();  
				} catch (Exception e) {  
				}  
			}  
		}  

	}


}

