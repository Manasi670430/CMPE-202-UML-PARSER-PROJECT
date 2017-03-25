import japa.parser.ast.body.Parameter;
import japa.parser.ast.type.Type;

public class MethodDetails {
String name;
Type returnType;
String modifier;
Boolean isConstructor;
Parameter paramaters;
/**
 * @return the name
 */
public String getName() {
	return name;
}
/**
 * @param name the name to set
 */
public void setName(String name) {
	this.name = name;
}
/**
 * @return the returnType
 */
public Type getReturnType() {
	return returnType;
}
/**
 * @param returnType the returnType to set
 */
public void setReturnType(Type returnType) {
	this.returnType = returnType;
}
/**
 * @return the modifier
 */
public String getModifier() {
	return modifier;
}
/**
 * @param modifier the modifier to set
 */
public void setModifier(String modifier) {
	this.modifier = modifier;
}
/**
 * @return the isConstructor
 */
public Boolean getIsConstructor() {
	return isConstructor;
}
/**
 * @param isConstructor the isConstructor to set
 */
public void setIsConstructor(Boolean isConstructor) {
	this.isConstructor = isConstructor;
}
/**
 * @return the paramaters
 */
public Parameter getParamaters() {
	return paramaters;
}
/**
 * @param paramaters the paramaters to set
 */
public void setParamaters(Parameter paramaters) {
	this.paramaters = paramaters;
}
}
