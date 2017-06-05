//package com.yidao.monitor.util;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.testng.Assert;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//public class InterfaceUtils {
//
//public Map<String,String> createAnOrder(){
//	 PSFClient psf = null;
//	 PSFClient.PSFRPCRequestData request = null;	
//	 JSONObject jsonObj = null;
//	 Map<String,String> orderParams = null;
//	 String orderId = "";
//	try{
//		//测试环境
//		String[] serviceCenter = {"10.0.11.71:5201","10.0.11.72:5201"};
//		//线上环境
////		String[] serviceCenter = {"172.17.0.77:5201","172.17.0.78:5201"};
//		 psf = new PSFClient(serviceCenter);	
//		 request = new PSFClient.PSFRPCRequestData();
//		//测试环境
//				String [] uids = {"13025137","13025135","13025134","13025133","13025131","13025127","13025126","13025124","13025123","13025122","13025118","13025116"};
//				String [] phones={"16809340982","16820161007","16899000094","16899000093","16832332152","16886318967","16820150516","16801018326","16810522684","16845645601","16891220002","16890938134"};
//		//线上环境
////				String [] uids = {"4982424","13859399","5697951"};
////				String [] phones={"13671126358","15910470744","15727388190"};		
//				JSONArray car_list = null;
//			     boolean acceptRS = false;
//			     int carNumbr = 0;
//				for(int i=0;i<3;i++){
//					request.data="";
//					request.service_uri = "state/createOrder?user_id="+uids[i]+"&corporate_id=0&passenger_phone="+phones[i]+"&passenger_name=王芳&passenger_number=1&city=hlbe&product_type_id=1&fixed_product_id=0&car_type_id=3&car_type_ids=3&source=20000001&expect_start_time="+(System.currentTimeMillis()/1000+120)+"&in_coord_type=baidu&expect_end_latitude=36.9021&expect_end_longitude=100.1521&expect_start_latitude=36.9022&expect_start_longitude=100.1522&start_position=testaddr&start_address=testaddr&end_position=testaddr&end_address=testaddr&flight_number=0&is_asap=1&app_version=iWeidao/6.2.5 D/877035&media_id=1&sms=passenger&time_span=0&has_custom_decision=1&is_need_manual_dispatch=0&is_auto_dispatch=1&estimate_price=0&device_id=0&corporate_dept_id=0&estimate_price=100.0&estimate_info=D123,T3700&flag=2&create_order_longitude=36.9022&create_order_latitude=36.9022&ip=10.1.7.202&order_port=60428&dispatch_type=2&time_length=1800";
//					String response = psf.call("order", request);
//				    System.out.println("----"+response);
//				    jsonObj = JSONObject.fromString(response);		    
//				   orderId = jsonObj.getJSONObject("result").getString("service_order_id");		    
//				    request.data = "";
//				    String params = "order_id="+orderId+"&out_coord_type=baidu&filter_driver_ids=0&count=5";
//				    System.out.println("orderid:"+params);
//				    request.service_uri = "Dispatch/getAcceptCars?"+params;
//					 for(int j=0;j<3;j++){
//						 Thread.sleep(3000); 
//						 response = psf.call("dispatch", request);
//					     System.out.println(response.toString());
//					     jsonObj = JSONObject.fromString(response);
//					     if(jsonObj.getInt("ret_code")==498){
//					    	 break;
//					     }else if(jsonObj.getInt("ret_code")==200){
//					    	 carNumbr = jsonObj.getJSONArray("car_list").length();
//						     System.out.println("carNumbr"+carNumbr);
//					    	 if(carNumbr>0){
//					    		 car_list = jsonObj.getJSONArray("car_list");
//					    		 orderParams = new HashMap<String,String>();
//					    		 orderParams.put("order_id", orderId);					    		 
//					    		 //获取司机端列表
//					    		 int driverId = car_list.getJSONObject(0).getInt("driver_id");
//								 int car_id = car_list.getJSONObject(0).getInt("car_id");
//								 orderParams.put("driverId", driverId+"");
//								 orderParams.put("car_id", car_id+"");
//								 System.out.println("order_id:"+orderId+",--car_id:"+car_id);
//								 request.data = "";
//								String param="service_order_id="+orderId+"&driver_id="+driverId+"&coupon_member_id=0&third_party_coupon=0";
//							    System.out.println("userDecision:"+param); 
//								request.service_uri = "Dispatch/userDecision?"+param;	
//							     psf.call("dispatch", request);	
//							     acceptRS=true;
//						    	 break;	 
//					    	 }
//					     }
//					 }	
//					 //如果司机抢单成功，则跳出循环，否则则再次创建订单，重复创建订单3次还抢单抢单失败则报警
//					 if(acceptRS){
//						 break;
//					 }
//			}
//   }catch(Exception e){
//	   e.printStackTrace();
//   }
//	return orderParams;
// }
//}
