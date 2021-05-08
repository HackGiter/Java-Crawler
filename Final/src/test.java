import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.testing.framework.TestingFramework;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class test {

    public void getImg(String url) {
        try {
//            File file = new File(url.split("/")[0].replaceAll(":", "_")+".jpg");
            File file = new File(url.replaceAll("[:/]", "_")+".jpg");
            try {
                URLConnection conn = new URL(url).openConnection();
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buf)) != -1) {
                    byteArrayOutputStream.write(buf, 0, len);
                }
                inputStream.close();
                byteArrayOutputStream.close();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                System.out.println(file.toString());
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
//        HttpGet httpget = new HttpGet("https://asjdlfk");
//        CloseableHttpClient httpcs = HttpClients.createDefault();
//        try {
//            CloseableHttpResponse closeableHttpResponse = httpcs.execute(httpget);
//            HttpEntity entity = closeableHttpResponse.getEntity();
//            InputStream inputStream = entity.getContent();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
//                    Charset.defaultCharset()));
//            StringBuffer stringBuffer = new StringBuffer();
//            String lineString = null;
//            while ((lineString = bufferedReader.readLine()) != null) {
//                stringBuffer.append(lineString);
//            }
//            lineString = stringBuffer.toString();
//            System.out.println(lineString);
//            inputStream.close();
//        } catch (IOException e) {
//            System.out.println(e.toString());
//            throw new RuntimeException(e);
//        }
        test t = new test();
        t.getImg("http://img03.sogoucdn.com/app/a/100520093/3c28af542f2d49f7-8331c86ff317d9f5-8ee52078d03feac9b8502dad26f33c31.jpg");
    }
}
