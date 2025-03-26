import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static String path = "./Poke_CEL_v7.ser";
    public static float learnRate = 0.5f, dataSetCoverage = 1f;
    public static int epochNumbers = 1000, batchSize = 50, threadNumber = 4;

    public static void main(String[] args) throws IOException, InterruptedException {
        Activation.computeValues(-10, 10, 10000);
        DataLoader.loadTrainDataPoints("./Data/Pokemon/AugmentedCardImages/CEL", "./Data/Pokemon/CardNames/CEL.txt", dataSetCoverage);
        DataLoader.loadTestDataPoints("./Data/Pokemon/ResizedCardImages/CEL", "./Data/Pokemon/CardNames/CEL.txt");

        NeuralNetwork network = getNetwork(path);
        loopEpochs(network, epochNumbers, batchSize, "./Data/Pokemon/ResizedCardImages/CEL", "./Data/Pokemon/CardNames/CEL.txt");
    }
    
    public static class BatchTask implements Runnable {
        NeuralNetwork network;
        DataPoint[] batch;

        public BatchTask(NeuralNetwork network, DataPoint[] batch) {
            this.network = network;
            this.batch = batch;
        }
        
        @Override
        public void run() {
            network.learn(batch);
            network.applyGradients(learnRate/batch.length);
        }
    }

    public static void loopEpochs(NeuralNetwork network, int epochNumbers, int batchSize, String testFolder, String testLabels) throws InterruptedException {
        Console.startProgressBar(3);
        for (int i = 1; i < epochNumbers+1; i++) {
            epoch(network, batchSize);
            Console.progress(i*100/(float)epochNumbers, 3);
            ReadWrite.serializeObject(network, path);
            test(network, testFolder, testLabels);
        }
    }

    public static void test(NeuralNetwork network, String testFolder, String testLabels) {
        int count = 0;
        float sum = 0;
        for (int i = 0; i < DataLoader.testData.length; i++) {
            DataPoint dataPoint = DataLoader.testData[i];
            Result res = network.result(dataPoint.inputs);
            sum += res.getCost(dataPoint.expectedOutputs);
            Result preRes = new Result(dataPoint.expectedOutputs);
            if (preRes.predicted == res.predicted) {
                count++;
            } else {
                //System.out.println(format(preRes.outputs)+" - "+format(res.outputs));
                //System.out.println(preRes.outputs[preRes.outputs.length-10]+" - "+res.outputs[res.outputs.length-10]);
                //System.out.println("Expected : "+preRes.predicted+" Predicted : "+res.predicted);
            }
        }
        System.out.println("Cost : "+(sum/DataLoader.testData.length));
        System.out.println("Accuracy : "+count+"/"+DataLoader.testData.length+" -> "+count/(float) DataLoader.testData.length);
    }

    public static NeuralNetwork getNetwork(String path) {
        if (ReadWrite.exists(path)) {
            return (NeuralNetwork)ReadWrite.deserializeObject(path);
        } else {
            NeuralNetwork network = new NeuralNetwork(1104, 786, 400, 50);
            network.randomInit(-1, 1);
            return network;
        }
    }

    public static void epoch(NeuralNetwork network, int batchSize) throws InterruptedException {
        DataLoader.setupBatches(batchSize);
        ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
        while (DataLoader.hasNextBatch()) {
            BatchTask task = new BatchTask(network, DataLoader.getBatch());
            executorService.submit(task);
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    public static String format(float[] outputs) {
        String ans = "[";
        for (float output : outputs) {
            ans += output+",";
        }
        return ans.substring(0, ans.length()-1)+"]";
    }

    public static double calculateStandardDeviation(int[] arr) {
        if (arr.length <= 1) {
            return 0;  // Standard deviation is undefined for arrays with only one element
        }

        double mean = Arrays.stream(arr).average().orElse(0);
        double[] squaredDeviations = Arrays.stream(arr)
                .mapToDouble(x -> Math.pow(x - mean, 2))
                .toArray();

        double variance = Arrays.stream(squaredDeviations).sum() / (arr.length - 1);
        double stdDev = Math.sqrt(variance);

        return stdDev;
    }
}