package evolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class CodeWriter {
	private Scope scope; 
	private List<String> annotations;
	private String clazz;
	private List<IField> iFields;
	private String extend;
	private List<String> imports;
	private String packet;
	
	public CodeWriter packet(String packet) {
		this.packet = packet;
		return this;
	}
	
	public String getPacket() {
		return packet;
	}

	public void setPacket(String packet) {
		this.packet = packet;
	}

	public List<String> getImports() {
		return imports;
	}

	public void setImports(List<String> imports) {
		this.imports = imports;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public List<String> getImplement() {
		return implement;
	}

	public void setImplement(List<String> implement) {
		this.implement = implement;
	}

	private List<String> implement;
	
	public CodeWriter field(Field field) {
		return field(field.getName());
	}
	
	public CodeWriter field(String fieldName) {
		IField iField = new IField();
		iField.setFieldName(fieldName);
		this.iFields.add(iField);
		this.scope = Scope.FIELD;
		return this;
	}
	
	public CodeWriter type(Class<?> type) {
		addImport(type);
		return type(type.getSimpleName());
	}
	
	public CodeWriter type(String type) {
		IField iField = getLastIField();
		iField.setType(type);
		return this;
	}
	
	public List<IField> getiFields() {
		return iFields;
	}

	public void setiFields(List<IField> iFields) {
		this.iFields = iFields;
	}

	private List<IMethod> iMethods;
	private BufferedWriter bufferedWriter;
	private int indentCount = 0;

	public CodeWriter() {
		this.iMethods = new LinkedList<>();
		this.scope = Scope.CLASS;
		this.annotations = new LinkedList<>();
		this.iFields = new LinkedList<>();
		this.implement = new LinkedList<>();
		this.imports = new LinkedList<>();
	}

	public CodeWriter annotation(List<String> annotations) {
		if (this.scope == Scope.METHOD) {
			IMethod iMethod = this.getLastIMethod();
			iMethod.getAnnotations().addAll(annotations);
		} else if (this.scope == Scope.CLASS) {
			this.annotations.addAll(annotations);
		} else if (this.scope == Scope.FIELD) {
			IField iField = this.getLastIField();
			iField.getAnnotations().addAll(annotations);
		}
		return this;
	}
	
	public CodeWriter annotation(String... annotations) {
		return annotation(Arrays.asList(annotations));
	}

	public CodeWriter body(List<String> methodBody) {
		IMethod iMethod = getLastIMethod();
		iMethod.getMethodBody().addAll(methodBody);
		return this;
	}

	public CodeWriter body(String... methodBody) {
		return body(Arrays.asList(methodBody));
	}

	public String capitalizeFirstChar(String string) {
		try {
			return string.substring(0, 1).toUpperCase() + string.substring(1);
		} catch (Exception e) {// The class name consists of only one character.
			return string.toUpperCase();
		}
	}

	public CodeWriter extend(String extend) {
		this.extend = extend;
		return this;
	}
	
	public CodeWriter implement(String... implement) {
		this.implement.addAll(Arrays.asList(implement));
		return this;
	}
	
	public void addImport(Class<?> clazz) {
		this.imports.add("import " + clazz.getName() + ";");
	}
	
	public CodeWriter clazz(Class<?> clazz) {
		addImport(clazz);
		return clazz(clazz.getSimpleName());
	}
	
	public CodeWriter clazz(String className) {
		this.clazz = capitalizeFirstChar(className);
		this.scope = Scope.CLASS;
		return this;
	}

	public List<String> getAnnotations() {
		return annotations;
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
	
	public IField getLastIField() {
		return this.iFields.get(this.iFields.size() - 1);
	}
	
	public Scope getScope() {
		return scope;
	}
	
	public String indent(int count) {
		StringBuilder indents = new StringBuilder();
		for (int i = 0; i < count; i++) {
			indents.append("    ");
		}
		return indents.toString();
	}
	
	public CodeWriter method(String prefix, Method method) {
		return method(prefix, method.getName());
	}
	
	public CodeWriter method(String prefix, String methodName) {
		return method(prefix + capitalizeFirstChar(methodName));
	}
	
	public CodeWriter method(Method method) {
		return method(method.getName());
	}
	
	public CodeWriter method(String methodName) {
		IMethod imethod = new IMethod();
		imethod.setMethodName(methodName);
		this.iMethods.add(imethod);
		this.scope = Scope.METHOD;
		return this;
	}
	
	public CodeWriter parameter(Class<?> parameterClass, String parameterName) {
		addImport(parameterClass);
		return parameter(parameterClass.getSimpleName(), parameterName);
	}
	
	public CodeWriter parameter(String parameterClass, String parameterName) {
		IMethod iMethod = getLastIMethod();
		iMethod.getParameters().put(parameterClass, parameterName);
		return this;
	} 
	
	public CodeWriter returnType(Class<?> returnType) {
		addImport(returnType);
		return returnType(returnType.getSimpleName());
	}
	
	public CodeWriter returnType(String returnType) {
		IMethod iMethod = getLastIMethod();
		iMethod.setReturnType(returnType);
		return this;
	}
	
	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}
	
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	public void setiMethods(List<IMethod> iMethods) {
		this.iMethods = iMethods;
	}
	
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	
	@Override
	public String toString() {
		return "CodeWriter [scope=" + scope + ", annotations=" + annotations + ", clazz=" + clazz + ", iMethods="
				+ iMethods + ", bufferedWriter=" + bufferedWriter + ", indentCount=" + indentCount + "]";
	}

	public void write(String line) throws Exception {
		line = line.trim();// Avoid the "{ " or "} "case.
		if (line.length() >0 && line.charAt(line.length() - 1) == '}') {
			this.indentCount--;
			if (this.indentCount < 0) {
				this.indentCount = 0;
			}
		}
		this.bufferedWriter.write(indent(this.indentCount) + line + "\n");
		if (line.length() > 0 && line.charAt(line.length() - 1) == '{') {
			this.indentCount++;
		} 
	}
	
	public void writeJava(String filePath) throws Exception {
		// Open Resources
		File file = new File(filePath + "/" + this.clazz + ".java");
		FileWriter fileWriter = new FileWriter(file);
		this.bufferedWriter = new BufferedWriter(fileWriter);
		// Package
		if (this.packet != null) {
			write("package " + this.packet + ";");
			writeln();
		}
		// Import
		for (String myImport : this.imports) {
			write(myImport);
		}
		if (this.getImports() != null && this.getImports().size() > 0) {
			writeln();
		}
		// Class
		// Class Annotations
		for (String annotation : this.annotations) {
			write(annotation);
		}
		// Class Signature
		StringBuilder extendAndImplement = new StringBuilder();
		if (this.extend != null) {
			extendAndImplement.append("extends " + this.extend + " ");
		}
		if (this.getImplement() != null && this.getImplement().size() > 0) {
			extendAndImplement.append("implements ");
			for (String implement : this.implement) {
				extendAndImplement.append(implement + ", ");
			}
		}
		write("public class " + this.clazz + " " + (extendAndImplement.length() == 0 ? "" : extendAndImplement.substring(0, extendAndImplement.length() - 2)) +" {");
		// Field
		for (IField iField : this.iFields) {
			for (String annotation : iField.getAnnotations()) {
				write(annotation);
			}
			write("private " + iField.getType() + " " + iField.getFieldName() + ";");
			writeln();
		}
		// Method
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
