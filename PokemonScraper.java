import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PokemonScraper {
    public static void main(String args[]) throws IOException, InterruptedException {
        //mapDataBases("TCG_DataBase","Pokecardex_DataBase");
        //matchCollectionIds("TCG_DataBase");
        String database = "Pokecardex_DataBase";
        //retrieveCardImages("CRZ", database);
        String[] collectionIds = ReadWrite.readText("D:/DataBases/Pokemon/CardNames/"+database+"/collectionIdsMatch.txt").split(";");
        for (int i = 0; i < collectionIds.length; i++) {
            String collectionId = collectionIds[i].split(":")[0];
            System.out.println("Retrieving : "+collectionId+", "+i+"/"+collectionIds.length);
            try {
                //retrieveCardIds(collectionId);
                retrieveCardImages(collectionId, database);
                Thread.sleep(500);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //resize("./Data/Pokemon/CardImages/CEL/CEL_11-2.png", "./Data/Pokemon/ResizedCardImages/CEL/CEL_11-2.png", 23, 32);
        //resizeAllImages("CEL", 23, 32);
    }

    public static String formatForEqual(String toFormat) {
        return toFormat.replace(" ", "").toLowerCase().replace("&amp;", "&").replace("?", "\'").trim();
    }

    public static void mapDataBases(String db1, String db2) throws IOException {
        String[] matchIds1 = ReadWrite.readText("D:/DataBases/Pokemon/CardNames/"+db1+"/collectionIdsMatch.txt").split(";");
        String[] matchIds2 = ReadWrite.readText("D:/DataBases/Pokemon/CardNames/"+db2+"/collectionIdsMatch.txt").split(";");

        String match = "";
        for (int i = 0; i < matchIds1.length; i++) {
            String[] couple1 = matchIds1[i].split(":");
            boolean matched = false;
            String last = "";
            for (int j = 0; j < matchIds2.length; j++) {
                String[] couple2 = matchIds2[j].split(":");
                if (formatForEqual(couple1[1]).equals(formatForEqual(couple2[1])) || formatForEqual(couple1[0]).equals(formatForEqual(couple2[0]))) {
                    matched = true;
                    last = couple1[0]+":"+couple2[0]+";";
                    match+=last;
                }
            }
            if (!matched)
                System.out.println("Unmatched : "+couple1[0]+" - "+couple1[1]);
        }
        match.substring(0, match.length()-1);
        ReadWrite.write("D:/DataBases/Pokemon/CardNames/"+db1+"-"+db2+"_match.txt", match);
    }

    public static String formatId(String cardId) {
        String prefix = "";
        String suffix = "";
        if (!Character.isDigit(cardId.charAt(cardId.length()-1))) {
            if (cardId.length() == 1) {
                return cardId;
            }
            suffix += cardId.charAt(cardId.length()-1);
            cardId = cardId.substring(0, cardId.length()-1);
        }
        for (int i = cardId.length(); i < 3; i++) {
            prefix = "0"+prefix;
        }
        return prefix+cardId+suffix;
    }

    public static void matchCollectionIds(String database) throws IOException {
        String names = "";
        String url = "https://limitlesstcg.com/cards/fr";
        String filePath = "D:/DataBases/Pokemon/HTML/"+database+"/collectionIdsMatch.txt";
        String page = "";
        if (!ReadWrite.exists(filePath)) {
            page = DataScraper.downloadWebPage(url);
            ReadWrite.write(filePath, page);
        } else {
            page = ReadWrite.readText(filePath);
        }
        Pattern p = Pattern.compile("<img class=\\\"set\\\" alt=\\\"([^\\\"]+)\\\"[^>]+>([^<]+)<");
        Matcher m = p.matcher(page);
        while(m.find()) {
            String collId = m.group(1);
            String collName = m.group(2).replace("&amp;", "&").replace("?", "\'").trim();
            System.out.println(collId+" : "+collName);
            names += collId+":"+collName+";";
        }
        if (names.length() == 0)
            return;
        names = names.substring(0, names.length()-1);
        System.out.println(names.split(";").length);
        ReadWrite.write("D:/DataBases/Pokemon/CardNames/"+database+"/collectionIdsMatch.txt", names);
    }

    public static void resizeAllImages(String collectionId, int width, int height) throws IOException {
        File file = new File("./Data/Pokemon/ResizedCardImages/"+collectionId);
        file.mkdirs();
        for (File sourceImageFile : new File("./Data/Pokemon/CardImages/"+collectionId).listFiles()) {
            resize(sourceImageFile.getAbsolutePath(), "./Data/Pokemon/ResizedCardImages/"+collectionId+"/"+sourceImageFile.getName(), width, height);
        }
    }

    public static void retrieveCardImages(String collectionId, String database) throws IOException, InterruptedException {
        String names = ReadWrite.readText("D:/DataBases/Pokemon/CardNames/"+database+"/"+collectionId+".txt");
        String[] ids = names.split(";");
        File file = new File("D:/DataBases/Pokemon/CardImages/"+database+"/"+collectionId);
        file.mkdirs();
        String missing = "";
        String replaced = "";
        Console.startProgressBar(1, ids.length);
        //ExecutorService executorService = Executors.newFixedThreadPool(7);
        for (int i = 0; i < ids.length; i++) {
            String id = (ids[i]);
            String url = "https://www.pokecardex.com/assets/images/sets/"+collectionId.toUpperCase()+"/HD/"+(i+1)+".jpg";
            String save_url = "https://www.pokecardex.com/assets/images/sets/"+collectionId.toUpperCase()+"/"+(i+1)+".jpg";
            String path = "D:/DataBases/Pokemon/CardImages/"+database+"/"+collectionId+"/"+collectionId+"_"+(i+1)+".jpg";
            Console.progress(1);
            //DataScraper.Scraper task = new DataScraper.Scraper(url, path);
            //executorService.submit(task);
            try {
                //if (!ReadWrite.exists(path))
                    ReadWrite.write(path, DataScraper.downloadImage(url));
            } catch (IOException e) {
                try {
                    //if (!ReadWrite.exists(path))
                    ReadWrite.write(path, DataScraper.downloadImage(save_url));
                } catch (IOException e2) {
                    missing += id+";";
                } 
            } 
            //ReadWrite.write(path, DataScraper.downloadImage(url));
        }
        //executorService.shutdown();
        //executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        Console.end();
        if (missing.length() > 0) {
            missing = missing.substring(0, missing.length()-1);
            ReadWrite.write(file.getAbsolutePath()+"/"+collectionId+"_missing.txt", missing);
            System.out.println("Missing "+missing.split(";").length+" cards");
        } else {
            if (ReadWrite.exists(file.getAbsolutePath()+"/"+collectionId+"_missing.txt")) {
                ReadWrite.delete(file.getAbsolutePath()+"/"+collectionId+"_missing.txt");
            }
        }
        if (replaced.length() > 0) {
            replaced = replaced.substring(0, replaced.length()-1);
            ReadWrite.write("./Data/Pokemon/CardImages/"+collectionId+"/"+collectionId+"_replaced.txt", replaced);
            System.out.println("Replaced "+replaced.split(";").length+" cards");
        } else {
            if (ReadWrite.exists("./Data/Pokemon/CardImages/"+collectionId+"/"+collectionId+"_replaced.txt")) {
                ReadWrite.delete("./Data/Pokemon/CardImages/"+collectionId+"/"+collectionId+"_replaced.txt");
            }
        }
    }

    public static void retrieveCardIds(String collectionId) throws IOException {
        int i = 1;
        boolean next = true;
        String names = "";
        while (next) {

        String url = "https://www.pokemon.com/fr/jcc-pokemon/cartes-pokemon/"+i+"?"+collectionId;
        //String[] array = url.split("/");
        String filePath = "D:/DataBases/Pokemon/HTML/Pokemon_DataBase/"+collectionId+"-"+i+".txt";
        String page = "";
        if (!ReadWrite.exists(filePath)) {
            page = DataScraper.downloadWebPage(url);
            ReadWrite.write(filePath, page);
        } else {
            page = ReadWrite.readText(filePath);
        }
        if (page.contains("Aucune carte ne correspond à votre recheche.")) {
            return;
        }

        Pattern p = Pattern.compile("(<a href=\")([^\"]*)(\")");
        Matcher m = p.matcher(page);
        while(m.find()) {
            String val = m.group(2);
            /*String[] array = val.split(" ");
            if (array.length == 0)
                continue;
            val = array[array.length-1].split("/")[0];
            
            names += val+";";*/
            if (val.length() >= 38 && val.substring(0, 38).equals("/fr/jcc-pokemon/cartes-pokemon/series/")) {
                String[] array = val.split("/");
                val = array[array.length-1];
                //System.out.println(val);
                if (names.contains(val)) {
                    next = false;
                    break;
                }
                names += val+";";
            }
        }
        i++;
        }
        if (names.length() == 0)
            return;
        names = names.substring(0, names.length()-1);
        System.out.println(names);
        ReadWrite.write("D:/DataBases/Pokemon/CardNames/Pokemon_DataBase/"+collectionId+".txt", names);
    }

    public static void retrieveCollectionIds() throws IOException {
        String url = "https://www.pokecardex.com/series";
        String[] array = url.split("/");
        String filePath = "./Data/Pokemon/HTML/Pokecardex_DataBase/collectionIds.txt";
        String page = "";
        if (!ReadWrite.exists(filePath)) {
            page = DataScraper.downloadWebPage(url);
            ReadWrite.write(filePath, page);
        } else {
            page = ReadWrite.readText(filePath);
        }

        Pattern p = Pattern.compile("(href=\")([^\"]*)(\")");
        Matcher m = p.matcher(page);
        String names = "";
        while(m.find()) {
            String val = m.group(2);
            if (val.contains("/series/")) {
                array = val.split("/");
                names += array[array.length-1]+";";
                System.out.println(array[array.length-1]);
            }
        }
        System.out.println(names.split(";").length);
        names = names.substring(0, names.length()-1);
        ReadWrite.write("./Data/Pokemon/CardNames/Pokecardex_DataBase/collectionIds.txt", names);
    }
    
    public static void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
 
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
 
    /**
     * Resizes an image by a percentage of original size (proportional).
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param percent a double number specifies percentage of the output image
     * over the input image.
     * @throws IOException
     */
    public static void resize(String inputImagePath,
            String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }
}
