public class DataPoint {
    
    public float[] inputs, expectedOutputs;

    public DataPoint(float[] inputs, float[] expectedOuputs) {
        this.inputs = inputs;
        this.expectedOutputs = expectedOuputs;
    }
}
