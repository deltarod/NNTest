import utils.Matrix;


public class NeuralNetwork
{
    private int[] layers;

    private int numLayers;

    private int sigType;

    private Layer head;


    class Layer
    {
        Matrix perceptronWeight;

        Matrix perceptronBias;

        int numPerceptrons;
        
        int prevLayerSize;

        Layer prevLayer = null;

        Layer nextLayer = null;


        /**
         * Default constructor for layer class
         * @param prevLayerSize size of previous layer
         * @param layerSize size for current layer
         */
        Layer( int prevLayerSize, int layerSize )
        {
            numPerceptrons = layerSize;
            
            this.prevLayerSize = prevLayerSize;

            perceptronWeight = new Matrix( prevLayerSize, layerSize, Matrix.SD );

            perceptronBias = new Matrix( 1, layerSize, Matrix.SD );

        }

        /**
         * Calculates values for current NN and returns output
         * @param input Input for calculation
         * @return Output from NN
         */
        Matrix calculateOutput( Matrix input )
        {
            return calculateOutput( input, this );
        }

        /**
         * Recursive helper for calculateOutput
         * @param input input Matrix
         * @param current current Layer
         * @return returns output matrix
         */
        Matrix calculateOutput( Matrix input, Layer current )
        {
            Matrix output = current.perceptronWeight.multiply( input );

            output = output.add( perceptronBias );

            if( current.nextLayer != null )
            {
                return calculateOutput( output, current.nextLayer );
            }
            else
            {
                return output;
            }

        }

        /**
         * setup for breeding
         * @param partner partner NN to breed with
         * @param deviation deviation to spice up the averaging
         * @return daughter NN
         */
        Layer breedLayer( Layer partner, double deviation )
        {
            Layer daughter = new Layer( prevLayerSize, numPerceptrons);

            breedLayer( this, partner, daughter, deviation );

            return daughter;
        }

        void breedLayer( Layer current, Layer partnerCurrent, Layer daughterCurrent, double deviation )
        {
            daughterCurrent.perceptronWeight = current.perceptronWeight.averageMatrix( partnerCurrent.perceptronWeight, deviation );

            daughterCurrent.perceptronBias = current.perceptronBias.averageMatrix( partnerCurrent.perceptronBias, deviation );

            if( current.nextLayer != null )
            {
                daughterCurrent.nextLayer = new Layer( daughterCurrent.numPerceptrons, current.nextLayer.numPerceptrons );

                daughterCurrent.nextLayer.prevLayer = daughterCurrent;

                breedLayer( current.nextLayer, partnerCurrent.nextLayer, daughterCurrent.nextLayer, deviation );
            }
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
        Layer childLayer;

        if( !isSameSize( partner ) )
        {
            throw new IllegalArgumentException("These NN's cant breed");
        }

        childLayer = head.breedLayer( partner.head, deviation );

        return new NeuralNetwork( this, childLayer );
    }

    /**
     * Asexual reproduction function
     * @param deviation deviation for how much to change by
     * @return daughter NN
     */
    NeuralNetwork asexRepro( double deviation )
    {
        Layer daughterLayer = head.breedLayer( head, deviation );

        return new NeuralNetwork( this, daughterLayer );
    }

    //TODO: needs more back prop

    Matrix calculateOutput( Matrix input )
    {
        return head.calculateOutput( input );
    }

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
            layer.nextLayer = new Layer( layer.numPerceptrons, layers[ layerIncrement ] );

            layer.nextLayer.prevLayer = layer;
        }
    }

    /**
     * check to see if NN are breedable
     * @param other other NN
     * @return isBreedable (same size)
     */
    private boolean isSameSize( NeuralNetwork other )
    {
        if( other.numLayers != numLayers )
        {
            return false;
        }

        for( int layerInc = 0; layerInc < numLayers; layerInc++ )
        {
            if( layers[layerInc] != other.layers[layerInc] )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates final value from NN
     * @param input input to be ran through the NN
     * @return output value
     */
    public Matrix calculateValue( Matrix input )
    {
        if( input.getHeight() != head.numPerceptrons )
        {
            throw new IllegalArgumentException("Input Size different than desired input");
        }

        return head.calculateOutput( input );
    }


    private static double sigmoid( double x )
    {
        return 1/(1+(Math.pow(Math.E, -x)));
    }

}
