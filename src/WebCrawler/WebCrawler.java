package WebCrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class WebCrawler {
	/**
	 * Crawing a url, output into a file, which is prepred for searching
	 * get all the hrefs, prepare for recursive crawing
	 * @author cbuyu
	 * @throws IOException 
	 *
	 */
	public static final String path =Attribute.getOutputPath();	
	public static final String baseWeb = Attribute.getBaseWeb(); //The target web
	public static Map<String, Boolean> badlinks = new HashMap<>();//for storing the links that cannot be fetched when crawling
	public static Set<String> badLinks = new HashSet<>();
	
	public static void searchEngine() throws IOException{
		Map<String, Boolean> alllinks = new HashMap<>();//for store all the links that are to be search.put(baseWeb, false);
		alllinks.put(baseWeb,false);
		alllinks = crawlPage(alllinks);
		
		//Store the url into one file
		File linkList = new File(path+"weblist/webLinkList.txt");
		try {     	  
 			// if file does not exists, then create it
 			if (!linkList.exists()) {
 				linkList.createNewFile();
 			}
 			FileWriter fw = new FileWriter(linkList.getAbsoluteFile());
 			BufferedWriter bw = new BufferedWriter(fw);
 			for(Map.Entry<String, Boolean> entry: alllinks.entrySet()) {
 				bw.write(entry.getKey());
 			}
 			bw.close();				
 		}catch (IOException e) {
	     	   e.printStackTrace();
	     	  }	
		
		// store bad links
		File badUrls = new File(path+"badweblist/badwebLinkList.txt");
		try {     	  
 			// if file does not exists, then create it
 			if (!badUrls.exists()) {
 				badUrls.createNewFile();
 			}
 			FileWriter fw = new FileWriter(badUrls.getAbsoluteFile());
 			BufferedWriter bw = new BufferedWriter(fw);
 			for(String s: badLinks) {
 				bw.write(s);
 			}
 			bw.close();				
 		}catch (IOException e) {
	     	   e.printStackTrace();
	     	  }	
	}
	public static Map<String, Boolean> crawlPage(Map<String, Boolean> mp) throws IOException {
		System.out.println("Processing-----------------------");
		Map<String, Boolean> newLinkList = new HashMap<>();//Links of the input link
		String curLink = "";
		for(Map.Entry<String, Boolean> link : mp.entrySet()){
			if(!link.getValue()) {//the link is not crawled
				curLink = link.getKey();
				try {
					Document doc =  Jsoup.connect(curLink).timeout(5000).get();
					String text  = doc.text();
					String title = doc.title();	
					//store the data into a file
					storingInFile(text,title,curLink,path);
					//find new link
					Elements ele = doc.select("a");
					for(Element e2:ele) {
			        	String href = e2.attr("abs:href");
			        	//not enpty, not find before, and it belongs to baseweb
			        	if(!href.isEmpty()&&!mp.containsKey(href)&&
			        			!newLinkList.containsKey(href)&&(href.startsWith(baseWeb))) {
			        		newLinkList.put(href, false);
			        	}
			        }	
				}catch (IOException e) {
					   mp.replace(curLink,false,true);//Set bad link processed
					   badLinks.add(curLink);
					   e.printStackTrace();
		 			   continue;   
			     	  }
			}
			mp.replace(curLink,false,true);//set the link as ture, means the li nk is processed
		}
		if(!newLinkList.isEmpty()) {
			mp.putAll(newLinkList);//merge the new links into the allLinkList
			Map<String, Boolean> tempLinks = crawlPage(mp);
			mp.putAll(tempLinks);
		}
		return mp;
	}

	private static void storingInFile(String text, String title, String url, String path) {
		String fixedTitle = adjustChar(title);
 		File temp = new File(path+fixedTitle+".txt");
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
 			   System.out.println(title);
	     	   e.printStackTrace();
	     	   
	     	  }
	}
	public static String adjustChar(String str){
		if(!str.contains("\\")&&!str.contains("/")&&!str.contains(":")&&
				!str.contains("?")&&!str.contains("<")&&!str.contains(">")
				&&!str.contains("|")&&!str.contains("\"")) {
			return str;
		}
		char[] chararr = str.toCharArray();
		for(int i=0;i<chararr.length;i++) {
			chararr[i] = chargeChar(chararr[i]);
		}
		return new String(chararr);
	}

	private static char chargeChar(char c) {
		if(c=='\\'||c=='/'||c==':'||c=='*'||c=='?'||c=='<'||c=='>'||c=='|'||c=='\"') {
			return '-';
		}else {
			return c;
		}
		
	}
	public static void main(String[] args) throws IOException {
		//baseWeb = "https://www.imdb.com";
		searchEngine();
	}
}		
