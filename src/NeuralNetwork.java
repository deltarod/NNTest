import utils.Matrix;

import java.lang.Math;

public class NeuralNetwork
{
    private Layer firstLayer;

    /**
     * Class for each individual neuron in a layer
     */
    private class Neuron
    {
        Matrix inputWeights;

        double bias;
    }

    /**
     * Class for each individual layer of the NN
     */
    private class Layer
    {

        Neuron[] neurons = null;

        int size;

        Matrix output;

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

        for ( int neuronInc = 0; neuronInc < input[0]; neuronInc++ )
        {
            Neuron neuron = new Neuron();

            neuron.inputWeights = null;

            neuron.bias = 0;

            firstLayer.neurons[neuronInc] = neuron;
        }

        firstLayer.output = new Matrix(1, input[0]);

        firstLayer.size = input[0];

        firstLayer.next = new Layer();

        firstLayer.next.prev = firstLayer;

        Layer current = firstLayer.next;


        for( int layer = 1; layer < inputSize; layer++ )
        {

            //setup neurons
            current.neurons = new Neuron[input[layer]];

            current.output = new Matrix(1, input[layer]);

            for ( int neuronInc = 0; neuronInc < input[layer]; neuronInc++ )
            {
                Neuron neuron = new Neuron();

                //setting up neuron weights, size is prev size because of connections
                neuron.inputWeights = new Matrix(1, current.prev.size, Matrix.SD);

                neuron.bias = 0;

                current.neurons[neuronInc] = neuron;
            }

            current.size = input[layer];

            //catch so it does not create a new layer on the last layer
            if( layer != inputSize - 1 )
            {
                current.next = new Layer();

                current.next.prev = current;
            }

            current = current.next;
        }

        //last layer set up should be output layer
        firstLayer.outputLayer = current;
    }

    /**
     * Function to run through the neural net with values
     * @param inputVals values to be inputted
     * @return Matrix of values on last layer
     */
    public Matrix update( Matrix inputVals )
    {
        if(!inputVals.isSameSize(firstLayer.output))
        {
            throw new IllegalArgumentException("Input values different size as input layer");
        }

        firstLayer.output = inputVals;

        return update(firstLayer.next);
    }

    /**
     * Helper recursive function for update
     * @param layer Current layer to update
     * @return final matrix for output values
     */
    public Matrix update( Layer layer )
    {
        Matrix prevResults = layer.prev.output.transpose();

        Matrix output = new Matrix(1, layer.size);

        for( int neuronIndex = 0; neuronIndex < layer.neurons.length; neuronIndex++)
        {
            Neuron currentNeuron = layer.neurons[neuronIndex];

            double outputVal = prevResults.multiply(currentNeuron.inputWeights).get(0,0);

            output.modify(0, neuronIndex, sigmoid(outputVal + currentNeuron.bias));
        }

        layer.output = output;

        if( layer.next == null )
        {
            return output;
        }
        else
        {
            return update(layer.next);
        }
    }

    // TODO: 5/2/19 Implement layer training




    private static double sigmoid( double x )
    {
        return 1/(1+(Math.pow(Math.E, -x)));
    }
}
