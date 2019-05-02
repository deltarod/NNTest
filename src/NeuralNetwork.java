import utils.Matrix;

import java.lang.Math;

public class NeuralNetwork
{
    private Layer firstLayer;


    private class Layer
    {
        Matrix layerWeights;

        Matrix layerBiases;

        Matrix currentValue;

        int layerSize;

        Layer nextLayer = null;

        Layer prevLayer = null;

        //will only be assigned in firstLayer
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

        Layer current = firstLayer;

        for( int layers = 0; layers < inputSize; layers++ )
        {
            current.layerWeights = new Matrix( 1, input[layers], Matrix.SD);

            current.layerBiases = new Matrix( 1, input[layers], Matrix.ZEROS);

            current.currentValue = new Matrix(1, input[layers], Matrix.ZEROS);

            current.layerSize = input[layers];

            current.nextLayer = new Layer();

            current.nextLayer.prevLayer = current;

            current = current.nextLayer;

            // TODO: 5/2/19 layer weight size needs to be size of previous layer for weights

        }

        firstLayer.outputLayer = current;
    }


    private static double sigmoid( double x )
    {
        return 1/(1+(Math.pow(Math.E, -x)));
    }


    // TODO: 5/2/19 Implement layer calculations

    // TODO: 5/2/19 Implement layer training
}
