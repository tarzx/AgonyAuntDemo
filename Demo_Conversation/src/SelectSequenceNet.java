import java.io.File;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;


public class SelectSequenceNet {
	private final int[] SEQ = { 1, 2, 3, 4, 5, 6, 7, 8 };
	private final int CTRL_DIGIT = 5;
	private final int AGE_DIGIT = 3;
	private final int GENDER_DIGIT = 1;
	private final int NUM_INPUT_BITS = CTRL_DIGIT + AGE_DIGIT + GENDER_DIGIT;
	private final int NUM_OUTPUT_BITS = SEQ.length;
	private final double INPUT[][] = { refineInput(1, 15, true), refineInput(10, 15, true), refineInput(20, 15, true),
			   						   refineInput(1, 15, false), refineInput(10, 15, false), refineInput(20, 15, false) };
	public double IDEAL[][] = { { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, 
								{ 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, 
								{ 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 } };
	
	static boolean ready_to_parse = false;
	
	public SelectSequenceNet() {
		createTrainingData();
	}
	
	public int getSequence(int idx) {
		if (idx<SEQ.length) {
			return SEQ[idx];
		} else { 
			return 0;
		}
	}
	
	public static void main(String[] args) {
		SelectSequenceNet ssn = new SelectSequenceNet();
		ssn.testoutput();
	}
	
	public double[] getRate(int controlLevel, int age, boolean isMale) {
		//Refine
		double[] input = refineInput(controlLevel, age, isMale);
		double[] output = new double[NUM_OUTPUT_BITS];
		
		BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(Util.SELECT_SEQUENCE_NET_EG));
		net.compute(input, output);
		
		return output;
	}
	
	private double[] refineInput(int controlLevel, int age, boolean isMale) {
		double[] input = new double[NUM_INPUT_BITS];
		double[] controlBit = refineBinary(controlLevel, CTRL_DIGIT);
		double[] ageBit = refineAge(age);
		double genderBit = refineGender(isMale);
		
		for (int i=0; i<CTRL_DIGIT; i++) {
			input[i] = controlBit[i];
		}
		for (int i=0; i<AGE_DIGIT; i++) {
			input[i+CTRL_DIGIT] = ageBit[i];
		}
		input[CTRL_DIGIT+AGE_DIGIT] = genderBit;
		
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
		
		BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(Util.SELECT_SEQUENCE_NET_EG));
		double[] input1 =  refineInput(1, 15, true);
		double[] input2 =  refineInput(10, 15, true);
		double[] input3 =  refineInput(20, 15, true);
		double[] input4 =  refineInput(1, 15, false);
		double[] input5 =  refineInput(10, 15, false);
		double[] input6 =  refineInput(20, 15, false);
		
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
	    
	    System.out.println("The output question is: " + output1[0] + " " + output1[1] + " " + output1[2] + " " + output1[3]
	    										+ " " + output1[4] + " " + output1[5] + " " + output1[6] + " " + output1[7]);
	    System.out.println("The output question is: " + output2[0] + " " + output2[1] + " " + output2[2] + " " + output2[3]
												+ " " + output2[4] + " " + output2[5] + " " + output2[6] + " " + output2[7]);
	    System.out.println("The output question is: " + output3[0] + " " + output3[1] + " " + output3[2] + " " + output3[3]
												+ " " + output3[4] + " " + output3[5] + " " + output3[6] + " " + output3[7]);
	    System.out.println("The output question is: " + output4[0] + " " + output4[1] + " " + output4[2] + " " + output4[3]
												+ " " + output4[4] + " " + output4[5] + " " + output4[6] + " " + output4[7]);
	    System.out.println("The output question is: " + output5[0] + " " + output5[1] + " " + output5[2] + " " + output5[3]
												+ " " + output5[4] + " " + output5[5] + " " + output5[6] + " " + output5[7]);
	    System.out.println("The output question is: " + output6[0] + " " + output6[1] + " " + output6[2] + " " + output6[3]
												+ " " + output6[4] + " " + output6[5] + " " + output6[6] + " " + output6[7]);
	    System.out.println();
   
	}
	
	public void writeFile(BasicNetwork network) {
		EncogDirectoryPersistence.saveObject(new File(Util.SELECT_SEQUENCE_NET_EG), network);
		ready_to_parse = true;
	}

}
