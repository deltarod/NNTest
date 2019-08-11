import utils.Matrix;
import java.util.Random;

public class NeuralNetwork
{
    Matrix input;

    int[] layers;

    int numLayers;

    int sigType = 2;

    Layer head;

    private class Neuron
    {
        Matrix prevLayerSig;

        double bias;

        //setup each individual node
        Neuron( int prevLayerSize, double bias )
        {
           prevLayerSig = new Matrix( prevLayerSize, 1, sigType );

           this.bias = bias;
        }
    }

    class Layer
    {
        Matrix layerOutputMatrix;

        Neuron[] neurons;

        int numNeurons;

        Layer prevLayer = null;

        Layer nextLayer = null;


        Layer( int prevLayerSize, int layerSize )
        {
            neurons = new Neuron[ layerSize ];

            numNeurons = layerSize;

            for ( Neuron n : neurons )
            {
                n.prevLayerSig = new Matrix( 1, prevLayerSize, Matrix.SD );


            }
        }

        Matrix calculateOutput( Matrix input )
        {
            Layer current = head;

            Matrix currentMatrix = input;

            double neuronOutput;


            int increment = 0;

            for ( Neuron n : current.neurons )
            {
                 if( current == head )
                 {
                     //getting from 0,0 as the output from multiplying will be only 1 value
                     neuronOutput = n.prevLayerSig.multiply( currentMatrix.transpose() ).get(0,0);

                    //run through sigmoid function with bias then store into output.
                     layerOutputMatrix.modify( 1, increment, sigmoid( neuronOutput + n.bias ) );
                 }

                 increment++;
            }




            if( nextLayer == null )
            {
                return layerOutputMatrix;
            }
            else
            {
                return nextLayer.calculateOutput();
            }
        }
    }


    NeuralNetwork(int[] layerSizes, int numLayers, int sigType )
    {
        layers = layerSizes;

        this.numLayers = numLayers;

        this.sigType = sigType;
    }


    private static double sigmoid( double x )
    {
        return 1/(1+(Math.pow(Math.E, -x)));
    }

}
