package org.mm.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HTTPRequestHandler {

    public static String getRequest(String url) throws Exception {
        String result = "";
        HttpGet request = new HttpGet(url);
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (response.getEntity() != null)
                result = EntityUtils.toString(entity);
        }
        return result;
    }
}
