package evolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

public class CodeWriter {
	private Scope scope; 
	private List<String> annotations;
	public List<String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}

	@Test
	public void test() throws Exception {
		CodeWriter codeWriter = new CodeWriter();
		codeWriter.clazz("AnyClass")
		.annotation("@RestController")
		.method("anyMethod")
		.parameter("String", "string").parameter("Date", "date")
		.body("System.out.println(\"Hello World\");")
		.body("int i = 0;", "int j = 0;")
		.body("for (int k = 0; k < 10; k++) {")
		.body("i++;")
		.body("}")
		.returnType("Date")
		.method("anotherMethod")
		.method("theOtherMethod")
		.annotation("@Test")
		.annotation("@PostMapping(\"/post\")");
		System.out.println(codeWriter);
		codeWriter.writeJava("/Users/chenli/Desktop");
	}
	
	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public CodeWriter annotation(String... annotations) {
		return annotation(Arrays.asList(annotations));
	}
	
	public CodeWriter annotation(List<String> annotations) {
		if (this.scope == Scope.METHOD) {
			IMethod iMethod = this.getLastIMethod();
			for (String annotation : annotations) {
				iMethod.getAnnotations().add(annotation);
			}
		} else {
			this.annotations.addAll(annotations);
		}
		return this;
	}

	private String clazz;

	private List<IMethod> iMethods;

	private BufferedWriter bufferedWriter;

	public CodeWriter() {
		this.iMethods = new LinkedList<>();
		this.scope = Scope.CLASS;
		this.annotations = new LinkedList<>();
	}

	public String capitalizeFirstChar(String string) {
		try {
			return string.substring(0, 1).toUpperCase() + string.substring(1);
		} catch (Exception e) {// The class name consists of only one character.
			return string.toUpperCase();
		}
	}

	public CodeWriter clazz(String clazz) {
		this.clazz = capitalizeFirstChar(clazz);
		return this;
	}
	public String getClazz() {
		return clazz;
	}
	
	public List<IMethod> getiMethods() {
		return iMethods;
	}
	
	public IMethod getLastIMethod() {
		return this.iMethods.get(this.iMethods.size() - 1);
	}
	
	public CodeWriter method(String methodName) {
		IMethod imethod = new IMethod();
		imethod.setMethodName(methodName);
		this.iMethods.add(imethod);
		this.scope = Scope.METHOD;
		return this;
	}
	
	public CodeWriter parameter(String parameterClass, String parameterName) {
		IMethod iMethod = getLastIMethod();
		iMethod.getParameters().put(parameterClass, parameterName);
		return this;
	}
	
	public CodeWriter body(List<String> methodBody) {
		IMethod iMethod = getLastIMethod();
		iMethod.getMethodBody().addAll(methodBody);
		return this;
	} 
	
	public CodeWriter body(String... methodBody) {
		return body(Arrays.asList(methodBody));
	}
	
	public CodeWriter returnType(String returnType) {
		IMethod iMethod = getLastIMethod();
		iMethod.setReturnType(returnType);
		return this;
	}
	
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	public void setiMethods(List<IMethod> iMethods) {
		this.iMethods = iMethods;
	}
	
	@Override
	public String toString() {
		return "CodeWriter [clazz=" + clazz + ", iMethods=" + iMethods + "]";
	}
	
	private int indentCount = 0;
	
	public void write(String line) throws Exception {
		line = line.trim();// Avoid the "{ " or "} "case.
		if (line.lastIndexOf("}") == line.length() - 1) {
			this.indentCount--;
			if (this.indentCount < 0) {
				this.indentCount = 0;
			}
		}
		this.bufferedWriter.write(indent(this.indentCount) + line + "\n");
		if (line.lastIndexOf("{") == line.length() - 1) {
			this.indentCount++;
		} 
	}
	
	public String indent(int count) {
		StringBuilder indents = new StringBuilder();
		for (int i = 0; i < count; i++) {
			indents.append("    ");
		}
		return indents.toString();
	}
	
	public void writeJava(String filePath) throws Exception {
		// Open Resources
		File file = new File(filePath + "/" + this.clazz + ".java");
		FileWriter fileWriter = new FileWriter(file);
		this.bufferedWriter = new BufferedWriter(fileWriter);
		// Write Code
		// Class Annotations
		for (String annotation : this.annotations) {
			write(annotation);
		}
		// Class Signature
		write("public class " + this.clazz + " {");
		for (IMethod iMethod : this.iMethods) {
			// Method Annotations
			for (String annotation : iMethod.getAnnotations()) {
				write(annotation);
			}
			// Method Parameters
			StringBuilder parameters = new StringBuilder();
			for (Entry<String, String> entry : iMethod.getParameters().entrySet()) {
				parameters.append(entry.getKey() + " " + entry.getValue() + ", ");// Fix the ", " format.
			}
			// Method Signature
			write("public " + iMethod.getReturnType() + " " + iMethod.getMethodName() + "("
			+ (parameters.length() == 0 ? "" : parameters.substring(0, parameters.length() - 2).toString()) 
			+ ") {");
			// Method Body
			for (String code : iMethod.getMethodBody()) {
				write(code);
			}
			// Method End
			write("}");
			writeln();
		}
		write("}");
		// Close Resources
		this.bufferedWriter.close();
		fileWriter.close();
	}
	
	public void writeln() throws Exception {
		write("");
	}
}
