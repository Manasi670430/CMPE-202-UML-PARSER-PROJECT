import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import java.io.FileInputStream;
import java.lang.reflect.Modifier;
import java.util.List;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;

public class UMLParser {
	
	private static HashMap<String, TypeDeclaration> hmap = new HashMap<String, TypeDeclaration>();
	private static List<FieldDeclaration> storeVariableDetails = new ArrayList<FieldDeclaration>();
	
	public static void main(String[] args) throws Exception {
		// creates an input stream for the file to be parsed
		FileInputStream in = new FileInputStream("C:/Users/Manasi Milind Joshi/workspace/uml-parser-test-1/src/A.java");
		// parse the file
				
		CompilationUnit cu = JavaParser.parse(in);

		
		// prints the changed compilation unit
		System.out.println(cu.toString());
		GetTheClassDetails(cu);
		GetVariableDetails(cu);
		GetMethodDetails(cu);
	}

	private static void GetMethodDetails(CompilationUnit cu) {
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
                    
                	System.out.println("Variable name is"+((FieldDeclaration) member).getVariables());
                	
                	
                	int x=((FieldDeclaration) member).getModifiers();
                	if(x==1){
                		System.out.println("Modifier of variable is: PublicVariable");
                	}
                	if(x==2)
                	{
                		System.out.println("Modifier of variable is : PrivateVariable");
                	}
                	
                	System.out.println("Type of variable is "+((FieldDeclaration) member).getType());
                	
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
