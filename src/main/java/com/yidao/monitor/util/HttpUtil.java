package com.yidao.monitor.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http工具类
 * 
 * @author shaoxiangfei
 *
 */
public class HttpUtil {

	private final CloseableHttpClient httpclient;
	private static final int SIZE = 1024 * 1024;
	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
	public  TrustManager trustManager = null;
    public SSLConnectionSocketFactory socketFactory = null;
	private static class HttpUtilHolder {
		private static final HttpUtil INSTANCE = new HttpUtil();
	}

	public static HttpUtil getIntance() {
		return HttpUtilHolder.INSTANCE;
	}
     
	private HttpUtil() {
			trustManager = new X509TrustManager() {			
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// TODO Auto-generated method stub
				
			}
		};
		enableSSL();
//		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//		// 将最大连接数增加到300
//		cm.setMaxTotal(300);
//		// 将每个路由基础的连接增加到100
//		cm.setDefaultMaxPerRoute(100);
		// 链接超时setConnectTimeout ，读取超时setSocketTimeout
//		RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(15000)
//				.build();
		RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(15000).
				setExpectContinueEnabled(true).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,AuthSchemes.DIGEST)).setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
	    Registry<ConnectionSocketFactory> socketFactoryRegistry =RegistryBuilder.<ConnectionSocketFactory>create().register("http",PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();
	    PoolingHttpClientConnectionManager conneManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	    conneManager.setMaxTotal(300);
	    conneManager.setDefaultMaxPerRoute(100);
		httpclient = HttpClients.custom().setConnectionManager(conneManager).setDefaultRequestConfig(defaultRequestConfig)
				.build();

		ThreadUtil.submit(new IdleConnectionMonitorThread(conneManager));
	}
	public void enableSSL(){
		try{
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[]{trustManager}, null);
			socketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	/**
	 * 编码默认UTF-8
	 * 
	 * @param url
	 * @return
	 */
	public String get(String url) {
		return this.get(url, CHARSET_UTF8.toString());
	}

	/**
	 * @param url
	 * @param code
	 * @return
	 */
	public String get(String url, final String code ) {
		String res = null;

		try {
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(final HttpResponse response) throws IOException {
					int status = response.getStatusLine().getStatusCode();
					if(status>=200&&status<300){
						HttpEntity entity = response.getEntity();
						return entity!=null?EntityUtils.toString(entity, code):null;
					}else{
						return status+"";
					}
				}

			};
			res = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			logger.error(url, e);
			res = e.getMessage();
			if(StringUtils.isEmpty(res)){
				res = "error unknow";
			}
		}
		return res;
	}

	public List<String> getList(String url) {
		List<String> res = null;
		try {
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<List<String>> responseHandler = new ResponseHandler<List<String>>() {
				@Override
				public List<String> handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						List<String> result = new ArrayList<String>();
						HttpEntity entity = response.getEntity();
						if (entity == null) {
							return result;
						}
						BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()), SIZE);
						while (true) {
							String line = in.readLine();
							if (line == null) {
								break;
							} else {
								result.add(line);
							}
						}
						in.close();
						return result;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			res = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			logger.error(url, e);
		}
		return res;
	}

	private String post(String url, List<NameValuePair> params, String code,String cookies) {
		String res = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			if(cookies!=null){
				httpPost.setHeader("Cookie",cookies );	
			}
			if (params != null)
				httpPost.setEntity(new UrlEncodedFormEntity(params, code));
			response = httpclient.execute(httpPost);
			HttpEntity entity2 = response.getEntity();
			res = EntityUtils.toString(entity2, code);
			EntityUtils.consume(entity2);
		} catch (Exception e) {
			logger.error(url, e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
		}
		return res;
	}

	/**
	 * 默认UTF-8
	 * cookies没有传null即可
	 * @param url
	 * @param params
	 * @return
	 */
	public String post(String url, Map<String, ?> params,String cookies) {
		return this.post(url, params, CHARSET_UTF8.toString(),cookies);
	}

	/**
	 * @param url
	 * @param params
	 * @param code
	 * @return
	 */
	public String post(String url, Map<String, ?> params, String code,String cookies) {
		List<NameValuePair> nvps = null;
		if (params != null && params.size() > 0) {
			nvps = new ArrayList<NameValuePair>();
			for (Entry<String, ?> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
		}
		return this.post(url, nvps, code,cookies);
	}

	// 监控有异常的链接
	private static class IdleConnectionMonitorThread extends Thread {

		private final HttpClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
			super();
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(5000);
						// 关闭失效的连接
						connMgr.closeExpiredConnections();
						// 可选的, 关闭30秒内不活动的连接
						connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	public String postBody(String url, String body) throws Exception{
		String res = null;
			HttpPost httppost = new HttpPost(url);
			if (StringUtils.isNotBlank(body)) {
				httppost.setHeader("RecContentType", "application/json");
				httppost.setEntity(new StringEntity(body, CHARSET_UTF8));
			}
			CloseableHttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				res = EntityUtils.toString(resEntity, CHARSET_UTF8);
				EntityUtils.consume(resEntity);
			}
		return res;
	}
	
	/**
	 * 获取URL返回的http状态码
	 * @param url
	 * @return
	 */
	public static int httpCode(String url) {
		// 用getResponseCode可以获取URL返回状态码
		int code = 0;
		try {
			URL surl = new URL(url);
			URLConnection rulConnection = surl.openConnection();
			HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
			httpUrlConnection.setConnectTimeout(300000);
			httpUrlConnection.setReadTimeout(300000);
			httpUrlConnection.connect();
			code = new Integer(httpUrlConnection.getResponseCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return code;
	}
}