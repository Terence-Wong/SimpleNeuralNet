

import java.io.*;





public class NeuralNetwork{
	
	static double[][] input = {
		{0.0, 0.0},
		{0.0, 1.0},
		{1.0, 0.0},
		{1.0, 1.0}
	};
	static double[] output = {
		0.0,
		1.0,
		1.0,
		0.0
	};
	
	
	public static void main(String[]args)throws IOException{
		Neuron[][] network = new Neuron[3][];
		network[0] = new Neuron[]{
			new Neuron(),
			new Neuron()
		};
		network[1] = new Neuron[]{
			new Neuron(network[0]),
			new Neuron(network[0]),
			new Neuron(network[0]),
		};
		network[2] = new Neuron[]{
			new Neuron(network[1])
		};
		NetworkVisualizer brain = new NetworkVisualizer(network);
		brain.frame.add(brain);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//test network
		while(true){
			String[] in = br.readLine().split(" ");
			if(in[0].equals("")){
				break;
			}
			//input layer
			for(int x = 0; x < network[0].length; x++){
				network[0][x].value = Double.parseDouble(in[x]);
			}
			for(int y = 1; y < network.length; y++){
				for(int x = 0; x < network[y].length; x++){
					network[y][x].process();
				}
			}
			brain.frame.repaint();
			System.out.println(network[network.length-1][0].value);
			
		}
		
		System.out.println("training");
		//train network?
		for(int epoch = 0; epoch < 600000; epoch++){
			//forward Propagation
			for(int in = 0; in < 4; in++){
				for(int x = 0; x < network[0].length; x++){
					network[0][x].value = input[in][x];
				}
				for(int y = 1; y < network.length; y++){
					for(int x = 0; x < network[y].length; x++){
						network[y][x].process();
					}
				}
				//back propagation
				double MarginOfError = output[in] - network[network.length-1][0].value; // error for layer 3
				if(epoch % 100000 == 0){
					brain.frame.repaint();
					br.readLine();
					System.out.println("Epoch " + epoch + ", Error: " + MarginOfError);
				}
				//double deltaError = MarginOfError * nonLin(network[network.length-1][0].sum,true) ; //delta for layer 3
				network[network.length-1][0].backProcess(MarginOfError,in);
			}
			network[network.length-1][0].learn();
			
		}
		System.out.println("finished training");
		
		//test network
		while(true){
			String[] in = br.readLine().split(" ");
			if(in[0].equals("")){
				break;
			}
			//input layer
			for(int x = 0; x < network[0].length; x++){
				network[0][x].value = Double.parseDouble(in[x]);
			}
			for(int y = 1; y < network.length; y++){
				for(int x = 0; x < network[y].length; x++){
					network[y][x].process();
				}
			}
			brain.frame.repaint();
			System.out.println(network[network.length-1][0].value);
		}
		System.out.println("ded");
	}
	public static double sigmoid(double i){
		return 1/(1+Math.exp(-i));
	}
	public static double nonLin(double i, boolean d){
		return i*(1-i);
	}
}

/*
feed forward

get value from prev neurons(including bias)
multiply values with weights
summation
sigmoid function



	{}
{}-/< \
  X	{}-{}
{}-\< /
	{}
  /	  /
[]	[]

*/



/*
notes

FORWARD PROPAGATION
-apply weights(first iteration use Gaussian distribution to set weights)
-calculate output

BACK PROPAGATION
-calculate error
-adjust weights to decrease error


ITERATION RUNTHROUGH
(0. Set weights using Gaussian/normal distribution)
 1. Input data to input layer
 2. Hidden layer calculate value,(i1 * w1 + i2 * w2 + .... = value)
 3. Apply 'activation' function to Hidden Layer neurons 
    - SIGMOID F(x) =     1
					 ----------
					 1 + e^(-x)
    Other activation functions include:
	-(Linear)
    -(Hyperbolic Tangent)
 4. Output layer calculate value,(i1 * w1 + i2 * w2 + .... = value)
 5. Apply 'activation' function to Output Layer neuron(s)
 --------OUTPUT RECIEVED----------
 6. Calculate Margin of Error(Targ: 0, Result: 0.77, Error: -0.77)
 
 7. Calculate DOS(apply derivative of Action Function to output sum)
 DELTA OUTPUT SUM: change in output sum{DOS = F'(outputSum[no activation]) * (marginOfError) }
 8. Calculate the change weights in each edge
    (chW = DOS * neuronValue, edge.weight += chW)
 DELTA HIDDEN SUM: DOS but hidden layer
 {DOS * OGhiddenWeight * F'(hiddenSum[no activation])}
 9. Calculate the change weights in each edge
    (chW = DHS * input)
*/