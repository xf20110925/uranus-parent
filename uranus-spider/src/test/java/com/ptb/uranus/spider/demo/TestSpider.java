package com.ptb.uranus.spider.demo;

import com.ptb.uranus.spider.common.webDriver.WebDriverProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by eric on 16/7/23.
 */
public class TestSpider {
	@Test
	public void test() throws IOException, InterruptedException {
		PhantomJSDriver webdiver = WebDriverProvider.createPcWebDriver(false, true);
		webdiver.manage().addCookie(new Cookie("SUB", UUID.randomUUID().toString(), ".weibo.com", "/", null, false));
		webdiver.get("http://d.weibo.com/102803_ctg1_1199_-_ctg1_1199#");
		Thread.sleep(3000);

		String page = webdiver.getPageSource();
		Elements select = Jsoup.parse(page).
				select("#Pl_Discover_NewMixTab__3  div.WB_innerwrap a span.text");
		for (Element element : select) {
			System.out.println(element.text().replaceAll("\\t", ""));
		}

	}

/*

	private static void loadDLL(String libFullName) {
		try {
			String nativeTempDir = System.getProperty("java.io.tmpdir");
			InputStream in = null;
			FileOutputStream writer = null;
			BufferedInputStream reader = null;
			File extractedLibFile = new File(nativeTempDir + File.separator + libFullName);
			if (!extractedLibFile.exists()) {
				try {
					in = Tesseract.class.getResourceAsStream("/" + libFullName);
					Tesseract.class.getResource(libFullName);
					reader = new BufferedInputStream(in);
					writer = new FileOutputStream(extractedLibFile);
					byte[] buffer = new byte[1024];
					while (reader.read(buffer) > 0) {
						writer.write(buffer);
						buffer = new byte[1024];
					}
					in.close();
					writer.close();
					System.load(extractedLibFile.toString());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						in.close();
					}
					if (writer != null) {
						writer.close();
					}
				}
			} else {
				System.load(extractedLibFile.toString());
			}
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) throws IOException {
		//System.load("/usr/local/Cellar/leptonica/1.72/lib/liblept.4.dylib");
		//System.load("/Users/eric/wk/company/ptb/git/uranus-parent/uranus-spider/src/test/resources/libtesseract.3.dylib");


		File imageFile = new File("/Users/eric/Desktop/code.png");
		ITesseract instance = new Tesseract();  // JNA Interface Mapping

		BufferedImage bufferedImage = ImageHelper.convertImageToBinary(ImageHelper.convertImageToGrayscale(ImageIO.read(new FileInputStream(imageFile))));
		bufferedImage = ImageHelper.getScaledInstance(bufferedImage,bufferedImage.getWidth()*2,bufferedImage.getHeight()*2);
		// ITesseract instance = new Tesseract1(); // JNA Direct Mapping

		ImageIO.write(bufferedImage,"png",new File("./2.png"));
		try {
			List<Word> words = instance.getWords(bufferedImage, 5);
			String result = instance.doOCR(bufferedImage);
			System.out.println(result);
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
	}*/
}
