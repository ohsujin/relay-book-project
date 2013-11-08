package relay.book.NaverOpenAPI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class NaverOpenAPI{


	
	boolean inItem = false, inTitle = false, inAuthor = false, inPublisher = false;
	 String title = null, author = null, publisher = null;
	 
	public void Search_book(CharSequence displayContents){
		 try {
			URL url = new URL("http://openapi.naver.com/search?key=c19d4fb6af5f59ddada2d0ded393fd77&target=book_adv&query=art&d_isbn="+displayContents);
			XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
	        XmlPullParser parser = parserCreator.newPullParser();
	             
	        parser.setInput(url.openStream(), null);
		                       
		     
		    int parserEvent = parser.getEventType();
		    
		    while (parserEvent != XmlPullParser.END_DOCUMENT){
		           
			       switch(parserEvent){                     
		            
				           case XmlPullParser.START_TAG:  //parser가 시작 태그를 만나면 실행
					             if(parser.getName().equals("item")){
					              inItem = true;
					             }
					             if(parser.getName().equals("title")){ //title 만나면 내용을 받을수 있게함
					              inTitle = true;              
					             }
					             if(parser.getName().equals("author")){ 
					            	 inAuthor = true;              
					             }
					             if(parser.getName().equals("publisher")){ 
					            	 inPublisher = true;              
					             }
				             break;
				             
				            case XmlPullParser.TEXT://parser가 내용에 접근했을때
				            	
					             if(inTitle){ //isTitle이 true일 때 태그의 내용을 저장.
					              setTitle(parser.getText());             
					                 inTitle = false;
					             }
					             if(inAuthor){ //isAddress이 true일 때 태그의 내용을 저장.
						             setAuthor(parser.getText());             
						             inAuthor = false;
					             }
					             if(inPublisher){ //isMapx이 true일 때 태그의 내용을 저장.
					              	setPublisher(parser.getText());             
					              	inPublisher = false;
					             }
				             break; 
				             
				                       
			       		}          
		            parserEvent = parser.next();
		    
		    }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
	 }
	 
	 
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	 
	 
	 
	
}
