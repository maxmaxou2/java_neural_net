import java.io.Serializable;

public class NeuralNetwork implements Serializable {

    public int inputSize;
    public Layer[] layers;

    public NeuralNetwork(int... layersNodes) {
        this.inputSize = layersNodes[0];
        this.layers = new Layer[layersNodes.length-1];
        for (int index = 0; index < layersNodes.length-1; index++) {
            this.layers[index] = new Layer(layersNodes[index], layersNodes[index+1]);
        }
    }

    public Result result(float[] inputs) {
        return new Result(forwardPropagate(inputs));
    }

    public void learn(DataPoint[] trainingBatch) {
        for (DataPoint dataPoint : trainingBatch) {
            backPropagate(dataPoint.inputs, dataPoint.expectedOutputs);
        }
    }

    public void backPropagate(float[] inputs, float[] expectedOuputs) {
        forwardPropagate(inputs);
        Layer outputLayer = layers[layers.length-1];
        float[] nodeValues = outputLayer.computeLLNodeValues(expectedOuputs);
        outputLayer.updateGradients(nodeValues);

        for (int hlIndex = layers.length-2; hlIndex >= 0; hlIndex--) {
            Layer hiddenLayer = layers[hlIndex];
            nodeValues = hiddenLayer.computeHLNodeValues(layers[hlIndex+1], nodeValues);
            hiddenLayer.updateGradients(nodeValues);
        }
    }

    public void applyGradients(float learnRate) {
        for (Layer layer : layers) {
            layer.applyGradients(learnRate);
        }
    }

    public float[] forwardPropagate(float[] inputs) {
        for (Layer layer : layers) {
            inputs = layer.computeOutput(inputs);
        }
        return inputs;
    }

    public void randomInit(float a, float b) {
        for (Layer layer : this.layers) {
            layer.randomInit(a, b);
        }
    }
}
