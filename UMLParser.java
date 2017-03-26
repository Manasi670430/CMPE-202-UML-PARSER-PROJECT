import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.plaf.metal.MetalToggleButtonUI;


import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;

public class UMLParser {


	private static HashMap<String, TypeDeclaration> hmap = new HashMap<String, TypeDeclaration>();
	//private static HashMap<String, TypeDeclaration> hmap1 = new HashMap<String, TypeDeclaration>();
	//private static HashMap<String, TypeDeclaration> hmap2 = new HashMap<String, TypeDeclaration>();


	private static List<FieldDeclaration> storeVariableDetails = new ArrayList<FieldDeclaration>();
	private static List<ClassDetails> classDetailList = new ArrayList<ClassDetails>();
	private static List<MethodDetails> methodDetaisList = new ArrayList<MethodDetails>();
	private static List<AttributeDetails> listOfFields = new ArrayList<AttributeDetails>();

	private static ClassTemplate ct = new ClassTemplate();

	private static ClassDetails cd = new ClassDetails();

	//public static HashMap<String, AttributeDetails> hashmp = new HashMap();
	//public static HashMap<String, ClassDetails> hashmp1 = new HashMap();
	//public static HashMap<String,> hashmpClassNameToMethodMap = new HashMap();
	//public static HashMap<String, MethodDetails> hashmp2 = new HashMap();


	private static HashMap<String, TypeDeclaration> typeCollectionMap = new HashMap<String, TypeDeclaration>();

	public static void main(String[] args) throws Exception {

		ArrayList<String> javaFileNames = new ArrayList<String>();
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
		//javaFileNames = listFilesForFolder(folder);
		CompilationUnit cu = null;
		//ClassTemplate ctemplate = null;
		
		 ClassDetails classDetailList1 = new ClassDetails();
		 List<MethodDetails> methodDetaisList1 = new ArrayList<MethodDetails>();
		 List<AttributeDetails> listOfFields1 = new ArrayList<AttributeDetails>();

		for (File individualFile : files) {
			//FileInputStream in = new FileInputStream(folderRoot + fileName);
			List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
			//ClassTemplate ctemplate = null;

			// parse the file
			cu = JavaParser.parse(individualFile);
			types = cu.getTypes();
			System.out.println("Types is"+types);
			classDetailList1  = GetTheClassDetails(types);
			listOfFields1 = GetVariableDetails(types);
			methodDetaisList1 = GetMethodDetails(types);
			
			//
			ct.setCd(classDetailList1);
			ct.setListOfFields(listOfFields1);
			ct.setListOfMethods(methodDetaisList1);
			ctList.add(ct);
			classTemplateMap.put(((ClassOrInterfaceDeclaration)types.get(0)).getName(), ct);
			//new ParseJavaToUML.ClassVisitor().visit(cu, umlParser);
		}

			// change the methods names and parameters
			// changeMethods(cu);

			// prints the changed compilation unit
			

			new PlantUMLInputStringBuilder().generatePlantUMlStringInput(classTemplateMap, imageFileName);

		}

	/*private static void GetMethodDetails(CompilationUnit cu) {
		// TODO Auto-generated method stub
		List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            // Go through all fields, methods, etc. in this type
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    
                	System.out.println("Method name is :"+((MethodDeclaration) member).getName());
                	
                	System.out.println(((MethodDeclaration) member).getModifiers());
                	int x=((MethodDeclaration) member).getModifiers();
                	
                	if(x==1){
                		System.out.println("Modifier of method is: "+ Modifier.toString(x));
                	}
                	if(x==2)
                	{
                		System.out.println("Modifier of method is : "+ Modifier.toString(x));
                	}
                	
            
                	
                	
                }
            }
        }
	}*/
		private static void GetMethodDetails(CompilationUnit cu) {
		// TODO Auto-generated method stub
		List<TypeDeclaration> types = cu.getTypes();
		
		MethodDetails md = new MethodDetails();
		for (TypeDeclaration type : types) {
			// Go through all fields, methods, etc. in this type
			List<BodyDeclaration> members = type.getMembers();
			for (BodyDeclaration member : members) {
				if (member instanceof MethodDeclaration) {

					System.out.println("Method name is :"+((MethodDeclaration) member).getName());
					md.Name = ((MethodDeclaration) member).getName();
					
					System.out.println(((MethodDeclaration) member).getModifiers());
					
					System.out.println(((MethodDeclaration) member).getType());
					md.returnType = ((MethodDeclaration) member).getType();
					
					int x=((MethodDeclaration) member).getModifiers();

					if(x==1){
						System.out.println("Modifier of method is: "+ Modifier.toString(x));
						md.Modifier = "Public";
					}
					if(x==2)
					{
						System.out.println("Modifier of method is : "+ Modifier.toString(x));
						md.Modifier = "Private";
					}

					List<Parameter> a = ((MethodDeclaration) member).getParameters();
					
					System.out.println("list of parameters is :"	+a);
					
					for(Parameter i: a)
					{
						System.out.println("list of parameters is :"	+i);
						md.Pramaters = i; 
					}

					hashmp2.put(md.Name,md);
					//System.out.println(((FieldDeclaration) member).getModifiers());

					//System.out.println(((TypeDeclaration) member).getModifiers());
					//String Mn = Modifier.toString(a);
					//System.out.println("Access Specifier of Class:" + Mn);

				}
			}
		}
	}
	private static void fetchVariable(HashMap<String, TypeDeclaration> hmap) {
		// TODO Auto-generated method stub
		Iterator<Entry<String, TypeDeclaration>> mapIterator = hmap.entrySet().iterator();
		System.out.print(hmap);
		List<FieldDeclaration> myVariableList = new ArrayList<FieldDeclaration>();

		for(FieldDeclaration fd: myVariableList){

			Type variable=fd.getType();
			System.out.println(variable);
			
			//Checking for the reference type of the variable whether primitive type or reference type
        	if(((FieldDeclaration) fd).getType() instanceof PrimitiveType)
			{
				//call generatePLantUML method to build the string input for plant UML
        		String plantUMLInputString = new PlantUMLInputStringBuilder().generatePlantUMlStringInput(fd);
			}
        	else if(((FieldDeclaration) fd).getType() instanceof ReferenceType)
        	{
				//call generatePLantUML method to build the string input for plant UML
        		
        	}


		}
	}
		private static void GetVariableDetails(CompilationUnit cu) {
		// TODO Auto-generated method stub
		
		List<TypeDeclaration> types = cu.getTypes();
		
		
		
		for (TypeDeclaration type : types) {
			// Go through all fields, methods, etc. in this type
			List<BodyDeclaration> members = type.getMembers();
			for (BodyDeclaration member : members) {
				if (member instanceof FieldDeclaration) {
					AttributeDetails attributedetails = new AttributeDetails();
					//FieldDeclaration
					System.out.println("Variable name is"+((FieldDeclaration) member).getVariables());
					
					String S = (((FieldDeclaration) member).getVariables()).toString();
					System.out.println("My S is" +S);
					String result = S.substring(S.indexOf("[") + 1, S.indexOf("]"));
					System.out.println(result);
					
					
					attributedetails.Name = result;
				
					attributedetails.Type = ((FieldDeclaration) member).getType();
					 
					//hashmp.put(attributedetails.Name, attributedetails);
					
					storeVariableDetails.add((FieldDeclaration)member);
					
					
					int x=((FieldDeclaration) member).getModifiers();
					if(x==1){
						System.out.println("Modifier of variable is: PublicVariable");
						attributedetails.Modifier = "Public";
					}
					if(x==2)
					{
						System.out.println("Modifier of variable is : PrivateVariable");
						attributedetails.Modifier = "Private";
					}
					
					hashmp.put(attributedetails.Name, attributedetails);
					
					System.out.println("Type of variable is "+((FieldDeclaration) member).getType());




					//System.out.println(((FieldDeclaration) member).getModifiers());

					//System.out.println(((TypeDeclaration) member).getModifiers());
					//String Mn = Modifier.toString(a);
					//System.out.println("Access Specifier of Class:" + Mn);

				}
			}
		}

		
	}

	private static void GetTheClassDetails(CompilationUnit cu) {
		//extracting ClassName
		List<TypeDeclaration> types1 = cu.getTypes();
		
		for (TypeDeclaration type : types1) {

			if (type instanceof ClassOrInterfaceDeclaration)
			
			{
				if (((ClassOrInterfaceDeclaration) type).isInterface()) {
					System.out.println("This is interface");
					break;
				} else {
					System.out.println("This is class");
					System.out.println("Class Name is:" + type.getName());
	

				}
			}
		}
	}

	private static void classOrInterface(CompilationUnit cu) {
		// TODO Auto-generated method stub

		List<TypeDeclaration> type1 = (List<TypeDeclaration>) cu.getTypes();

		

		for (TypeDeclaration t : type1) {
			ClassOrInterfaceDeclaration clv = (ClassOrInterfaceDeclaration) t;
			if (clv.isInterface()) {
				String classOrInterface = "Interface";

				hmap.put(classOrInterface, t);
				fetchVariable(hmap);
			} else {
				String classOrInterface = "class";

				hmap.put(classOrInterface, t);
				fetchVariable(hmap);
			}
		}
	}

	private static void GetModifiersDetails(CompilationUnit cu) {
		
		List<TypeDeclaration> types1 = cu.getTypes();
		for (TypeDeclaration type : types1) {
			if (type instanceof ClassOrInterfaceDeclaration) {
				int a = type.getModifiers();
				String M = Modifier.toString(a);
				System.out.println("Access Specifier of Class is:" + M);
			}
		}
 
	}
}
