
Simple Http request framework,based apache httpcomponent.

Usage
	HttpRequest.getInstance()
			   .setRequestConfig()
			   .GET()|POST()
			   .addHeaders()
			   .addParams()|setRequestEntity()
			   .setClientConfig()
			   .execute();
examples: Main.java
	