import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import au.com.bytecode.opencsv.CSVWriter;

/** This class generates artificial patient testing
 * 
 * @author Patomporn Loungvara
 *
 */
public class testPerformance {
	// number of testing cycle
	private static final int MAX = 40;
	// number of testing neural network
	private static final int NUMNET = 5;
	// result file
	public static String resultFilename = "result.csv";
	
	public static void main(String[] args) {
		boolean isConnectServer = true;
		
		SelectRecommendation sr = new SelectRecommendation(isConnectServer);		
		Random rand = new Random();
		
		ArrayList<double[]> result = new ArrayList<double[]>();
		
		double[] x = new double[MAX];
		double[] y1 = new double[MAX];
		double[] y2 = new double[MAX];
		double[] z1 = new double[MAX];
		double[] z2 = new double[MAX];
		
		double[][] nn1 = new double[NUMNET][MAX];
		double[][] nn2 = new double[NUMNET][MAX];
		
		double[][] r1 = new double[NUMNET][MAX];
		double[][] r2 = new double[NUMNET][MAX];
		
		Preference p1 = ArtificialPatient.refineCSV(3, 30, true, ArtificialPatient.patient3Filename);
		p1.print();
		Preference p2 = ArtificialPatient.refineCSV(4, 45, false, ArtificialPatient.patient4Filename);
		p2.print();
		int P1Nctrl = rand.nextInt(10) + 1; //Neural Networks
		int P1Rctrl = P1Nctrl; //Random
		int P2Nctrl = 4; //rand.nextInt(10) + 1; //Neural Networks
		int P2Rctrl = P2Nctrl; //Random
		for (int i=0; i<MAX; i++) {
			System.out.println("Time : " + i);
//			System.out.println("Patient 1 Control Level (Neural Networks) : " + P1Nctrl);
//			System.out.println("Patient 1 Control Level (Random) : " + P1Rctrl);
//			System.out.println("Patient 2 Control Level (Neural Networks) : " + P2Nctrl);
//			System.out.println("Patient 2 Control Level (Random) : " + P2Rctrl);
			
			x[i] = i;
			y1[i] = P1Nctrl;
			y2[i] = P1Rctrl;
			z1[i] = P2Nctrl;
			z2[i] = P2Rctrl;
			
			int P1numRate = 0, P2numRate = 0, P1RnumRate = 0, P2RnumRate = 0;
			double P1sumRate = 0, P2sumRate = 0, P1RsumRate = 0, P2RsumRate = 0;
//			
//			int P1freq = sr.selectFrequency(P1Nctrl, p1.getAge(), p1.getGender());
//			double P1freqRate = p1.getFrequencies(P1Nctrl, P1freq);
//			Util.recordFrequency(String.valueOf(p1.getID()), P1Nctrl, P1freq, P1freqRate);
//			P1numRate++; P1sumRate += P1freqRate;
//			System.out.println(P1freqRate);
//			int P2freq = sr.selectFrequency(P2Nctrl, p2.getAge(), p2.getGender());
//			double P2freqRate = p2.getFrequencies(P2Nctrl, P2freq);
//			Util.recordFrequency(String.valueOf(p2.getID()), P2Nctrl, P2freq, P2freqRate);
//			P2numRate++; P2sumRate += P2freqRate;
//			System.out.println(P2freqRate);
//			
//			int P1Rfreq = rand.nextInt(7) + 1;
//			double P1RfreqRate = p1.getFrequencies(P1Rctrl, P1Rfreq);
//			P1RnumRate++; P1RsumRate += P1RfreqRate;
//			System.out.println(P1RfreqRate);
			int P2Rfreq = rand.nextInt(7) + 1;
			double P2RfreqRate = p2.getFrequencies(P2Rctrl, P2Rfreq);
			P2RnumRate++; P2RsumRate += P2RfreqRate;
			System.out.println(P2RfreqRate);
//			
//			ArrayList<Integer> P1slot = sr.selectTimeSlot(P1Nctrl, p1.getAge(), p1.getGender());
//			double[] P1slotRate = new double[P1slot.size()];
//			for (int j=0; j<P1slot.size(); j++) {
//				P1slotRate[j] = p1.getSlots(P1Nctrl, P1slot.get(j));
//				Util.recordSlot(String.valueOf(p1.getID()), P1Nctrl, P1slot.get(j), P1slotRate[j]);
//				P1numRate++; P1sumRate += P1slotRate[j];
//				System.out.println(P1slotRate[j]);
//			}
//			ArrayList<Integer> P2slot = sr.selectTimeSlot(P2Nctrl, p2.getAge(), p2.getGender());
//			double[] P2slotRate = new double[P2slot.size()];
//			for (int j=0; j<P2slot.size(); j++) {
//				P2slotRate[j] = p2.getSlots(P2Nctrl, P2slot.get(j));
//				Util.recordSlot(String.valueOf(p2.getID()), P2Nctrl, P2slot.get(j), P2slotRate[j]);
//				P2numRate++; P2sumRate += P2slotRate[j];
//				System.out.println(P2slotRate[j]);
//			}
//			
//			ArrayList<Integer> P1Rslot = randomSlots();
//			double[] P1RslotRate = new double[P1Rslot.size()];
//			for (int j=0; j<P1Rslot.size(); j++) {
//				P1RslotRate[j] = p1.getSlots(P1Rctrl, P1Rslot.get(j));
//				P1RnumRate++; P1RsumRate += P1RslotRate[j];
//				System.out.println(P1RslotRate[j]);
//			}
			ArrayList<Integer> P2Rslot = randomSlots();
			double[] P2RslotRate = new double[P2Rslot.size()];
			for (int j=0; j<P2Rslot.size(); j++) {
				P2RslotRate[j] = p2.getSlots(P2Rctrl, P2Rslot.get(j));
				P2RnumRate++; P2RsumRate += P2RslotRate[j];
				//System.out.println(P2RslotRate[j]);
			}
//			
//			int P1seq = sr.selectSequence(P1Nctrl, p1.getAge(), p1.getGender());
//			double P1seqRate = p1.getSequences(P1Nctrl, P1seq);
//			Util.recordSelectSequence(String.valueOf(p1.getID()), P1Nctrl, P1seq, P1seqRate);
//			P1numRate++; P1sumRate += P1seqRate;
//			System.out.println(P1seqRate);
//			int P2seq = sr.selectSequence(P2Nctrl, p2.getAge(), p2.getGender());
//			double P2seqRate = p2.getSequences(P2Nctrl, P2seq);
//			Util.recordSelectSequence(String.valueOf(p2.getID()), P2Nctrl, P2seq, P2seqRate);
//			P2numRate++; P2sumRate += P2seqRate;
//			System.out.println(P2seqRate);
//			
//			int P1Rseq = rand.nextInt(8)+1;
//			double P1RseqRate = p1.getSequences(P1Rctrl, P1Rseq);
//			P1RnumRate++; P1RsumRate += P1RseqRate;
//			System.out.println(P1RseqRate);
			int P2Rseq = rand.nextInt(8)+1;
			double P2RseqRate = p2.getSequences(P2Rctrl, P2Rseq);
			P2RnumRate++; P2RsumRate += P2RseqRate;
			System.out.println(P2RseqRate);
//			
//			int P1prevGbehav = getPrevGBehav(P1seq);
//			if (P1seq==2 || P1seq==6) {
//				int P1behav = sr.selectReflectBehaviour(P1Nctrl, p1.getAge(), p1.getGender(), P1prevGbehav);
//				double P1behavRate = p1.getBehaviours(P1Nctrl, P1behav, P1prevGbehav);
//				Util.recordSelectGroupQuestion(String.valueOf(p1.getID()), P1Nctrl, P1prevGbehav, P1behav, P1behavRate);
//				P1numRate++; P1sumRate += P1behavRate;
//				System.out.println(P1behavRate);
//			}
//			int P2prevGbehav = getPrevGBehav(P2seq);
//			if (P2seq==2 || P2seq==6) {
//				int P2behav = sr.selectReflectBehaviour(P2Nctrl, p2.getAge(), p2.getGender(), P2prevGbehav);
//				double P2behavRate = p2.getBehaviours(P2Nctrl, P2behav, P2prevGbehav);
//				Util.recordSelectGroupQuestion(String.valueOf(p2.getID()), P2Nctrl, P2prevGbehav, P2behav, P2behavRate);
//				P2numRate++; P2sumRate += P2behavRate;
//				System.out.println(P2behavRate);
//			}
//			
//			int P1RprevGbehav = getPrevGBehav(P1Rseq);
//			if (P1Rseq==2 || P1Rseq==6) {
//				int P1Rbehav = rand.nextInt(2) + 10;
//				double P1RbehavRate = p1.getBehaviours(P1Rctrl, P1Rbehav, P1RprevGbehav);
//				P1RnumRate++; P1RsumRate += P1RbehavRate;
//				System.out.println(P1RbehavRate);
//			}
			int P2RprevGbehav = getPrevGBehav(P2Rseq);
			if (P2Rseq==2 || P2Rseq==6) {
				int P2Rbehav = rand.nextInt(2) + 10;
				double P2RbehavRate = p2.getBehaviours(P2Rctrl, P2Rbehav, P2RprevGbehav);
				P2RnumRate++; P2RsumRate += P2RbehavRate;
				//System.out.println(P2RbehavRate);
			}
//			
//			int P1prevGgoal = getPrevGGoal(P1seq, P1prevGbehav);
//			if (P1seq==2 || P1seq==6 || P1seq==7 || P1seq==8) {
//				int P1goal = sr.selectReplayGoal(P1Nctrl, p1.getAge(), p1.getGender(), P1prevGgoal);
//				double P1goalRate = p1.getGoals(P1Nctrl, P1goal, P1prevGgoal);
//				Util.recordSelectGroupQuestion(String.valueOf(p1.getID()), P1Nctrl, P1prevGgoal, P1goal, P1goalRate);
//				P1numRate++; P1sumRate += P1goalRate;
//				System.out.println(P1goalRate);
//			}
//			int P2prevGgoal = getPrevGGoal(P2seq, P2prevGbehav);
//			if (P2seq==2 || P2seq==6 || P2seq==7 || P2seq==8) {
//				int P2goal = sr.selectReplayGoal(P2Nctrl, p2.getAge(), p2.getGender(), P2prevGgoal);
//				double P2goalRate = p2.getGoals(P2Nctrl, P2goal, P2prevGgoal);
//				Util.recordSelectGroupQuestion(String.valueOf(p2.getID()), P2Nctrl, P2prevGgoal, P2goal, P2goalRate);
//				P2numRate++; P2sumRate += P2goalRate;
//				System.out.println(P2goalRate);
//			}
//			
//			int P1RprevGgoal = getPrevGGoal(P1Rseq, P1RprevGbehav);
//			if (P1Rseq==2 || P1Rseq==6 || P1Rseq==7 || P1Rseq==8) {
//				int P1Rgoal = rand.nextInt(2) + 3;
//				double P1RgoalRate = p1.getGoals(P1Rctrl, P1Rgoal, P1RprevGgoal);
//				P1RnumRate++; P1RsumRate += P1RgoalRate;
//				//System.out.println(P1RgoalRate);
//			}
			int P2RprevGgoal = getPrevGGoal(P2Rseq, P2RprevGbehav);
			if (P2Rseq==2 || P2Rseq==6 || P2Rseq==7 || P2Rseq==8) {
				int P2Rgoal = rand.nextInt(2) + 3;
				double P2RgoalRate = p2.getGoals(P2Rctrl, P2Rgoal, P2RprevGgoal);
				P2RnumRate++; P2RsumRate += P2RgoalRate;
				//System.out.println(P2RgoalRate);
			}
//			
//    		System.out.println("P1 Rate : " + P1sumRate/P1numRate + " " + P1sumRate + " " + P1numRate);
//    		System.out.println("P1 Random Rate : " + P1RsumRate/P1RnumRate + " " + P1RsumRate + " " + P1RnumRate);
//    		System.out.println("P2 Rate : " + P2sumRate/P2numRate + " " + P2sumRate + " " + P2numRate);
//    		System.out.println("P2 Random Rate : " + P2RsumRate/P2RnumRate + " " + P2RsumRate + " " + P2RnumRate);
//			
			// individual neural testing
    		if (i>0) {
	    		//Freq NN
//	    		int nn1freq = sr.selectFrequency((int)nn1[0][i-1], p1.getAge(), p1.getGender());
//				double nn1freqRate = p1.getFrequencies((int)nn1[0][i-1], nn1freq);
//				Util.recordFrequency(String.valueOf(p1.getID()), (int)nn1[0][i-1], nn1freq, nn1freqRate);
//				nn1[0][i] = getCtrl(nn1freqRate, (int)nn1[0][i-1]);
//				System.out.println(nn1[0][i-1] + " " + nn1[0][i] + " - " + nn1freq + " : " + nn1freqRate);
//				int nn2freq = sr.selectFrequency((int)nn2[0][i-1], p2.getAge(), p2.getGender());
//				double nn2freqRate = p2.getFrequencies((int)nn2[0][i-1], nn2freq);
//				Util.recordFrequency(String.valueOf(p2.getID()), (int)nn2[0][i-1], nn2freq, nn2freqRate);
//				nn2[0][i] = getCtrl(nn2freqRate, (int)nn2[0][i-1]);
//				System.out.println(nn2[0][i-1] + " " + nn2[0][i] + " - " + nn2freq + " : " + nn2freqRate);
//				
    			//Freq Random
//				int r1freq = rand.nextInt(7) + 1;
//				double r1freqRate = p1.getFrequencies((int)r1[0][i-1], r1freq);
//				r1[0][i] = getCtrl(r1freqRate, (int)r1[0][i-1]);
//				System.out.println(r1[0][i] + " - " + r1freq + " : " + r1freqRate);
				int r2freq = rand.nextInt(7) + 1;
				double r2freqRate = p2.getFrequencies((int)r2[0][i-1], r2freq);
				r2[0][i] = getCtrl(r2freqRate, (int)r2[0][i-1]);
			
				//Slot NN
//    			double nn1sumSlotRate = 0;
//				ArrayList<Integer> nn1slot = sr.selectTimeSlot((int)nn1[1][i-1], p1.getAge(), p1.getGender());
//				double[] nn1slotRate = new double[nn1slot.size()];
//				for (int j=0; j<nn1slot.size(); j++) {
//					nn1slotRate[j] = p1.getSlots((int)nn1[1][i-1], nn1slot.get(j));
//					Util.recordSlot(String.valueOf(p1.getID()), (int)nn1[1][i-1], nn1slot.get(j), nn1slotRate[j]);
//					nn1sumSlotRate += nn1slotRate[j];
//					System.out.println(nn1slot.get(j) + " : " + nn1slotRate[j]);
//				}
//				nn1[1][i] = getCtrl(nn1sumSlotRate/nn1slot.size(), (int)nn1[1][i-1]);
//				System.out.println(nn1sumSlotRate/nn1slot.size() + " " + nn1[1][i-1] + " " + nn1[1][i]);
//				double nn2sumSlotRate = 0;
//				ArrayList<Integer> nn2slot = sr.selectTimeSlot((int)nn2[1][i-1], p2.getAge(), p2.getGender());
//				double[] nn2slotRate = new double[nn2slot.size()];
//				for (int j=0; j<nn2slot.size(); j++) {
//					nn2slotRate[j] = p2.getSlots((int)nn2[1][i-1], nn2slot.get(j));
//					Util.recordSlot(String.valueOf(p2.getID()), (int)nn2[1][i-1], nn2slot.get(j), nn2slotRate[j]);
//					nn2sumSlotRate += nn2slotRate[j];
//					System.out.println(nn2slot.get(j) + " : " + nn2slotRate[j]);
//				}
//				nn2[1][i] = getCtrl(nn2sumSlotRate/nn2slot.size(), (int)nn2[1][i-1]);
//				System.out.println(nn2sumSlotRate/nn2slot.size() + " " + nn2[1][i-1] + " " + nn2[1][i]);
				
				//Slot Random
//				double r1sumSlotRate = 0;
//				ArrayList<Integer> r1slot = randomSlots();
//				double[] r1slotRate = new double[r1slot.size()];
//				for (int j=0; j<r1slot.size(); j++) {
//					r1slotRate[j] = p1.getSlots((int)r1[1][i-1], r1slot.get(j));
//					r1sumSlotRate += r1slotRate[j];
//					System.out.println(r1slot.get(j) + " : " + r1slotRate[j]);
//				}
//				r1[1][i] = getCtrl(r1sumSlotRate/r1slot.size(), (int)r1[1][i-1]);
//				System.out.println(r1sumSlotRate/r1slot.size() + " " + r1[1][i-1] + " " + r1[1][i]);
				double r2sumSlotRate = 0;
				ArrayList<Integer> r2slot = randomSlots();
				double[] r2slotRate = new double[r2slot.size()];
				for (int j=0; j<r2slot.size(); j++) {
					r2slotRate[j] = p2.getSlots((int)r2[1][i-1], r2slot.get(j));
					r2sumSlotRate += r2slotRate[j];
					System.out.println(r2slot.get(j) + " : " + r2slotRate[j]);
				}
				r2[1][i] = getCtrl(r2sumSlotRate/r2slot.size(), (int)r2[1][i-1]);
				System.out.println(r2sumSlotRate/r2slot.size() + " " + r2[1][i-1] + " " + r2[1][i]);
				
				//Seq NN
//				int nn1seq = sr.selectSequence((int)nn1[2][i-1], p1.getAge(), p1.getGender());
//				double nn1seqRate = p1.getSequences((int)nn1[2][i-1], nn1seq);
//				Util.recordSelectSequence(String.valueOf(p1.getID()), (int)nn1[2][i-1], nn1seq, nn1seqRate);
//				nn1[2][i] = getCtrl(nn1seqRate, (int)nn1[2][i-1]);
//				System.out.println(nn1seq + " : " + nn1seqRate + " " + nn1[2][i-1] + " " + nn1[2][i]);
//				int nn2seq = sr.selectSequence((int)nn2[2][i-1], p2.getAge(), p2.getGender());
//				double nn2seqRate = p2.getSequences((int)nn2[2][i-1], nn2seq);
//				Util.recordSelectSequence(String.valueOf(p2.getID()), (int)nn2[2][i-1], nn2seq, nn2seqRate);
//				nn2[2][i] = getCtrl(nn2seqRate, (int)nn2[2][i-1]);
//				System.out.println(nn2seq + " : " + nn2seqRate + " " + nn2[2][i-1] + " " + nn2[2][i]);
				
				//Seq Random
//				int r1seq = rand.nextInt(8)+1;
//				double r1seqRate = p1.getSequences((int)r1[2][i-1], r1seq);
//				r1[2][i] = getCtrl(r1seqRate, (int)r1[2][i-1]);
//				System.out.println(r1seq + " : " + r1seqRate + " " + r1[2][i-1] + " " + r1[2][i]);
				int r2seq = rand.nextInt(8)+1;
				double r2seqRate = p2.getSequences((int)r2[2][i-1], r2seq);
				r2[2][i] = getCtrl(r2seqRate, (int)r2[2][i-1]);
				System.out.println(r2seq + " : " + r2seqRate + " " + r2[2][i-1] + " " + r2[2][i]);
				
				//Behaviour NN - together with Seq NN
//				int nn1prevGbehav = getPrevGBehav(nn1seq);
//				if (nn1seq==2 || nn1seq==6) {
//					int nn1behav = sr.selectReflectBehaviour((int)nn1[3][i-1], p1.getAge(), p1.getGender(), nn1prevGbehav);
//					System.out.println(nn1[3][i-1] + " "+ nn1behav);
//					double nn1behavRate = p1.getBehaviours((int)nn1[3][i-1], nn1behav, nn1prevGbehav);
//					Util.recordSelectGroupQuestion(String.valueOf(p1.getID()), (int)nn1[3][i-1], nn1prevGbehav, nn1behav, nn1behavRate);
//					nn1[3][i] = getCtrl(nn1behavRate, (int)nn1[3][i-1]);
//					System.out.println("B1 " + nn1behav + " : " + nn1behavRate + " " + nn1[3][i-1] + " " + nn1[3][i]);
//				} else {
//					nn1[3][i] = nn1[3][i-1];
//				}
//				int nn2prevGbehav = getPrevGBehav(nn2seq);
//				if (nn2seq==2 || nn2seq==6) {
//					int nn2behav = sr.selectReflectBehaviour((int)nn2[3][i-1], p2.getAge(), p2.getGender(), nn2prevGbehav);
//					System.out.println(nn2[3][i-1] + " "+ nn2behav);
//					double nn2behavRate = p2.getBehaviours((int)nn2[3][i-1], nn2behav, nn2prevGbehav);
//					Util.recordSelectGroupQuestion(String.valueOf(p2.getID()), (int)nn2[3][i-1], nn2prevGbehav, nn2behav, nn2behavRate);
//					nn2[3][i] = getCtrl(nn2behavRate, (int)nn2[3][i-1]);
//					System.out.println("B2 " + nn2behav + " : " + nn2behavRate + " " + nn2[3][i-1] + " " + nn2[3][i]);
//				} else {
//					nn2[3][i] = nn2[3][i-1];
//				}
//				
				//Behaviour Random - together with Seq Random
//				int r1prevGbehav = getPrevGBehav(r1seq);
//				if (r1seq==2 || r1seq==6) {
//					int r1behav = rand.nextInt(2) + 10;
//					System.out.println(r1[3][i-1] + " "+ r1behav);
//					double r1behavRate = p1.getBehaviours((int)r1[3][i-1], r1behav, r1prevGbehav);
//					r1[3][i] = getCtrl(r1behavRate, (int)r1[3][i-1]);
//					System.out.println("B1R " + r1behav + " : " + r1behavRate + " " + r1[3][i-1] + " " + r1[3][i]);
//				} else {
//					r1[3][i] = r1[3][i-1];
//				}
				int r2prevGbehav = getPrevGBehav(r2seq);
				if (r2seq==2 || r2seq==6) {
					int r2behav = rand.nextInt(2) + 10;
					//System.out.println(r2[3][i-1] + " "+ r2behav);
					double r2behavRate = p2.getBehaviours((int)r2[3][i-1], r2behav, r2prevGbehav);
					r2[3][i] = getCtrl(r2behavRate, (int)r2[3][i-1]);
					System.out.println("B2R " + r2behav + " : " + r2behavRate + " " + r2[3][i-1] + " " + r2[3][i]);
				} else {
					r2[3][i] = r2[3][i-1];
				}
				
				//Goal NN - together with Seq NN and Behaviour NN
//				int nn1prevGgoal = getPrevGGoal(nn1seq, nn1prevGbehav);
//				if (nn1seq==2 || nn1seq==6 || nn1seq==7 || nn1seq==8) {
//					int nn1goal = sr.selectReplayGoal((int)nn1[4][i-1], p1.getAge(), p1.getGender(), nn1prevGgoal);
//					double nn1goalRate = p1.getGoals((int)nn1[4][i-1], nn1goal, nn1prevGgoal);
//					Util.recordSelectGroupQuestion(String.valueOf(p1.getID()), (int)nn1[4][i-1], nn1prevGgoal, nn1goal, nn1goalRate);
//					nn1[4][i] = getCtrl(nn1goalRate, (int)nn1[4][i-1]);
//					System.out.println("G1 " + nn1goal + " : " + nn1goalRate + " " + nn1[4][i-1] + " " + nn1[4][i]);
//				} else {
//					nn1[4][i] = nn1[4][i-1];
//				}
//				int nn2prevGgoal = getPrevGGoal(nn2seq, nn2prevGbehav);
//				if (nn2seq==2 || nn2seq==6 || nn2seq==7 || nn2seq==8) {
//					int nn2goal = sr.selectReplayGoal((int)nn2[4][i-1], p2.getAge(), p2.getGender(), nn2prevGgoal);
//					double nn2goalRate = p2.getGoals((int)nn2[4][i-1], nn2goal, nn2prevGgoal);
//					Util.recordSelectGroupQuestion(String.valueOf(p2.getID()), (int)nn2[4][i-1], nn2prevGgoal, nn2goal, nn2goalRate);
//					nn2[4][i] = getCtrl(nn2goalRate, (int)nn2[4][i-1]);
//					System.out.println("G2 " + nn2goal + " : " + nn2goalRate + " " + nn2[4][i-1] + " " + nn2[4][i]);
//				} else {
//					nn2[4][i] = nn2[4][i-1];
//				}
				
				//Goal Random - together with Seq Random and Behaviour Random
//				int r1prevGgoal = getPrevGGoal(r1seq, r1prevGbehav);
//				if (r1seq==2 || r1seq==6 || r1seq==7 || r1seq==8) {
//					int r1goal = rand.nextInt(2) + 3;
//					double r1goalRate = p1.getGoals((int)r1[4][i-1], r1goal, r1prevGgoal);
//					r1[4][i] = getCtrl(r1goalRate, (int)r1[4][i-1]);
//					System.out.println("G1R " + r1goal + " : " + r1goalRate + " " + r1[4][i-1] + " " + r1[4][i]);
//				} else {
//					r1[4][i] = r1[4][i-1];
//				}
				int r2prevGgoal = getPrevGGoal(r2seq, r2prevGbehav);
				if (r2seq==2 || r2seq==6 || r2seq==7 || r2seq==8) {
					int r2goal = rand.nextInt(2) + 3;
					double r2goalRate = p2.getGoals((int)r2[4][i-1], r2goal, r2prevGgoal);
					r2[4][i] = getCtrl(r2goalRate, (int)r2[4][i-1]);
					System.out.println("G2R " + r2goal + " : " + r2goalRate + " " + r2[4][i-1] + " " + r2[4][i]);
				} else {
					r2[4][i] = r2[4][i-1];
				}
				
    		} else {
    			// Initial control level
    			for (int j=0; j<NUMNET; j++) {
    				nn1[j][i] = P1Nctrl;
    				nn2[j][i] = P2Nctrl;
    				r1[j][i] = P1Rctrl;
    				r2[j][i] = P2Rctrl;
    			}
    		}
    		
//    		System.out.println("P1 Rate: " + (P1sumRate/P1numRate));
    		
    		// adjust control level
//    		P1Nctrl = getCtrl(P1sumRate/P1numRate, P1Nctrl);
//			P1Rctrl = getCtrl(P1RsumRate/P1RnumRate, P1Rctrl);
//			P2Nctrl = getCtrl(P2sumRate/P2numRate, P2Nctrl);
			P2Rctrl = getCtrl(P2RsumRate/P2RnumRate, P2Rctrl);
    		
    		//Train and load neural network
    		//Util.trainNet(Util.TRAIN_FREQUENCY_INTERVENTION_NET_URL);
			//Util.trainNet(Util.TRAIN_SLOT_INTERVENTION_NET_URL);
			//Util.trainNet(Util.TRAIN_SELECT_SEQUENCE_NET_URL);
			//Util.trainNet(Util.TRAIN_SELECT_BEHAVIOUR_NET_URL);
			//Util.trainNet(Util.TRAIN_SELECT_GOAL_NET_URL);
			//Util.loadNet(Util.FREQUENCY_INTERVENTION_NET_URL, Util.FREQUENCY_INTERVENTION_NET_EG);
    		//Util.loadNet(Util.SLOT_INTERVENTION_NET_URL, Util.SLOT_INTERVENTION_NET_EG);
    		//Util.loadNet(Util.SELECT_SEQUENCE_NET_URL, Util.SELECT_SEQUENCE_NET_EG);
    		//Util.loadNet(Util.SELECT_BEHAVIOUR_NET_URL, Util.SELECT_BEHAVIOUR_NET_EG);
    		//Util.loadNet(Util.SELECT_GOAL_NET_URL, Util.SELECT_GOAL_NET_EG);	
		}
		
		result.add(x);
		result.add(y1);
		result.add(y2);
		result.add(z1);
		result.add(z2);
		result.add(nn1[0]);
		result.add(r1[0]);
		result.add(nn2[0]);
		result.add(r2[0]);
		result.add(nn1[1]);
		result.add(r1[1]);
		result.add(nn2[1]);
		result.add(r2[1]);
		result.add(nn1[2]);
		result.add(r1[2]);
		result.add(nn2[2]);
		result.add(r2[2]);
		result.add(nn1[3]);
		result.add(r1[3]);
		result.add(nn2[3]);
		result.add(r2[3]);
		result.add(nn1[4]);
		result.add(r1[4]);
		result.add(nn2[4]);
		result.add(r2[4]);
		
		saveData(result, resultFilename);
		
		// plot graph
//		plotGraph pg1 = new plotGraph("Control level Patient 1", "Time", "Control", result.get(0), result.get(1), result.get(2));
//		pg1.plot();
//		plotGraph pg2 = new plotGraph("Control level Patient 2", "Time", "Control", result.get(0), result.get(3), result.get(4));
//		pg2.plot();
//		plotGraph pg11 = new plotGraph("Control level Patient 1 (Frequency)", "Time", "Control", result.get(0), result.get(5), result.get(6));
//		pg11.plot();
//		plotGraph pg21 = new plotGraph("Control level Patient 2 (Frequency)", "Time", "Control", result.get(0), result.get(7), result.get(8));
//		pg21.plot();
//		plotGraph pg12 = new plotGraph("Control level Patient 1 (Slot)", "Time", "Control", result.get(0), result.get(9), result.get(10));
//		pg12.plot();
		plotGraph pg22 = new plotGraph("Control level Patient 2 (Slot)", "Time", "Control", result.get(0), result.get(11), result.get(12));
		pg22.plot();
//		plotGraph pg13 = new plotGraph("Control level Patient 1 (Sequence)", "Time", "Control", result.get(0), result.get(13), result.get(14));
//		pg13.plot();
//		plotGraph pg23 = new plotGraph("Control level Patient 2 (Sequence)", "Time", "Control", result.get(0), result.get(15), result.get(16));
//		pg23.plot();
//		plotGraph pg14 = new plotGraph("Control level Patient 1 (Behaviour)", "Time", "Control", result.get(0), result.get(17), result.get(18));
//		pg14.plot();
//		plotGraph pg24 = new plotGraph("Control level Patient 2 (Behaviour)", "Time", "Control", result.get(0), result.get(19), result.get(20));
//		pg24.plot();
//		plotGraph pg15 = new plotGraph("Control level Patient 1 (Goal)", "Time", "Control", result.get(0), result.get(21), result.get(22));
//		pg15.plot();
//		plotGraph pg25 = new plotGraph("Control level Patient 2 (Goal)", "Time", "Control", result.get(0), result.get(23), result.get(24));
//		pg25.plot();
	}
	
	/** Adjust control level 
	 * 
	 * @param	rate	prefernece rate
	 * @param	ctrl	current control level
	 * @return			control level
	 */	
	private static int getCtrl(double rate, int ctrl) {
		if (rate > 0.6) {
			if (ctrl==20) { return 20; }
			return ++ctrl;
		} else if (rate < 0.4) {
			if (ctrl==1) { return 1; }
			return --ctrl;
		} else {
			return ctrl;
		}
	}
	
	/** Get previous group of question behaviour from sequence  
	 * 
	 * @param	seq		sequence
	 * @return			previous group question
	 */	
	private static int getPrevGBehav(int seq) {
		if (seq==2) {
			return 2;
		} else if (seq==6) {
			return 6;
		} else {
			return 0;
		}
	}
	
	/** Get previous group of question goal from sequence  
	 * 
	 * @param	seq			sequence
	 * @param	prevBehav	previous group question behaviour
	 * @return				previous group question
	 */	
	private static int getPrevGGoal(int seq, int prevBehav) {
		if (seq==2 || seq ==6) {
			return prevBehav;
		} else if (seq==7) {
			return 10;
		} else if (seq==8) {
			return 11;
		} else {
			return 0;
		}
	}
	
	/** Randomly select recommended slots intervention 
	 * 
	 * @return			slots intervention
	 */	
	private static ArrayList<Integer> randomSlots() {
		ArrayList<Integer> slots = new ArrayList<Integer>();
		Random rand = new Random();
		
		if (rand.nextBoolean()) slots.add(1);
		if (rand.nextBoolean()) slots.add(2);
		if (rand.nextBoolean()) slots.add(3);
		if (rand.nextBoolean()) slots.add(4);
		if (rand.nextBoolean()) slots.add(5);
		if (rand.nextBoolean() || slots.isEmpty()) slots.add(6);
		// have to recommend at leasst one slot
		
		return slots;
	}
	
	/** Save result to CSV file  
	 * 
	 * @param	data		result
	 * @param	filename	file
	 */	
	private static void saveData(ArrayList<double[]> data, String filename) {
		System.out.println("Refine process...");
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(filename));
				if (data.size()>0) {
					String [] inLine = new String[data.size()];	
					for (int j=0; j<data.get(0).length; j++) {
						for (int i=0; i<data.size(); i++) {
							inLine[i] = String.valueOf(data.get(i)[j]);
						}
						writer.writeNext(inLine);
					}
				}
				writer.close();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

}
