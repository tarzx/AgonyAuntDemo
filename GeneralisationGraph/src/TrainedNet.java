
import org.encog.neural.networks.BasicNetwork;

/** This class control the information of neural network to serve generalisation technique
 * 
 * @author Patomporn Loungvara
 *
 */
public class TrainedNet {
	// neural network
	private BasicNetwork bestNetwork = null;
	// validation error for the whole trial
	double[] validation = null;
	// training error for the whole trial
	private double[] train = null;
	// best validation error
	private double error = Double.MAX_VALUE;
	// epoch at stopped point
	private int epoch = 0;
	// number of hidden neuron for the network
	private int neurones = 0;
	// trial number
	private int trail = 0;
	// training error at the stopped point
	private double errorTrain = Double.MAX_VALUE;
	// worst validation error and worst training error
	private double[] worstError = { Double.MAX_VALUE, Double.MAX_VALUE };
	
	public TrainedNet() {
		this.bestNetwork = new BasicNetwork();
	}
	
	public TrainedNet(BasicNetwork net, double[] val, double error, int epoch, int neurones, int trail) {
		this.bestNetwork = net;
		this.validation = val;
		this.error = error;
		this.epoch = epoch;
		this.neurones = neurones;
		this.trail = trail;
	}
	
	public void setValidation(double[] val) {
		this.validation = val;
	}
	public void setTrain(double[] val) {
		this.train = val;
	}
	public void setWorstError(double[] error) {
		this.worstError = error;
	}
	public void setTrainError(double error) {
		this.errorTrain = error;
	}
	public void setValidationIdx(int idx, double val) {
		// set if input index is in the validation error array
		if (idx<this.validation.length) {
			this.validation[idx] = val;
		}
	}
	
	public BasicNetwork getNetwork() {
		return this.bestNetwork;
	}
	public double[] getValidation() {
		return this.validation;
	}
	public double[] getTrain() {
		return this.train;
	}
	public double[] getWorstError() {
		return this.worstError;
	}
	public double getError() {
		return this.error;
	}
	public double getTrainError() {
		return this.errorTrain;
	}
	public int getEpoch() {
		return this.epoch;
	}
	public int getNeurons() {
		return this.neurones;
	}
	public int getTrail() {
		return this.trail;
	}
	
	/***
	 * clone this TrainedNet object - aims to avoid the problem from copy reference
	 * @return - new TrainedNet object that has the same information with this TrainedNet
	 */
	public TrainedNet clone() {
		return new TrainedNet(this.bestNetwork, this.validation, this.error, this.epoch, this.neurones, this.trail);
	}
}
