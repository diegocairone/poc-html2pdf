package com.cairone.html2pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;

public class App {

	public static final String BASE_URL = "http://www.informescea.com.ar";
	public static final String TARGET_URL = BASE_URL + "/login.php";
	
	public static final String OUTPUT_FILE = "src/output/informe.pdf";
	
	public static void main(String[] args) throws IOException {
		
		Document htmlDoc = getHtmlDoc(TARGET_URL);
		
		List<String> linksList = getLinksList(htmlDoc);
		
		htmlDoc.getElementsByTag("script").remove();
		htmlDoc.getElementsByTag("link").remove();
		
		Element headTag = htmlDoc.getElementsByTag("HEAD").get(0);
		
		linksList.forEach(link -> {
			
			Document cssDoc = getHtmlDoc(link);
			Element styleTag = headTag.appendElement("style");
			
			styleTag.attr("type", "text/css");
			styleTag.html(cssDoc.html());
		});
		
		OutputStream os = new FileOutputStream(OUTPUT_FILE);
		
		ConverterProperties props = new ConverterProperties();
		props.setMediaDeviceDescription(new MediaDeviceDescription(MediaType.PRINT));
				
		HtmlConverter.convertToPdf(htmlDoc.html(), os);
		
		System.out.println("Listo!");
	}

	public static Document getHtmlDoc(String url) {
		try {
			
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet req = new HttpGet(url);
			
			HttpResponse res = client.execute(req);
			InputStream is = res.getEntity().getContent();
			
			Document htmlDoc = Jsoup.parse(is, "utf-8", BASE_URL);
			
			return htmlDoc;
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static List<String> getLinksList(Document doc) {
		
		final List<String> list = new ArrayList<>();
		
		doc.getElementsByTag("link").stream()
			.filter(p -> p.attr("rel").equals("stylesheet"))
			.forEach(link -> {
				String href = link.attr("href");
				if(!href.startsWith("http")) {
					href = String.format("%s/%s", BASE_URL, href);
				}
				list.add(href);
			});
		
		return list;
	}
}
