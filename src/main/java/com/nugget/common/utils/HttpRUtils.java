package com.nugget.common.utils;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * ClassName: HttpUtils
 *
 * @Description: http请求工具类
 */
@SuppressWarnings("deprecation")
public class HttpRUtils {

	/**
	 * @param @param  reqUrl
	 * @param @param  params
	 * @param @return
	 * @param @throws Exception
	 * @Description: http get请求共用方法
	 * @date 2016年3月10日 下午3:57:39
	 */
	@SuppressWarnings("resource")
	public static String sendGetParam(String reqUrl, Map<String, String> params)
			throws Exception {
		InputStream inputStream = null;
		HttpGet request = new HttpGet();
		try {
			String url = buildUrl(reqUrl, params);
			HttpClient client = new DefaultHttpClient();
			request.setHeader("Accept-Encoding", "gzip");
			request.setURI(new URI(url));

			HttpResponse response = client.execute(request);

			inputStream = response.getEntity().getContent();
			String result = getJsonStringFromGZIP(inputStream);
			return result;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			request.releaseConnection();
		}

	}


	/**
	 * @param @param  reqUrl
	 * @param @param  params
	 * @param @return
	 * @param @throws Exception
	 * @Description: http get请求共用方法
	 * @date 2016年3月10日 下午3:57:39
	 */
	@SuppressWarnings("resource")
	public static String sendGet(String reqUrl, Map<String, String> params)
			throws Exception {
		InputStream inputStream = null;
		HttpGet request = new HttpGet();
		try {
//			String url = buildUrl(reqUrl, params);
			HttpClient client = new DefaultHttpClient();
			request.setHeader("Accept-Encoding", "gzip");
			request.setURI(new URI(reqUrl));
			RequestConfig config = RequestConfig.custom().setConnectTimeout(600000) //连接超时时间
					.setConnectionRequestTimeout(600000) //从连接池中取的连接的最长时间
					.setSocketTimeout(60 *1000) //数据传输的超时时间
					.setStaleConnectionCheckEnabled(true) //提交请求前测试连接是否可用
					.build();
			//设置请求配置时间
			request.setConfig(config);

			HttpResponse response = client.execute(request);

			inputStream = response.getEntity().getContent();
			String result = getJsonStringFromGZIP(inputStream);
			return result;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			request.releaseConnection();
		}

	}

	/**
	 * @param @param  reqUrl
	 * @param @param  params
	 * @param @return
	 * @param @throws Exception
	 * @Description: http post请求共用方法
	 */
	@SuppressWarnings("resource")
	public static String sendPost(String url, String data){
	String response = null;

    try {
		       CloseableHttpClient httpclient = null;
		        CloseableHttpResponse httpresponse = null;
		       try {
		       	JSONArray jsonArray= new JSONArray(data);
			            httpclient = HttpClients.createDefault();
			            HttpPost httppost = new HttpPost(url);
			           StringEntity stringentity = new StringEntity(jsonArray.toString(), "UTF-8");
				        stringentity.setContentEncoding("UTF-8");
				        stringentity.setContentType ("application/json");

			            httppost.setEntity(stringentity);

			            httpresponse = httpclient.execute(httppost);

			           response = EntityUtils
			                    .toString(httpresponse.getEntity());

			       } finally {
			           if (httpclient != null) {
				httpclient.close();
			}
			           if (httpresponse != null) {
				               httpresponse.close();
				           }
			        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
//		JSONObject jsonObject = new JSONObject(response);
    return response;
	}

	/**
	 * @param @param  urls
	 * @param @param  params
	 * @param @return
	 * @param @throws ClientProtocolException
	 * @param @throws IOException
	 * @Description: http post请求json数据
	 */
	public static String sendPostBuffer(String urls, String params)
			throws ClientProtocolException, IOException {
		HttpPost request = new HttpPost(urls);

		StringEntity se = new StringEntity(params, HTTP.UTF_8);
		request.setEntity(se);
		// 发送请求
		@SuppressWarnings("resource")
  HttpResponse httpResponse = new DefaultHttpClient().execute(request);
		// 得到应答的字符串，这也是一个 JSON 格式保存的数据
		String retSrc = EntityUtils.toString(httpResponse.getEntity());
		request.releaseConnection();
		return retSrc;

	}

	/**
	 * @param @param  urlStr
	 * @param @param  xmlInfo
	 * @param @return
	 * @Description: http请求发送xml内容
	 */
	public static String sendXmlPost(String urlStr, String xmlInfo) {
		// xmlInfo xml具体字符串

		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");
			OutputStreamWriter out = new OutputStreamWriter(
					con.getOutputStream());
			out.write(new String(xmlInfo.getBytes("utf-8")));
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String lines = "";
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				lines = lines + line;
			}
			return lines; // 返回请求结果
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "fail";
	}

	private static String getJsonStringFromGZIP(InputStream is) {
		String jsonString = null;
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			bis.mark(2);
			// 取前两个字节
			byte[] header = new byte[2];
			int result = bis.read(header);
			// reset输入流到开始位置
			bis.reset();
			// 判断是否是GZIP格式
			int headerData = getShort(header);
			// Gzip 流 的前两个字节是 0x1f8b
			if (result != -1 && headerData == 0x1f8b) {
				// LogUtil.i("HttpTask", " use GZIPInputStream  ");
				is = new GZIPInputStream(bis);
			} else {
				// LogUtil.d("HttpTask", " not use GZIPInputStream");
				is = bis;
			}
			InputStreamReader reader = new InputStreamReader(is, "utf-8");
			char[] data = new char[100];
			int readSize;
			StringBuffer sb = new StringBuffer();
			while ((readSize = reader.read(data)) > 0) {
				sb.append(data, 0, readSize);
			}
			jsonString = sb.toString();
			bis.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	private static int getShort(byte[] data) {
		return (data[0] << 8) | data[1] & 0xFF;
	}

	/**
	 * 构建get方式的url
	 *
	 * @param reqUrl 基础的url地址
	 * @param params 查询参数
	 * @return 构建好的url
	 */
	public static String buildUrl(String reqUrl, Map<String, String> params) {
		StringBuilder query = new StringBuilder();
		Set<String> set = params.keySet();
		for (String key : set) {
			query.append(String.format("%s=%s&", key, params.get(key)));
		}
		return reqUrl + "?" + query.toString().substring(0,query.toString().length()-1);
	}

	public static void test(String ADD_URL,List<Map<String, Object>> list) {
		//创建连接
		try {
			URL url = new URL(ADD_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.connect();
		//POST请求
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());

		out.write(list.toString().getBytes("UTF-8"));
		out.flush();
		out.close();
		//读取响应
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String lines;
		StringBuffer sb = new StringBuffer("");
		while ((lines = reader.readLine()) != null) {
			lines = new String(lines.getBytes(), "utf-8");
			sb.append(lines);
		}
		System.out.println(sb);
		reader.close();
		// 断开连接
		connection.disconnect();
	} catch(
	MalformedURLException e)

	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch(
	UnsupportedEncodingException e)

	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch(
	IOException e)

	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


	public static String sendPost1(String reqUrl, Map<String, String> params)
			throws Exception {
		try {
			Set<String> set = params.keySet();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (String key : set) {
				list.add(new BasicNameValuePair(key, params.get(key)));
			}
			if (list.size() > 0) {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost request = new HttpPost(reqUrl);

					request.setHeader("Accept-Encoding", "gzip");
					request.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
							client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
					client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
					HttpResponse response = client.execute(request);

					InputStream inputStream = response.getEntity().getContent();
					try {
						String result = getJsonStringFromGZIP(inputStream);

						return result;
					} finally {
						inputStream.close();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					throw new Exception("网络连接失败,请连接网络后再试");
				}
			} else {
				throw new Exception("参数不全，请稍后重试");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("发送未知异常");
		}
	}

}
