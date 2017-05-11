package evolution;

import java.util.LinkedList;
import java.util.List;

public class IField {
	private String fieldName;
	private List<String> annotations;
	private String type;
	
	public IField() {
		this.type = "void";
		this.annotations = new LinkedList<>();
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public List<String> getAnnotations() {
		return annotations;
	}
	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
