package life_assistant;

import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

public class LifeAssistantGeitui{
	protected final static SerializerFeature[] features = {
		SerializerFeature.SortField,
		SerializerFeature.WriteMapNullValue, 
		SerializerFeature.WriteNullListAsEmpty,
		SerializerFeature.WriteNullNumberAsZero, 
		SerializerFeature.WriteNullStringAsEmpty,
		SerializerFeature.DisableCircularReferenceDetect,
		SerializerFeature.WriteDateUseDateFormat
	};
		
	private final static SerializerFeature[] featuresNoNull = {
		SerializerFeature.SortField,
		SerializerFeature.DisableCircularReferenceDetect,
		SerializerFeature.WriteDateUseDateFormat
	};
	
	/**
	 * 这些参数均由客户端启动应用时传递过来
	 */
	private static String appId = "8u1ahUQswrA2vkXLFZiYXA";
	private static String appkey = "37jWluo5kV6ag9nrbVORnA";
	private static String master = "sNA9JArjQU7TMoWsNzgOv3";
	private static String CID = "766a93818dcf1eec9152232f27121e52"; //clientId,启动应用时通过个推注册的广播接收值
	
	private static String host = "http://sdk.open.api.igexin.com/apiex.htm"; //使用个推开放平台地址

	
	/**
	 * 透传模板
	 * @return
	 */
	public static TransmissionTemplate transmissionTemplate(String status,String data) {
		TransmissionText myText = new TransmissionText();
		myText.setStatus(status);
		myText.setData(data);
//		String json = JSONObject.toJSONString(myText,features); 
		String json = JSONObject.toJSONString(myText,featuresNoNull); //成功返回,null值不输出
		
	    TransmissionTemplate template = new TransmissionTemplate();
	    template.setAppId(appId);
	    template.setAppkey(appkey);
	    // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	    template.setTransmissionType(2);
	    template.setTransmissionContent(json);
	    // 设置定时展示时间
	    // template.setDuration("2016-01-16 11:40:00", "2016-01-16 12:24:00");
	    return template;
	}
	
	public static void myTransmissionGeTuiPushSDK(int intervalTime)throws Exception{
		IGtPush push = new IGtPush(host, appkey, master);
		push.connect();

		SingleMessage message = new SingleMessage();
		message.setOffline(true); //离线有效时间，当用户当前不在线时，是否离线存储，可选，默认不存储 ,单位为毫秒
		message.setOfflineExpireTime(24 * 3600 * 1000);

		Target target1 = new Target();
		target1.setAppId(appId);
		target1.setClientId(CID);

		while (true) {
			String data = UUID.randomUUID().toString();
			TransmissionTemplate template = transmissionTemplate("success",data);
			message.setData(template);
			
			IPushResult ret = push.pushMessageToSingle(message, target1);
			String result = ret.getResponse().toString();
			if("successed_online".equals(result) || "successed_offline".equals(result)){
				//用户在线,消息在线下发   或   用户离线,消息存入离线系统(一般应用进程被杀死后,处于离线状态;前台进程或后台进程均属于在线状态)
			}
			System.out.println(result);
			Thread.sleep(intervalTime);  
		}
	}
	
	static class TransmissionText{
		public String status;
		public String data;
		
		public void setStatus(String status) {
			this.status = status;
		}
		public void setData(String data) {
			this.data = data;
		}
	}
	
	public static void main(String[] args) throws Exception {
		myTransmissionGeTuiPushSDK(5*1000); //5s发送一次透传消息
	}
}
