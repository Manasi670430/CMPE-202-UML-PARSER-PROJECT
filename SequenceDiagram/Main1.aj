import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Stack;

import net.sourceforge.plantuml.SourceStringReader;

public aspect Main1 {
	pointcut executeAll(Object o) : !within(CustomStack) && !within(Main1) && target(o) && execution(public * *.*(..))
	 && !execution(*.new(..));

	pointcut excuteConstructors() : !within(CustomStack) && !within(Main1) && execution(*.new(..));	
	
	public static class AspectState {
		private String startUMLstring = "@startuml\n activate main";
		private String endUMLstring = "deactivate main \n@enduml";
		private CustomStack<Integer> id = new CustomStack<>();
		private int idVal = 1;
		
		
		public void decrementConstructorCall() {
			depth--;
		}
		
		public void popStackVal() {
			if(depth ==0) {
				updateStackVal();
				printSequenceDiagram();
			}
		}

		
		
    
		private void updateStackVal() {
			stackDepth--;
			
			finalMsg + = "deactivate ";
			if(!xStack.isEmpty()) {
				xStack.pop();
			}
			if(!id.isEmpty()) {
				idVal = (Integer)id.pop()+1;
			}
		}
		
		public String getLeftClassName() {
			return xStack.isEmpty()?"Main":(String)xStack.peek();
		}
		
	}
	public static String getClass(String joinPoint) {
			String messageSignature = joinPoint.substring(10,joinPoint.lastIndexOf(')'));
			String components[] = messageSignature.split(" ");
			return components[1].substring(0, components[1].indexOf('.'));
		}

		private static String getMetho(String joinPoint) {
			return joinPoint.substring(10,joinPoint.lastIndexOf(')'));
		}
		
		public static String getMessage(String joinPoint) {
			String messageSignature = joinPoint.substring(10,joinPoint.lastIndexOf(')'));
			
			String components[] = messageSignature.split(" ");
			return components[1].substring(components[1].indexOf('.')+1) + " : "+ components[0];
		}
	
	AspectState aspectState = new AspectState();

	before() : excuteConstructors() {
		aspectState.incrementConstructorCall();
	}
  	
	before(Object o)  : executeAll(o) { 
		aspectState.pushStackVal(thisJoinPoint.toString());
	}
	
	after(Object o) : executeAll(o){
		aspectState.popStackVal();
	
	after() :  excuteConstructors() {
		aspectState.decrementConstructorCall();
	}

	}
}
