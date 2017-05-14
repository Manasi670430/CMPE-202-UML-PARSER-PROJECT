import java.util.Stack;

public class InformationStack<E> extends Stack<E>{

	public InformationStack(){}
	private static final long serialVersionUID = 1L;



	private String combineElementsIntoSingleValue(int len) {
		StringBuffer resultStringBuffer= new StringBuffer();
		for(int i=0;i<len;i++){
			resultStringBuffer.append(elementData[i]);
			if(i!=(len-1)) {
				resultStringBuffer.append( ".");
			}
		}
		return resultStringBuffer.toString();
	}

	private int fetchIndex(int len) {
		for(int i=0;i<len;i++){
			if(elementData[i] == null)
			{
				len =i;
				break;
			}
		}
		return len;
	}
}
