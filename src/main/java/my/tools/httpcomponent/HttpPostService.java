package my.tools.httpcomponent;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

public class HttpPostService extends HttpService {
	private HttpPost post;

	public HttpPostService(String uri) {
		post = new HttpPost(uri);
//		RequestConfig config = createRequestConfig();
//		post.setConfig(config);
	}

	@Override
	public HttpUriRequest getRequest() {
		return post;
	}

	@Override
	public void addParams(Map<String, String[]> params) {
		if (params != null && !params.isEmpty()) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(
					params.size());
			for (Map.Entry<String, String[]> param : params.entrySet()) {
				String paramName = param.getKey();
				String[] paramValues = param.getValue();
				for (String paramValue : paramValues) {
					pairs.add(new BasicNameValuePair(paramName, paramValue));
				}
			}
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs,
						getHttpMsgCharset());
				setHttpEntity(entity);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void setHttpEntity(HttpEntity entity) {
		post.setEntity(entity);
	}

}
