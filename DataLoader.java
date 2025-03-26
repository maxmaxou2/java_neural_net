import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class DataLoader {

    public static DataPoint[] trainData, testData;
    public static int[] indexes;
    public static int batchSize, currentBatch;
    
    public static void main(String args[]) throws IOException {
        Map<String,Integer> classes = loadOutputs("./Data/Pokemon/CardNames/CEL.txt");
        System.out.println(Main.format(getExpectedOutput(classes.size(), classes.get(getCollectionId(new File("./Data/Pokemon/ResizedCardImages/CEL/CEL_11.png"))))));
    }

    public static boolean hasNextBatch() {
        return currentBatch*batchSize < trainData.length;
    }

    public static void setupBatches(int batchMaxSize) {
        batchSize = batchMaxSize < trainData.length ? batchMaxSize : trainData.length;
        indexes = Random.randomUniqueInts(0, trainData.length, trainData.length);
        currentBatch = 0;
    }

    public static DataPoint[] getBatch() {
        int start = batchSize*currentBatch;
        int end = batchSize*(currentBatch+1);
        end = end < trainData.length ? end : trainData.length;
        DataPoint[] batch = new DataPoint[end-start];
        for (int i = start; i < end; i++) {
            batch[i-start] = trainData[indexes[i]];
        }
        currentBatch++;
        return batch;
    }

    public static void loadTrainDataPoints(String folderPath, String labelsPath, float dataSetCoverage) throws IOException {
        File folder = new File(folderPath);
        Map<String,Integer> classes = loadOutputs(labelsPath);

        File[] files = folder.listFiles();
        int length = (int) (files.length*dataSetCoverage);
        DataPoint[] dataPoints = new DataPoint[length];
        for (int i = 0; i < length; i++) {
            float[] inputs = loadImage(files[i]);
            float[] outputs = getExpectedOutput(classes.size(), classes.get(getCollectionId(files[i])));
            dataPoints[i] = new DataPoint(inputs, outputs);
        }
        trainData = dataPoints;
    }

    public static void loadTestDataPoints(String folderPath, String labelsPath) throws IOException {
        File folder = new File(folderPath);
        Map<String,Integer> classes = loadOutputs(labelsPath);

        File[] files = folder.listFiles();
        DataPoint[] dataPoints = new DataPoint[files.length];
        for (int i = 0; i < files.length; i++) {
            float[] inputs = loadImage(files[i]);
            float[] outputs = getExpectedOutput(classes.size(), classes.get(getCollectionId(files[i])));
            dataPoints[i] = new DataPoint(inputs, outputs);
        }
        testData = dataPoints;
    }

    public static String getCollectionId(File file) {
        return file.getName().split("\\.")[0].split("-")[0].split("_")[1];
    }

    public static float[] loadImage(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        float[] imageData = new float[image.getWidth() * image.getHeight() * 3];
            
        int[] pixel;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getWidth(); y++) {
                pixel = image.getRaster().getPixel(x, y, new int[3]);
                imageData[x+y*image.getWidth()] = pixel[0]/255.0f;
                imageData[x+y*image.getWidth()+1] = pixel[1]/255.0f;
                imageData[x+y*image.getWidth()+2] = pixel[2]/255.0f;
            }
        }
        return imageData;
    }
    
    public static float[] getExpectedOutput(int outputSize, int index) {
        float[] ans = new float[outputSize];
        ans[index] = 1;
        return ans;
    }

    public static Map<String,Integer> loadOutputs(String path) throws IOException {
        String names = ReadWrite.readText(path);
        String[] ids = names.split(";");
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < ids.length; i++) {
            map.put(ids[i], i);
        }
        return map;
    }
}
