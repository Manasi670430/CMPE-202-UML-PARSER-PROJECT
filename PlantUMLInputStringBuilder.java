import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import japa.parser.ast.body.FieldDeclaration;
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
		finalString1 = a + finalString1 + b;
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
		if(cDetails.isInterface)
		{
			umlClassInterfaceBody += "<<interface>> interface"+ cDetails.getName() +"\n";
		}
		else
		{
			umlClassInterfaceBody += "Class "+ cDetails.getName() +"{\n";
		}
		
		List<AttributeDetails> attributeDetails = cTemplate.getListOfFields();
		
		for(AttributeDetails eachAttribute : attributeDetails)
		{
			if(eachAttribute.getType() instanceof ReferenceType)
			{
				if(eachAttribute.getType().toString().equals("int[]") || eachAttribute.getType().toString().equals("String[]"))
				{
					if(eachAttribute.getModifier().equals("Private"))
					{
						umlClassInterfaceBody += "-"+ eachAttribute.getName() + ":" + ((ReferenceType)eachAttribute.getType()).getType() + "[*]" +"\n";
					}
					if(eachAttribute.getModifier().equals("Public"))
					{
						umlClassInterfaceBody += "+" + eachAttribute.getName() + ":" + ((ReferenceType)eachAttribute.getType()).getType() + "[*]" + "\n";																				
					}
				}
			}
			if(eachAttribute.getType() instanceof PrimitiveType)
			{
				if(eachAttribute.getModifier().equals("Private"))
				{
					umlClassInterfaceBody += "-"+ eachAttribute.getName() + ":" + eachAttribute.getType() + "\n";
				}
				if(eachAttribute.getModifier().equals("Public"))
				{
					umlClassInterfaceBody += "+" + eachAttribute.getName() + ":" + eachAttribute.getType() + "\n";																				
				}
			}
		}
		
		
		
		return umlClassInterfaceBody;
	}
	


}



