import java.util.ArrayList;
import java.util.Random;



public class SelectRecommedation {
	
	private Random rand;
	private final double RATE = 0.1;
	private final double BASE_RATE = 0.8;
	
	private boolean isConnectServer = false;
	
	public SelectRecommedation(boolean isConnectServer) {
		rand = new Random();
		this.isConnectServer = isConnectServer;
		if (this.isConnectServer) Util.loadNet();
	}
	
	public int selectFrequency(int lastCtlLv, int age, boolean isMale) {
		FrequencyInterventionNet fin = new FrequencyInterventionNet();
		
		double[] freqRate = fin.getRate(lastCtlLv, age, isMale);
		
		return fin.getFreq(getMaxRandomRate(freqRate));
	}
	
	public ArrayList<Integer> selectTimeSlot(int lastCtlLv, int age, boolean isMale) {
		TimeSlotInterventionNet tsin = new TimeSlotInterventionNet();
		
		double[] slotRate = tsin.getRate(lastCtlLv, age, isMale);
		
		ArrayList<Integer>  idxSet = getSetMaxRandomRate(slotRate);
		ArrayList<Integer>  slotSet = new ArrayList<Integer>();
		for (int idx : idxSet) {
			slotSet.add(tsin.getSlot(idx));
		}
		
		return slotSet;
	}
	
	public int selectSequence(int ctlLv, int age, boolean isMale) {
		SelectSequenceNet ssq = new SelectSequenceNet();
		
		double[] seqRate = ssq.getRate(ctlLv, age, isMale);

		return ssq.getSequence(getMaxRandomRate(seqRate));
	}
	
	public int selectReplayGoal(int ctlLv, int age, boolean isMale, int prevGroup) {
		SelectGQGoalNet sgqg = new SelectGQGoalNet();
		
		double[] rateGQGoal = sgqg.getRate(ctlLv, age, isMale, prevGroup);
		
		return sgqg.getGroupQuestion(getMaxRandomRate(rateGQGoal));
	}
	
	public int selectReflectBehaviour(int ctlLv, int age, boolean isMale, int prevGroup) {
		SelectGQBehaviourNet sgqb = new SelectGQBehaviourNet();
		
		double[] rateGQBehaviour = sgqb.getRate(ctlLv, age, isMale, prevGroup);
		
		return sgqb.getGroupQuestion(getMaxRandomRate(rateGQBehaviour));
	}
	
	private int getMaxRandomRate(double[] rate) {
		int maxIdx = 0;
		double max = Double.MIN_VALUE;
		for (int i=0; i<rate.length; i++) {
			double rndRate = rate[i] + getRandomRate();
			if (max<rndRate) {
				max = rndRate;
				maxIdx = i;
			}
		}
		return maxIdx;
	}
	
	private ArrayList<Integer> getSetMaxRandomRate(double[] rate) {
		double[] rndRate = new double[rate.length];
		double max = Double.MIN_VALUE;
		for (int i=0; i<rate.length; i++) {
			rndRate[i] = rate[i] + getRandomRate();
			if (max<rndRate[i]) {
				max = rndRate[i];
			}
		}
		ArrayList<Integer> maxSet = new ArrayList<Integer>();
		for (int i=0; i<rndRate.length; i++) {
			if (rndRate[i]>=BASE_RATE || rndRate[i]==max) {
				maxSet.add(i);
			}
		}
		return maxSet;
	}
	
	private double getRandomRate() {
		switch (rand.nextInt(3)) {
		case 0:
			return 0;
		case 1:
			return RATE;
		default:
			return -RATE;
		}
	}

	
}

