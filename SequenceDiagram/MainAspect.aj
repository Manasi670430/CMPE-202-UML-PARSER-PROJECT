import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

import net.sourceforge.plantuml.SourceStringReader;

public aspect MainAspect {
	pointcut executeAll(Object object) : !within(InformationStack) && !within(MainAspect) && target(object) && execution(public * *.*(..))
	 && !execution(*.new(..));

	pointcut excuteConstructors() : !within(InformationStack) && !within(MainAspect) && execution(*.new(..));	
	
	public static class AspectState {
		private int stackDepth =0 ;
		private int depth = 0;
		private Stack<String> xStack = new Stack<String>();
		private String  finalMsg = "";
		
		private String startUMLstring = "@startuml\n activate Main";
		private String endUMLstring = "deactivate Main \n@enduml";
		private InformationStack<Integer> id = new InformationStack<>();
		private int idVal = 1;
		
		public void incrementConstructorCall() {
			depth++;
		}
		
		public void decrementConstructorCall() {
			depth--;
		}
		private void updateStack() {
			stackDepth--;
			
		
			if(!xStack.isEmpty()) {
				xStack.pop();
			}
			if(!id.isEmpty()) {
				idVal = (Integer)id.pop()+1;
			}
		}
		
		public void removeFromStack() {
			if(depth ==0) {
				
				String deactivatingClass = xStack.peek();
				
				finalMsg += "deactivate " + deactivatingClass  +"\n";
				
				
				updateStack();
				SequenceGeneration();
			}
		}

		private void SequenceGeneration() {
			if(stackDepth == 0) {
				
				printSequencePlantUMLString(finalMsg, "D://SequenceImage");
			}
		}
		
		private void printSequencePlantUMLString(String finalString, String imagePath){
			
			
			System.out.println(finalString);
			String finalString1 = startUMLstring + "\nskinparam classAttributeIconSize 0\n" + finalString + endUMLstring;
			SourceStringReader sr = new SourceStringReader(finalString1);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream =  new FileOutputStream(imagePath + ".png");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sr.generateImage(fileOutputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}


		
		
		public String getLeftClassName() {
			return xStack.isEmpty()?"Main":(String)xStack.peek();
		}
		
		public static String getMessage(String joinPoint) {
			String messageSignature = joinPoint.substring(10,joinPoint.lastIndexOf(')'));
			
			String components[] = messageSignature.split(" ");
			return components[1].substring(components[1].indexOf('.')+1) + " : "+ components[0];
		}
		
		public void addToStack(String joinPoint) {
			if(depth ==0) {
				id.push(idVal);
				idVal=1;
				String result = "";
				String resultFinal = "";
				String replaceRightClass = "";
				String replaceBothClass = "";
				
				String rightClassName = getClass(joinPoint);
				String leftClassName = getLeftClassName();
				
				result =  leftClassName + " ->" + rightClassName + ":" + id.printAll() + " " + getMessage(joinPoint) + "\n";
				
				if(rightClassName.equals("ConcreteSubject") && !leftClassName.equals("ConcreteSubject"))
				{
					replaceRightClass = result.replaceAll("ConcreteSubject", "TheEconomy");
					resultFinal = replaceRightClass + "activate " + "TheEconomy" + "\n";
					xStack.push("TheEconomy");
				}
				else if(rightClassName.equals("ConcreteSubject") && leftClassName.equals("ConcreteSubject"))
				{
					replaceBothClass = result.replaceAll("ConcreteSubject", "TheEconomy");
					resultFinal = replaceBothClass + "activate " + "TheEconomy" + "\n";
					xStack.push("TheEconomy");
				}
				else if(rightClassName.equals("ConcreteObserver") && result.contains("ConcreteObserver:5"))
				{
					replaceRightClass = result.replaceAll("ConcreteObserver", "Pessimist");
					resultFinal = replaceRightClass + "activate " + "Pessimist" + "\n";
					xStack.push("Pessimist");
				}
				else if(rightClassName.equals("ConcreteObserver") && result.contains("ConcreteObserver:6"))
				{
					replaceRightClass = result.replaceAll("ConcreteObserver", "Optimist");
					resultFinal = replaceRightClass + "activate " + "Optimist" + "\n";
					xStack.push("Optimist");
				}
				else
				{
					resultFinal =  result + "activate " + rightClassName + "\n";
					xStack.push(rightClassName);
				}
				
				 
				
				
				
				System.out.print(resultFinal);
				finalMsg += resultFinal;
				stackDepth += 1;
				
				
			}
		}
		
		public static String getClass(String joinPoint) {
			String messageSignature = joinPoint.substring(10,joinPoint.lastIndexOf(')'));
			String components[] = messageSignature.split(" ");
			return components[1].substring(0, components[1].indexOf('.'));
		}

		
		
		
	}

	
	AspectState aspectState = new AspectState();
	
	after(Object o) : executeAll(o){
		aspectState.removeFromStack();
	}
	before() : excuteConstructors() {
		aspectState.incrementConstructorCall();
	}
	
	before(Object o)  : executeAll(o) { 
		aspectState.addToStack(thisJoinPoint.toString());
	}
	after() :  excuteConstructors() {
		aspectState.decrementConstructorCall();
	}
	
	
	
	
}
