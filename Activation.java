public class Activation{

    public static float[] activationValues, activationDerivative;
    public static float a, b, step, deriv2ndLeft, deriv2ndRight;
    public static int pointsNumber;

    public static void computeValues(float a, float b, int pointsNumber) {
        activationValues = new float[pointsNumber];
        activationDerivative = new float[pointsNumber];
        Activation.pointsNumber = pointsNumber;
        step = (b-a)/pointsNumber;
        for (int i = 0; i < pointsNumber; i++) {
            activationValues[i] = activationFunction(step*i+a);
            activationDerivative[i] = activationDerivative(step*i+a);
        }
        deriv2ndLeft = (activationDerivative[1]-activationDerivative[0])/step;
        deriv2ndRight = (activationDerivative[pointsNumber-1]-activationDerivative[pointsNumber-2])/step;
    }

    public static float getActivationValue(float input) {
        if (input < a) {
            return activationValues[0]+activationDerivative[0]*(input-a);
        } else if (input > b) {
            return activationValues[pointsNumber-1]+activationDerivative[pointsNumber-1]*(b-input);
        } else {
            return activationValues[(int)((input-a)/step)];
        }
    }

    public static float getActivationDerivativeValue(float input) {
        if (input < a) {
            return activationDerivative[0]+deriv2ndLeft*(input-a);
        } else if (input > b) {
            return activationDerivative[pointsNumber-1]+deriv2ndRight*(b-input);
        } else {
            return activationDerivative[(int)((input-a)/step)];
        }
    }

    public static float activationFunction(float input) {
        return (float) (1 / (1 + Math.exp(-input)));
    }

    public static float activationDerivative(float input) {
        float activation = activationFunction(input);
        return activation*(1-activation);
    }
}