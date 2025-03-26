import java.io.Serializable;

public class Layer implements Serializable {

    public int prevNodes, nodes;
    public float[][] weights;
    public float[] biases;

    public float[] inputs, outputs, weightedInputs, biasesGradient;
    public float[][] weightsGradient;

    public Layer(int prevNodes, int nodes) {
        this.biases = new float[nodes];
        this.biasesGradient = new float[nodes];
        this.weights = new float[nodes][prevNodes];
        this.weightsGradient = new float[nodes][prevNodes];
        this.prevNodes = prevNodes;
        this.nodes = nodes;
    }

    public void load(float[][] weights, float[] biases) {
        this.weights = weights;
        this.weightsGradient = new float[nodes][prevNodes];
        this.biases = biases;
        this.biasesGradient = new float[nodes];
        this.prevNodes = weights[0].length;
        this.nodes = weights.length;
    }

    public void randomInit(float a, float b) {
        for (int currNode = 0; currNode < this.nodes; currNode++) {
            biases[currNode] = (float) (Math.random()*(b-a)+a);
            for (int prevNode = 0; prevNode < this.prevNodes; prevNode++) {
                weights[currNode][prevNode] = (float) (Math.random()*(b-a)+a);
            }
        }
    }

    public float[] computeOutput(float[] inputs) {
        this.inputs = inputs;
        this.outputs = new float[this.nodes];
        this.weightedInputs = new float[this.nodes];

        for (int currNode = 0; currNode < this.nodes; currNode++) {
            outputs[currNode] = biases[currNode];
            for (int prevNode = 0; prevNode < this.prevNodes; prevNode++) {
                outputs[currNode] += inputs[prevNode] * weights[currNode][prevNode];
            }
            this.weightedInputs[currNode] = outputs[currNode];
            outputs[currNode] = activationFunction(outputs[currNode]);
        }
        return outputs;
    }

    public float[] computeLLNodeValues(float[] expected) {
        float[] nodeValues = new float[this.nodes];
        for (int i = 0; i < this.nodes; i++) {
            nodeValues[i] = activationDerivative(weightedInputs[i])*2*(outputs[i]-expected[i]);
        }
        return nodeValues;
    }

    public float[] computeHLNodeValues(Layer oldLayer, float[] oldNodeValues) {
        float[] nodeValues = new float[nodes];
        
        for (int node = 0; node < nodes; node++) {
            for (int oldNode = 0; oldNode < oldNodeValues.length; oldNode++) {
                nodeValues[node] += oldLayer.weights[oldNode][node]*oldNodeValues[oldNode];
            }
            nodeValues[node] *= activationDerivative(weightedInputs[node]);
        }

        return nodeValues;
    }

    public void updateGradients(float[] nodeValues) {
        for (int node = 0; node < nodes; node++) {
            for (int prevNode = 0; prevNode < prevNodes; prevNode++) {
                weightsGradient[node][prevNode] += inputs[prevNode] * nodeValues[node];
            }
            biasesGradient[node] += nodeValues[node];
        }
    }

    public void applyGradients(float learnRate) {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                weights[i][j] -= weightsGradient[i][j]*learnRate;
                weightsGradient[i][j] = 0;
            }
        }

        for (int i = 0; i < biases.length; i++) {
            biases[i] -= biasesGradient[i]*learnRate;
            biasesGradient[i] = 0;
        }
    }

    public float activationFunction(float input) {
        return (float) (1 / (1 + Math.exp(-input)));
    }

    public float activationDerivative(float input) {
        float activation = activationFunction(input);
        return activation*(1-activation);
    }
}
