import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import japa.parser.ast.body.FieldDeclaration;
import net.sourceforge.plantuml.SourceStringReader;


/**
 * @author Manasi Milind Joshi
 *
 */
public class PlantUMLInputStringBuilder {

	//OutputStream png = null;
	String source = "@startuml\n" ;
	//attributeDetail
	//source += "@enduml\n";
	
	String imputStringForPlantUML = ""; 
	
	public void generatePlantUMlStringInput(HashMap<String, ClassTemplate> classTemplateMap, String imageFileName)
	{
		
		
		
		Iterator<Entry<String, ClassTemplate>> it = classTemplateMap.entrySet().iterator();
		
		
		while(it.hasNext())
		{
			ClassTemplate classTemplate = it.next().getValue();
			
			
			String classorInterfaceName = classTemplate.getCd().getName();
			
			if(classTemplate.getCd().isInterface)
			{
				imputStringForPlantUML = imputStringForPlantUML + "interface " + classorInterfaceName + "<<interface>>" + "{\n"; 
			}
			else
			{
				imputStringForPlantUML = imputStringForPlantUML + "class " + classorInterfaceName +  "{\n";
			}
			
			imputStringForPlantUML += "}";
			
			
		}
		
		
		String finalString = source + imputStringForPlantUML + "\n@enduml";
		SourceStringReader sr = new SourceStringReader(finalString);
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
	
		//return null;

	}

}
