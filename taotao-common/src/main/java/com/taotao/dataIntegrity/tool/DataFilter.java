package com.taotao.dataIntegrity.tool;

public class DataFilter {
	//�������뱣��precisionλС��
		public static Double roundDouble(double val, int precision) {  
			  Double ret = null;  
			        try {  
			           double factor = Math.pow(10, precision);  
			           ret = Math.floor(val * factor + 0.5) / factor;  
			        } catch (Exception e) {  
			           e.printStackTrace();  
			        }  
			       return ret;  
			    } 
		
}
