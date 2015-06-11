/** This class provide useful function for transform the question to interact with user
 * 
 * @author Patomporn Loungvara
 *
 */
public class Transform {
	private static int MAX_PARTIAL = 10;
	private static String[][] mapSubject = { {"i", "i'm", "i'd", "my", "mine", "myself", "me"}, 
											 {"you", "you're", "you'd", "your", "yours", "yourself", "you"} };
	
	private static String partialTransform(final String input) {
		if (input.indexOf(". ") > 0) {
			String sentence = input.substring(0, input.indexOf(". "));
			if (sentence.split(" ").length < MAX_PARTIAL) {
				return fullTransform(trimEnd(sentence), false); 
			}
		} else {
			if (input.split(" ").length < MAX_PARTIAL) {
				return fullTransform(trimEnd(input), false); 
			}
		}
		return "";
	}
	
	private static String fullTransform(final String input, final boolean isStart) {
		StringBuilder output = new StringBuilder();
		String[] component = input.toLowerCase().split(" ");
		for (int i=0; i<component.length; i++) {
			component[i] = grammarPersonal(negativeForm(component[i]));
		}
		for (int i=0; i<component.length-1; i++) {
			component[i+1] = componentSubject(component[i], component[i+1]);
		}
		for (int i=0; i<component.length; i++) {
			output.append(component[i] + " ");
		}
		
		return trimEnd(startSentence(concat(output.toString().trim()), isStart));
	}
	
	private static String concat(final String input) {
		if (input.contains(". ")) {
			return input.replace(". ", " and ");
		} else {
			return input;
		}
	}
	
	private static String trimEnd(final String input) {
		if (input.trim().endsWith(".")) {
			return input.substring(0, input.lastIndexOf("."));
		} else {
			return input;
		}
	}
	
	private static String negativeForm(final String input) {
		if (input.equals("can't")) {
			return "cannot";
		} else if (input.endsWith("n't")) {
			return input.replace("n't", " not");
		} else {
			return input;
		}
	}
	
	private static String grammarPersonal(final String input) {
		String output = input;
		for (int i=0; i<mapSubject[0].length; i++) {
			if (input.equals(mapSubject[0][i])) {
				output = mapSubject[1][i];
				break;
			} else if (input.equals(mapSubject[1][i])) {
				output = mapSubject[0][i];
				break;
			} 
		}
		return output;
	}
	
	private static String componentSubject(final String input1, final String input2) {
		String output = input2;
		if (input1.equals("i")) {
			if (input2.equals("were")) {
				output = "was";
			} else if (input2.equals("are")) {
				output = "am";
			}
		} else if (input1.equals("you")) {
			if (input2.equals("was")) {
				output = "were";
			} else if (input2.equals("am")) {
				output = "are";
			}
		}
		return output;
	}
	
	public static String startSentence(final String input, boolean isUpper) {
		if (isUpper) {
			return input.substring(0, 1).toUpperCase() + input.substring(1);
		} else {
			String output = " " + input.toLowerCase();
			output = output.replace(" i ", " I ");
			output = output.replace(" i'", " I'");
			return output.substring(1);
		}
	}
	
	public static String replacePattern(String pattern, String input, String prevAns) {
		return input.replace(pattern, prevAns);
	}
	
	public static String replaceTransformPattern(String pattern, String input, String prevAns, boolean isStart) {
		return input.replace(pattern, Transform.fullTransform(prevAns, isStart));
	}	
	
	public static String replacePartial(String pattern, String input, String prevAns, String default_text) {
		String partial =  Transform.partialTransform(prevAns);
		if (!partial.equals("")) {
			return input.replace(pattern, partial);
		} else {
			return input.replace(pattern, default_text);
		}
	}
	
	public static String replaceQuestion(String qPattern, String pattern, String input, String prevAns, String text, String default_text) {
		return input.replace(qPattern, replacePartial(pattern, text, prevAns, default_text));
	}
	
}
