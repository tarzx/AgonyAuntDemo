
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.encog.ml.data.MLDataSet;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.TrainingSetUtil;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/** This class provide useful function for training process
 * 
 * @author Patomporn Loungvara
 *
 */
public class UtilSeq implements IUtil {
	// CSV File in process
	private String dataCSVFileNameLoad = "seq.csv";
	private String dataCSVFileName = "seqRefine.csv";
	
	// Training data | Validation data
	private MLDataSet trainingSet;
	private MLDataSet[] trainingSets = new MLDataSet[nSet];
	private MLDataSet[] validationSets = new MLDataSet[nSet];

	// input | output bit unit and each value in binary bit unit
	private final int NUM_SEQ = 8; 
	private final int CTRL_DIGIT = 5;
	private final int AGE_DIGIT = 3;
	private final int GENDER_DIGIT = 1;
	// Number of bits representing input
	private final int NUM_INPUT_BITS = CTRL_DIGIT + AGE_DIGIT + GENDER_DIGIT;
	// Number of bits representing output 
	private final int NUM_OUTPUT_BITS = NUM_SEQ;

	// Whether the net can be parsed for sending to the app
	private boolean ready_to_parse = false;
	
	// neural network file
	private File neuralNetfile = new File("neuralNetSelectSeq.eg");
	
	/** get loaded CSV file name
	 * 
	 * @return		CSV file name
	 */
	public String getLoadFile() {
		return dataCSVFileNameLoad;
	}
	/** get refined CSV file name
	 * 
	 * @return		CSV file name
	 */
	public String getRefineFile() {
		return dataCSVFileName;
	}
	
	/** Created training and validation dataSet as CSV files
	 * 
	 */
	public void setTraining() {
		// load all data from CVS to dataset
		trainingSet = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, 
				dataCSVFileName, true, NUM_INPUT_BITS, NUM_OUTPUT_BITS);	
		
		// divide data into nSet trial
		divideCSVset();
				
		// set training data and validation dataset for each trial
		for (int i=0; i<nSet; i++) {
			// load training data from CSV file
			trainingSets[i] = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, 
					dataCSVSet[i], true, NUM_INPUT_BITS, NUM_OUTPUT_BITS);
			// load validation data from CSV file
			validationSets[i] = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, 
					dataCSVValidationSet[i], true, NUM_INPUT_BITS, NUM_OUTPUT_BITS);
		}
	}
	/** get all Training dataset
	 * 
	 * @return		dataset
	 */
	public MLDataSet getTrainingSet() {
		return trainingSet;
	}
	/** get Training dataset for trial k
	 * 
	 * @param	k	trial number
	 * @return		dataset
	 */
	public MLDataSet getTrainingSets(int i) {
		return trainingSets[i];
	}
	/** get Validation dataset for trial k
	 * 
	 * @param	k	trial number
	 * @return		dataset
	 */
	public MLDataSet getValidationSets(int i) {
		return validationSets[i];
	}
	
	/** set whether the net can be parsed for sending to the app
	 * 
	 * @param	set	
	 */
	public void setParse(boolean set) {
		this.ready_to_parse = set;
	}
	/** check whether the net can be parsed for sending to the app
	 * 
	 * @return		boolean
	 */
	public boolean isReadyToPasre() {
		return this.ready_to_parse;
	}
	/** get neural network file (.eg)
	 * 
	 * @return	File	
	 */
	public File getNetFile() {
		return this.neuralNetfile;
	}
	
	// input | output bit unit and each value in binary bit unit
	public int getNumInput() {
		return NUM_INPUT_BITS;
	}
	public int getNumOutput() {
		return NUM_OUTPUT_BITS;
	}
	public int getNumPrevG() {
		return 0;
	}
	public int getNumCtrl() {
		return CTRL_DIGIT;
	}
	public int getNumAge() {
		return AGE_DIGIT; 
	}
	public int getNumGender() {
		return GENDER_DIGIT;
	}
	public int getNumResult() {
		return NUM_SEQ;
	}
	
	
	/** Divide all data in loaded CSV file to nSet trial including training data and validation data as CSV files
	 * 
	 */
	public void divideCSVset(){
		try {
			// loop for each trial
			for (int i=0; i<nSet; i++) {
				CSVReader reader = new CSVReader(new FileReader(dataCSVFileName));
				CSVWriter writer, writerVal = null;
				try {
					// write training data CSV file
					writer = new CSVWriter(new FileWriter(dataCSVSet[i], false));
					// write validation data CSV file
					writerVal = new CSVWriter(new FileWriter(dataCSVValidationSet[i], false));
				
					String [] nextLine;
					int loopCounter = 0;
					while ((nextLine = reader.readNext()) != null) {
						//System.out.println(nextLine[0]+nextLine[1]+nextLine[2]+nextLine[3]);
						if (loopCounter == 0) {
							// header
							writer.writeNext(nextLine);
							writerVal.writeNext(nextLine);
						} else if (loopCounter%nSet==i) {
							// select row for validation
							writerVal.writeNext(nextLine);
						} else {
							// other row
							writer.writeNext(nextLine);
						}
						loopCounter++;
					}
					
					writer.close();
					writerVal.close();
					reader.close();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Refine value to binary bit
	 * 
	 * @param input		integer input value
	 * @param digit	 	number of bit that it should produce
	 * @return			array of binary bit
	 */
	public double[] refineBinary(final int input, int digit) {
		int decimal = input;
		double[] binary = new double[digit];
		
		// loop for each bit
		for (int i=digit-1; i>=0; i--) {
			// find each bit value
			binary[i] = Math.floor(decimal/Math.pow(2, i));
			decimal %= Math.pow(2, i);
		}
		return binary;
	}
	
	/** Refine value to binary bit
	 * 
	 * @param 	age		input age
	 * @return			array of binary bit for age group
	 */
	public double[] refineAge(final int age) {
		// define value for different group of age
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
}


