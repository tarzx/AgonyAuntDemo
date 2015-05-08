import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Conversation {
	public static void main(String[] args) {
		int lastCtlLv = 1;
		int age = 15;
		boolean isMale = false;
		boolean isConnectServer = false;
		
		Scanner sc = new Scanner(System.in);
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader keyboard = new BufferedReader(in);
		
		System.out.println("Connect to Server (Y/N)?:");
		System.out.println("[Y : Use update neuron network on server N: Offline Neural network]");
    	char mode = sc.next().charAt(0);
    	if (mode=='Y') {
    		isConnectServer = true;
    	}
    	
    	GroupQuestion gq = new GroupQuestion();
		SelectRecommedation sr = new SelectRecommedation(isConnectServer);		
		System.out.println("---------------------------------------------------");
		while (true) {
			StringBuilder sb = new StringBuilder();
	    	sb.append("Enter Mode: ");
        	sb.append("\n (1) Chatting with the system ");
        	sb.append("\n (2) Show transcript example ");
        	if (isConnectServer) sb.append("\n (3) Train & Update Network ");
        	sb.append("\n (0) Exit Program");
        	System.out.println(sb.toString());
        	mode = sc.next().charAt(0);
        	if (mode=='0') {
        		System.exit(0);
        	} else if (mode=='1' || mode=='2') {
        		ArrayList<Integer> slots;
        		int freq;
        		if (isConnectServer) {
        			HashMap<String, String> patient = selectPatient(keyboard);
        			System.out.println("Patient:" + Util.getStringPatirntInfo(patient));
                	if (patient != null) {
        				age = Integer.parseInt(patient.get(Util.TAG_AGE));
        				isMale = (patient.get(Util.TAG_GENDER).equals("0"));
        				
        				HashMap<String, String> preference = Util.getPatientPreference(patient.get(Util.TAG_PID), lastCtlLv);
        				if (patient.get(Util.TAG_SET_FREQ).equals("1")) {
        			        freq = Util.getFrequency(preference);
        				} else {
		        			freq = sr.selectFrequency(lastCtlLv, age, isMale);
	        			}
        				if (patient.get(Util.TAG_SET_SLOT).equals("1")) {
        			        slots = Util.getSlots(preference);
        				} else {
		        			slots = sr.selectTimeSlot(lastCtlLv, age, isMale);
	        			}
	        			printIntervention(slots, freq);
	        			
	        			System.out.println("---------------------------------------------------");
	        			System.out.println("Start Converastion");
	        			System.out.println("---------------------------------------------------");
	        			
	        			int preCtlLv = getControlLevel(keyboard, gq, true);
		        		int seq = sr.selectSequence(preCtlLv, age, isMale);
		        		SequenceQuestion seqQ;
		        		if (mode=='2') {
		        			seqQ = getTranscript(gq, seq, sr, preCtlLv, age, isMale);
		        		} else {
		        			seqQ = startChatting(sc, gq, seq, sr, preCtlLv, age, isMale);
		        		}
		        		int postCtlLv = getControlLevel(keyboard, gq, true);
		        		
		        		//Get Feedback
		        		recordControlLevel(patient.get(Util.TAG_PID), seq, preCtlLv, postCtlLv);
		        		SeqRespond(sc, gq, seqQ, patient.get(Util.TAG_PID), preCtlLv);
		        		
		        		boolean isStart = true;
		        		if (!patient.get(Util.TAG_SET_SLOT).equals("1") && !slots.isEmpty()) { 
		        			slotFeedbackRespond(sc, gq, patient.get(Util.TAG_PID), preCtlLv, slots.get(0), isStart);
		        			isStart = false;
		        		}
		        		if (!patient.get(Util.TAG_SET_FREQ).equals("1") && freq!=0) { 
		        			freqFeedbackRespond(sc, gq, patient.get(Util.TAG_PID), preCtlLv, freq, isStart); 
		        		}
		        		
		        		System.out.println("---------------------------------------------------");
	        			System.out.println("Thank you for your attention!");
	        			System.out.println("---------------------------------------------------");
	        			
	        			lastCtlLv = preCtlLv;
        			} else {
        				System.out.println("Error: please try again!!");
        			}
        		} else {
        			slots = sr.selectTimeSlot(lastCtlLv, age, isMale);
        			freq = sr.selectFrequency(lastCtlLv, age, isMale);
        			printIntervention(slots, freq);
        			
        			System.out.println("---------------------------------------------------");
        			System.out.println("Start Converastion");
        			System.out.println("---------------------------------------------------");
        			
        			int preCtlLv = getControlLevel(keyboard, gq, true);
	        		int seq = sr.selectSequence(preCtlLv, age, isMale);
	        		SequenceQuestion seqQ;
	        		if (mode=='2') {
	        			seqQ = getTranscript(gq, seq, sr, preCtlLv, age, isMale);
	        		} else {
	        			seqQ = startChatting(sc, gq, seq, sr, preCtlLv, age, isMale);
	        		}
	        		int postCtlLv = getControlLevel(keyboard, gq, true);
	        		//Get Feedback
	        		recordControlLevel("", seq, preCtlLv, postCtlLv);
	        		SeqRespond(sc, gq, seqQ, "", preCtlLv);
	        		
	        		System.out.println("---------------------------------------------------");
        			System.out.println("Thank you for your attention!");
        			System.out.println("---------------------------------------------------");
        			
	        		lastCtlLv = preCtlLv;
        		}
        	} else if (mode=='3') {
        		Util.trainNet();
        		Util.loadNet();
        	}
    	}
	}
	
	private static HashMap<String, String> selectPatient(BufferedReader keyboard) {
		int n = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("Select Patient: ");
		ArrayList<HashMap<String, String>> patients = Util.getAllPatient();
		for (HashMap<String, String> patient : patients) {
			sb.append("\n(" + (++n) + ")");
			sb.append(Util.getStringPatirntInfo(patient));
		}
		System.out.println(sb.toString());

		try {
			String lv = keyboard.readLine();
			if (Integer.valueOf(lv) > 0 && Integer.valueOf(lv) <= patients.size()) {
				return patients.get(Integer.valueOf(lv)-1);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static void printIntervention(ArrayList<Integer> slots, int freq) {
		StringBuilder sb = new StringBuilder();
		sb.append("Slot: ");
		sb.append(Util.getSlotTime(slots));
		sb.append(" (Always select the first one)");
		sb.append("\nFrequency: " + freq);
		
		System.out.println(sb.toString());
	}
	
	private static int getControlLevel(BufferedReader keyboard, GroupQuestion gq, boolean isPre) {
		if (isPre) {
			System.out.println(gq.getPreControl());
		} else {
			System.out.println(gq.getPostControl());
		}
		try {
			String lv = keyboard.readLine();
			if (Integer.valueOf(lv) > 0 && Integer.valueOf(lv) <= 20) {
				return Integer.valueOf(lv);
			} else if (Integer.valueOf(lv) <= 0) {
				return 1;
			} else {
				return 20;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	private static SequenceQuestion startChatting(Scanner sc, GroupQuestion gq, int seq,
		SelectRecommedation sr, int ctlLv, int age, boolean isMale) {
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader keyboard = new BufferedReader(in);
		SequenceQuestion seqQ = new SequenceQuestion();
		Question q1, q2, q3, q4, q5, q6;
		String answer = "";
		String target_thought = "";
		String target_behaviour = "";
		String target_goal = "";
		try {
			System.out.println("---------------------------------------------------");
			switch (seq) {
				case 1:
					System.out.println("Sequence 1:");
					
					q1 = gq.getQuestionG1(0);
					System.out.println("C: " + q1);	
					System.out.print("P: " + q1.getAnswerPrefix());
					target_thought = keyboard.readLine();
					seqQ.setQuestion(q1); 
					seqQ.setAnswer(target_thought);
					seqQ.setThought(target_thought);
					
					q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q2); 
					seqQ.setAnswer(answer);
					
					q3 = gq.getQuestionG5(q2.getGroup(), answer, true);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					seqQ.setQuestion(q3); 
					seqQ.setAnswer(target_behaviour);
					seqQ.setBehaviour(target_behaviour);
					
					q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true, false);
					System.out.println("C: " + q4);
					System.out.print("P: " + q4.getAnswerPrefix());
					target_goal = keyboard.readLine();
					seqQ.setQuestion(q4); 
					seqQ.setAnswer(target_goal);
					seqQ.setGoal(target_goal);
					
					q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
					System.out.println("C: " + q5);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q5); 
					seqQ.setAnswer(answer);
					
					break;
				case 2:
					System.out.println("Sequence 2:");
					
					q1 = gq.getQuestionG1(0);
					System.out.println("C: " + q1);	
					System.out.print("P: " + q1.getAnswerPrefix());
					target_thought = keyboard.readLine();
					seqQ.setQuestion(q1); 
					seqQ.setAnswer(target_thought);
					seqQ.setThought(target_thought);
					
					q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q2); 
					seqQ.setAnswer(answer);
					
					q3 = gq.getGroup10OR11(sr, ctlLv, age, isMale, q2.getGroup(), answer, true);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					seqQ.setQuestion(q3); 
					seqQ.setAnswer(target_behaviour);
					seqQ.setBehaviour(target_behaviour);
					
					q4 = q3.getNextQuestion(q3.getGroup(), "", "", false);
					System.out.println("C: " + q4);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q4); 
					seqQ.setAnswer(answer);
					
					q5 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q4.getGroup(), target_thought, "", true, false);
					System.out.println("C: " + q4);
					System.out.print("P: " + q5.getAnswerPrefix());
					target_goal = keyboard.readLine();
					seqQ.setQuestion(q5); 
					seqQ.setAnswer(target_goal);
					seqQ.setGoal(target_goal);
					
					break;
				case 3:
					System.out.println("Sequence 3:");
					
					q1 = gq.getQuestionG9(0);
					System.out.println("C: " + q1);	
					System.out.print("P: " + q1.getAnswerPrefix());
					target_thought = keyboard.readLine();
					seqQ.setQuestion(q1); 
					seqQ.setAnswer(target_thought);
					seqQ.setThought(target_thought);
					
					q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q2); 
					seqQ.setAnswer(answer);
					
					q3 = q1.getNextQuestion(q2.getGroup(), "", answer, true);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					seqQ.setQuestion(q3); 
					seqQ.setAnswer(target_behaviour);
					seqQ.setBehaviour(target_behaviour);
					
					q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true, false);
					System.out.println("C: " + q4);
					System.out.print("P: " + q4.getAnswerPrefix());
					target_goal = keyboard.readLine();
					seqQ.setQuestion(q4); 
					seqQ.setAnswer(target_goal);
					seqQ.setGoal(target_goal);
					
					q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
					System.out.println("C: " + q5);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q5); 
					seqQ.setAnswer(answer);
					
					break;
				case 4:
					System.out.println("Sequence 4:");
					
					q1 = gq.getQuestionG6(0);
					System.out.println("C: " + q1);	
					System.out.print("P: " + q1.getAnswerPrefix());
					target_thought = keyboard.readLine();
					seqQ.setQuestion(q1); 
					seqQ.setAnswer(target_thought);
					seqQ.setThought(target_thought);
					
					q2 = q1.getNextQuestion(q1.getGroup(), target_thought, "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q2); 
					seqQ.setAnswer(answer);
					
					q3 = gq.getQuestionG5(q2.getGroup(), answer, true);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					seqQ.setQuestion(q3); 
					seqQ.setAnswer(target_behaviour);
					seqQ.setBehaviour(target_behaviour);
					
					q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true, false);
					System.out.println("C: " + q4);
					System.out.print("P: " + q4.getAnswerPrefix());
					target_goal = keyboard.readLine();
					seqQ.setQuestion(q4); 
					seqQ.setAnswer(target_goal);
					seqQ.setGoal(target_goal);
					
					q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
					System.out.println("C: " + q5);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q5); 
					seqQ.setAnswer(answer);
					
					break;
				case 5:
					System.out.println("Sequence 5:");
					
					q1 = gq.getQuestionG6(0);
					System.out.println("C: " + q1);	
					System.out.print("P: " + q1.getAnswerPrefix());
					target_thought = keyboard.readLine();
					seqQ.setQuestion(q1); 
					seqQ.setAnswer(target_thought);
					seqQ.setThought(target_thought);
					
					q2 = q1.getNextQuestion(q1.getGroup(), target_thought, "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q2); 
					seqQ.setAnswer(answer);
					
					q3 = gq.getGroup10OR11(sr, ctlLv, age, isMale, q2.getGroup(), answer, true);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					seqQ.setQuestion(q3); 
					seqQ.setAnswer(target_behaviour);
					seqQ.setBehaviour(target_behaviour);
					
					q4 = q3.getNextQuestion(q3.getGroup(), "", "", false);
					System.out.println("C: " + q4);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q4); 
					seqQ.setAnswer(answer);
					
					q5 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q4.getGroup(), target_thought, "", true, false);
					System.out.println("C: " + q5);
					System.out.print("P: " + q5.getAnswerPrefix());
					target_goal = keyboard.readLine();
					seqQ.setQuestion(q5); 
					seqQ.setAnswer(target_goal);
					seqQ.setGoal(target_goal);
					
					break;
				case 6:
					System.out.println("Sequence 6:");
					
					q1 = gq.getQuestionG5(0, "", false);
					System.out.println("C: " + q1);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					seqQ.setQuestion(q1); 
					seqQ.setAnswer(target_behaviour);
					seqQ.setBehaviour(target_behaviour);
					
					q2 = gq.getQuestionG8(q1.getGroup(), "", target_behaviour, false, true);
					System.out.println("C: " + q2);
					System.out.print("P: " + q2.getAnswerPrefix());
					target_goal = keyboard.readLine();
					seqQ.setQuestion(q2); 
					seqQ.setAnswer(target_goal);
					seqQ.setGoal(target_goal);
					
					q3 = q2.getNextQuestion(q2.getGroup(), "", "", false);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q3); 
					seqQ.setAnswer(answer);
					
					q4 = gq.getQuestionG7(q3.getGroup(), target_goal, true);
					System.out.println("C: " + q4);
					System.out.print("P: " + q4.getAnswerPrefix());
					target_thought = keyboard.readLine();
					seqQ.setQuestion(q4); 
					seqQ.setAnswer(target_thought);
					seqQ.setThought(target_thought);
					
					q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
					System.out.println("C: " + q5);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q5); 
					seqQ.setAnswer(answer);
					
					q6 = gq.getQuestionG2(q5.getGroup(), target_thought, "", false);
					System.out.println("C: " + q6);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q6); 
					seqQ.setAnswer(answer);
					
					break;
				case 7:
					System.out.println("Sequence 7:");
					
					q1 = gq.getQuestionG10(0, "", false, false);
					System.out.println("C: " + q1);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					seqQ.setQuestion(q1); 
					seqQ.setAnswer(target_behaviour);
					seqQ.setBehaviour(target_behaviour);
					
					q2 = q1.getNextQuestion(q1.getGroup(), "", "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q2); 
					seqQ.setAnswer(answer);
					
					q3 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q1.getGroup(), "", target_behaviour, false, true);
					System.out.println("C: " + q3);
					System.out.print("P: " + q3.getAnswerPrefix());
					target_goal = keyboard.readLine();
					seqQ.setQuestion(q3); 
					seqQ.setAnswer(target_goal);
					seqQ.setGoal(target_goal);
					
					q4 = gq.getQuestionG7(q3.getGroup(), target_goal, true);
					System.out.println("C: " + q4);	
					System.out.print("P: " + q4.getAnswerPrefix());
					target_thought = keyboard.readLine();
					seqQ.setQuestion(q4); 
					seqQ.setAnswer(target_thought);
					seqQ.setThought(target_thought);
					
					q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
					System.out.println("C: " + q5);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q5); 
					seqQ.setAnswer(answer);
					
					q6 = gq.getQuestionG2(q5.getGroup(), target_thought, "", false);
					System.out.println("C: " + q6);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q6); 
					seqQ.setAnswer(answer);
					
					break;
				case 8:
					System.out.println("Sequence 8:");
					
					q1 = gq.getQuestionG11(0, "", false, false);
					System.out.println("C: " + q1);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					seqQ.setQuestion(q1); 
					seqQ.setAnswer(target_behaviour);
					seqQ.setBehaviour(target_behaviour);
					
					q2 = q1.getNextQuestion(q1.getGroup(), "", "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q2); 
					seqQ.setAnswer(answer);
					
					q3 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q1.getGroup(), "", target_behaviour, false, true);
					System.out.println("C: " + q3);
					System.out.print("P: " + q3.getAnswerPrefix());
					target_goal = keyboard.readLine();
					seqQ.setQuestion(q3); 
					seqQ.setAnswer(target_goal);
					seqQ.setGoal(target_goal);
					
					q4 = gq.getQuestionG7(q3.getGroup(), target_goal, true);
					System.out.println("C: " + q4);	
					System.out.print("P: " + q4.getAnswerPrefix());
					target_thought = keyboard.readLine();
					seqQ.setQuestion(q4); 
					seqQ.setAnswer(target_thought);
					seqQ.setThought(target_thought);
					
					q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
					System.out.println("C: " + q5);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q5); 
					seqQ.setAnswer(answer);
					
					q6 = gq.getQuestionG2(q5.getGroup(), target_thought, "", false);
					System.out.println("C: " + q6);
					System.out.print("P: ");
					answer = keyboard.readLine();
					seqQ.setQuestion(q6); 
					seqQ.setAnswer(answer);
					
					break;
				default:
					System.out.println("Sequence number is not match. try again! " + seq);
					q1 = null; q2 = null; q3 = null; q4 = null; q5 = null; q6 = null;
					break;
			}
			System.out.println("C: " + gq.getSummary(seqQ.getThought(),seqQ.getBehaviour(), seqQ.getGoal()));
			System.out.println("---------------------------------------------------");
			
		} catch (Exception ex) { 
			ex.printStackTrace(); 
		}
		return seqQ;
	}
	
	private static SequenceQuestion getTranscript(GroupQuestion gq, int seq,
			SelectRecommedation sr, int ctlLv, int age, boolean isMale) {
			SequenceQuestion seqQ = new SequenceQuestion();
			Question q1, q2, q3, q4, q5, q6;
			String answer = "";
			String target_thought = "";
			String target_behaviour = "";
			String target_goal = "";
		System.out.println("---------------------------------------------------");
		switch (seq) {
			case 1:
				System.out.println("Sequence 1:");
				
				q1 = gq.getQuestionG1(0);
				System.out.println("C: " + q1);	
				target_thought = "my work.";
				System.out.println("P: " + q1.getAnswerPrefix() + target_thought);
				seqQ.setQuestion(q1); 
				seqQ.setAnswer(target_thought);
				seqQ.setThought(target_thought);
				
				q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
				System.out.println("C: " + q2);
				answer = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q2); 
				seqQ.setAnswer(answer);
				
				q3 = gq.getQuestionG5(q2.getGroup(), answer, true);
				System.out.println("C: " + q3);
				target_behaviour = "I am waiting for the feedback and I haven't talked to my supervisor about it yet.";
				System.out.println("P: " + target_behaviour);
				seqQ.setQuestion(q3); 
				seqQ.setAnswer(target_behaviour);
				seqQ.setBehaviour(target_behaviour);
				
				q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true, false);
				System.out.println("C: " + q4);
				target_goal = "course about presentation for academic";
				System.out.println("P: " + q4.getAnswerPrefix() + target_goal);
				seqQ.setQuestion(q4); 
				seqQ.setAnswer(target_goal);
				seqQ.setGoal(target_goal);
				
				q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
				System.out.println("C: " + q5);
				answer = "I think it would help me to improve my presentation skill.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q5); 
				seqQ.setAnswer(answer);
				
				break;
			case 2:
				System.out.println("Sequence 2:");
				
				q1 = gq.getQuestionG1(0);
				System.out.println("C: " + q1);	
				target_thought = "my work.";
				System.out.println("P: " + q1.getAnswerPrefix() + target_thought);
				seqQ.setQuestion(q1); 
				seqQ.setAnswer(target_thought);
				seqQ.setThought(target_thought);
				
				q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
				System.out.println("C: " + q2);
				answer = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q2); 
				seqQ.setAnswer(answer);
				
				q3 = gq.getGroup10OR11(sr, ctlLv, age, isMale, q2.getGroup(), answer, true);
				System.out.println("C: " + q3);
				target_behaviour = "I cannot stop thinking about it.";
				System.out.println("P: " + target_behaviour);
				seqQ.setQuestion(q3); 
				seqQ.setAnswer(target_behaviour);
				seqQ.setBehaviour(target_behaviour);
				
				q4 = q3.getNextQuestion(q3.getGroup(), "", "", false);
				System.out.println("C: " + q4);
				answer =  "I feel I am too serious about it.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q4); 
				seqQ.setAnswer(answer);
				
				q5 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q4.getGroup(), target_thought, "", true, false);
				System.out.println("C: " + q4);
				target_goal = "my grade and whether I graduate.";
				System.out.println("P: " + q5.getAnswerPrefix() + target_goal);
				seqQ.setQuestion(q5); 
				seqQ.setAnswer(target_goal);
				seqQ.setGoal(target_goal);
				
				break;
			case 3:
				System.out.println("Sequence 3:");
				
				q1 = gq.getQuestionG9(0);
				System.out.println("C: " + q1);	
				target_thought = "my work.";
				System.out.println("P: " + q1.getAnswerPrefix() + target_thought);
				seqQ.setQuestion(q1); 
				seqQ.setAnswer(target_thought);
				seqQ.setThought(target_thought);
				
				q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
				System.out.println("C: " + q2);
				answer = "I feel unhappy about it. I was too nervous and not confident.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q2); 
				seqQ.setAnswer(answer);
				
				q3 = q1.getNextQuestion(q2.getGroup(), "", answer, true);
				System.out.println("C: " + q3);
				target_behaviour = "I aware of the feedback of my last presentation.";
				System.out.println("P: " + target_behaviour);
				seqQ.setQuestion(q3); 
				seqQ.setAnswer(target_behaviour);
				seqQ.setBehaviour(target_behaviour);
				
				q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true, false);
				System.out.println("C: " + q4);
				target_goal = "course about presentation for academic.";
				System.out.println("P: " + q4.getAnswerPrefix() + target_goal);
				seqQ.setQuestion(q4); 
				seqQ.setAnswer(target_goal);
				seqQ.setGoal(target_goal);
				
				q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
				System.out.println("C: " + q5);
				answer = "I think it would help me to improve my presentation skill.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q5); 
				seqQ.setAnswer(answer);
				
				break;
			case 4:
				System.out.println("Sequence 4:");
				
				q1 = gq.getQuestionG6(0);
				System.out.println("C: " + q1);	
				target_thought = "my work.";
				System.out.println("P: " + q1.getAnswerPrefix() + target_thought);
				seqQ.setQuestion(q1); 
				seqQ.setAnswer(target_thought);
				seqQ.setThought(target_thought);
				
				q2 = q1.getNextQuestion(q1.getGroup(), target_thought, "", false);
				System.out.println("C: " + q2);
				answer = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q2); 
				seqQ.setAnswer(answer);
				
				q3 = gq.getQuestionG5(q2.getGroup(), answer, true);
				System.out.println("C: " + q3);
				target_behaviour = "I am waiting for the feedback and I haven’t talked to my supervisor about it yet.";
				System.out.println("P: " + target_behaviour);
				seqQ.setQuestion(q3); 
				seqQ.setAnswer(target_behaviour);
				seqQ.setBehaviour(target_behaviour);
				
				q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true, false);
				System.out.println("C: " + q4);
				target_goal = "course about presentation for academic.";
				System.out.println("P: " + q4.getAnswerPrefix() + target_goal);
				seqQ.setQuestion(q4); 
				seqQ.setAnswer(target_goal);
				seqQ.setGoal(target_goal);
				
				q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
				System.out.println("C: " + q5);
				answer = "I think it would help me to improve my presentation skill.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q5); 
				seqQ.setAnswer(answer);
				
				break;
			case 5:
				System.out.println("Sequence 5:");
				
				q1 = gq.getQuestionG6(0);
				System.out.println("C: " + q1);	
				target_thought = "my work.";
				System.out.println("P: " + q1.getAnswerPrefix() + target_thought);
				seqQ.setQuestion(q1); 
				seqQ.setAnswer(target_thought);
				seqQ.setThought(target_thought);
				
				q2 = q1.getNextQuestion(q1.getGroup(), target_thought, "", false);
				System.out.println("C: " + q2);
				answer = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q2); 
				seqQ.setAnswer(answer);
				
				q3 = gq.getGroup10OR11(sr, ctlLv, age, isMale, q2.getGroup(), answer, true);
				System.out.println("C: " + q3);
				target_behaviour = "I cannot stop thinking about it.";
				System.out.println("P: " + target_behaviour);
				seqQ.setQuestion(q3); 
				seqQ.setAnswer(target_behaviour);
				seqQ.setBehaviour(target_behaviour);
				
				q4 = q3.getNextQuestion(q3.getGroup(), "", "", false);
				System.out.println("C: " + q4);
				answer = "I feel I am too serious about it.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q4); 
				seqQ.setAnswer(answer);
				
				q5 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q4.getGroup(), target_thought, "", true, false);
				System.out.println("C: " + q5);
				target_goal = "my grade and whether I graduate.";
				System.out.println("P: " + q5.getAnswerPrefix() + target_goal);
				seqQ.setQuestion(q5); 
				seqQ.setAnswer(target_goal);
				seqQ.setGoal(target_goal);
				
				break;
			case 6:
				System.out.println("Sequence 6:");
				
				q1 = gq.getQuestionG5(0, "", false);
				System.out.println("C: " + q1);
				target_behaviour = "I am waiting for the feedback of my presentation and I haven’t talked to my supervisor about it yet.";
				System.out.println("P: " + target_behaviour);
				seqQ.setQuestion(q1); 
				seqQ.setAnswer(target_behaviour);
				seqQ.setBehaviour(target_behaviour);
				
				q2 = gq.getQuestionG8(q1.getGroup(), "", target_behaviour, false, true);
				System.out.println("C: " + q2);
				target_goal = "course about presentation for academic.";
				System.out.println("P: " + q2.getAnswerPrefix() + target_goal);
				seqQ.setQuestion(q2); 
				seqQ.setAnswer(target_goal);
				seqQ.setGoal(target_goal);
				
				q3 = q2.getNextQuestion(q2.getGroup(), "", "", false);
				System.out.println("C: " + q3);
				answer = "I think it would help me to improve my presentation skill.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q3); 
				seqQ.setAnswer(answer);
				
				q4 = gq.getQuestionG7(q3.getGroup(), target_goal, true);
				System.out.println("C: " + q4);
				target_thought = "my work.";
				System.out.println("P: " + q4.getAnswerPrefix() + target_thought);
				seqQ.setQuestion(q4); 
				seqQ.setAnswer(target_thought);
				seqQ.setThought(target_thought);
				
				q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
				System.out.println("C: " + q5);
				answer = "I feel I was too nervous and not confident on my last presentation and I should try to way to improve myself.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q5); 
				seqQ.setAnswer(answer);
				
				q6 = gq.getQuestionG2(q5.getGroup(), target_thought, "", false);
				System.out.println("C: " + q6);
				answer = "I feel unhappy about it and want to do it better.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q6); 
				seqQ.setAnswer(answer);
				
				break;
			case 7:
				System.out.println("Sequence 7:");
				
				q1 = gq.getQuestionG10(0, "", false, false);
				System.out.println("C: " + q1);
				target_behaviour = "I worry about my last presentation.";
				System.out.println("P: " + target_behaviour);
				seqQ.setQuestion(q1); 
				seqQ.setAnswer(target_behaviour);
				seqQ.setBehaviour(target_behaviour);
				
				q2 = q1.getNextQuestion(q1.getGroup(), "", "", false);
				System.out.println("C: " + q2);
				answer = "I cannot stop thinking about it even though I feel I am too serious about it.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q2); 
				seqQ.setAnswer(answer);
				
				q3 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q1.getGroup(), "", target_behaviour, false, true);
				System.out.println("C: " + q3);
				target_goal = "to graduate with a good grade.";
				System.out.println("P: " + q3.getAnswerPrefix() + target_goal);
				seqQ.setQuestion(q3); 
				seqQ.setAnswer(target_goal);
				seqQ.setGoal(target_goal);
				
				q4 = gq.getQuestionG7(q3.getGroup(), target_goal, true);
				System.out.println("C: " + q4);	
				target_thought = "my work.";
				System.out.println("P: " + q4.getAnswerPrefix() + target_thought);
				seqQ.setQuestion(q4); 
				seqQ.setAnswer(target_thought);
				seqQ.setThought(target_thought);
				
				q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
				System.out.println("C: " + q5);
				answer = "I feel I was too nervous and not confident on my last presentation and I might not get a good grade on it.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q5); 
				seqQ.setAnswer(answer);
				
				q6 = gq.getQuestionG2(q5.getGroup(), target_thought, "", false);
				System.out.println("C: " + q6);
				answer = "I feel unhappy about it and want to do it better.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q6); 
				seqQ.setAnswer(answer);
				
				break;
			case 8:
				System.out.println("Sequence 8:");
				
				q1 = gq.getQuestionG11(0, "", false, false);
				System.out.println("C: " + q1);
				target_behaviour = "I worry about my last presentation.";
				System.out.println("P: " + target_behaviour);
				seqQ.setQuestion(q1); 
				seqQ.setAnswer(target_behaviour);
				seqQ.setBehaviour(target_behaviour);
				
				q2 = q1.getNextQuestion(q1.getGroup(), "", "", false);
				System.out.println("C: " + q2);
				answer = "I cannot stop thinking about it even though I feel I am too serious about it.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q2); 
				seqQ.setAnswer(answer);
				
				q3 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q1.getGroup(), "", target_behaviour, false, true);
				System.out.println("C: " + q3);
				target_goal = "to graduate with a good grade.";
				System.out.println("P: " + q3.getAnswerPrefix() + target_goal);
				seqQ.setQuestion(q3); 
				seqQ.setAnswer(target_goal);
				seqQ.setGoal(target_goal);
				
				q4 = gq.getQuestionG7(q3.getGroup(), target_goal, true);
				System.out.println("C: " + q4);	
				target_thought = "my work.";
				System.out.println("P: " + q4.getAnswerPrefix() + target_thought);
				seqQ.setQuestion(q4); 
				seqQ.setAnswer(target_thought);
				seqQ.setThought(target_thought);
				
				q5 = q4.getNextQuestion(q4.getGroup(), "", "", false);
				System.out.println("C: " + q5);
				answer = "I feel I was too nervous and not confident on my last presentation and I might not get a good grade on it.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q5); 
				seqQ.setAnswer(answer);
				
				q6 = gq.getQuestionG2(q5.getGroup(), target_thought, "", false);
				System.out.println("C: " + q6);
				answer = "I feel unhappy about it and want to do it better.";
				System.out.println("P: " + answer);
				seqQ.setQuestion(q6); 
				seqQ.setAnswer(answer);
				
				break;
			default:
				System.out.println("Sequence number is not match. try again!" + seq);
				q1 = null; q2 = null; q3 = null; q4 = null;
				break;
		}
		System.out.println("C: " + gq.getSummary(seqQ.getThought(), seqQ.getBehaviour(), seqQ.getGoal()));
		System.out.println("---------------------------------------------------");
		return seqQ;
	}

	private static void SeqRespond(Scanner sc, GroupQuestion gq, SequenceQuestion seqQ, String pid, int preCtlLv) {
		boolean isFirst = true;
		for (int i=2; i<seqQ.getQNo()+1; i++) {
			if (seqQ.getQuestion(i).isAmbiguious()) {
				System.out.println(gq.getAmbiguous(seqQ.getQuestion(i-1).getQuestion(), seqQ.getQuestion(i).getQuestion(), isFirst));
				double rate = getLikeRate(sc);
				
				recordGroupQuestion(pid, preCtlLv, seqQ.getQuestion(i-1).getGroup(), seqQ.getQuestion(i).getGroup(), rate);
				isFirst = false;
			}
		}
	}

	private static void freqFeedbackRespond(Scanner sc, GroupQuestion gq, String pid, int preCtlLv, int freq, boolean isStart) {
		System.out.println(gq.getIntvention(false, isStart));
		double freqRate = getLikeRate(sc);
		//Record Data
		Util.recordFrequency(pid, preCtlLv, freq, freqRate);
	}
	
	private static void slotFeedbackRespond(Scanner sc, GroupQuestion gq, String pid, int preCtlLv, int slot, boolean isStart) {
		System.out.println(gq.getIntvention(true, isStart));
		double slotRate = getLikeRate(sc);
		//Record Data
		Util.recordSlot(pid, preCtlLv, slot, slotRate);
	}
	
	private static double getLikeRate(Scanner sc) {
		double rate = 0.0;
		try {
			char lv = sc.next().charAt(0);
			if (lv=='1') {
				rate = 1.0;
			} else if (lv=='2') {
				rate = 0.5;
			} else {
				rate = 0.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rate;
	}
	
	private static void recordGroupQuestion(String pid, int preCtlLv, int prevG, int GQ, double rate) {
		if (!pid.equals("")) {
			Util.recordSelectGroupQuestion(pid, preCtlLv, prevG, GQ, rate);
		}
	}
	
	private static void recordControlLevel(String pid, int seq, int preCtlLv, int postCtlLv) {
		if (!pid.equals("")) {
	        Util.recordSelectSequence(pid, seq, preCtlLv, (preCtlLv>postCtlLv?0:preCtlLv<postCtlLv?1:0.5));
		}
	}

}
