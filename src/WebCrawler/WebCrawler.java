package WebCrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {
	/**
	 * Crawing a url, output into a file, which is prepred for searching
	 * get all the hrefs, prepare for recursive crawing
	 * @author cbuyu
	 * @throws IOException 
	 *
	 */
	public static String crawlOnePage(String url) throws IOException {
		Document doc =  Jsoup.connect(url).get();
		String text  = doc.text();
		String title = doc.title();
		String path = Attribute.getOutputPath();
		
		storing(text,title,url,path);
		String links="";
		Elements ele = doc.select("a");
		for(Element e2:ele) {
        	String href = e2.attr("abs:href");
        	if(href.length()>3) {
        		links = links+"\n"+href;        	
        	}
        }
		return links;
	}
	public static void makeTheDataBase(String url) throws IOException{
		String allLinks = crawlOnePage(url);
		String[] links = allLinks.split("\n");
		for(String lk:links) {
			System.out.println(lk);
		}
		
		
	}
	private static void storing(String text, String title, String url, String path) {
 		File temp = new File(path+title+".txt");
 		try {     	  
 			// if file does not exists, then create it
 			if (!temp.exists()) {
 				temp.createNewFile();
 			}else {
 				return;
 			}
 			//BufferedWriter bw = new BufferedWriter(new FileWriter("runoob.txt"));
 			//System.out.println(temp.getAbsoluteFile());
 			FileWriter fw = new FileWriter(temp.getAbsoluteFile());
 			BufferedWriter bw = new BufferedWriter(fw);
 			bw.write(title+"\n");//first line
 			bw.write(url+"\n");//second line
 			bw.write(text);
 			bw.close();	
 		}catch (IOException e) {
	     	   e.printStackTrace();
	     	  }
	}
	public static void main(String[] args) throws IOException {
		String url = "https://www.baidu.com";
		makeTheDataBase(url);
	}
}		
