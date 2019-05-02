import utils.Matrix;

public class Runner

{
    public static void main(String[] args)
    {
        int[] x = {10,5,3};

        NeuralNetwork nn = new NeuralNetwork(x, 3);

        Matrix m = new Matrix(1, 10, 2);

        System.out.println(nn.update(m));





        // TODO: 5/2/19 Implement stupid thing to show how this all works
    }
}
