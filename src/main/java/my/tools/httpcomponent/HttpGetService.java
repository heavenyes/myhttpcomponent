package my.tools.httpcomponent;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpGetService extends HttpService {

	private HttpGet get;

	public HttpGetService(String uri) {
		get = new HttpGet(uri);
//		RequestConfig config = createRequestConfig();
//		get.setConfig(config);
	}

	@Override
	public HttpUriRequest getRequest() {
		return get;
	}

	@Override
	public void addParams(Map<String, String[]> params) {
		StringBuilder uriArgs = new StringBuilder();
		for (Map.Entry<String, String[]> param : params.entrySet()) {
			String paramName = param.getKey();
			String[] paramValues = param.getValue();
			for (String paramValue : paramValues) {
				try {
					//中文,URL编码
					if (isContainsChinese(paramValue))
						paramValue = URLEncoder.encode(paramValue,
								getHttpMsgCharset());

				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
				uriArgs.append(paramName).append("=").append(paramValue)
						.append("&");
			}
		}
		// delete last '&'
		int argsLen = uriArgs.length();
		if (uriArgs.length() > 0)
			uriArgs.delete(argsLen - 1, argsLen);
		try {
			String newUri = get.getURI().toString() + "?" + uriArgs;
			URI resetUri = new URI(newUri);
			get.setURI(resetUri);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 判断字符串中是否包含中文
	 * @param str
	 * @return
	 */
	private  boolean isContainsChinese(String str) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find()) {
			flg = true;
		}
		return flg;
	}
}
