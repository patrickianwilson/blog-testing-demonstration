package com.github.patrickianwilson.template.java.web.shortener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by pwilson on 9/20/16.
 */
public class GoogleURLShortenerAdaptor implements URLShortenerAdaptor {

    public static final String SHORTENER_SERVICE_ENDPOINT = "https://www.googleapis.com/urlshortener/v1/url?key=%s";
    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String apiKey;

    public GoogleURLShortenerAdaptor(String apiKey) {
        this.apiKey = apiKey;
    }

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @Override
    public String shortenUrl(String longUrl) {
        HttpPost urlShortenerReq = new HttpPost(String.format(SHORTENER_SERVICE_ENDPOINT, apiKey));
        urlShortenerReq.addHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON);
        urlShortenerReq.setEntity(new StringEntity(gson.toJson(new UrlInfoTransferObject().withLongUrl(longUrl)), Charset.forName("UTF-8")));

        try (CloseableHttpResponse urlShortenerResp = httpClient.execute(urlShortenerReq)) {
            if (urlShortenerResp.getStatusLine().getStatusCode() == 403 || urlShortenerResp.getStatusLine().getStatusCode() == 400) {
                throw new UrlShorteningServiceUnavailableException("Unauthorized! Looks like an invalid Google API Key.");
            }
            HttpEntity respPayload = urlShortenerResp.getEntity();

            UrlInfoTransferObject responseObject = gson.fromJson(new InputStreamReader(respPayload.getContent()), UrlInfoTransferObject.class);

            return responseObject.getId();
        } catch (IOException e) {
           throw new UrlShorteningServiceUnavailableException(e);
        }
    }

    private static class UrlInfoTransferObject {
        private String kind;
        private String id;
        private String longUrl;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLongUrl() {
            return longUrl;
        }

        public void setLongUrl(String longUrl) {
            this.longUrl = longUrl;
        }

        public UrlInfoTransferObject withLongUrl(String longUrl) {
            this.longUrl = longUrl;
            return this;
        }
    }
}
