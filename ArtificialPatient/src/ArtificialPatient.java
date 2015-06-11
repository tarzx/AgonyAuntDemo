import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/** This class generates artificial patient from preference file
 * 
 * @author Patomporn Loungvara
 *
 */
public class ArtificialPatient {
	private static final int NUM_CONTROL = 20;
	private static final int NUM_FREQ = 7;
	private static final int NUM_SLOT = 6;
	private static final int NUM_SEQ = 8;
	private static final int NUM_GOAL = 4;
	private static final int NUM_BEHAV = 4;
	public static final String patient1Filename = "patient1.csv";
	public static final String patient2Filename = "patient2.csv";
	public static final String patient3Filename = "patient3.csv";
	public static final String patient4Filename = "patient4.csv";
	
	public static void main(String[] args) {
		Preference p1 = refineCSV(3, 30, true, patient1Filename);
		p1.print();
		Preference p2 = refineCSV(4, 45, false, patient2Filename);
		p2.print();
	}
	
	public static Preference refineCSV(int pid, int age, boolean isMale, String fileName){
		double[][] freq = new double[NUM_CONTROL][NUM_FREQ];
		double[][] slot = new double[NUM_CONTROL][NUM_SLOT];
		double[][] seq = new double[NUM_CONTROL][NUM_SEQ];
		double[][] goal = new double[NUM_CONTROL][NUM_GOAL];
		double[][] behav = new double[NUM_CONTROL][NUM_BEHAV];
		try {
			CSVReader reader = new CSVReader(new FileReader(fileName));
			
			String [] nextLine;
			int loopCounter = 0;
			try {
				while ((nextLine = reader.readNext()) != null) {
					if (loopCounter > 0 && loopCounter <= 20) {	
						for (int i=0; i<NUM_FREQ; i++) {
							freq[loopCounter-1][i] = Double.parseDouble(nextLine[i+1]);
						}
						for (int i=0; i<NUM_SLOT; i++) {
							slot[loopCounter-1][i] = Double.parseDouble(nextLine[NUM_FREQ+i+1]);
						}
						for (int i=0; i<NUM_SEQ; i++) {
							seq[loopCounter-1][i] = Double.parseDouble(nextLine[NUM_FREQ+NUM_SLOT+i+1]);
						}
						for (int i=0; i<NUM_GOAL; i++) {
							goal[loopCounter-1][i] = Double.parseDouble(nextLine[NUM_FREQ+NUM_SLOT+NUM_SEQ+i+1]);
						}
						for (int i=0; i<NUM_BEHAV; i++) {
							behav[loopCounter-1][i] = Double.parseDouble(nextLine[NUM_FREQ+NUM_SLOT+NUM_SEQ+NUM_GOAL+i+1]);
						}
					}
					loopCounter++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Preference p = new Preference(pid, age, isMale);
		p.setFrequencies(freq);
		p.setSlots(slot);
		p.setSequences(seq);
		p.setGoals(goal);
		p.setBehaviours(behav);
		
		return p;
	}
	
}

class Preference {
	private int pid;
	private int age;
	private boolean gender;
	private double[][] frequencies;
	private double[][] slots;
	private double[][] sequences;
	private double[][] goals;
	private double[][] behaviours;

	Preference(int pid, int age, boolean isMale) {
		this.pid = pid;
		this.age = age;
		this.gender = isMale;
	}
	void setFrequencies(double[][] freq) {
		this.frequencies = freq;
	}
	void setSlots(double[][] slots) {
		this.slots = slots;
	}
	void setSequences(double[][] seq) {
		this.sequences = seq;
	}
	void setGoals(double[][] goals) {
		this.goals = goals;
	}
	void setBehaviours(double[][] behav) {
		this.behaviours = behav;
	}
	
	int getID() {
		return this.pid;
	}
	int getAge() {
		return this.age;
	}
	boolean getGender() {
		return this.gender;
	}
	double[][] getFrequencies() {
		return this.frequencies;
	}
	double[][] getSlots() {
		return this.slots;
	}
	double[][] getSequences() {
		return this.sequences;
	}
	double[][] getGoals() {
		return this.goals;
	}
	double[][] getBehaviours() {
		return this.behaviours;
	}
	
	double getFrequencies(int ctl, int freq) {
		return this.frequencies[ctl-1][freq-1];
	}
	double getSlots(int ctl, int slot) {
		return this.slots[ctl-1][slot-1];
	}
	double getSequences(int ctl, int seq) {
		return this.sequences[ctl-1][seq-1];
	}
	double getGoals(int ctl, int goal, int prevG) {
		int slot = 0;
		if (prevG==11) slot+=2;
		if (goal==4) slot+=1;
		return this.goals[ctl-1][slot];
	}
	double getBehaviours(int ctl, int behav, int prevG) {
		int slot = 0;
		if (prevG==6) slot+=2;
		if (behav==11) slot+=1;
		return this.behaviours[ctl-1][slot];
	}
	
	void print() {
		StringBuilder sb = new StringBuilder();
		sb.append("Patient : " + this.pid + "\n");
		sb.append("Age : " + this.age + "\n");
		sb.append("Gender : " + (this.gender?"Male":"Female") + "\n");
		sb.append("Frequencies : \n");
		for (int i=0; i<this.frequencies.length; i++) {
			sb.append((i+1) + ": ");
			for (int j=0; j<this.frequencies[0].length; j++) {
				sb.append(this.frequencies[i][j] + " ");
			}
			sb.append("\n");
		}
		sb.append("Slots : \n");
		for (int i=0; i<this.slots.length; i++) {
			sb.append((i+1) + ": ");
			for (int j=0; j<this.slots[0].length; j++) {
				sb.append(this.slots[i][j] + " ");
			}
			sb.append("\n");
		}
		sb.append("Sequences : \n");
		for (int i=0; i<this.sequences.length; i++) {
			sb.append((i+1) + ": ");
			for (int j=0; j<this.sequences[0].length; j++) {
				sb.append(this.sequences[i][j] + " ");
			}
			sb.append("\n");
		}
		sb.append("Goals : \n");
		for (int i=0; i<this.goals.length; i++) {
			sb.append((i+1) + ": ");
			for (int j=0; j<this.goals[0].length; j++) {
				sb.append(this.goals[i][j] + " ");
			}
			sb.append("\n");
		}
		sb.append("Behaviours : \n");
		for (int i=0; i<this.behaviours.length; i++) {
			sb.append((i+1) + ": ");
			for (int j=0; j<this.behaviours[0].length; j++) {
				sb.append(this.behaviours[i][j] + " ");
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
	}
}
