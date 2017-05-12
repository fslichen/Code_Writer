package evolution;
import org.junit.Test;

import evolution.CodeWriter;

public class CodeWriterTest {
	@Test
	public void test() throws Exception {
		CodeWriter codeWriter = new CodeWriter();
		codeWriter.packet("com.evolution")
		.clazz("AnyClass")
		.annotation("@RestController")
		.extend("SuperClass").implement("Serializable", "Cloneable")
		.field("name").annotation("@Autowired").type("String")
		.field(Boolean.class, "gender").annotation("@Value")
		.method("anyMethod")
		.parameter(String.class, "string").parameter("Date", "date")
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
}
