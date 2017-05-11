package evolution;

import java.util.LinkedHashMap;
import java.util.Map;

public class IMethod {
	private String methodName;
	private Map<String, String> parameters;
	private String returnType;
	
	public IMethod() {
		this.parameters = new LinkedHashMap<>();
	}
	
	
	
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	
	
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}



	public String getMethodName() {
		return methodName;
	}



	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}



	@Override
	public String toString() {
		return "IMethod [methodName=" + methodName + ", parameters=" + parameters + ", returnType=" + returnType + "]";
	}
	
	
}
