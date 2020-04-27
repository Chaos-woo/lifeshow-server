package per.chao.lifeshow.utils;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/1/13 14:12
 **/
public class HttpClientUtils {
	private static final String CHARSET = "UTF-8";

	public static String doGet(String url) {
		return doGet(url, null);
	}

	/**
	 * 模拟get请求
	 * @param url 请求地址
	 * @param param 请求参数
	 * @return
	 */
	public static String doGet(String url, Map<String, String> param) {
		CloseableHttpClient client = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				param.forEach(builder::addParameter);
			}
			URI uri = builder.build();
			HttpGet httpGet = new HttpGet(uri);
			response = client.execute(httpGet);

			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), CHARSET);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	public static String doPost(String url) {
		return doPost(url, null);
	}

	/**
	 * 模拟get请求
	 * @param url 请求地址
	 * @param param 请求参数
	 * @return
	 */
	public static String doPost(String url, Map<String, String> param) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";

		try {
			HttpPost httpPost = new HttpPost(url);
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				param.forEach((k, v) -> paramList.add(new BasicNameValuePair(k, v)));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	public static String doPostWithJson(String url, String json) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";

		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);

			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	public static String getRedirectInfo(String path){
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(path);
		try {
			//将HttpContext对象作为参数传给execute()方法,则HttpClient会把请求响应交互过程中的状态信息存储在HttpContext中
			HttpResponse response = httpClient.execute(httpGet, httpContext);
			//获取重定向之后的主机地址信息
			HttpHost targetHost = (HttpHost)httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
			//获取实际的请求对象的URI
			HttpUriRequest realRequest = (HttpUriRequest)httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
			return targetHost.toString() + realRequest.getURI();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		return "";
	}
}
