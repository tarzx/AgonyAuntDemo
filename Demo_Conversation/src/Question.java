/** This class generates the individual question
 * 
 * @author Patomporn Loungvara
 *
 */
public class Question {
	private int _questionGroup;
	private int _prevGroup;
	private Question _nextQuestion = null;
	private String _definedQuestion = "";
	private String _question = "";
	private String _answerPrefix = "";
	private String _echo = "";
	private String _changeTopic = "";
	private String _defaultTopic = "";
	private String _target = "";
	private String _prevAnswer = "";
	private String _echoAnswer = "";
	private String _pattern = "";
	private String _defaultPattern = "";
	private String _link = "";
	private boolean _isEcho;
	private boolean _isChangeTopic;
	private boolean _isAmbiguous;
	private boolean _isDefined;
	
	public Question(int group, String question) {
		this._questionGroup = group;
		this._question = question;
	}
	
	public int getGroup() {
		return (int)(this._questionGroup/10);
	}
	public int getPrevGroup() {
		return this._prevGroup;
	}
	public void setPrevGroup(int prevGroup) {
		this._prevGroup = prevGroup;
	}
	
	public void setIsAmbiguous(boolean isAmbiguous) {
		this._isAmbiguous = isAmbiguous;
	}
	public boolean isAmbiguous() {
		return this._isAmbiguous;
	}
	
	public String getAnswerPrefix() {
		return this._answerPrefix;
	}
	public void setAnswerPrefix(String answerPrefix) {	
		this._answerPrefix = answerPrefix;
	}
	
	public Question getNextQuestion(int prevGroup, String prevAnswer, String echoAnswer, boolean isEcho) {
		this._nextQuestion.setPrevGroup(prevGroup);
		this._nextQuestion.setIsEcho(isEcho);
		this._nextQuestion.setPrevEcho(echoAnswer);;
		this._nextQuestion.setPrevAnswer(prevAnswer);
		return this._nextQuestion;
	}
	public void setNextQuestion(Question nextQuestion) {
		this._nextQuestion = nextQuestion;
	}
	
	public void setEcho(String echo) {
		this._echo = echo;
	}
	public void setChangeTopic(String changeTopic) {
		this._changeTopic = changeTopic;
	}
	public void setDefaultTopic(String defaultTopic) {
		this._defaultTopic = defaultTopic;
	}
	public void setTarget(String target) {
		this._target = target;
	}
	public void setPrevAnswer(String prevAnswer) {
		this._prevAnswer = prevAnswer;
	}
	public void setPrevEcho(String echoAnswer) {
		this._echoAnswer = echoAnswer;
	}
	public void setPattern(String pattern) {
		this._pattern = pattern;
	}
	public void setDefaultPattern(String defaultPattern) {
		this._defaultPattern = defaultPattern;
	}
	public void setLink(String link) {
		this._link = link;
	}
	public void setIsEcho(boolean isEcho) {
		this._isEcho = isEcho;
	}
	public void setIsChangeTopic(boolean isChangeTopic) {
		this._isChangeTopic = isChangeTopic;
	}
	
	private void defineQuestion() {
		StringBuilder sb = new StringBuilder();
		switch (this._questionGroup) {
		case 10:
			sb.append(this._question);
			break;
		case 20:
			if (this._isEcho) {
				sb.append(Transform.replaceTransformPattern("%%%", this._echo, this._echoAnswer, false));
				sb.append(this._link);
				sb.append(Transform.startSentence(
							Transform.replaceQuestion("$$$", "###", this._question, this._prevAnswer, this._pattern, this._defaultPattern),
							false));
			} else {
				sb.append(Transform.startSentence(
							Transform.replaceQuestion("$$$", "###", this._question, this._prevAnswer, this._pattern, this._defaultPattern),
							true));
			}
			break;
		case 30:
			if (this._isChangeTopic) {
				sb.append(Transform.replacePattern("@@@",
							Transform.replacePartial("###", this._changeTopic, this._prevAnswer, this._defaultTopic), 
							this._target));
				sb.append(this._question);
			} else if (this._isEcho) {
				sb.append(Transform.replaceTransformPattern("%%%", this._echo, this._echoAnswer, false));
				sb.append(this._link);
				sb.append(Transform.startSentence(this._question, false));
			}	
			break;
		case 40:
			if (this._isChangeTopic) {
				sb.append(Transform.replacePattern("@@@",
							Transform.replacePartial("###",this._changeTopic, this._prevAnswer, this._defaultTopic), 
							this._target));
				sb.append(this._question);
			} else if (this._isEcho) {
				sb.append(Transform.replaceTransformPattern("%%%", this._echo, this._echoAnswer, false));
				sb.append(this._link);
				sb.append(Transform.startSentence(this._question, false));
			}	
			break;
		case 50:
			if (this._isEcho) {
				sb.append(Transform.replaceTransformPattern("%%%", this._echo, this._echoAnswer, false));
				sb.append(this._link);
				sb.append(Transform.startSentence(this._question, false));
			} else {
				sb.append(this._question);
			}
			break;
		case 61:
			sb.append(this._question);
			break;
		case 62:
			//sb.append(this._link);
			sb.append(Transform.replaceQuestion("$$$", "###", this._question, this._prevAnswer, this._pattern, this._defaultPattern));
			break;
		case 71:
			sb.append(Transform.replacePattern("@@@",
					Transform.replacePartial("###", this._changeTopic, this._prevAnswer, this._defaultTopic), 
					this._target));	
			sb.append(this._question);
			break;
		case 72:
			sb.append(this._link);
			sb.append(Transform.startSentence(this._question, false));
			break;
		case 81:
			if (this._isChangeTopic) {
				sb.append(Transform.replacePattern("@@@",
							Transform.replacePartial("###", this._changeTopic, this._prevAnswer, this._defaultTopic), 
							this._target));
				sb.append(this._question);
			} else if (this._isEcho) {
				sb.append(Transform.replaceTransformPattern("%%%", this._echo, this._echoAnswer, false));
				sb.append(this._link);
				sb.append(Transform.startSentence(this._question, false));
			}	
			break;
		case 82:
			//sb.append(this._link);
			sb.append(this._question);
			break;
		case 91:
			sb.append(this._question);
			break;
		case 92:
			if (this._isEcho) {
				sb.append(Transform.replaceTransformPattern("%%%", this._echo, this._echoAnswer, false));
				sb.append(this._link);
				sb.append(Transform.startSentence(this._question, false));
			} else {
				sb.append(this._question);
			}
			break;
		case 101:
			if (this._isEcho) {
				sb.append(Transform.replaceTransformPattern("%%%", this._echo, this._echoAnswer, false));
				sb.append(this._link);
				sb.append(Transform.startSentence(this._question, false));
			} else {
				sb.append(this._question);
			}
			break;
		case 102:
			//sb.append(this._link);
			sb.append(this._question);
			break;
		case 111:
			if (this._isEcho) {
				sb.append(Transform.replaceTransformPattern("%%%", this._echo, this._echoAnswer, false));
				sb.append(this._link);
				sb.append(Transform.startSentence(this._question, false));
			} else {
				sb.append(this._question);
			}
			break;
		case 112:
			//sb.append(this._link);
			sb.append(this._question);
			break;
		default:
			break;
		}
		this._definedQuestion = sb.toString();
		this._isDefined = true;
	}
	
	public String getQuestion() {
		if (this._question.contains("$$$")) {
			return Transform.replaceQuestion("$$$", "###", this._question, this._prevAnswer, this._pattern, this._defaultPattern);
		} else {
			return this._question;
		}
	}
	
	@Override
	public String toString() {
		if (!this._isDefined) {
			defineQuestion();
		}
		return this._definedQuestion;
	}
}
