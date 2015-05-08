import java.io.File;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.persist.EncogDirectoryPersistence;


public class NeuralNetUtil {
	// CSV File
	static String interventionNetCSV = "intervention_double_value.csv";
	static String subQuestionNetCSV = "normalizedSubQuestion.csv";
	static String[] netCSVset = {"value_set1.csv", "value_set2.csv", "value_set3.csv", 
								 "value_set4.csv", "value_set5.csv"};
	static int nSet = netCSVset.length;
	
	// Number of bits representing input
	static int NUM_INPUT_BITS = 3;
	// Number of bits representing output 
	static int NUM_OUTPUT_BITS = 1;
	// Training data
	static MLDataSet trainingSet;
	static MLDataSet[] trainingSets = new MLDataSet[nSet];

	// Whether the net can be parsed for sending to the app
	static boolean ready_to_parse = false;
	static File neuralNetBackfile = new File("neuralNetBack.eg");
	static File neuralNetResilientfileRp = new File("neuralNetResilientRp.eg");
	static File neuralNetResilientfileRm = new File("neuralNetResilientRm.eg");
	static File neuralNetResilientfileiRp = new File("neuralNetResilientiRp.eg");
	static File neuralNetResilientfileiRm = new File("neuralNetResilientiRm.eg");
	static File[] neuralNetset = {new File("neuralNetset1.eg"), 
		new File("neuralNetset2.eg"), new File("neuralNetset3.eg"), 
		new File("neuralNetset4.eg"), new File("neuralNetset5.eg")};

	// Maximum number of neurons to have
	final static int MAX_NEURONS = 15;
	// Maximum learning rate
	final static double MAX_LEARNING = 1.0;
	// Maximum momentum
	final static double MAX_MOMENTUM = 1.0;
	// Amount to increase learning rate and momentum by
	final static double STEP = 0.1;
	// Maximum epoch 
	final static int MAX_EPOCH = 1500;
	// Threshold for Error
	final static double TRAIN_THRESHOLD = 1E-4;
	final static double THRESHOLD = 3000.0;
	
	/**  Write out adult net to file
	 * 
	 * @param network	Fully trained net
	 */
	public static void writeFile(File netFile, BasicNetwork network) {
		EncogDirectoryPersistence.saveObject(netFile, network);
		NeuralNetUtil.ready_to_parse = true;
	}
	
	/** Create a net with a given number of hidden neurons
	 * 
	 * @param neurons	Number of hidden neurons
	 * @return			New network
	 */
	public static BasicNetwork createNet(int neurons) {
		BasicNetwork network = new BasicNetwork();
		// First layer
		network.addLayer(new BasicLayer(null, true, NUM_INPUT_BITS));
		// Hidden layer, with [neurons] neurons
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, neurons));
		// Output later
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, NUM_OUTPUT_BITS));
		network.getStructure().finalizeStructure();
		network.reset();
		
		new ConsistentRandomizer(-1, 1, 500).randomize(network);
		System.out.println(network.dumpWeights());

		//double[] weights = new double[network.getFlat().getWeights().length];
		//network.getFlat().setWeights(weights);
		
		return network;
	}
}
