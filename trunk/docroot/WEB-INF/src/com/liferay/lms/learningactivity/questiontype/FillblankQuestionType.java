package com.liferay.lms.learningactivity.questiontype;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.portlet.ActionRequest;

import com.liferay.lms.model.TestAnswer;
import com.liferay.lms.model.TestQuestion;
import com.liferay.lms.service.LearningActivityLocalServiceUtil;
import com.liferay.lms.service.TestAnswerLocalService;
import com.liferay.lms.service.TestAnswerLocalServiceUtil;
import com.liferay.lms.service.TestQuestionLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.theme.ThemeDisplay;

public class FillblankQuestionType extends BaseQuestionType {

	private static final long serialVersionUID = 1L;
	
	public long getTypeId(){
		return 3;
	}
	
	public String getName() {
		return "fillblank";
	}

	public String getTitle(Locale locale) {
		return LanguageUtil.get(locale, "fillblank.title");
	}

	public String getDescription(Locale locale) {
		return LanguageUtil.get(locale, "fillblank.description");
	}
	
	public String getAnswerEditingAdvise(Locale locale) {
		return LanguageUtil.get(locale, "fillblank.advise");
	}
	
	public String getURLEdit(){
		return "/html/execactivity/test/admin/editFillblankQTAnswers.jsp";
	}
	
	public boolean correct(ActionRequest actionRequest, long questionId){
		List<TestAnswer> testAnswers = new ArrayList<TestAnswer>();
		try {
			testAnswers = TestAnswerLocalServiceUtil.getTestAnswersByQuestionId(questionId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		TestAnswer solution = null;
		if(testAnswers!=null && testAnswers.size()>0)
			solution = testAnswers.get(0);
		
		if(solution!=null){
			int correctAnswers=0;
			
			List<String> sols = getQuestionSols(solution.getAnswer());
			int i=0;
			for(String sol:sols){
				String answer= ParamUtil.getString(actionRequest, "question_"+questionId+"_"+i, "");
				if(isCorrect(sol, answer)){
					correctAnswers++;
				}
				i++;
			}
			
			if(correctAnswers==sols.size())	return true;
			else return false;
		}
	
		return false;
	}

	private List<String> getQuestionSols(String textAnswer) {
		List<String> sols = new ArrayList<String>();//array con las soluciones {...}
		String temp="";
		int start = textAnswer.indexOf("{"), end = 0;
		while (start != -1){
			end = textAnswer.indexOf("}");
			if(end != -1){
				if(end+1 == textAnswer.length()) temp = textAnswer.substring(start);
				else {
					if(textAnswer.charAt(end+1) == '}')
						if(end+2 == textAnswer.length()) temp = textAnswer.substring(start);
						else temp = textAnswer.substring(start, end+2);
					else temp = textAnswer.substring(start, end+1);
				}
				if(temp.startsWith("{{") || isMoodleAnswer(temp))sols.add(temp);
				textAnswer = textAnswer.replace(temp, "");
				start = textAnswer.indexOf("{");
			}
		}
		return sols;
	}
	
	protected boolean isCorrect(String solution, String answer){
		boolean correct = false;
		Collator c = Collator.getInstance();
		c.setStrength(Collator.PRIMARY);
		List<String> sols = getBlankSols(solution, true);
		for(String sol:sols)
			if(c.compare(answer,sol)==0) {
				correct = true;
				break;
			}
		return correct;
	}

	private List<String> getBlankSols(String solution, boolean onlyCorrectOnes) {
		List<String> correctSols =new ArrayList<String>();
		if(solution.startsWith("{{")){
			solution = solution.replace("{{", "");
			if(solution.contains("}}")) solution = solution.replace("}}", "");
			correctSols.add(solution);
		}else if(solution.startsWith("{")){
			boolean isNumerical = false;
			if(solution.contains(":NUMERICAL:") || solution.contains(":NM:")) isNumerical = true;
			String aux = solution.substring(solution.indexOf(":", solution.indexOf(":")+1)+1);
			if(aux.endsWith("}")) aux = aux.substring(0, aux.length()-1);
			String[] sols = aux.split("~");
			for(String sol:sols){
				if(!sol.startsWith("*#")){
					if(sol.startsWith("=")) sol = sol.replace("=", "");
					else if(sol.startsWith("%") && !sol.startsWith("%0%")) sol = sol.replace(sol.substring(sol.indexOf("%"), sol.lastIndexOf("%")+1), "");
					else {
						if(sol.startsWith("%0%")) sol = sol.replace(sol.substring(sol.indexOf("%"), sol.lastIndexOf("%")+1), "");
						if(onlyCorrectOnes) sol = "*#";//para que no incluya las q son falsas
					}
					if(!sol.startsWith("*#")){
						if(sol.contains("#")) sol=sol.substring(0,sol.indexOf("#"));
						if(isNumerical && sol.contains(":")) sol = sol.substring(0, sol.indexOf(":"));
						if(!correctSols.contains(sol)) correctSols.add(sol);	
					}
				}
			}
		}
		return correctSols;
	}
	
	public String getHtmlView(long questionId, ThemeDisplay themeDisplay, Document document){
		String view = "";
		String answersView="";
		
		try {
			//Cogemos las respuestas a los blancos (separadas por coma) de la pregunta a partir del xml de learningactivityresult
			TestQuestion question = TestQuestionLocalServiceUtil.fetchTestQuestion(questionId);
			String answer="";
			if (document != null) {
				Iterator<Element> nodeItr = document.getRootElement().elementIterator();
				while(nodeItr.hasNext()) {
					Element element = nodeItr.next();
			         if("question".equals(element.getName()) && questionId == Long.valueOf(element.attributeValue("id"))){
			        	 Iterator<Element> elementItr = element.elementIterator();
			        	 if(elementItr.hasNext()) {
			        		 Element elementElement = elementItr.next();
			        		 if("answer".equals(elementElement.getName())) {
			        			 try {
									answer = elementElement.getText();
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
			        		 }
			        	 }
			         }
			    }	
			}
			List<TestAnswer> testAnswers= TestAnswerLocalServiceUtil.getTestAnswersByQuestionId(question.getQuestionId());
			if(testAnswers!=null && testAnswers.size()>0){
								
				view += "<div class=\"question\">"+
						"<input type=\"hidden\" name=\""+themeDisplay.getPortletDisplay().getNamespace()+"question\" value=\"" + question.getQuestionId() + "\"/>"+
						"<div class=\"questiontext\">" + question.getText() + "</div>"+
						"<div class=\"answer\">";
				
				
				TestAnswer solution = testAnswers.get(0);
				List<String> sols = getQuestionSols(solution.getAnswer());
				String[] answers = answer.split(",");
				answersView = translateNewLines(solution.getAnswer());
				int i=0;
				for(String sol:sols){
					String ans = (answers.length>i)?answers[i]:"";
					String auxans = "";
					
					if(sol.contains(":SHORTANSWER") || sol.contains(":SA") || sol.contains(":MW")
							|| sol.contains(":NUMERICAL:") || sol.contains(":NM:") || sol.contains("{{")) {
						auxans= "<input type=\"text\" name=\""+themeDisplay.getPortletDisplay().getNamespace()+"question_" + question.getQuestionId()+"_"+i + "\" value=\""+ans+"\" >";//input
					}
					else if(sol.contains(":MULTICHOICE_") || sol.contains(":MCV") || sol.contains(":MCH")){
						String aux = "";
						auxans = "<br/>";
						List<String> totalBlankSols = getBlankSols(sol, false);
						for(String blankSol:totalBlankSols){
							String checked = "";
							if(blankSol.equals(ans)) {
								checked="checked='checked'";
							}
							aux = "<div class=\"answer\"><input type=\"radio\"" + checked + "name=\""+themeDisplay.getPortletDisplay().getNamespace()+"question_" + question.getQuestionId()+"_"+i + "\" value=\"" + sol + "\" >" + sol + "</div>";//radiobuttons
							auxans += aux;
						}
					}else if(sol.contains(":MULTICHOICE:") || sol.contains(":MC:")){
						auxans+="<select name=\""+themeDisplay.getPortletDisplay().getNamespace()+"question_" + question.getQuestionId()+"_"+i + "\">";
						auxans+="<option value=\"\" label=\"\"/>";
						List<String> totalBlankSols = getBlankSols(sol, false);
						for(String blankSol:totalBlankSols){
							String selected = "";
							if(ans.equals(blankSol)) {
								selected ="selected";
							}
							auxans+="<option value=\""+ blankSol +"\" label=\""+blankSol +"\" "+ selected +"/>";//dropdown
						}
						auxans+="</select>";
					}else {
						auxans+=sol;
					}
					answersView = answersView.replace(sol, auxans);
					i++;
				}
			}
			
			view += answersView+"</div>" +
				"</div>";
			
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		
		return view;
	}
	
	private boolean isMoodleAnswer(String temp) {
		if(temp.contains(":SHORTANSWER") || temp.contains(":SA") || temp.contains(":MW")
				|| temp.contains(":NUMERICAL:") || temp.contains(":NM:") || temp.contains("{{") 
				|| temp.contains(":MULTICHOICE_") || temp.contains(":MCV") || temp.contains(":MCH")
				|| temp.contains(":MULTICHOICE:") || temp.contains(":MC:")) return true;
		return false;
	}
	
	public Element getResults(ActionRequest actionRequest, long questionId){
		List<TestAnswer> testAnswers = new ArrayList<TestAnswer>();
		try {
			testAnswers = TestAnswerLocalServiceUtil.getTestAnswersByQuestionId(questionId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		TestAnswer solution = null;
		if(testAnswers!=null && testAnswers.size()>0)
			solution = testAnswers.get(0);
		
		String answer = "";
		
		if(solution!=null){
			int i = getQuestionSols(solution.getAnswer()).size();
			for(int k=0; k<i; k++){
				if(answer!="") answer+=",";
				answer+= ParamUtil.getString(actionRequest, "question_"+questionId+"_"+k, "");
			}
		}
    	
		Element questionXML=SAXReaderUtil.createElement("question");
		questionXML.addAttribute("id", Long.toString(questionId));
		
		long currentQuestionId = ParamUtil.getLong(actionRequest, "currentQuestionId");
		if (currentQuestionId == questionId) {
			questionXML.addAttribute("current", "true");
		}
		
		Element answerXML=SAXReaderUtil.createElement("answer");
		answerXML.addText(answer);
		questionXML.add(answerXML);
		
		return questionXML;
	}
	
	public String getHtmlFeedback(Document document,long questionId){
		String feedBack = "", answersFeedBack="";
		try {
			
			//Cogemos las respuestas a los blancos (separadas por coma) de la pregunta a partir del xml de learningactivityresult
			TestQuestion question = TestQuestionLocalServiceUtil.fetchTestQuestion(questionId);
			String feedMessage = LanguageUtil.get(Locale.getDefault(),"answer-in-blank") ;
			String answer="";
			Iterator<Element> nodeItr = document.getRootElement().elementIterator();
			while(nodeItr.hasNext()) {
				Element element = nodeItr.next();
		         if("question".equals(element.getName()) && questionId == Long.valueOf(element.attributeValue("id"))){
		        	 Iterator<Element> elementItr = element.elementIterator();
		        	 if(elementItr.hasNext()) {
		        		 Element elementElement = elementItr.next();
		        		 if("answer".equals(elementElement.getName())) {
		        			 try {
								answer = elementElement.getText();
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
		        		 }
		        	 }
		         }
		    }	
			
			//Cogemos el TestAnswer de la pregunta en formato Moodle para el chequeo del feedback
			String cssclass="question incorrect";
			List<TestAnswer> testAnswers= TestAnswerLocalServiceUtil.getTestAnswersByQuestionId(question.getQuestionId());
			if(testAnswers!=null && testAnswers.size()>0){
				
				//Comprobamos si todos los blancos son acertados para ver si la pregunta resulta correcta o no
				TestAnswer solution = testAnswers.get(0);
				String showCorrectAnswer = LearningActivityLocalServiceUtil.getExtraContentValue(question.getActId(), "showCorrectAnswer");
				List<String> sols = getQuestionSols(solution.getAnswer());
				String[] answers = answer.split(",");
				int i=0, correctAnswers=0;
				for(String sol:sols){
					String ans= (answers.length>i)?answers[i]:"";
					if(isCorrect(sol, ans)){
						correctAnswers++;
					}
					i++;
				}
				if(correctAnswers==sols.size()){
					feedMessage=solution.getFeedbackCorrect();
					cssclass="question correct";
				}else {
					feedMessage=solution.getFeedbacknocorrect();
					cssclass="question incorrect";
				}
				
				//Obtain feedback
				String solok="";
				answersFeedBack = translateNewLines(solution.getAnswer());
				
				i=0;
				for(String sol:sols){
					String ans = (answers.length>i)?answers[i]:"";
					String auxans = "";
					List<String> blankSols = getBlankSols(sol, true);
					
					if(sol.contains(":SHORTANSWER") || sol.contains(":SA") || sol.contains(":MW")
							|| sol.contains(":NUMERICAL:") || sol.contains(":NM:") || sol.contains("{{")) {
						auxans= "<input readonly type=\"text\" value=\""+ans+"\" >";//input
						if("true".equals(showCorrectAnswer)) {
							for(String blankSol:blankSols){
								if(solok != "") solok += " | ";
								solok += blankSol;
							}
							auxans += "<div class=\" font_14 color_cuarto negrita\"> (" + solok + ") </div>";
						}
					}
					else if(sol.contains(":MULTICHOICE_") || sol.contains(":MCV") || sol.contains(":MCH")){
						String aux = "";
						auxans = "<br/>";
						List<String> totalBlankSols = getBlankSols(sol, false);
						for(String blankSol:totalBlankSols){
							String checked = "", correct = "";
							if(blankSol.equals(ans)) checked="checked='checked'";
							if("true".equals(showCorrectAnswer) && blankSols.contains(blankSol)) correct = "font_14 color_cuarto negrita";
							aux = "<div class=\"answer " + correct + "\"><input type=\"radio\"" + checked + "value=\"" + blankSol + " disabled=\"disabled\" \" >" + blankSol + "</div>";//radiobuttons
							auxans += aux;
						}
					}else if(sol.contains(":MULTICHOICE:") || sol.contains(":MC:")){
						auxans+="<select>";
						auxans+="<option value=\"\" disabled label=\"\"/>";//primer valor vac�o
						List<String> totalBlankSols = getBlankSols(sol, false);
						for(String blankSol:totalBlankSols){
							String selected = "";
							if(ans.equals(blankSol)) selected ="selected";
							auxans+="<option value=\""+ blankSol +"\" disabled label=\""+blankSol +"\" "+ selected +"/>";//dropdown
						}
						auxans+="</select>";
						if("true".equals(showCorrectAnswer)) {
							for(String blankSol:blankSols){
								if(solok != "") solok += " | ";
								solok += blankSol;
							}
							auxans += "<div class=\" font_14 color_cuarto negrita\"> (" + solok + ") </div>";
						}
					}else auxans+=sol;
					answersFeedBack = answersFeedBack.replace(sol, auxans);
					i++;solok="";
				}
				
				feedBack += "<div class=\"" + cssclass + "\">" + 
								"<div class=\"questiontext\">" + question.getText() + "</div>" +
								"<div class=\"content_answer\">" +
									answersFeedBack +
								"</div>" +
								"<div class=\"questionFeedback\">" + feedMessage + "</div>" +
							"</div>";
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return feedBack;
	}
	
	public void importMoodle(long actId, Element question, TestAnswerLocalService testAnswerLocalService)throws SystemException, PortalException {
		//"cloze"
		Element name=question.element("name");
		String description=(name!=null)?name.elementText("text"):"";
		TestQuestion theQuestion=TestQuestionLocalServiceUtil.addQuestion(actId,description,getTypeId());
		Element questiontext=question.element("questiontext");
		String answer=questiontext.elementText("text");
		Element generalFeedback=question.element("generalfeedback");
		String feedback=generalFeedback.elementText("text");
		testAnswerLocalService.addTestAnswer(theQuestion.getQuestionId(), answer, feedback, feedback, true);
	}
	
	private String translateNewLines(String input){
		input = input.replace("\n", "<br/>");
		input = input.replace("\r", "<br/>");
		return input;
	}
	
}
