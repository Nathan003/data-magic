package com.dodoca.datamagic.common.utils;

import com.dodoca.dataMagic.common.model.BaseResponse ;
import com.dodoca.dataMagic.common.model.HttpDelete;
import com.dodoca.dataMagic.common.model.HttpGet;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpClientUtils {

    private static Logger logger = Logger.getLogger(HttpClientUtils.class);

    /**
     * 发送request请求，采用json和utf-8
     *
     * @param requestBase
     * @param data
     * @param header
     * @return
     */
    public static BaseResponse request(HttpEntityEnclosingRequestBase requestBase, String data, Map<String, String> header) {
        logger.debug(requestBase.getURI().toString());
        logger.debug(data);
        logger.debug(header);
        //设置Header
        if (header != null) {
            for (String name : header.keySet()) {
                requestBase.addHeader(name, header.get(name));
            }
        }
        try {
            //设置参数
            if (StringUtils.isEmpty(data)) {
                data = "{}";
            }
            requestBase.addHeader("Content-type", "application/json; charset=utf-8");
            requestBase.addHeader("Accept", "application/json");
            StringEntity s = new StringEntity(data, Charset.forName("UTF-8"));
            requestBase.setEntity(s);
            long start = System.currentTimeMillis();
            HttpResponse httpResponse = new DefaultHttpClient().execute(requestBase);
            long start2 = System.currentTimeMillis();
            logger.debug("调用接口" + requestBase.getURI() + "耗时：" + (start2 - start));
            return toHttpClientResponse(httpResponse);

        } catch (Exception e) {
            logger.debug(e.getMessage());
            return new BaseResponse(403, e.getMessage());
        }
    }

    /**
     * 发送request请求，采用json和utf-8
     *
     * @param requestBase
     * @param data
     * @param headerName
     * @param headerValue
     * @return
     */
    public static BaseResponse request(HttpEntityEnclosingRequestBase requestBase, String data, String headerName, String headerValue) {
        HashMap<String, String> header = new HashMap<String, String>();
        if (!StringUtils.isEmpty(headerName) && !StringUtils.isEmpty(headerValue)) {
            header.put(headerName, headerValue);
        }
        return request(requestBase, data, header);
    }

    /**
     * 发送get请求
     *
     * @param url
     * @param data
     * @param header
     * @return
     */
    public static BaseResponse get(String url, String data, Map<String, String> header) {
        HttpGet request = new HttpGet(url);
        return request(request, data, header);
    }

    /**
     * 发送get请求
     *
     * @param url
     * @param data
     * @param headerName
     * @param headerValue
     * @return
     */
    public static BaseResponse get(String url, String data, String headerName, String headerValue) {

        //创建request
        HttpGet request = new HttpGet(url);
        return request(request, data, headerName, headerValue);

    }

    /**
     * 发送post请求
     *
     * @param url
     * @param data
     * @param header
     * @return
     */
    public static BaseResponse post(String url, String data, Map<String, String> header) {
        HttpPost request = new HttpPost(url);
        return request(request, data, header);
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param data
     * @param headerName
     * @param headerValue
     * @return
     */
    public static BaseResponse post(String url, String data, String headerName, String headerValue) {

        //创建request
        HttpPost post = new HttpPost(url);
        //设置请求的内容为 chunked类型

        return request(post, data, headerName, headerValue);

    }

    /**
     * 发送put请求
     *
     * @param url
     * @param data
     * @param header
     * @return
     */
    public static BaseResponse put(String url, String data, Map<String, String> header) {
        HttpPut request = new HttpPut(url);
        return request(request, data, header);
    }

    /**
     * 发送put请求
     *
     * @param url
     * @param data
     * @param headerName
     * @param headerValue
     * @return
     */
    public static BaseResponse put(String url, String data, String headerName, String headerValue) {

        //创建request
        HttpPut request = new HttpPut(url);
        return request(request, data, headerName, headerValue);

    }

    /**
     * 发送delete请求
     *
     * @param url
     * @param data
     * @param header
     * @return
     */
    public static BaseResponse delete(String url, String data, Map<String, String> header) {
        HttpDelete request = new HttpDelete(url);
        return request(request, data, header);
    }

    /**
     * 发送delete请求
     *
     * @param url
     * @param data
     * @param headerName
     * @param headerValue
     * @return
     */
    public static BaseResponse delete(String url, String data, String headerName, String headerValue) {

        //创建request
        HttpDelete request = new HttpDelete(url);
        return request(request, data, headerName, headerValue);

    }


    /**
     * 将HttpClient的响应转成自定义的response
     *
     * @param httpResponse
     * @return
     */
    private static BaseResponse toHttpClientResponse(HttpResponse httpResponse) {
        BaseResponse baseResponse = new BaseResponse();
        //设置baseResponse
        try {

            logger.debug(httpResponse.getEntity().isChunked() + ":" + httpResponse.getEntity().getContent());
            ContentType contentType = ContentType.getOrDefault(httpResponse.getEntity());
//            Charset charset = contentType.getCharset();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), charset));
//            String content = "";
//            String line = null;
//            while ((line = reader.readLine()) != null){
//                content += line;
//            }
//            logger.debug(content);
            baseResponse.setData(EntityUtils.toString(httpResponse.getEntity()));
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }

        baseResponse.setStatus(httpResponse.getStatusLine().getStatusCode());

        //设置Header
        Header[] headers = httpResponse.getAllHeaders();
        for (Header headerTemp : headers) {
            if ("Set-Cookie".equals(headerTemp.getName())) {

                baseResponse.addCookie(getCookie(headerTemp.getValue()));
            } else {
                baseResponse.setHeader(headerTemp.getName(), headerTemp.getValue());
            }
        }
        return baseResponse;
    }

    /**
     * 将字符串转成cookie
     *
     * @param value
     * @return
     */
    private static Cookie getCookie(String value) {
        Cookie cookie = null;
        if (StringUtils.isEmpty(value)) {
            return cookie;
        }
        int index = value.indexOf("=");

        if (index <= 0) {
            return cookie;
        }

        String name = value.substring(0, index);
        String[] cookieEntrys = value.split(";");

        for (int i = 0; i < cookieEntrys.length; i++) {
            String[] cookieKeyValue = cookieEntrys[i].split("=");
            if (0 == i) {
                if (cookieKeyValue.length == 2) {
                    cookie = new Cookie(cookieKeyValue[0], cookieKeyValue[1]);
                } else {
                    break;
                }
            }
            if ("Path".equals(cookieKeyValue[0])) {
                //设置path
                cookie.setPath(cookieKeyValue[1]);
            } else if ("Expires".equals(cookieKeyValue[0])) {
                //将时间 转化为秒数
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date ftime = null;
                try {
                    ftime = sdf.parse(cookieKeyValue[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long str = (ftime.getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) str);
            }

        }

        return cookie;
    }


    public static void main(String[] args) throws Exception {
        /*String url = "http://123.56.25.140:8088/api/auth/login?project=wxrrd_test&token=f7eeaf2371fbbc9c9937a0bf1d725b382c4e5304bd259949dfa609cd651cbf7d";
        String json = "{\"username\":\"admin\",\"password\":\"dodocatestproduct\",\"expired_interval\":0}";
        List<Cookie> cookies = post(url, json, null).getCookies();
        //String ret = httpGet(url).toString();
        for (Cookie cookie : cookies) {
			System.out.println("name:"+cookie.getName()+"-----"+cookie.getValue());
		}*/
//        String url = "http://123.56.25.140:8107/api/events/report/?bookmarkId=684&project=wxrrd_test_product_new&token=16ec56de81bd0e4eb9d379cd3c58374b09d072757a2bf0122af649ac2933f00c";
//        String data = "{\"measures\":[{\"expression\":\"sum(event.pay_order1018.ORDER_amount)*1|%2f\",\"events\":[\"pay_order1018\"],\"name\":\"支付金额\",\"format\":\"%2f\"},{\"expression\":\"count(event.pay_order1018)*1|%d\",\"events\":[\"pay_order1018\"],\"name\":\"支付订单数\",\"format\":\"%d\"},{\"expression\":\"uniqcount(event.pay_order1018)*1|%d\",\"events\":[\"pay_order1018\"],\"name\":\"支付买家数\",\"format\":\"%d\"},{\"expression\":\"uniqavg(event.pay_order1018.ORDER_amount)*1|%2f\",\"events\":[\"pay_order1018\"],\"name\":\"客单价\",\"format\":\"%2f\"},{\"expression\":\"sum(event.confirm_order1018.real_amount)*1|%d\",\"events\":[\"confirm_order1018\"],\"name\":\"订单金额GMV\",\"format\":\"%d\"},{\"expression\":\"count(event.pay_order1018)/count(event.confirm_order1018)*1|%2p\",\"events\":[\"pay_order1018\",\"confirm_order1018\"],\"name\":\"下单支付率\",\"format\":\"%2p\"},{\"expression\":\"uniqavg(event.pay_order1018)*1|%2f\",\"events\":[\"pay_order1018\"],\"name\":\"客单数u003cbru003e(客均支付单数)\",\"format\":\"%2f\"},{\"expression\":\"avg(event.pay_order1018.real_amount)*1|%2f\",\"events\":[\"pay_order1018\"],\"name\":\"支付订单均额\",\"format\":\"%2f\"}],\"unit\":\"day\",\"filter\":{\"conditions\":[{\"field\":\"event.$Anything.SHOP_id\",\"function\":\"equal\",\"params\":[13288398]}]},\"by_fields\":[\"event.$Anything.GUIDER_title\"],\"chartsType\":\"line\",\"sampling_factor\":64,\"from_date\":\"2016-12-01\",\"to_date\":\"2016-12-07\",\"bookmarkid\":\"684\",\"rollup_date\":\"false\"}";
//        BaseResponse baseResponse = post(url, data, null);
//        System.out.println(baseResponse.getData());
//        System.out.println(baseResponse.getHeader("Transfer-Encoding"));
    }

}
