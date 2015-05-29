package my.tools.httpcomponent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public abstract class HttpService {

	public static String DEFAULT_CHARSET = Consts.UTF_8.name();

	public abstract HttpUriRequest getRequest();

	/** requestConfig **/
	private RequestConfig requestConfig;

	//扩展编码,目前默认使用UTF-8,如需使用其他编码,Add->HttpService.setCharset();
	public String getHttpMsgCharset() {
		return DEFAULT_CHARSET;
	}

	public void addHeaders(Map<String, String> headers) {
		for (Map.Entry<String, String> header : headers.entrySet()) {
			getRequest().addHeader(header.getKey(), header.getValue());
		}
	};

	public void addParams(Map<String, String[]> params) {

	};

	public void setHttpEntity(HttpEntity entity) {

	};

	public String execute() throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = createHttpClient();
		try {
			CloseableHttpResponse response = httpClient.execute(getRequest());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long content_len = entity.getContentLength();
				if (content_len != -1 && content_len < 2048) {
					return EntityUtils.toString(entity);
				} else {
					ByteArrayOutputStream output = new ByteArrayOutputStream(1 << 11);//2048
					entity.writeTo(output);
					return output.toString(getHttpMsgCharset());
				}
			}
			return "";
		} finally {
			httpClient.close();
		}
	}

	public <T> T execute(ResponseHandler<T> responseHandler) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = createHttpClient();
		try {
			return httpClient.execute(getRequest(), responseHandler);
		} finally {
			httpClient.close();
		}
	}

	/**
	 * 创建HttpClient对象
	 * @return
	 */
	public CloseableHttpClient createHttpClient() {
		//HttpClients.createDefault();
		return HttpClients.custom().setRetryHandler(new HttpRequestRetryHandler() {
			/** 重连机制 **/
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 5) {
					// Do not retry if over max retry count
					return false;
				}
				if (exception instanceof InterruptedIOException) {
					// Timeout
					return false;
				}
				if (exception instanceof UnknownHostException) {
					// Unknown host
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {// Connection refused
					return false;
				}
				if (exception instanceof SSLException) {
					// SSL handshake exception
					return false;
				}
				return false;
			}
		}).build();
	}

	/**
	 * 默认的Request配置,如果没有配置request,启用默认配置
	 * @return
	 */
	public RequestConfig createDefaultRequestConfig() {
		return RequestConfig.custom() //
				.setSocketTimeout(3 * 1000) // 3s
				.setConnectTimeout(3 * 1000) //3s
				.build();
	}

	/** http请求配置 **/
	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}
}
