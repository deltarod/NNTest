import utils.Matrix;


public class NeuralNetwork
{
    Matrix input;

    private int[] layers;

    private int numLayers;

    private int sigType;

    private Layer head;

    private class Neuron
    {
        Matrix prevLayerSig;

        int prevLayerSize;

        double bias;

        //setup each individual node
        Neuron( int prevLayerSize, double bias )
        {
           prevLayerSig = new Matrix( prevLayerSize, 1, sigType );

           this.prevLayerSize = prevLayerSize;

           this.bias = bias;
        }

        Neuron( Matrix newMatrix, int prevLayerSize, double bias )
        {
            this.prevLayerSize = prevLayerSize;

            prevLayerSig = newMatrix;

            this.bias = bias;
        }

        Neuron breedNeuron( Neuron other, double deviation )
        {
            double biasAvg, biasDeviation;

            biasAvg = ( bias + other.bias ) / 2;

            biasDeviation = biasAvg + biasAvg * deviation;

            return new Neuron( prevLayerSig.averageMatrix( other.prevLayerSig, deviation ), prevLayerSize, biasDeviation );
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

            double neuronOutput;


            int increment = 0;

            for ( Neuron n : current.neurons )
            {
                 if( current == head )
                 {
                     //getting from 0,0 as the output from multiplying will be only 1 value
                     neuronOutput = n.prevLayerSig.multiply( input.transpose() ).get(0,0);

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

        Layer breedLayer( Layer partner, double deviation )
        {
            Layer current = head, partnerCurrent = partner;

            //used for getting input size for first layer of perceptrons
            int inputSize = current.neurons[ 0 ].prevLayerSize;

            Layer child = new Layer( inputSize, current.numNeurons );

            while( current != null )
            {
                for (int neuronInc = 0; neuronInc < numNeurons; neuronInc++) {
                    //long ugly statement, basically is breeding each neuron 1 by 1
                    child.neurons[neuronInc] = current.neurons[neuronInc].breedNeuron(partnerCurrent.neurons[neuronInc], deviation);
                }

                current = current.nextLayer;

                partnerCurrent = partnerCurrent.nextLayer;
            }

            return child;
        }
    }

    NeuralNetwork( int[] layerSizes, int numLayers, int sigType )
    {
        layers = layerSizes;

        this.numLayers = numLayers;

        this.sigType = sigType;
    }

    /**
     * Used for creating child NN
     * @param parent parent to child
     * @param childLayer new childLayer
     */
    NeuralNetwork( NeuralNetwork parent, Layer childLayer )
    {
        layers = parent.layers;

        numLayers = parent.numLayers;

        sigType = parent.sigType;

        head = childLayer;
    }

    /**
     * Used for breeding 2 NN
     * @param partner partner to breed with
     * @param deviation additional deviation for spice
     * @return child NN
     */
    NeuralNetwork breed( NeuralNetwork partner, double deviation )
    {
        Layer child = head.breedLayer( partner.head, deviation );

        return new NeuralNetwork( this, child );

    }

    //TODO: implement asexual reproduction
    //TODO: Layer Calculation

    /**
     * Generates random layers
     */
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
