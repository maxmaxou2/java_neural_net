import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataScraper {
    
    public static class Scraper implements Runnable {
        String url, path;

        public Scraper(String url, String path) {
            this.url = url;
            this.path = path;
        }
        
        @Override
        public void run() {
            try {
                Console.progress(1);
                if (!ReadWrite.exists(path))
                    DataScraper.writeWebPage(url, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeWebPage(String url, String absolute_path) throws IOException {
        InputStream in = new URL(url).openStream();
        Files.copy(in, Paths.get(absolute_path));
        in.close();
    }
    
    public static String downloadWebPage(String webpage)
    {
        String html = null;
        try {
  
            // Create URL object
            URL url = new URL(webpage);
            BufferedReader readr = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
  
            // read each line from stream till end
            String line;
            while ((line = readr.readLine()) != null) {
                html += line;
            }
            readr.close();
        }

        // Exceptions
        catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        }
        catch (IOException ie) {
            System.out.println("IOException raised");
        }

        return html;
    }
    
    public static String downloadWebPage(String webpage, String charset)
    {
        String html = null;
        try {
  
            // Create URL object
            URL url = new URL(webpage);
            BufferedReader readr = new BufferedReader(new InputStreamReader(url.openStream(), charset));
  
            // read each line from stream till end
            String line;
            while ((line = readr.readLine()) != null) {
                html += line;
            }
            readr.close();
        }

        // Exceptions
        catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        }
        catch (IOException ie) {
            System.out.println("IOException raised");
        }

        return html;
    }

    public static byte[] downloadImage(String webUrl) throws IOException {
        URL url = new URL(webUrl);
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf))) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        return out.toByteArray();
    }
}
