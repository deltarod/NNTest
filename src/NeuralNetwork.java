import utils.Matrix;

import java.lang.Math;

public class NeuralNetwork
{
    private Layer firstLayer;

    private class Neuron
    {
        Matrix inputWeights;

        double bias;

        double currentVal;
    }

    private class Layer
    {

        Neuron[] neurons = null;

        int size;

        Layer prev = null;

        Layer next = null;

        //only assigned in firstLayer to make getting outputs easier
        Layer outputLayer = null;

    }

    /**
     * Initializer for customizable layer sizes
     * @param input layer structure
     * @param inputSize number of layers
     */
    public NeuralNetwork(int[] input, int inputSize )
    {
        firstLayer = new Layer();

        //setup first layer, needs special setting up as weights/bias not needed
        firstLayer.neurons = new Neuron[input[0]];

        for ( Neuron neuron : firstLayer.neurons )
        {
            neuron.inputWeights = null;

            neuron.bias = 0;

            neuron.currentVal = 0;
        }

        firstLayer.size = input[0];

        firstLayer.next = new Layer();

        Layer current = new Layer();

        for( int layer = 1; layer < inputSize; layer++ )
        {
            //setup neurons
            current.neurons = new Neuron[input[layer]];

            for ( Neuron neuron : current.neurons )
            {
                //setting up neuron weights, size is prev size because of connections
                neuron.inputWeights = new Matrix(1, current.prev.size, Matrix.SD);

                neuron.bias = 0;

                neuron.currentVal = 0;
            }
        }

        //last layer set up should be output layer
        firstLayer.outputLayer = current;
    }


    private static double sigmoid( double x )
    {
        return 1/(1+(Math.pow(Math.E, -x)));
    }


    // TODO: 5/2/19 Implement layer calculations

    // TODO: 5/2/19 Implement layer training
}
