package ar.com.easytech.faces.utils;

public class Chain {

	StringBuilder chain = new StringBuilder(100);

	public Chain() {
		chain.append("jsf.util.chain(this,event,");
	}
	
	public void addScript(String script) {
	
		if (chain.charAt(chain.length() - 1) != ',')
            chain.append(',');
		
		appendQuotedValue(script);
		
	}
	
	@Override
	public String toString() {
		chain.append(")");
		return chain.toString();
	}
	
	private void appendQuotedValue( String script) {

		chain.append("'");
		int length = script.length();
		
		for (int i = 0; i < length; i++) {
			char c = script.charAt(i);
			
			if (c == '\'' || c == '\\') {
				chain.append('\\');
			} 
			
			chain.append(c);
		}
		
		chain.append("'");
	}
	
	
	
	
}
