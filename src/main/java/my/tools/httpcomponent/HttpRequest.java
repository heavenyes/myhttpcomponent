package my.tools.httpcomponent;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;

/**
 * 
 * @Function :Http简易客户端{支持发送GET|POST请求}
 * @Usage : HttpRequest.getInstance()
 * 					   .GET|POST(url) 
 * 				       .addHeaders() 
 *                     .addParams() 
 *                     .execute();
 * @Date 2015年3月4日
 * @Author Jacc
 * @Contact heavenyes@163.com
 * Thread not safe,per-request per instance.
 */
public class HttpRequest {

	private HttpService httpService;

	private HttpRequest() {
	}

	public static HttpRequest getInstance() {
		return new HttpRequest();
	}

	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}

	public HttpRequest GET(String uri) {
		httpService = new HttpGetService(uri);
		return this;
	}

	public HttpRequest POST(String uri) {
		httpService = new HttpPostService(uri);
		return this;
	}
	/**
	 * 设置请求配置
	 * @param requestConfig
	 * @return
	 */
	public HttpRequest setRequestConfig(RequestConfig requestConfig){
		httpService.setRequestConfig(requestConfig);
		return this;
	}
	/**
	 * 添加消息头
	 * 
	 * @param headers
	 */
	public HttpRequest addHeaders(Map<String, String> headers) {
		httpService.addHeaders(headers);
		return this;
	}

	/**
	 * 添加请求K-V参数{name=Jack&Email=Jack@126.com}
	 * 
	 * @param params
	 */
	public HttpRequest addParams(Map<String, String[]> params) {
		httpService.addParams(params);
		return this;
	}

	/**
	 * Post请求设置请求体(可以通过请求体传递参数内容或者二进制数据)
	 * 
	 * @param entity
	 */
	public HttpRequest setHttpEntity(HttpEntity entity) {
		httpService.setHttpEntity(entity);
		return this;
	}

	/**
	 * 执行请求体方法
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String execute() throws ClientProtocolException, IOException {
		return httpService.execute();
	}

	/**
	 * 执行请求体方法,自定义Response处理
	 * @param responseHandler
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public <T> T execute(ResponseHandler<T> responseHandler)
			throws ClientProtocolException, IOException {
		return httpService.execute(responseHandler);
	}
}
