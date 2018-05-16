package com.taotao.dataIntegrity.publicVerification;


import java.io.IOException;
import java.util.Properties;

import com.taotao.dataIntegrity.db.DBOperation;
import com.taotao.dataIntegrity.file.FileOperation;
import com.taotao.dataIntegrity.tool.PropertiesUtil;
import com.taotao.dataIntegrity.tool.Stopwatch;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * 数据拥有者，进行数字签名
 */
public class DataOwner {

	public static final String FILETAG = "filetag";// 存储文件块标签数据表
	public static final String PUBLIC = "public"; // 存储公开信息

	protected Pairing pairing;
	public Element g1;
	public Element g2;
	private Element[] ps;
	public Element[] us;
	private Element x;// 私钥
	public Element v;// 公钥

	public DataOwner(Pairing p) {
		this.pairing = p;
	}
	/**
	 * 初始化设置
	 * 根据段数s ， 生成 s=200个Zp 中元素随机数
	 * @param
	 * @throws IOException
	 */
	public void setup(int s) {
		g1 = pairing.getG1().newRandomElement().getImmutable();
		g2 = pairing.getG2().newRandomElement().getImmutable();
		ps = psGen(s);  // Zr 中的元素数组
		// ui=g1^ai 为每个随机数ps[i]  生成u  校验标签减少计算开销
		us = usGen(ps);
	}

	/**
	 * ���ɳ�ʼ����Կ
	 * 
	 * @return
	 */
	public void keyGen() {

		x = pairing.getZr().newRandomElement().getImmutable();// 私钥
		// 公钥：  v=g2^x
		v = g2.duplicate().powZn(x);  // 公钥

	}

	/**
	 * 生成指定s 个  G1 中的元素，随机数不需要保存 只要保存us即可
	 * @param ps 随机数的数组
	 */
	public Element[] usGen(Element[] ps) {
		int s = ps.length;
		Element[] us = new Element[s];
		for (int i = 0; i < s; i++) {
			// ui=g1^ai
			us[i] = g1.duplicate().powZn(ps[i]);
		}

		return us;
	}

	/**
	 * 根据段数s ,随机生成s个Zr 中的元素
	 * 
	 * @param s
	 * @return
	 */
	public Element[] psGen(int s) {
		Element[] ps = new Element[s];
		for (int i = 0; i < s; i++) {
			// a1,...as
			ps[i] = pairing.getZr().newRandomElement();
//			pairing.getZr().newElementFromHash()

		}
		return ps;
	}

	/**
	 * 生成某块标签
	 * 
	 * @param blockNum 块编号
	 * @param mij 块中段元素数组
	 * @return 块标签
	 */
	private Element metaGen(int blockNum, Element[] mij) {
//**VMA
		int s = mij.length; // 第i个  数据块第长度
// 生成文件块标签：t=(H(blockNum)*(g1^(aj*mij))^x
		Element aggSum = pairing.getZr().newZeroElement();
		for (int j = 0; j < s; j++) {
			aggSum = aggSum.add(ps[j].duplicate().mulZn(mij[j]));
		}
//		对块号 进行取哈希   这里是不是可以应自己的密码？？？？
		byte[] data = String.valueOf(blockNum).getBytes();
		Element Hid = pairing.getG1().newElementFromHash(data, 0, data.length);
		Element t = (Hid.duplicate().mul(g1.duplicate().powZn(aggSum))).powZn(x);

		return t;
//**/
/*		int s = mij.length;
		String viti = System.currentTimeMillis()+"blockNum";
		Element aggSum = pairing.getZr().newZeroElement();       
//		byte[] data = String.valueOf(viti).getBytes();
		byte[] data = viti.getBytes();
		Element Hid1 = pairing.getZr().newElementFromHash(data, 0, data.length);
		
		for (int j = 0; j < s; j++) {
			aggSum = aggSum.add(mij[j]);
		}
		aggSum = aggSum.add(Hid1);
		Element Hid = pairing.getG1().newElementFromHash(data, 0, data.length);
		Element t = (Hid.duplicate().mul(g1.duplicate().powZn(aggSum))).powZn(x);
		return t;
*/
	}

	/**
	 * 计算多个数据块的 数字标签    利用私钥
	 * 
	 * @param mij  多块数据集 二维数组
	 * @param count 数据块数量
	 *
	 * @return 多块的数字标签
	 */
	public Element[] metaGen(Element[][] mij, int count) {
		Element[] blockTags = new Element[count];
		for (int i = 0; i < count; i++) {
			blockTags[i] = metaGen(i + 1, mij[i]);
		}
		return blockTags; 
	}

	/**
	 * 数据所有者公开信息
	 */
	public void publicInfor() {
		DBOperation.clear(PUBLIC);
		DBOperation.storeData(PUBLIC, g1, "g1");
		DBOperation.storeData(PUBLIC, g2, "g2");
		DBOperation.storeData(PUBLIC, v, "v");
		DBOperation.storeData(PUBLIC, us, "us");
	}

	public void storeTag(Element[] tags) {
		DBOperation.clear(FILETAG);
		DBOperation.storeData(FILETAG, tags, "");
	}

}
