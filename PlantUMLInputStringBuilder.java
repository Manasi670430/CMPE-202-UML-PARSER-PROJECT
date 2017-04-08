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

	public void generatePlantUMlStringInput(HashMap<String, ClassTemplate> classTemplateMap, String imageFileName)
	{


		String source = "@startuml\n" ;
		Set set = classTemplateMap.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) {
			String inputStringForPlantUML= "" ;
			Map.Entry mentry = (Map.Entry)iterator.next();
			//imputStringForPlantUML ="Class "+ mentry.getKey()+"\n";


			ClassTemplate cTemplate = (ClassTemplate)mentry.getValue();

			inputStringForPlantUML += generateUMLClassOrInterfaceBody(cTemplate);

			finalString += inputStringForPlantUML + "}\n" ;


		}

		Iterator iterator1 = set.iterator();
		while(iterator1.hasNext()) {
			Map.Entry mentry = (Map.Entry)iterator1.next();
			ClassTemplate cTemplate = (ClassTemplate)mentry.getValue();

			finalString += generateUMLConnections(cTemplate);

		}






		finalString1 += finalString + "\n" ;
		//finalString + "}\n";
		//finalString1 =	a + finalString + b + "}\n"; 

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



	public String generateUMLClassOrInterfaceBody(ClassTemplate cTemplate)
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
						umlClassInterfaceBody += "-"+ eachMethod.getName() + "() :" + eachMethod.getReturnType().toString() + "\n";
					}
					
					
				}
				
			}
		}
		return umlClassInterfaceBody;
	}

	public String generateUMLConnections(ClassTemplate cTemplate)
	{
		String umlConnectionsString = "";
		List<AttributeDetails> attributeDetailsList = cTemplate.getListOfFields();
		List<MethodDetails> methodDetailsList = cTemplate.getListOfMethods();

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
								
							}
							else
							{
								boolean connectionExistFlag = false;
								associationEndClass = ((ClassOrInterfaceType)((ReferenceType)eachAttribute.getType()).getType()).getName().toString();
								associationStartClass = cTemplate.getCd().getName().toString();

							
							}
						}
					}
				}
			}
		}
		
	
		}
		
	
}





