package com.realoldroot.nginx;

import com.realoldroot.nginx.util.ConstDefine;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * https://github.com/yzprofile/ngx_http_dyups_module
 * 调用该接口，当返回码HTTP_INTERNAL_SERVER_ERROR 500为时，您需要重新加载nginx以使Nginx处于良好状态。
 * 如果得到了HTTP_CONFLICT 409，则需要稍后再次重新发送相同的命令。
 * <p>
 * 如果HTTP_NO_CONTENT 204没有upstream，则/list和/detail接口将返回。
 */
public class NginxUpstreamClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NginxUpstreamClient.class);


    static void update(String upstreamName, String data) {
        LOGGER.info("update upstream : {} data : {}", upstreamName, data);
        System.out.println(upstreamName + "\t" + data);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(ConstDefine.URL + "/" + upstreamName);
            httpPost.setEntity(new StringEntity(data));
            String responseBody = httpClient.execute(httpPost, httpResponse -> {
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status < 200 || status >= 300) {
                    LOGGER.error("httpClient response error, statusCode %d response %s", status, EntityUtils.toString(httpResponse.getEntity()));
                }
                HttpEntity entity = httpResponse.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 粗颗粒度删除，整个upstream删除
     *
     * @param upstreamName upstreamName
     */
    static void remove(String upstreamName) {
        LOGGER.info("remove upstream : {}", upstreamName);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete(ConstDefine.URL + "/" + upstreamName);
            String responseBody = httpClient.execute(httpDelete, httpResponse -> {
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status < 200 || status >= 300) {
                    LOGGER.error("httpClient response error, statusCode %d response %s", status, EntityUtils.toString(httpResponse.getEntity()));
                }
                HttpEntity entity = httpResponse.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
