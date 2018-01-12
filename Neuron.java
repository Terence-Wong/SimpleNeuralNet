import java.util.Random;
public class Neuron{
	final double LEARNING_RATE = 0.1;
	
	
	//double in_data[]; // values sent to this neuron
	double[] weights; // weights on each edge
	double value = 0.0; // value this neuron will be sending out
	double sum = 0.0; // value before sigmoid function
	
	//double MarginOfError;
	double deltaError;
	double[][] deltaWeights;
	
	Neuron[] pLayer;
	int edges;
	boolean inputNeuron = true;
	public Neuron(Neuron[] prev){ //seperate constructor for input neurons?
		//in_data = new double[prev.length];
		pLayer = prev;
		weights = new double[prev.length + 1]; // 1 + for bias
		deltaWeights = new double[4][prev.length + 1];
		
		edges = prev.length;
		
		Random rand = new Random();//seed 1
		for(int x = 0; x < edges + 1; x++){
			weights[x] = rand.nextGaussian();
		}
		inputNeuron = false;
	}
	public Neuron(){
		
	}
	
	public static double nonLin(double i){
		return i*(1-i);
	}
	public static double sigmoid(double i){
		double returnValue = 1/(1+Math.exp(-i));
		if(Double.isInfinite(returnValue)){
			return 1.0;
		}
		return returnValue;
	}
	
	
	void get_value (){
		value = 0;//(combine weights and data);
	}
	void process(){
		if(inputNeuron){//neuron in input layer
			
		}else{
			sum = 0.0;
			for(int x = 0; x < edges; x++){
				sum += weights[x] * pLayer[x].value;
			}
			sum += weights[edges];//bias calculation
			value = sigmoid(sum);
		}
		//value = sigmoid(sum);
	}
	void backProcess(double moError,int inputType){//moError = (pN_dError * correspondingWeight);
		//double MarginOfError = output[0] - network[network.length-1][0].value; // error for layer 3
		//double deltaError = MarginOfError * nonLin(network[network.length-1][0].sum,true) ; //delta for layer 3
		
		deltaError = moError * nonLin(value);
		if(!pLayer[0].inputNeuron){
			for(int x = 0; x < pLayer.length; x++){
				pLayer[x].backProcess(deltaError*weights[x],inputType);
			}
		}
		for(int x = 0; x < deltaWeights[inputType].length - 1; x++){//changed From 1 to 2
			deltaWeights[inputType][x] = deltaError * pLayer[x].value;
		}
		//bias
		deltaWeights[inputType][deltaWeights[inputType].length - 1] = deltaError;// * 1 cancelled     //changed From 1 to 2
		
	}
	void learn(){
		for(int x = 0; x < weights.length; x++){
			double deltaSum = 0.0;
			for(int y = 0; y < 4; y++){ 
				deltaSum += deltaWeights[y][x];
			}
			weights[x] += LEARNING_RATE * (deltaSum);
		}
		if(!pLayer[0].inputNeuron){
			for(int x = 0; x < pLayer.length; x++){
				pLayer[x].learn();
			}
		}
	}
	
	/*void send_value(){
		for(int x = 0; x < out_edges.length; x++){
			out_edges[x].accept_data(value);
		}
	}*/
	//next layer will get value, just compute the value
}