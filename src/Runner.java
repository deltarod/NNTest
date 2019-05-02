import utils.Matrix;

public class Runner

{
    public static void main(String[] args)
    {
        int[] x = {10,5,1};

        NeuralNetwork nn = new NeuralNetwork(x, 3);

        Matrix m = new Matrix(10, 1, 1);

        System.out.println(m);

        // TODO: 5/2/19 Implement stupid thing to show how this all works
    }
}
