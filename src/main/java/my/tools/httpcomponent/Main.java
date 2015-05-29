package my.tools.httpcomponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class Main {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("Email", new String[] { "aa@12.com", "ddd@3.com" });
		params.put("Name", new String[] { "陈晨" });
		//		HttpRequest.GET("http://localhost:8080/learn_web/HttpClientServlet")
		//				.addParams(params).execute();
		String content = HttpRequest.getInstance() //
				.POST("http://localhost:8080/learn_web/HttpClientServlet")//
				.addParams(params)//
				.execute(new ResponseHandler<String>() {

					public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
						HttpEntity entity = response.getEntity();
						return EntityUtils.toString(entity);
					}
				});
		System.err.println("content: " + content);
	}
}
