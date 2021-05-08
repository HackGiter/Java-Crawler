import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.util.Timeout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.HashSet;
//import java.util.concurrent.Callable;
//import java.util.concurrent.FutureTask;
//import java.util.concurrent.SynchronousQueue;

public class ThreadURL{
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(3))
            .setConnectionRequestTimeout(Timeout.ofSeconds(3))
            .build();
    private static CloseableHttpClient httpcs = HttpClients.createDefault();


    //    private ArrayList<String> urls = new ArrayList<String>();
    public ArrayList<urlLogs> urls = new ArrayList<>();
    public ArrayList<String> urlcollects = new ArrayList<>();
    public int i = 0;

    String selects, select, target, nowTime;
    String[] topics;
    int index, numbers;
    long time;


    public void settings(String[] a, String b, String c , String d, int e, int f) {
        topics = a;
        selects = b;
        select = c;
        target = d;
        index = e;
        numbers = f;
    }

//    public void run() {
//        while (i != urls.size()) {
//            int a;
//            synchronized (this) {
//                a = i;
//                i++;
//            }
//            System.out.println(a);
//            System.out.println(urls.get(a));
//            putIntoArray(urls.get(a));
//        }
//    }

    public void getImg(int a) {
        try {
            Document document = collectUrls(a);
            Elements elements = getElements(document);
            File file = new File("files/"+urls.get(a).getFile());
            for (Element element:elements) {
                if (filter(element.attr(select))) {
                    if (urlcollects.contains(urls.get(a).getFile()))
                        continue;
                    try {
                        URLConnection conn = new URL(element.attr(select)).openConnection();
                        InputStream inputStream = conn.getInputStream();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        int len = 0;
                        byte[] buf = new byte[inputStream.available()];
                        while ((len = inputStream.read(buf)) != -1) {
                            byteArrayOutputStream.write(buf, 1, len);
                        }
                        inputStream.close();
                        byteArrayOutputStream.close();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(byteArrayOutputStream.toByteArray());
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void putIntoArray(int a) {
        try {
            Document document = collectUrls(a);
            Elements elements = getElements(document);
            File file = new File("files/"+urls.get(a).getFile().replaceAll("[?=]", "_")+".html");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(document.toString().getBytes());
                fileOutputStream.close();
            } catch (IOException e ) {
                System.out.println(urls.get(a).getFile());
                e.printStackTrace();
            }
            for (Element element:elements) {
                if (filter(element.attr(select))) {
                    String url = "https://baike.baidu.com" + element.attr(select);
                    if (urlcollects.contains(url))
                        continue;
                    try {
                        if (!findTopic(URLDecoder.decode(url, "utf-8")))
                            continue;
                        urlcollects.add(url);
                        urls.add(new urlLogs(url));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void writeTextFile (String content) {
        try {
            FileWriter outStream = new FileWriter ("historylist",true);
            outStream.write (content);
            outStream.close ();
        } catch (IOException e) {
            e. printStackTrace ();
        }
    }


    public String getChinese(String string) {
        String regex = "([\u4e00-\u9fa5]+/\\d*)";
        String str = "";
        Matcher matcher = Pattern.compile(regex).matcher(string);
        while (matcher.find()) {
            str += matcher.group(0);
        }
        return str.replaceAll("/", "_");
//        return str.split("/")[0];
    }

    public boolean findTopic(String string) {
        for (String topic:topics) {
            if (string.indexOf(topic) != -1) {
                return true;
            }
        }
        return false;
    }

//    public int writeIntoLogs(int b) {
//        File file = new File("urls.log");
//        int a = b;
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
//            for (; a != urls.size() ; a++) {
////                System.out.println(URLDecoder.decode(urls.get(a).getString(), "utf-8"));
//                fileOutputStream.write((urls.get(a).getString()+"\n").getBytes());
//            }
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return a;
//    }

    public void writeIntoLogs() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'at'HH_mm_ss_z");
        Date date = new Date(System.currentTimeMillis());
        File log = new File(format.format(date)+".logs");
        writeTextFile(format.format(date)+".logs"+"\r\n");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(log, true);
            fileOutputStream.write(("URLS:"+String.valueOf(urls.size())+" "+"CURRENT TIME:"+nowTime+" "+"USE:"+String.valueOf(time)+"\n").getBytes());
            for (urlLogs url:urls) {
                fileOutputStream.write(("URL:"+url.getString().strip()+" ").getBytes());
                if (url.judge()) {
                    fileOutputStream.write(("WARNING:"+url.getErrors()+"\n").getBytes());
                } else {
                    fileOutputStream.write(("size:"+String.valueOf(url.read())+" ").getBytes());
                    fileOutputStream.write(("file:"+url.getFile()+"\n").getBytes());
                }
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean filter(String string1) {
        return string1.indexOf(target) == index;
    }

    public Elements getElements(Document document) {
        return document.select(selects);
    }

    public Document collectUrls(int a) {
        HttpGet httpget = new HttpGet(urls.get(a).getString().trim());
        httpget.setConfig(requestConfig);
        try {
            CloseableHttpResponse closeableHttpResponse = httpcs.execute(httpget);
            HttpEntity entity = closeableHttpResponse.getEntity();
            InputStream inputStream = entity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
                    Charset.defaultCharset()));
            StringBuffer stringBuffer = new StringBuffer();
            String lineString = null;
            while ((lineString = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineString);
            }
            lineString = stringBuffer.toString();
            String title = getChinese(URLDecoder.decode(urls.get(a).getString(), "utf-8"));
            urls.get(a).set(a, lineString.length(), title);
            Document document = Jsoup.parse(lineString);
            inputStream.close();
            return document;
        } catch (IOException e) {
            System.out.println(e.toString());
            urls.get(a).somethingWrong(true, e.toString());
            throw new RuntimeException(e);
        }
    }

    public void deleteDir(File file) {
        if (file.isDirectory()) {
            File[] dirs = file.listFiles();
            for (File file1:dirs) {
                deleteDir(file1);
            }
        } else {
            file.delete();
        }

    }

    public void init() {
        File dir = new File("files");
        if (dir.exists()) {
            deleteDir(dir);
        } else {
            dir.mkdir();
        }

        File collectors = new File("collects");
        try {
            FileInputStream fileInputStream = new FileInputStream(collectors);
            byte[] buf = new byte[fileInputStream.available()];
            fileInputStream.read(buf);
            String[] strings = new String(buf).split("\n");
            for (String string:strings) {
                urls.add(new urlLogs(string.strip()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Recorddate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        nowTime = format.format(date);
    }
//
//    public static void main(String[] args) {
////        FutureTask<Integer> task = new FutureTask<Integer>((Callable<Integer>)()->{
////            int a;
////            while (i < urls.size()) {
////                synchronized (this) {
////                    a = i;
////                    i ++;
////                }
////                putIntoArray(urls.get(a));
////            }
////            return a;
////        });
//
//        long start = System.currentTimeMillis();
//
//        ThreadURL threadURL = new ThreadURL();
//        threadURL.init();
//        threadURL.settings(new String[]{"维生素", "大学"}, "a[href]", "href", "item", 1, 6);
////        threadURL.putIntoArray(0);
//
////        for (int a = 0; a < 4; a++) {
////            new Thread(new ThreadURL()).start();
////        }
//
//        Runnable runnable = () -> {
//            while (threadURL.i != threadURL.urls.size()) {
//                int a;
//                synchronized(threadURL) {
//                    a = threadURL.i;
//                    threadURL.i++;
//                }
////                System.out.println(a);
////                threadURL.putIntoArray(threadURL.urls.get(a));
//                threadURL.putIntoArray(a);
//            }
//        };
//
///*        Runnable fileLog = () -> {
//            int b = 0;
//            while (b != threadURL.urls.size()) {
//                b = threadURL.writeIntoLogs(b);
//                try {
//                    Thread.sleep(1000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };*/
//
//        threadURL.Recorddate();
//        for (int a = 0; a < threadURL.numbers; a++) {
//            new Thread(runnable).start();
//        }
//
////        new Thread(fileLog).start();
//        while (Thread.activeCount() != 2) { }
//
//
//        threadURL.time = System.currentTimeMillis() - start;
//        threadURL.writeIntoLogs();
//
//    }

}
