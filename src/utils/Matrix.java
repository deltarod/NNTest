package utils;

import java.util.Random;

/**
 * Class for storing and modifying matrices
 */
public class Matrix
{
    public static final int ZEROS = 0, ONES = 1, SD = 2;

    private double[][] matrix;

    private int width, height;

    private Random rand = new Random();

    /**
     * Initalizes blank matrix
     * @param width width of matrix
     * @param height height of matrix
     */
    public Matrix( int width, int height )
    {
        matrix = new double[width][height];
        this.width = width;
        this.height = height;
    }

    /**
     * initalizes matrix of type
     * @param width width of matrix
     * @param height height of matrix
     * @param type type of matrix
     */
    public Matrix( int width, int height, int type )
    {
        matrix = new double[width][height];
        this.width = width;
        this.height = height;
        initialize( type );
    }

    /**
     * used for adding or modifying value
     * @param x x value of location
     * @param y y value of location
     * @param input what to change location to
     */
    public void modify(int x, int y, double input )
    {
        matrix[x][y] = input;
    }

    public double get(int x, int y)
    {
        return matrix[x][y];
    }

    /**
     * Multiplies two matrices
     * @param other other matrix to multiply with this
     * @return output matrix
     */
    public Matrix multiply( Matrix other )
    {
        if( width != other.height )
        {
            throw new IllegalArgumentException("Cannot multiply these arrays");
        }

        Matrix output = new Matrix( other.width, height, ZEROS);

        for( int xOutput = 0; xOutput < output.width; xOutput++ )
        {

            for( int yOutput = 0; yOutput < output.height; yOutput++ )
            {
                double outputVal = 0;

                //loop for calculating the dot product
                for( int dot = 0; dot < width; dot++ )
                {
                    outputVal += get(dot, yOutput)*other.get(xOutput, dot);
                }


                output.modify(xOutput, yOutput, outputVal);
            }
        }

        return output;
    }

    /**
     * Transposes a matrix
     * @return Transposed Matrix
     */
    public Matrix transpose()
    {
        Matrix output = new Matrix( height, width );

        for( int xIncrement = 0; xIncrement < width; xIncrement++ )
        {
            for( int yIncrement = 0; yIncrement < height; yIncrement++ )
            {
                output.modify(yIncrement, xIncrement, matrix[xIncrement][yIncrement]);
            }
        }

        return output;
    }

    /**
     * Adds two matrices together
     * @param other other matrix
     * @return matrix of the two added together
     */
    public Matrix add( Matrix other )
    {
        if( isSameSize( other ) )
        {
            throw new IllegalArgumentException("Arrays not same size");
        }

        Matrix output = new Matrix( width, height );

        for( int xIncrement = 0; xIncrement < width; xIncrement++ )
        {
            for( int yIncrement = 0; yIncrement < height; yIncrement++ )
            {
                output.modify(xIncrement, yIncrement, get(xIncrement, yIncrement) + other.get(xIncrement, yIncrement));
            }
        }

        return output;
    }

    public boolean isSameSize( Matrix other )
    {

        return ( width == other.width && height == other.height );
    }

    public Matrix averageMatrix( Matrix other, double deviationVal )
    {
        if ( !isSameSize( other ) )
        {
            throw new IllegalArgumentException( "Arrays not same size" );
        }

        Matrix child = new Matrix( width, height, ZEROS );

        double current, partner, average, deviation;

        for( int xInc = 0; xInc < width; xInc++ )
        {
            for( int yInc = 0; yInc < height; yInc++ )
            {
                current = matrix[xInc][yInc];

                partner = other.matrix[xInc][yInc];

                average = ( current + partner ) / 2;

                deviation = average * deviationVal;

                //sets child matrix to average, with a deviation value added in for extra "spice" in the algorithm.
                child.matrix[xInc][yInc] = average + deviation;

            }
        }

        return child;
    }

    /**
     * used for initializing array of type
     * @param type type to initialize to
     */
    private void initialize( int type )
    {
        double fill = 0;



        switch ( type )
        {
            case ZEROS:
                fill = 0;
                break;

            case ONES:
                fill = 1;
                break;
        }

        for( int yIncrement = 0; yIncrement < height; yIncrement++ )
        {
            for( int xIncrement = 0; xIncrement < width; xIncrement++ )
            {
                if( type == SD )
                {

                    matrix[xIncrement][yIncrement] = rand.nextGaussian();
                }
                else
                {
                    matrix[xIncrement][yIncrement] = fill;
                }
            }
        }
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("[ ");

        for( int yIncrement = 0; yIncrement < height; yIncrement++ )
        {
            sb.append("[ ");

            for( int xIncrement = 0; xIncrement < width; xIncrement++ )
            {
                sb.append(matrix[xIncrement][yIncrement]);

                if(xIncrement == width-1)
                {
                    if( yIncrement == height-1 )
                    {
                        sb.append(" ] ]\n");
                    }
                    else sb.append(" ], \n");
                }
                else
                {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    int getWidth()
    {
        return width;
    }

    int getHeight()
    {
        return height;
    }
}
