import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.*;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HtmlFinder {
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(3))
            .setConnectionRequestTimeout(Timeout.ofSeconds(3))
            .build();
    private static CloseableHttpClient httpcs = HttpClients.createDefault();
//    httpclient = HttpClientBuilder.create()
//                  .setMacConnTotal(config.maxConnTotal)
    public static void main(String[] args) {
//        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        //get请求
        HttpGet httpget = new HttpGet("https://baike.baidu.com/item/%E5%85%8B%E8%8B%8F%E9%B2%81/3738237?fr=aladdin");
        httpget.setConfig(requestConfig);
        HttpResponse response = null;
        try {
            response = httpcs.execute(httpget);
            CloseableHttpResponse closeableHttpResponse = httpcs.execute(httpget);
            System.out.println(response.toString());
            System.out.println("response=" +
                    response.getCode());
            HttpEntity entity =  closeableHttpResponse.getEntity();
            InputStream inputStream = entity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
                    Charset.defaultCharset()));
            StringBuffer stringBuffer = new StringBuffer();
            String lineString = null;
            while ((lineString = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineString);
            }
            Document document = Jsoup.parse(stringBuffer.toString());
            Elements elements = document.select("a[href]");
            for (Element element:elements) {
                System.out.println(element.attr("href"));
            }
            inputStream.close();
            httpcs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //post请求
//        HttpPost httppost = new HttpPost("http://hotels.ctrip.com/Domestic/ShowHotelList.aspx");
//        httppost.setConfig(requestConfig);
//        List<NameValuePair> nameValuePairs = new ArrayList<>(3);
//        nameValuePairs.add(new BasicNameValuePair("checkIn", "2020-4-15"));
//        nameValuePairs.add(new BasicNameValuePair("checkOut", "2020-4-25"));
//        nameValuePairs.add(new BasicNameValuePair("cityId", "1"));
//        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//        try {
//            CloseableHttpResponse response1 = httpcs.execute(httppost);
//            HttpEntity entity = response1.getEntity();
////            Header[] responesHeaders = response1.getHeaders();
//            System.out.println("response=" +
//                    response1.getCode());
////            System.out.println(entity);
//            InputStream inputStream = entity.getContent();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
//            StringBuffer stringBuffer = new StringBuffer();
//            String lineString = null;
//            while ((lineString = bufferedReader.readLine()) != null) {
//                stringBuffer.append(lineString);
////                stringBuffer.append("\n");
//            }
//            bufferedReader.close();
//            inputStream.close();
//            Document doc = Jsoup.parse(stringBuffer.toString());
//            Elements elements = doc.getElementsByClass("compare_pop");
//            for (Element element:elements) {
////                System.out.println(element.text());
//                System.out.println(element.toString());
//            }
//            Elements links = doc.select("a[href]");
//            for (Element link: links) {
//                String linkHref = link.attr("href");
//                String linkText = link.text();
//                System.out.println(linkHref+" "+linkText);
//            }
//            httpcs.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
