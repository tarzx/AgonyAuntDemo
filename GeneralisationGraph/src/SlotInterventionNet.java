
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/** This class generates the best possible net for slot intervention
 * 
 * @author Patomporn Loungvara
 *
 */
public class SlotInterventionNet {
	
	// default setting for testing observation (draw full graph if true)
	private final static boolean isTest = false;
	// specify util class
	private UtilSlot util;
	
	/** Constructor
	 * 
	 * @param u util class
	 */
	public SlotInterventionNet(UtilSlot u) {
		this.util = u;
	}
	
	public static void main(String[] args) {
		System.out.println("Slot Intervention");
		SlotInterventionNet s = new SlotInterventionNet(new UtilSlot());
		
		// File
		s.refineCSV();
		
		// Neural Network
		s.getNet();
		
		// Test
		if(isTest) s.testoutput();
	}
	
	/** Find the best neural network using automatic training harness with generalisation technique 
	 * 
	 */
	public void getNet() {
		AutomaticTrainingGeneralisation atg = new AutomaticTrainingGeneralisation(util, isTest);
		
		// Retrieve net
		BasicNetwork network = atg.runGeneralisation();
		// write neural network file (.eg)
		writeFile(network);		
	}
	
	/** Refine data in CSV file to the appropriate format
	 * 
	 */
	public void refineCSV(){
		System.out.println("Refine process...");
		try {
			CSVReader reader = new CSVReader(new FileReader(util.getLoadFile()));
			CSVWriter writer = null;
			try {
				// output file
				writer = new CSVWriter(new FileWriter(util.getRefineFile()));
			
				String [] nextLine;
				int loopCouter = 0;
				// loop for each row
				while ((nextLine = reader.readNext()) != null) {
					String [] inLine = new String[util.getNumInput()+util.getNumOutput()];
					if (loopCouter > 0) {
//						for (int i=0; i<nextLine.length; i++) {
//							System.out.print(nextLine[i] + " ");
//						}
						
						// Change the control level to binary
						double[] ctrl = util.refineBinary(Integer.parseInt(nextLine[0]), util.getNumCtrl());
						for (int i=0; i<util.getNumCtrl(); i++) {
							inLine[i] = String.valueOf(ctrl[i]);
						}
						
						// Change the age to group of age in binary
						double[] age = util.refineAge(Integer.parseInt(nextLine[1]));
						for (int i=0; i<util.getNumAge(); i++) {
							inLine[util.getNumCtrl()+i] = String.valueOf(age[i]);
						}
						
						// Add the gender
						inLine[util.getNumCtrl()+util.getNumAge()] = nextLine[2];	
						
						// Change the sequence rate if null
						for (int i=0; i<util.getNumResult(); i++) {
							inLine[util.getNumAge()+util.getNumGender()+util.getNumCtrl()+i] = (nextLine[3+i].isEmpty()?"0.5":nextLine[3+i]);
						}
						
					} else {
						// header
						inLine = new String[] {"control_level_0", "control_level_1", "control_level_2", "control_level_3", "control_level_4",
									  		   "age_0", "age_1", "age_2", "gender", 
									  		   "slot_1", "slot_2", "slot_3", "slot_4", "slot_5", "slot_6" };
					}
					
					writer.writeNext(inLine);			
					loopCouter++;
					
				}
				
				writer.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**  Write out adult net to file
	 * 
	 * @param network	Fully trained net
	 */
	public void writeFile(BasicNetwork network) {
		EncogDirectoryPersistence.saveObject(util.getNetFile(), network);
		util.setParse(true);
		System.out.println("Written Net File!!");
	}
	
	public void testoutput(){
		if (util.isReadyToPasre()) {
			BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(util.getNetFile());
			
			double[] input1 =  { 1, 1, 0, 0, 0, 1, 0, 0, 1 };
			double[] input2 =  { 0, 1, 0, 1, 0, 0, 1, 0, 0 };
			double[] input3 =  { 1, 0, 1, 1, 0, 1, 1, 0, 0 };
			double[] input4 =  { 0, 1, 0, 0, 1, 0, 0, 1, 1 };
			
			double[] output1 = new double[8];
		    double[] output2 = new double[8];
		    double[] output3 = new double[8];
		    double[] output4 = new double[8];
		    
		    net.compute(input1, output1);
		    net.compute(input2, output2);
		    net.compute(input3, output3);
		    net.compute(input4, output4);
		    
		    System.out.println();
		    System.out.println("The output question is: " + output1[0] + " " + output1[1] + " " + output1[2] + " " + output1[3] + 
		    										  " " + output1[4] + " " + output1[5] + " " + output1[6] + " " + output1[7]);
		    System.out.println("The output question is: " + output2[0] + " " + output2[1] + " " + output2[2] + " " + output2[3] + 
					  								  " " + output2[4] + " " + output2[5] + " " + output2[6] + " " + output2[7]);
		    System.out.println("The output question is: " + output3[0] + " " + output3[1] + " " + output3[2] + " " + output3[3] + 
					  								  " " + output3[4] + " " + output3[5] + " " + output3[6] + " " + output3[7]);
		    System.out.println("The output question is: " + output4[0] + " " + output4[1] + " " + output4[2] + " " + output4[3] + 
		    										  " " + output4[4] + " " + output4[5] + " " + output4[6] + " " + output4[7]);
		    System.out.println();
		}
	}
	
}
