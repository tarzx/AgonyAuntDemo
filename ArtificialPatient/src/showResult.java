import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

/** This class generates graph from result CSV file
 * 
 * @author Patomporn Loungvara
 *
 */
public class showResult {
	private static final int WINDOW = 10;
	
	public static void main(String[] args) {
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y1 = new ArrayList<Double>();
		ArrayList<Double> yr1 = new ArrayList<Double>();
		ArrayList<Double> y2 = new ArrayList<Double>();
		ArrayList<Double> yr2 = new ArrayList<Double>();
		ArrayList<Double> f1 = new ArrayList<Double>();
		ArrayList<Double> fr1 = new ArrayList<Double>();
		ArrayList<Double> f2 = new ArrayList<Double>();
		ArrayList<Double> fr2 = new ArrayList<Double>();
		ArrayList<Double> s1 = new ArrayList<Double>();
		ArrayList<Double> sr1 = new ArrayList<Double>();
		ArrayList<Double> s2 = new ArrayList<Double>();
		ArrayList<Double> sr2 = new ArrayList<Double>();
		ArrayList<Double> q1 = new ArrayList<Double>();
		ArrayList<Double> qr1 = new ArrayList<Double>();
		ArrayList<Double> q2 = new ArrayList<Double>();
		ArrayList<Double> qr2 = new ArrayList<Double>();
		ArrayList<Double> b1 = new ArrayList<Double>();
		ArrayList<Double> br1 = new ArrayList<Double>();
		ArrayList<Double> b2 = new ArrayList<Double>();
		ArrayList<Double> br2 = new ArrayList<Double>();
		ArrayList<Double> g1 = new ArrayList<Double>();
		ArrayList<Double> gr1 = new ArrayList<Double>();
		ArrayList<Double> g2 = new ArrayList<Double>();
		ArrayList<Double> gr2 = new ArrayList<Double>();
	
		try {
			CSVReader reader = new CSVReader(new FileReader("showResult.csv"));
			
			String [] nextLine;
			try {
				while ((nextLine = reader.readNext()) != null) {
					x.add(Double.parseDouble(nextLine[0]));
					y1.add(Double.parseDouble(nextLine[1]));
					yr1.add(Double.parseDouble(nextLine[2]));
					y2.add(Double.parseDouble(nextLine[3]));
					yr2.add(Double.parseDouble(nextLine[4]));
					f1.add(Double.parseDouble(nextLine[5]));
					fr1.add(Double.parseDouble(nextLine[6]));
					f2.add(Double.parseDouble(nextLine[7]));
					fr2.add(Double.parseDouble(nextLine[8]));
					s1.add(Double.parseDouble(nextLine[9]));
					sr1.add(Double.parseDouble(nextLine[10]));
					s2.add(Double.parseDouble(nextLine[11]));
					sr2.add(Double.parseDouble(nextLine[12]));
					q1.add(Double.parseDouble(nextLine[13]));
					qr1.add(Double.parseDouble(nextLine[14]));
					q2.add(Double.parseDouble(nextLine[15]));
					qr2.add(Double.parseDouble(nextLine[16]));
					b1.add(Double.parseDouble(nextLine[17]));
					br1.add(Double.parseDouble(nextLine[18]));
					b2.add(Double.parseDouble(nextLine[19]));
					br2.add(Double.parseDouble(nextLine[20]));
					g1.add(Double.parseDouble(nextLine[21]));
					gr1.add(Double.parseDouble(nextLine[22]));
					g2.add(Double.parseDouble(nextLine[23]));
					gr2.add(Double.parseDouble(nextLine[24]));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double[] a = new double[x.size()];
		for (int i=0; i<x.size(); i++) a[i] = x.get(i);
		
		boolean isSmooth = true;
		print(isSmooth, a, y1, yr1, "Control level Patient 1");
		print(isSmooth, a, f1, fr1, "Control level Patient 1 (Frequency)");
		print(isSmooth, a, s1, sr2, "Control level Patient 1 (Slot)");
		print(isSmooth, a, q1, qr1, "Control level Patient 1 (Sequence)");
		print(isSmooth, a, b1, br1, "Control level Patient 1 (Behaviour)");
		print(isSmooth, a, g1, gr1, "Control level Patient 1 (Goal)");
		
//		print(isSmooth, a, y2, yr2, "Control level Patient 2");
//		print(isSmooth, a, f2, fr2, "Control level Patient 2 (Frequency)");
//		print(isSmooth, a, s2, sr2, "Control level Patient 2 (Slot)");
//		print(isSmooth, a, q2, qr2, "Control level Patient 2 (Sequence)");
//		print(isSmooth, a, b2, br2, "Control level Patient 2 (Behaviour)");
//		print(isSmooth, a, g2, gr2, "Control level Patient 2 (Goal)");
	}
	
	public static void print(boolean isSmooth, double[] a, ArrayList<Double> in1, ArrayList<Double> in2, String title) {
		double[] y1, y2;
		if (isSmooth) {
			y1 = boxcarFilter(in1);
			y2 = boxcarFilter(in2);
		} else {
			y1 = new double[in1.size()];
			for (int i=0; i<in1.size(); i++) y1[i] = in1.get(i);
			y2 = new double[in2.size()];
			for (int i=0; i<in2.size(); i++) y2[i] = in2.get(i);
		}
		
		plotGraph pg = new plotGraph(title, "Time", "Control", a, y1, y2);
		pg.plot();
	}
	
	public static double[] boxcarFilter(ArrayList<Double> input) {
		double[] smooth = new double[input.size()];
		
		for (int i=0; i<input.size(); i++) {
			double sum = 0, avg;
			if (i+1<WINDOW) {
				for (int j=0; j<=i; j++) {
					sum += input.get(j);
				}
				avg = sum/(i+1);
			} else if (input.size()-i<WINDOW) {
				for (int j=i; j<input.size(); j++) {
					sum += input.get(j);
				}
				avg = sum/(input.size()-i);
			} else {
				for (int j=i-WINDOW+1; j<=i; j++) {
					sum += input.get(j);
				}
				avg = sum/WINDOW;
			}
			smooth[i] = avg;
		}
		return smooth;
	}
}
