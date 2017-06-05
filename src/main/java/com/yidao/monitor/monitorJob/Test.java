package com.yidao.monitor.monitorJob;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class Test {

	 private final static Logger logger = LoggerFactory.getLogger(Test.class); 
	public static void main(String[] args) {
//		HashMap<String,String> hashMap = Test.toHashMap("{\"name\":\"张三\",\"age\":24}");
//		
//		Set<String> keySet = hashMap.keySet();
//		for (String string : keySet) {
//			System.out.println(string);
//			System.out.println(hashMap.get(string));
//			System.out.println("---------------");
//		}
//		
//        logger.info("logback 成功了");
//        logger.error("logback 成功了");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 0);
		jsonObject.put("code1", 1);
		jsonObject.put("code2", 2);
		System.out.println(jsonObject.toJSONString());
		jsonObject.getIntValue("code");
		jsonObject.put("code", 400);
		System.out.println(jsonObject.toJSONString());
	}
	/** 
	    * 将json格式的字符串解析成Map对象 <li> 
	    * json格式：{"name":"admin","retries":"3fff","testname" 
	    * :"ddd","testretries":"fffffffff"} 
	    */  
//	   private static HashMap<String, String> toHashMap(String object)  
//	   {  
//	       HashMap<String, String> data = new HashMap<String, String>();  
//	       // 将json字符串转换成jsonObject  
//	       
//	       // 遍历jsonObject数据，添加到Map对象  
//	       Set<String> keySet = jb.keySet();
//	       for (String jsonKey : keySet) {
//	           String value = jb.getString(jsonKey);  
//	           data.put(jsonKey, value);  
//	       }
//	       return data;  
//	  } 
}
