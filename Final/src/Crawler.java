import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Crawler extends JFrame implements ActionListener{
	private JTextArea website;
	private JTextArea output;
    private JTextField keyword;
    private JTextField log;
    private JButton run;
    private JButton checklog;
    private JButton checklogall;
    private JCheckBox four;
    private JCheckBox five;
    private JCheckBox six;
    String name = "outcome";
    String history = "collects";
    public static String topicwords;
    public static int num;
//    private String fileName;

	//主函数
	public static void main(String[] args){
		try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch(Exception e) {
        	System.out.println(e);
        }
		Crawler start = new Crawler();
	}

	//生成GUI界面
	public Crawler() {
		buildGUI();    
        setTitle("Crawler");
        setSize(1000,900);
        setVisible(true);
        pack();
	}

	//GUI界面组件设置
	private void buildGUI()
    {  Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        website = new JTextArea(20,80);
        output = new JTextArea(15,40);
        keyword = new JTextField(10);
        log = new JTextField(10);
        four = new JCheckBox("4",false);
        five = new JCheckBox("5",false);
        six = new JCheckBox("6",false);
        
        JScrollPane scroll = new JScrollPane(output);
        JScrollPane scrollPane = new JScrollPane(website);
        scroll.setVerticalScrollBarPolicy( 
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		run = new JButton("Run");
        run.addActionListener(this);
        checklog = new JButton("进入");
        checklog.addActionListener(this);
        checklogall = new JButton("查看历史日志列表");
        checklogall.addActionListener(this);
        
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel jlb1 = new JLabel("请输入爬取网址:");
        JLabel jlb2 = new JLabel("主题词：");
        JLabel jlb3 = new JLabel("线程数量：");
        JLabel jlb4 = new JLabel("查看日志：");
        JLabel jlb5 = new JLabel("显示日志：");
        Font font = new Font("宋体",Font.PLAIN,18);
        Font forButton = new Font("微软雅黑",Font.PLAIN,13);
        jlb1.setFont(font); jlb2.setFont(font); jlb3.setFont(font); 
        jlb4.setFont(font); jlb5.setFont(font); 
        checklog.setFont(forButton); checklogall.setFont(forButton);
//        inputPanel.add("North", new JLabel("请输入爬取网址:"));
        inputPanel.add("North", jlb1);
//        inputPanel.add(new JLabel());
        inputPanel.add("South",scrollPane);
        
        JPanel setPanel = new JPanel(new GridLayout(3,8,1,15));
        setPanel.add(jlb2);
        setPanel.add(keyword);
        setPanel.add(new JLabel());
        setPanel.add(jlb3);
        setPanel.add(four);
        setPanel.add(five);
        setPanel.add(six);
        setPanel.add(run);
        
        setPanel.add(checklogall);
        setPanel.add(new JLabel());
        setPanel.add(jlb4);
        setPanel.add(log);
        setPanel.add(checklog);
        setPanel.add(new JLabel());setPanel.add(new JLabel());setPanel.add(new JLabel());
        setPanel.add(jlb5);
        for (int i=0; i<7; i++) setPanel.add(new JLabel());
        
//        checklogs.add("North",check);
//        checklogs.add("Center",output);

        contentPane.add("North",inputPanel);
        contentPane.add("Center",setPanel);
        contentPane.add("South",scroll);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      }//buildGUI

	//爬虫主线程
	public void start() {
		long start = System.currentTimeMillis();
		ThreadURL threadURL = new ThreadURL();
		threadURL.init();
		threadURL.settings(new String[]{topicwords}, "a[href]", "href", "item", 1, num);
		Runnable runnable = () -> {
			while (threadURL.i != threadURL.urls.size()) {
				int a;
				synchronized(threadURL) {
					a = threadURL.i;
					threadURL.i++;
				}
//                threadURL.putIntoArray(threadURL.urls.get(a));
				threadURL.putIntoArray(a);
			}
		};

		threadURL.Recorddate();
		for (int a = 0; a < threadURL.numbers; a++) {
			new Thread(runnable).start();
		}

//        new Thread(fileLog).start();
		while (Thread.activeCount() != 4) {
		}


		threadURL.time = System.currentTimeMillis() - start;
		threadURL.writeIntoLogs();
		output.setText("Done");
	}

	public void actionPerformed(ActionEvent e)
    { 
		
		String line;
		if (e.getSource() == checklogall) {
			output.setText("");
			readTextFile(output,"historylist");
		}
		
		if (e.getSource() == checklog) {
			output.setText("");
			readTextFile(output,log.getText());
//			printTextFile(output,log.getText());
		}
		
		if(e.getSource() == run){
			 Runnable runnable = () -> {
			 	start();
			 };
			 new Thread(runnable).start();
			 writeTextFile(website,history);
			 num=getThread(four,five,six);
			 topicwords = keyword.getText();

//			 line=getURL(name);
//			 if(line!=null)
//			 major(getURL(name),keyword.getText(),num);
		 }
	  
    }

    //设置线程数量
	public int getThread(JCheckBox four,JCheckBox six,JCheckBox five){
		 if(four.isSelected())
			 num=4;
		 else if(five.isSelected())
		 num=5;
		 else 
		num=6;
		 return num;
	}
	

	//读取日志文件
	private void readTextFile (JTextArea display, String fileName){
	    try {
//		BufferedReader inStream = new BufferedReader (new FileReader (fileName));
//		String line = inStream.readLine();
//		while ( line != null) {
//			display.append( line + "\n" );
//			line = inStream.readLine();}
//			inStream.close ();
			File file = new File(fileName);
			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] buf = new byte[1024];
			int len = 0;
			try {
				while ((len = fileInputStream.read(buf)) != -1) {
					display.append(new String(buf, 0, len));
				}

				fileInputStream.close();
			} catch (IOException e) {
				display.setText ("IOERROR: " + e. getMessage () + "\n" );
				e. printStackTrace ();
			}

	    }     catch (FileNotFoundException e) {
	    	display.setText("IOERROR: "+ fileName +" NOT found\n" );
	    	e. printStackTrace ();
	        }


	    }
	
//	private void printTextFile (JTextArea output, String fileName) {
//	    try {
//			PrintWriter outStream = new PrintWriter (fileName );
//			outStream.println();
//			outStream.close ();
//	    } catch (IOException e) {
//		output.setText("IOERROR: " + e. getMessage () + "\n" );
//		e. printStackTrace ();
//	    }
//	}

	//读取网址
	private void writeTextFile (JTextArea website, String fileName) {
        try {//输入读取逻辑：预设一个专门的记录网址文件，先将网址输入进去，读取时再读文件内容
			FileWriter outStream = new FileWriter (fileName,true);
			outStream.write (website.getText ());
			outStream.close ();
        } catch (IOException e) {
			website.setText("IOERROR: " + e. getMessage () + "\n" );
			e. printStackTrace ();
        }
	}

	//测试用例
	private void turnText (int Thread,String topics,String filename) {
		try {//这个处理用于实际的爬取，major即主体运行
			BufferedReader inStream = new BufferedReader (new FileReader (filename));
			String line = inStream.readLine();
			while ( line != null) {
//				major(Thread,line,filename);
				line = inStream.readLine();
			}
				inStream.close ();
		} catch (FileNotFoundException e) {
		    	e. printStackTrace ();
		        } catch ( IOException e ) {
		    	e. printStackTrace ();
		}

	}
	
//	"C:\\Users\\韩嗣麒\\Desktop\\spider\\2020-06-09at01_43_49_CST.logs"


}
