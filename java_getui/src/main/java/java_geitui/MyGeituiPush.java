package java_geitui;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

public class MyGeituiPush{
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
	private static String appId = "fcu0WnMzgg7a29ibvJ4dm";
	private static String appkey = "uDc6n5VBLT6Q4lqdGHHdC";
	private static String master = "Ig7heG9VXo8pEvHCmDR8t4";
	private static String CID = "31e17b34fc9d28c91e6d0ce6b8e41db3"; //clientId,启动应用时通过个推注册的广播接收值
	
	private static String host = "http://sdk.open.api.igexin.com/apiex.htm"; //使用个推开放平台地址

	//通知打开链接模板
	public static LinkTemplate linkTemplate() {
		LinkTemplate template = new LinkTemplate();
		// 设置APPID与APPKEY
		template.setAppId(appId);
		template.setAppkey(appkey);
		// 设置通知栏标题与内容
		template.setTitle("欢迎使用百度");
		template.setText("点击进入百度官网");
		// 配置通知栏图标
		//template.setLogo("icon.png");
		// 配置通知栏网络图标
		template.setLogoUrl("http://imgsrc.baidu.com/forum/pic/item/e824b899a9014c0858f1f52c0a7b02087af4f475.jpg");
		// 设置通知是否响铃，震动，或者可清除
		template.setIsRing(true);
		template.setIsVibrate(true);
		template.setIsClearable(true);
		// 设置打开的网址地址
		template.setUrl("http://www.baidu.com");
		return template;
	}
	
	//通知弹框下载模板
	public static NotyPopLoadTemplate NotyPopLoadTemplate() {
		NotyPopLoadTemplate template = new NotyPopLoadTemplate();
		// 设置APPID与APPKEY
		template.setAppId(appId);
		template.setAppkey(appkey);
		// 设置通知栏标题与内容
		template.setNotyTitle("ZDemo");
		template.setNotyContent("点击下载ZDemo.apk");
		// 配置通知栏图标
		template.setNotyIcon("icon.png");
		// 设置通知是否响铃，震动，或者可清除
		template.setBelled(true);
		template.setVibrationed(true);
		template.setCleared(true);

		// 设置弹框标题与内容
		template.setPopTitle("ZDemo");
		template.setPopContent("这是一款示例app");
		// 设置弹框显示的图片
		template.setPopImage("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
		template.setPopButton1("下载");
		template.setPopButton2("取消");

		// 设置下载标题
		template.setLoadTitle("ZDemo.apk");
		template.setLoadIcon("file://icon.png");
		//设置apk下载地址(apk保存在阿里云的云服务器上)		
		template.setLoadUrl("http://zhangmingqian.oss-cn-beijing.aliyuncs.com/ZDemo.apk");
		template.setAutoInstall(false); //是否自动安装(默认否)
		template.setActived(true); //安装完成后是否自动启动应用程序(默认否)
		
		return template;
	}
	
	/**
	 * 透传模板
	 * @return
	 */
	public static TransmissionTemplate transmissionTemplate() {
		TransmissionText myText = new TransmissionText();
		myText.setStatus("success");
		myText.setData("this is a transimission message!");
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
	
	
	/**
	 * 通知打开链接模板推送代码
	 * @param intervalTime 间隔时间
	 * @throws Exception
	 */
	public static void myLinkGeiTuiPushSDK(int intervalTime)throws Exception{
		IGtPush push = new IGtPush(host, appkey, master);
		push.connect();

		LinkTemplate template = linkTemplate();
		SingleMessage message = new SingleMessage();
		message.setOffline(true); //离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(template);

		//List<Target>targets = new ArrayList<Target>(); //可推送给多个客户端
		Target target1 = new Target();
		//Target target2 = new Target();  

		target1.setAppId(appId);
		target1.setClientId(CID);

		while (true) {
			IPushResult ret = push.pushMessageToSingle(message, target1);
			String result = ret.getResponse().toString();
			System.out.println(result);
			Thread.sleep(intervalTime);  
		}
	}
	
	public static void myNotyPopLoadGeiTuiPushSDK(int intervalTime)throws Exception{
		IGtPush push = new IGtPush(host, appkey, master);
		push.connect();

		NotyPopLoadTemplate template = NotyPopLoadTemplate();
		SingleMessage message = new SingleMessage();
		message.setOffline(true); //离线有效时间，当用户当前不在线时，是否离线存储，可选，默认不存储 ,单位为毫秒
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(template);

		//List<Target>targets = new ArrayList<Target>(); //可推送给多个客户端
		Target target1 = new Target();
		//Target target2 = new Target();  

		target1.setAppId(appId);
		target1.setClientId(CID);

		while (true) {
			IPushResult ret = push.pushMessageToSingle(message, target1);
			String result = ret.getResponse().toString();
			if("successed_online".equals(result) || "successed_offline".equals(result)){
				//用户在线,消息在线下发   或   用户离线,消息存入离线系统
			}
			System.out.println(result);
			Thread.sleep(intervalTime);  
		}
	}
	
	public static void myTransmissionGeTuiPushSDK(int intervalTime)throws Exception{
		IGtPush push = new IGtPush(host, appkey, master);
		push.connect();

		TransmissionTemplate template = transmissionTemplate();
		SingleMessage message = new SingleMessage();
		message.setOffline(true); //离线有效时间，当用户当前不在线时，是否离线存储，可选，默认不存储 ,单位为毫秒
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(template);

		Target target1 = new Target();
		target1.setAppId(appId);
		target1.setClientId(CID);

		while (true) {
			IPushResult ret = push.pushMessageToSingle(message, target1);
			String result = ret.getResponse().toString();
			if("successed_online".equals(result) || "successed_offline".equals(result)){
				//用户在线,消息在线下发   或   用户离线,消息存入离线系统
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
		//myLinkGeiTuiPushSDK(10*1000); //10s推送一次
		//myNotyPopLoadGeiTuiPushSDK(10*1000);
		myTransmissionGeTuiPushSDK(10*1000); //10s一次透传消息
	}
	
	
}
