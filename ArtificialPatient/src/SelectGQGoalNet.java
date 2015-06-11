import java.io.File;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

/** This class generates the best possible net for select group question goal
 * 
 * @author Patomporn Loungvara
 *
 */
public class SelectGQGoalNet {
	private final int[] GROUP = { 3, 4 };
	private final int CTRL_DIGIT = 5;
	private final int AGE_DIGIT = 3;
	private final int GENDER_DIGIT = 1;
	private final int PREVG_DIGIT = 4;
	private final int NUM_INPUT_BITS = CTRL_DIGIT + AGE_DIGIT + GENDER_DIGIT + PREVG_DIGIT;
	private final int NUM_OUTPUT_BITS = GROUP.length;
	private final double INPUT[][] = { refineInput(1, 15, true, 10), refineInput(10, 15, true, 10), refineInput(20, 15, true, 10),
			   						   refineInput(1, 15, true, 11), refineInput(10, 15, true, 11), refineInput(20, 15, true, 11) };
	private final double IDEAL[][] = { { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 } };

	
	public boolean ready_to_parse = false;
	
	public SelectGQGoalNet(boolean isConnected) {
		// if offline, create local neural network
		if (!isConnected) createTrainingData();
	}
	
	public int getGroupQuestion(int idx) {
		if (idx<GROUP.length) {
			return GROUP[idx];
		} else { 
			return 0;
		}
	}
	
	public static void main(String[] args) {
		SelectGQGoalNet sgqg = new SelectGQGoalNet(false);
		sgqg.testoutput();
	}
	
	public double[] getRate(int controlLevel, int age, boolean isMale, int prevGroup) {
		//Refine
		double[] input = refineInput(controlLevel, age, isMale, prevGroup);
		double[] output = new double[NUM_OUTPUT_BITS];
		
		BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(Util.SELECT_GOAL_NET_EG));
		net.compute(input, output);
		
		return output;
	}
	
	private double[] refineInput(int controlLevel, int age, boolean isMale, int prevGroup) {
		double[] input = new double[NUM_INPUT_BITS];
		double[] controlBit = refineBinary(controlLevel, CTRL_DIGIT);
		double[] ageBit = refineAge(age);
		double genderBit = refineGender(isMale);
		double[] prevGBit = refineBinary(prevGroup, PREVG_DIGIT);
		
		for (int i=0; i<CTRL_DIGIT; i++) {
			input[i] = controlBit[i];
		}
		for (int i=0; i<AGE_DIGIT; i++) {
			input[i+CTRL_DIGIT] = ageBit[i];
		}
		input[CTRL_DIGIT+AGE_DIGIT] = genderBit;
		for (int i=0; i<PREVG_DIGIT; i++) {
			input[i+CTRL_DIGIT+AGE_DIGIT+GENDER_DIGIT] = prevGBit[i];
		}
		
		return input;
	}
	
	private double[] refineBinary(final int input, int digit) {
		int decimal = input;
		double[] binary = new double[digit];
		for (int i=digit-1; i>=0; i--) {
			binary[i] = Math.floor(decimal/Math.pow(2, i));
			decimal %= Math.pow(2, i);
		}
		return binary;
	}
	
	private double[] refineAge(final int age) {
		if (age<=17) {
			return new double[] { 0.0, 0.0, 0.0 };
		} else if (age<=24) {
			return new double[] { 0.0, 0.0, 1.0 };
		} else if (age<=49) {
			return new double[] { 0.0, 1.0, 0.0 };
		} else if (age<=64) {
			return new double[] { 0.0, 1.0, 1.0 };
		} else {
			return new double[] { 1.0, 0.0, 0.0 };
		}
	}
	
	private double refineGender(final boolean isMale) {
		return (isMale?0.0:1.0);
	}
	
	public void createTrainingData() {
		AutomaticTraining ath = new AutomaticTraining();
		
		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(INPUT, IDEAL);
		
		// Retrieve net
		BasicNetwork network = ath.calibrate(trainingSet, NUM_INPUT_BITS, NUM_OUTPUT_BITS);
		writeFile(network);
	}
	
	public void testoutput(){
		
		BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(Util.SELECT_GOAL_NET_EG));
		double[] input1 =  refineInput(1, 15, true, 10);
		double[] input2 =  refineInput(10, 15, true, 10);
		double[] input3 =  refineInput(20, 15, true, 10);
		double[] input4 =  refineInput(1, 15, true, 11);
		double[] input5 =  refineInput(10, 15, true, 11);
		double[] input6 =  refineInput(20, 15, true, 11);
		
	    double[] output1 = new double[NUM_OUTPUT_BITS];
	    double[] output2 = new double[NUM_OUTPUT_BITS];
	    double[] output3 = new double[NUM_OUTPUT_BITS];
	    double[] output4 = new double[NUM_OUTPUT_BITS];
	    double[] output5 = new double[NUM_OUTPUT_BITS];
	    double[] output6 = new double[NUM_OUTPUT_BITS];
	    
	    net.compute(input1, output1);
	    net.compute(input2, output2);
	    net.compute(input3, output3);
	    net.compute(input4, output4);
	    net.compute(input5, output5);
	    net.compute(input6, output6);
	    
	    System.out.println("The output possibility for questions are: " + output1[0] + " "  + output1[1]);
	    System.out.println("The output possibility for questions are: " + output2[0] + " "  + output2[1]);
	    System.out.println("The output possibility for questions are: " + output3[0] + " "  + output3[1]);
	    System.out.println("The output possibility for questions are: " + output4[0] + " "  + output4[1]);
	    System.out.println("The output possibility for questions are: " + output5[0] + " "  + output5[1]);
	    System.out.println("The output possibility for questions are: " + output6[0] + " "  + output6[1]);
	    System.out.println();
   
	}
	
	public void writeFile(BasicNetwork network) {
		EncogDirectoryPersistence.saveObject(new File(Util.SELECT_GOAL_NET_EG), network);
		ready_to_parse = true;
	}
}
