package evolution;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IMethod {
	private List<String> annotations;
	

	public List<String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}

	private String methodName;
	private Map<String, String> parameters;
	private List<String> methodBody;
	private String returnType;

	public IMethod() {
		this.parameters = new LinkedHashMap<>();
		this.methodBody = new LinkedList<>();
		this.returnType = "void";// Set the return type as void by default.
		this.annotations = new LinkedList<>();
	}

	public List<String> getMethodBody() {
		return methodBody;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	public void setMethodBody(List<String> methodBody) {
		this.methodBody = methodBody;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	@Override
	public String toString() {
		return "IMethod [methodName=" + methodName + ", parameters=" + parameters + ", methodBody=" + methodBody
				+ ", returnType=" + returnType + "]";
	}
}
