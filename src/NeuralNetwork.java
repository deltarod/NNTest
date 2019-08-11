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
                return nextLayer.calculateOutput( layerOutputMatrix );
            }
        }
    }

    //TODO: implement breeding and/or asexual reproduction

    NeuralNetwork breed( NeuralNetwork partner )
    {
        NeuralNetwork child = new NeuralNetwork( layers, numLayers, sigType );

        child.generate();

        Layer layer = head, other = partner.head, childHead = child.head;


    }

    NeuralNetwork( int[] layerSizes, int numLayers, int sigType )
    {
        layers = layerSizes;

        this.numLayers = numLayers;

        this.sigType = sigType;
    }

    private void generate()
    {
        int layerIncrement;

        Layer layer, head;

        //layer 0 is input layer, layer 1 is first layer of perceptrons
        head = new Layer(layers[ 0 ], layers[ 1 ] );

        layer = head;

        for( layerIncrement = 2; layerIncrement < numLayers; layerIncrement++ )
        {
            layer.nextLayer = new Layer( layer.numNeurons, layers[ layerIncrement] );

            layer.nextLayer.prevLayer = layer;
        }
    }


    private static double sigmoid( double x )
    {
        return 1/(1+(Math.pow(Math.E, -x)));
    }

}
