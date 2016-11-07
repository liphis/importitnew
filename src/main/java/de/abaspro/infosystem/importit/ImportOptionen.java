package de.abaspro.infosystem.importit;

public enum ImportOptionen {

	NOTEMPTY ("@notempty" ) , MODIFIABLE( "@modifiable") , SKIP("@skip") , KEY("@Schl�ssel") , DONT_CHANGE_IF_EQUAL("@dontChangeIfEqual");

	private String eintrag;
	
	private ImportOptionen(String text) {
		
		this.eintrag = text ;
	}
	
	public String getSearchstring(){
		return eintrag;
	}
	
	
}
