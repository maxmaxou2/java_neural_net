public class Result {
    public int predicted;
    public float[] outputs;

    public Result(float[] outputs) {
        this.predicted = maxIndex(outputs);
        this.outputs = outputs;
    }

    public int maxIndex(float[] data) {
        int index = 0;
        float max = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
                index = i;
            }
        }
        return index;
    }

    public float getCost(float[] predicted) {
        float sum = 0;
        for (int i = 0; i < predicted.length; i++) {
            float diff = outputs[i]-predicted[i];
            sum += diff*diff;
        }
        return (float) Math.sqrt(sum);
    }
}
