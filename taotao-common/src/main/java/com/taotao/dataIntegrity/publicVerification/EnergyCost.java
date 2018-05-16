package com.taotao.dataIntegrity.publicVerification;

public class EnergyCost {
//У�����ܺļ������
private static double PsCPU=2.5;
private static double PdCPU=5;
private static double PsRF=1.25;
private static double PdRF=1.25;

//�������ܺļ������
private static int k=1;
private static int a=3;
private static double f=3.2;


/**
 * ����ʱ�����ļ���У���ߵ���������(�������ĺʹ������ģ�
 * @param TsCPU		��̬CPU����ʱ��
 * @param TdCPU		��̬CPU����ʱ��
 * @param TsRF		��̬RF����ʱ��
 * @param TdRF		��̬RF����ʱ��
 * @return			���ĵ�������
 */
public static double verEnergy(double TsCPU,double TdCPU,double TsRF,double TdRF){
	return verComputeEnergy(TsCPU,TdCPU)+verTransEnergy(TsRF,TdRF);
}

private static double verComputeEnergy(double t1,double t2){
	return PsCPU*t1+PdCPU*t2;
}

private static double verTransEnergy(double t1,double t2){
	return PsRF*t1+PdRF*t2;
}


/**
 * ������������������ġ���t*k*(f^a)
 * @param Tcsp
 * @return
 */
public static double CSPEnergy(double Tcsp){
	return Tcsp*k*Math.pow(f, a);
}

public static double energyCost(double t,double f){
	return t*k*Math.pow(f, a);
}
}
