import java.util.List;

import japa.parser.ast.body.VariableDeclarator;

public class AttributeDetails {

	String Name;
	japa.parser.ast.type.Type Type;
	String Modifier;
	
	
	
	
	
	public AttributeDetails() {
	
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public japa.parser.ast.type.Type getType() {
		return Type;
	}
	public void setType(japa.parser.ast.type.Type type) {
		Type = type;
	}
	public String getModifier() {
		return Modifier;
	}
	public void setModifier(String modifier) {
		Modifier = modifier;
	}
	
	

}
