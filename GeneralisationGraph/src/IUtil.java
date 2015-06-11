import java.io.File;

import org.encog.ml.data.MLDataSet;

/** This class provide useful function for training process
 * 
 * @author Patomporn Loungvara
 *
 */
public interface IUtil {
	
	// CSV File in process
	final String[] dataCSVSet = {"value_set1.csv", "value_set2.csv", "value_set3.csv", 
		 "value_set4.csv", "value_set5.csv"};
	final String[] dataCSVValidationSet = {"validation_set1.csv", "validation_set2.csv", "validation_set3.csv", 
			 		   "validation_set4.csv", "validation_set5.csv"};
	// number of trail
	final int nSet = dataCSVSet.length;
		
	// Maximum number of neurons to have
	final int MAX_NEURONS = 10;
	// Maximum epoch 
	final int MAX_EPOCH = 1000;
	// Amount to increase epoch by
	final int STEP_EPOCH = 10;
	// Threshold for Error
	final double ERROR_THRESHOLD = 1E-10;
	// Gradient
	final double THRESHOLD_GRADIENT = 1E-10;
	final double MIN_GRADIENT = 1E-5;
	final double MAX_GRADIENT = 1E-3;
	
	public void setTraining();
	public MLDataSet getTrainingSet();
	public MLDataSet getTrainingSets(int i);
	public MLDataSet getValidationSets(int i);
	public void setParse(boolean set);
	public boolean isReadyToPasre();
	public File getNetFile();
	public int getNumInput();
	public int getNumOutput();
	public int getNumPrevG();
	public int getNumCtrl();
	public int getNumAge();
	public int getNumGender();
	public int getNumResult();
	public void divideCSVset();
	public double[] refineBinary(final int input, int digit);
	public double[] refineAge(final int age);
}
