import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
//import java.util.Map.Entry;
//import javax.swing.plaf.metal.MetalToggleButtonUI;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
//import japa.parser.ast.type.PrimitiveType;
//import japa.parser.ast.type.ReferenceType;
//import japa.parser.ast.type.Type;

public class UMLParser {

	public static void main(String[] args) throws Exception {
		ClassTemplate ct = new ClassTemplate();
		String folderRoot = args[0];
		String imageFileName = args[1];
		final File folder = new File(folderRoot);
		ArrayList<File> files = new ArrayList<File>();

		File[] fileArray = folder.listFiles();
		for(File file : fileArray)
		{
			if(file.isFile() && file.getName().endsWith(".java"))
			{
				files.add(file);
			}
		}
		HashMap<String, ClassTemplate> classTemplateMap = new HashMap<String,ClassTemplate>();
		List<ClassTemplate> ctList = new ArrayList<ClassTemplate>();
		List<MethodDetails> methodDetaisList1 = new ArrayList<MethodDetails>();
		List<AttributeDetails> listOfFields1 = new ArrayList<AttributeDetails>();

		CompilationUnit cu = null;
		ClassDetails classDetails = new ClassDetails();

		for (File individualFile : files) {
			List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
			cu = JavaParser.parse(individualFile);
			types = cu.getTypes();
			ct = getClassTemplate(types);
			ctList.add(ct);
			classTemplateMap.put(((ClassOrInterfaceDeclaration)types.get(0)).getName(), ct);


		}
		new PlantUMLInputStringBuilder().generatePlantUMlStringInput(classTemplateMap, imageFileName, ctList);
	}
	public static ClassTemplate getClassTemplate(List<TypeDeclaration> types)
	{
		ClassDetails cd = new ClassDetails();
		ClassTemplate ct = new ClassTemplate();
		List<AttributeDetails> listOfFields = new ArrayList<AttributeDetails>();
		List<MethodDetails> methodDetaisList = new ArrayList<MethodDetails>();
		List<ConstructorDetails> constructorDetailsList = new ArrayList<ConstructorDetails>();
		List<ClassOrInterfaceType> extendsList = new ArrayList<ClassOrInterfaceType>();
		List<ClassOrInterfaceType> implementsList = new ArrayList<ClassOrInterfaceType>();

		for (TypeDeclaration type : types) {
			if (type instanceof ClassOrInterfaceDeclaration)
			{
				extendsList = ((ClassOrInterfaceDeclaration) type).getExtends();
				implementsList = ((ClassOrInterfaceDeclaration) type).getImplements();

				if(implementsList!=null)
				{
					cd.setImplementsList(implementsList);
				}
				if(extendsList!=null)
				{
					cd.setExtendsList(extendsList);
				}
				if (((ClassOrInterfaceDeclaration) type).isInterface()) 
				{
					cd.setInterface(true);
					cd.setName(type.getName().toString());
				} 
				else 
				{
					cd.setInterface(false);
					cd.setName(type.getName().toString());
				}

			}
			List<BodyDeclaration> members = type.getMembers();
			for (BodyDeclaration member : members) 
			{
				if (member instanceof FieldDeclaration) 
				{
					AttributeDetails attributedetails = new AttributeDetails();
					String S = (((FieldDeclaration) member).getVariables()).toString();
					String result = S.substring(S.indexOf("[") + 1, S.indexOf("]"));
					int x=((FieldDeclaration) member).getModifiers();
					if(x==1)
					{
						attributedetails.setModifier("Public");
						attributedetails.setName(result);
						attributedetails.setType(((FieldDeclaration) member).getType());
					}
					if(x==2)
					{
						attributedetails.setModifier("Private");
						attributedetails.setName(result);
						attributedetails.setType(((FieldDeclaration) member).getType());
					}
					if(Modifier.toString(x).equals("protected"))
					{
						attributedetails.setModifier("protected");
						attributedetails.setName(result);
						attributedetails.setType(((FieldDeclaration) member).getType());
					}
					listOfFields.add(attributedetails);
				}
				if (member instanceof MethodDeclaration) 
				{

					MethodDetails md = new MethodDetails();
					String methodGetterName = ((MethodDeclaration) member).getName();
					String className = type.getName();
					String getMethodFiledName = methodGetterName.substring(3);
					boolean getterSetterMethodMatch = false;

					boolean getMethodExist = methodGetterName.substring(0, 3).equals("get");
					boolean setMethodExist = methodGetterName.substring(0, 3).equals("set");

					if(getMethodExist || setMethodExist)
					{
						for(AttributeDetails eachAttribute : listOfFields)
						{
							if(getMethodFiledName.toLowerCase().equals(eachAttribute.getName()))
							{
								eachAttribute.setModifier("public");
								getterSetterMethodMatch = true;
								break;
							}

						}
					}
					if(!getterSetterMethodMatch)
					{
						md.setName(((MethodDeclaration) member).getName());
						md.setReturnType(((MethodDeclaration) member).getType());

						int x=((MethodDeclaration) member).getModifiers();
						if(x==1){
							md.setModifier(Modifier.toString(x));
						}
						if(x==2)
						{
							md.setModifier(Modifier.toString(x));
						}
						if(Modifier.toString(x).equals("public abstract"))
						{
							md.setModifier(Modifier.toString(x));
						}
						if(Modifier.toString(x).equals("public static"))
						{
							md.setModifier(Modifier.toString(x));
							List bodyString = ((BlockStmt)((MethodDeclaration) member).getBody()).getStmts();
							md.setBodyElements(bodyString);
						}
						List<Parameter> paramList = ((MethodDeclaration) member).getParameters();
						if(paramList!=null)
						{
							for(Parameter eachParam: paramList)
							{
								md.setParamaters(eachParam);
							}
						}
						methodDetaisList.add(md);
					}
				}
				if(member instanceof ConstructorDeclaration)
				{
					ConstructorDetails consDetails = new ConstructorDetails();
					consDetails.setConstructorName(((ConstructorDeclaration) member).getName());
					consDetails.setModifier("public");
					List<Parameter> paramList = ((ConstructorDeclaration) member).getParameters();
					if(paramList!=null)
					{
						for(Parameter eachParam: paramList)
						{
							consDetails.setConstructorParams(eachParam);
						}
					}					
					constructorDetailsList.add(consDetails);
				}
			}
			ct.setListOfFields(listOfFields);
			ct.setListOfMethods(methodDetaisList);
			ct.setListOfConstructor(constructorDetailsList);
			ct.setCd(cd);
		}
		return ct;
	}
}
