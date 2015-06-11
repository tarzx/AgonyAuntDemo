import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/** This class generates the basic training neural network just in case offline service
 * 
 * @author Patomporn Loungvara
 *
 */
public class AutomaticTraining {
	// Maximum number of neurons to have
	final int MAX_NEURONES = 15;
	// Maximum learning rate
	final double MAX_LEARNING = 1.0;
	// Maximum momentum
	final double MAX_MOMENTUM = 1.0;
	// Amount to increase learning rate and momentum by
	final double STEP = 0.1;
	// Maximum epoch 
	final int MAX_EPOCH = 1500;
	// Variables for finding best net
	double best_error = 0.0;
	int best_neurons = 0;

	/** Create a net with a given number of input/hidden/output neurons
	 * 
	 * @param 	neurons		Number of hidden neurones
	 * @param 	inputBits	Number of input neurones
	 * @param 	outputBits	Number of output neurones
	 * @return				New network
	 */
	public BasicNetwork createNet(int neurons, int inputBits, int outputBits) {
		BasicNetwork network = new BasicNetwork();
		// First layer
		network.addLayer(new BasicLayer(null, true, inputBits));
		// Hidden layer, with [neurons] neurons
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, neurons));
		// Output later
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputBits));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}
	
	/** Create normal training process 
	 * 
	 * @param	neurones	number of hidden neuron
	 * @param	trainingSet	training data set
	 * @return				training error
	 */	
	public double train(BasicNetwork network, MLDataSet trainingSet) {
		// Using BackProp
		final ResilientPropagation train = new ResilientPropagation(network, trainingSet);
		int epoch = 1;
		// Train
		do {
			train.iteration();
			epoch++;
		} while (epoch < MAX_EPOCH);
		return train.getError();
	}
	
	/** Create a net with the best error 
	 * 
	 * @return			Best network
	 */	
	public BasicNetwork calibrate(MLDataSet trainingSet, int inputBits, int outputBits) {
		BasicNetwork bestNet = null;
		//long start = System.currentTimeMillis();
		// Iterate through a number of neurons, increasing each time
		for (int i = 1; i < MAX_NEURONES; i++) {
			BasicNetwork network = createNet(i, inputBits, outputBits);
			double current_error = train(network, trainingSet);
			//System.out.println("Weight: " + network.getWeight(0, 0, 0));
			// Check everything went as expected
			if (!Double.isNaN(current_error)) {
				// If it's the first run, or if the net has the best (lowest) error yet
				if (i==1 || current_error < best_error) {
					// Record info
					best_error = current_error;
					bestNet = network;
					best_neurons = i;
				}
			}
		}
//		System.out.println("Done!");
//		System.out.println("Best neurons: "+bestNet.getLayerNeuronCount(1)+
//				"\nLowest error: " + best_error);
//		System.out.println("---------------------------------------------------");
		
		// Return the best ever network
		return bestNet;
	}
}
