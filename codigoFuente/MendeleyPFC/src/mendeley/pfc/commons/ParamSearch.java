package mendeley.pfc.commons;

public class ParamSearch {

	private TypeOperatorSearch operator;
	private TypeComponentSearch component;
	private String value;
	
	public ParamSearch(TypeOperatorSearch operator,
			TypeComponentSearch component, String value) {
		this.operator = operator;
		this.component = component;
		this.value = value;
	}
	
	public TypeOperatorSearch getOperator() {
		return operator;
	}
	public void setOperator(TypeOperatorSearch operator) {
		this.operator = operator;
	}
	public TypeComponentSearch getComponent() {
		return component;
	}
	public void setComponent(TypeComponentSearch component) {
		this.component = component;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
