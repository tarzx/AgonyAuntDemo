import java.util.ArrayList;


public class SequenceQuestion {
	private ArrayList<Question> qList;
	private ArrayList<String> aList;
	private String target_thought = "";
	private String target_behaviour = "";
	private String target_goal = "";
	
	public SequenceQuestion() {
		qList = new ArrayList<Question>();
		aList = new ArrayList<String>();
	}
	
	public int getQNo() {
		return qList.size();
	}
	
	public void setQuestion(Question q) {
		qList.add(q);
	}
	
	public Question getQuestion(int no) {
		if (no-1<qList.size()) {
			return qList.get(no-1);
		} else {
			return null;
		}
	}
	
	public void setThought(String input) {
		target_thought = input;
	}
	public void setBehaviour(String input) {
		target_behaviour = input;
	}
	public void setGoal(String input) {
		target_goal = input;
	}
	public void setAnswer(String input) {
		aList.add(input);
	}
	
	public String getThought() {
		return target_thought;
	}
	public String getBehaviour() {
		return target_behaviour;
	}
	public String getGoal() {
		return target_goal;
	}
	public String getAnswer(int no) {
		if (no-1<aList.size()) {
			return aList.get(no-1);
		} else {
			return null;
		}
	}
}
