import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandleProxies;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import net.sourceforge.plantuml.SourceStringReader;
/**
 * @author Manasi Milind Joshi
 *
 */
public class PlantUMLInputStringBuilder {
	String finalString="";
	String finalString1="";
	String a="@startuml\n";
	String b="@enduml";

	List<ConnectionDetails> connectionDetailList = new ArrayList<ConnectionDetails>();

	public void generatePlantUMlStringInput(HashMap<String, ClassTemplate> classTemplateMap, String imageFileName, List<ClassTemplate> ctList)
	{
		String source = "@startuml\n" ;
		Set set = classTemplateMap.entrySet();
		Iterator iterator = set.iterator();

		while(iterator.hasNext()) {
			String inputStringForPlantUML= "" ;
			Map.Entry mentry = (Map.Entry)iterator.next();
			ClassTemplate cTemplate = (ClassTemplate)mentry.getValue();
			inputStringForPlantUML += generateUMLClassOrInterfaceBody(cTemplate, ctList);
			finalString += inputStringForPlantUML + "}\n" ;
		}

		Iterator iterator1 = set.iterator();
		while(iterator1.hasNext()) {
			Map.Entry mentry = (Map.Entry)iterator1.next();
			ClassTemplate cTemplate = (ClassTemplate)mentry.getValue();
			if(!cTemplate.getCd().isInterface())
			{
				finalString += generateUMLConnections(cTemplate,ctList);
			}
		}
		finalString1 += finalString + "\n" ;
		System.out.println(finalString1);
		finalString1 = a + "skinparam classAttributeIconSize 0\n" + finalString1 + b;
		SourceStringReader sr = new SourceStringReader(finalString1);
		FileOutputStream fos = null;
		try {
			fos =  new FileOutputStream(imageFileName + ".png");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sr.generateImage(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String generateUMLClassOrInterfaceBody(ClassTemplate cTemplate, List<ClassTemplate> ctList)
	{
		String umlClassInterfaceBody = "";

		ClassDetails cDetails = cTemplate.getCd();
		if(cDetails.isInterface())
		{
			umlClassInterfaceBody += "interface "+ cDetails.getName() +"<<interface>>"+"{\n";
		}
		else
		{
			umlClassInterfaceBody += "Class "+ cDetails.getName() +"{\n";
		}

		List<AttributeDetails> attributeDetails = cTemplate.getListOfFields();
		if(attributeDetails!=null)
		{
			for(AttributeDetails eachAttribute : attributeDetails)
			{
				if(eachAttribute.getType() instanceof ReferenceType)
				{
					if(eachAttribute.getType().toString().equals("int[]") || eachAttribute.getType().toString().equals("String[]"))
					{
						if(eachAttribute.getModifier().equalsIgnoreCase("Private"))
						{
							umlClassInterfaceBody += "-"+ eachAttribute.getName() + ":" + ((ReferenceType)eachAttribute.getType()).getType() + "[*]" +"\n";
						}
						if(eachAttribute.getModifier().equalsIgnoreCase("Public"))
						{
							umlClassInterfaceBody += "+" + eachAttribute.getName() + ":" + ((ReferenceType)eachAttribute.getType()).getType() + "[*]" + "\n";																				
						}
					}
					if(eachAttribute.getType().toString().equals("String"))
					{
						if(eachAttribute.getModifier().equalsIgnoreCase("Private"))
						{
							umlClassInterfaceBody += "-"+ eachAttribute.getName() + ":" + ((ReferenceType)eachAttribute.getType()).getType() +"\n";
						}
						if(eachAttribute.getModifier().equalsIgnoreCase("Public"))
						{
							umlClassInterfaceBody += "+" + eachAttribute.getName() + ":" + ((ReferenceType)eachAttribute.getType()).getType() + "\n";																				
						}
					}
				}
				if(eachAttribute.getType() instanceof PrimitiveType)
				{
					if(eachAttribute.getModifier().equalsIgnoreCase("Private"))
					{
						umlClassInterfaceBody += "-"+ eachAttribute.getName() + ":" + eachAttribute.getType() + "\n";
					}
					if(eachAttribute.getModifier().equalsIgnoreCase("Public"))
					{
						umlClassInterfaceBody += "+" + eachAttribute.getName() + ":" + eachAttribute.getType() + "\n";																				
					}
				}
			}
		}

		List<MethodDetails> methodDetailsList = cTemplate.getListOfMethods();
		if(methodDetailsList!=null)
		{
			for(MethodDetails eachMethod : methodDetailsList)
			{
				Parameter params = eachMethod.getParamaters();

				if(eachMethod.getModifier().equals("public"))
				{
					if(params!=null)
					{
						umlClassInterfaceBody += "+"+ eachMethod.getName() + "(" + params.getId().toString() + ":" + params.getType().toString() + ") :" + eachMethod.getReturnType().toString() + "\n";
					}
					else
					{
						umlClassInterfaceBody += "+"+ eachMethod.getName() + "() :" + eachMethod.getReturnType().toString() + "\n";
					}
				}
				if(eachMethod.getModifier().equals("public abstract"))
				{
					if(params!=null)
					{
						umlClassInterfaceBody += "+"+ eachMethod.getName() + "(" + params.getId().toString() + ":" + params.getType().toString() + ") :" + eachMethod.getReturnType().toString() + "\n";
					}
					else
					{
						umlClassInterfaceBody += "+"+ eachMethod.getName() + "() :" + eachMethod.getReturnType().toString() + "\n";
					}
				}
				if(eachMethod.getModifier().equals("public static"))
				{
					if(params!=null)
					{
						umlClassInterfaceBody += "+"+ eachMethod.getName() + "(" + params.getId().toString() + ":" + params.getType().toString() + ") :" + eachMethod.getReturnType().toString() + "\n";
					}
				}
			}
		}

		List<ConstructorDetails> consDetails = cTemplate.getListOfConstructor();
		if(consDetails!=null)
		{
			for(ConstructorDetails eachConstructor : consDetails)
			{
				Parameter params = eachConstructor.getConstructorParams();
				if(params!=null)
				{
					umlClassInterfaceBody += "+"+ eachConstructor.getConstructorName() + "(" + params.getId().toString() + ":" + params.getType().toString() + ")" +  "\n";
				}
				else
				{
					umlClassInterfaceBody += "+"+ eachConstructor.getConstructorName() + "()" + "\n";
				}
			}
		}
		return umlClassInterfaceBody;
	}

	public String generateUMLConnections(ClassTemplate cTemplate, List<ClassTemplate> ctList)
	{
		String umlConnectionsString = "";
		List<AttributeDetails> attributeDetailsList = cTemplate.getListOfFields();
		List<MethodDetails> methodDetailsList = cTemplate.getListOfMethods();
		List<ConstructorDetails> constDetailsList = cTemplate.getListOfConstructor();
		if(attributeDetailsList!=null)
		{
			for(AttributeDetails eachAttribute : attributeDetailsList)
			{
				if(eachAttribute.getType() instanceof ReferenceType)
				{
					if(((ReferenceType)eachAttribute.getType()).getType() instanceof ClassOrInterfaceType)
					{
						String typeCollectionOrOnlyClass = ((ClassOrInterfaceType)((ReferenceType)eachAttribute.getType()).getType()).getName().toString();
						if(!typeCollectionOrOnlyClass.equals("String"))
						{
							String associationEndClass = "";
							String associationStartClass = "";
							if(typeCollectionOrOnlyClass.equals("Collection"))
							{
								List<Type> associationEndClassTypeList = ((ClassOrInterfaceType)((ReferenceType)eachAttribute.getType()).getType()).getTypeArgs();
								associationEndClass = associationEndClassTypeList.get(0).toString();
								associationStartClass = cTemplate.getCd().getName().toString();
								boolean connectionExistFlag = false;
								for(ConnectionDetails eachConnection : connectionDetailList)
								{
									boolean associationStartClassExist = associationStartClass.equals(eachConnection.getConnectionStart()) || associationStartClass.equals(eachConnection.getConnectionEnd());
									boolean associationEndClassExist = associationEndClass.equals(eachConnection.getConnectionEnd()) || associationEndClass.equals(eachConnection.getConnectionStart());
									if(associationStartClassExist && associationEndClassExist)
									{
										boolean connectionExist = eachConnection.getConnetionType()!=null && eachConnection.getConnetionType().toString().equals("ASSOCIATION");
										if(connectionExist)
										{
											connectionExistFlag = true;
										}
									}
								}
								if(!connectionExistFlag)
								{
									ConnectionDetails connectionDetails = new ConnectionDetails();
									connectionDetails.setConnectionStart(associationStartClass);
									connectionDetails.setConnectionEnd(associationEndClass);
									connectionDetails.setConnetionType("ASSOCIATION");
									connectionDetailList.add(connectionDetails);

									umlConnectionsString += associationEndClass + "\"*\"" + " -- " + "\"1\"" + cTemplate.getCd().getName() + "\n";
								}
							}
							else
							{
								boolean connectionExistFlag = false;
								associationEndClass = ((ClassOrInterfaceType)((ReferenceType)eachAttribute.getType()).getType()).getName().toString();
								associationStartClass = cTemplate.getCd().getName().toString();

								for(ConnectionDetails eachConnection : connectionDetailList)
								{
									boolean associationStartClassExist = associationStartClass.equals(eachConnection.getConnectionStart()) || associationStartClass.equals(eachConnection.getConnectionEnd());
									boolean associationEndClassExist = associationEndClass.equals(eachConnection.getConnectionEnd()) || associationEndClass.equals(eachConnection.getConnectionStart());
									if(associationStartClassExist && associationEndClassExist)
									{
										boolean connectionExist = eachConnection.getConnetionType()!=null && eachConnection.getConnetionType().toString().equals("ASSOCIATION");
										if(connectionExist)
										{
											connectionExistFlag = true;
										}
									}
								}
								if(!connectionExistFlag)
								{
									ConnectionDetails connectionDetails = new ConnectionDetails();
									connectionDetails.setConnectionStart(associationStartClass);
									connectionDetails.setConnectionEnd(associationEndClass);
									connectionDetails.setConnetionType("ASSOCIATION");
									connectionDetailList.add(connectionDetails);
									umlConnectionsString += associationEndClass + "\"1\"" + " -- " + "\"1\"" + cTemplate.getCd().getName() + "\n";
								}
							}
						}
					}
				}
			}
		}
		if(constDetailsList!=null)
		{
			for(ConstructorDetails eachM : constDetailsList)
			{
				Parameter methodParams = eachM.getConstructorParams();
				for(ClassTemplate eachClass : ctList)
				{
					if(methodParams!=null){
						if(methodParams.getType().toString().equals(eachClass.getCd().getName()))
						{
							if(eachClass.getCd().isInterface())
							{
								boolean connectionExistFlag = false;
								String dependencyDestinationClass = methodParams.getType().toString();
								String dependencySourceClass = cTemplate.getCd().getName();
								for(ConnectionDetails eachConnection : connectionDetailList)
								{
									boolean dependencySourceClassExist = dependencySourceClass.equals(eachConnection.getConnectionStart()) || dependencySourceClass.equals(eachConnection.getConnectionEnd());
									boolean dependencyDestinationClassExist = dependencyDestinationClass.equals(eachConnection.getConnectionEnd()) || dependencyDestinationClass.equals(eachConnection.getConnectionStart());
									if(dependencySourceClassExist && dependencyDestinationClassExist)
									{
										boolean connectionExist = eachConnection.getConnetionType()!=null && eachConnection.getConnetionType().toString().equals("DEPENDENCY");
										if(connectionExist)
										{
											connectionExistFlag = true;
										}
									}
								}
								if(!connectionExistFlag)
								{
									ConnectionDetails connectionDetails = new ConnectionDetails();
									connectionDetails.setConnectionStart(dependencySourceClass);
									connectionDetails.setConnectionEnd(dependencyDestinationClass);
									connectionDetails.setConnetionType("DEPENDENCY");
									connectionDetailList.add(connectionDetails);
									umlConnectionsString += dependencyDestinationClass + "<.. " + dependencySourceClass + "\n";
								}
							}
						}
					}
				}
			}

		}

		if(methodDetailsList!=null)
		{
			for(MethodDetails eachMethod : methodDetailsList)
			{
				Parameter methodParams = eachMethod.getParamaters();
				if(eachMethod.getName().equals("main"))
				{
					String bodyString = ((ExpressionStmt) eachMethod.getBodyElements().get(0)).toString();
					System.out.println("BODYSTRING"+bodyString);
					String bodyStmtArr[] = bodyString.split("=");
					String interfaceName = bodyStmtArr[0].split(" ")[0];
					for(ClassTemplate eachClass : ctList)
					{
						if(interfaceName.equals(eachClass.getCd().getName()))
						{
							if(eachClass.getCd().isInterface())
							{
								boolean connectionExistFlag = false;
								String dependencyDestinationClass = interfaceName;
								String dependencySourceClass = cTemplate.getCd().getName();
								for(ConnectionDetails eachConnection : connectionDetailList)
								{
									boolean dependencySourceClassExist = dependencySourceClass.equals(eachConnection.getConnectionStart()) || dependencySourceClass.equals(eachConnection.getConnectionEnd());
									boolean dependencyDestinationClassExist = dependencyDestinationClass.equals(eachConnection.getConnectionEnd()) || dependencyDestinationClass.equals(eachConnection.getConnectionStart());
									if(dependencySourceClassExist && dependencyDestinationClassExist)
									{
										boolean connectionExist = eachConnection.getConnetionType()!=null && eachConnection.getConnetionType().toString().equals("DEPENDENCY");
										if(connectionExist)
										{
											connectionExistFlag = true;
										}
									}
								}
								if(!connectionExistFlag)
								{
									ConnectionDetails connectionDetails = new ConnectionDetails();
									connectionDetails.setConnectionStart(dependencySourceClass);
									connectionDetails.setConnectionEnd(dependencyDestinationClass);
									connectionDetails.setConnetionType("DEPENDENCY");
									connectionDetailList.add(connectionDetails);
									umlConnectionsString += dependencyDestinationClass + "<.." + dependencySourceClass + "\n";
								}
							}
						}
					}
				}
				for(ClassTemplate eachClass : ctList)
				{
					if(methodParams!=null)
					{
						if(methodParams.getType().toString().equals(eachClass.getCd().getName()))
						{
							if(eachClass.getCd().isInterface())
							{
								boolean connectionExistFlag = false;
								String dependencyDestinationClass = methodParams.getType().toString();
								String dependencySourceClass = cTemplate.getCd().getName();
								for(ConnectionDetails eachConnection : connectionDetailList)
								{
									boolean dependencySourceClassExist = dependencySourceClass.equals(eachConnection.getConnectionStart()) || dependencySourceClass.equals(eachConnection.getConnectionEnd());
									boolean dependencyDestinationClassExist = dependencyDestinationClass.equals(eachConnection.getConnectionEnd()) || dependencyDestinationClass.equals(eachConnection.getConnectionStart());
									if(dependencySourceClassExist && dependencyDestinationClassExist)
									{
										boolean connectionExist = eachConnection.getConnetionType()!=null && eachConnection.getConnetionType().toString().equals("DEPENDENCY");
										if(connectionExist)
										{
											connectionExistFlag = true;
										}
									}
								}
								if(!connectionExistFlag)
								{
									ConnectionDetails connectionDetails = new ConnectionDetails();
									connectionDetails.setConnectionStart(dependencySourceClass);
									connectionDetails.setConnectionEnd(dependencyDestinationClass);
									connectionDetails.setConnetionType("DEPENDENCY");
									connectionDetailList.add(connectionDetails);
									umlConnectionsString += dependencyDestinationClass + "<.." + dependencySourceClass + "\n";
								}
							}
						}
					}
				}
			}
			List<ClassOrInterfaceType> extendsList = cTemplate.getCd().getExtendsList();
			List<ClassOrInterfaceType> implementsList = cTemplate.getCd().getImplementsList();
			if(extendsList!=null)
			{
				for(ClassOrInterfaceType extendDef : extendsList)
				{
					String extendSourceClass = "";
					String extendDestinationClass = "";
					extendDestinationClass = cTemplate.getCd().getExtendsList().get(0).toString();
					extendSourceClass = cTemplate.getCd().getName().toString();

					ConnectionDetails connectionDetails = new ConnectionDetails();
					connectionDetails.setConnectionStart(extendSourceClass);
					connectionDetails.setConnectionEnd(extendDestinationClass);
					connectionDetails.setConnetionType("EXTENDS");
					connectionDetailList.add(connectionDetails);
					umlConnectionsString += extendDestinationClass + "<|-- " + extendSourceClass + "\n";
				}
			}
			if(implementsList!=null)
			{
				for(ClassOrInterfaceType implementsDef : implementsList)
				{
					String implementsSourceClass = "";
					String implementsDestinationClass = "";

					implementsDestinationClass = implementsDef.toString();
					implementsSourceClass = cTemplate.getCd().getName().toString();

					ConnectionDetails connectionDetails = new ConnectionDetails();
					connectionDetails.setConnectionStart(implementsSourceClass);
					connectionDetails.setConnectionEnd(implementsDestinationClass);
					connectionDetails.setConnetionType("IMPLEMENTS");
					connectionDetailList.add(connectionDetails);
					umlConnectionsString += implementsDestinationClass + "<|.. " + implementsSourceClass + "\n";
				}
			}
		}
		return umlConnectionsString;
	}
}
