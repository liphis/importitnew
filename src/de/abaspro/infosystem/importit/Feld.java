package de.abaspro.infosystem.importit;




/**
 * @author tkellermann
 *
 */
public class Feld {

	

	private String  name;
	private String  value;
	private String  key;
	private Boolean option_notEmpty;
	private Boolean option_modifiable;
	private Boolean option_skip;
	private Boolean option_dontChangeIfEqual;
	private String  error;
	private String  completeContent;
	private Boolean fieldtoCopy;
	private Integer colNumber;
	private String 	abasTyp;
	private long abasFieldLength;
	
	
	


	@Override
	public String toString() {
		return "Feld [name=" + name + ", value=" + value + ", abasTyp="
				+ abasTyp + "]";
	}

	
	/**
	 * @param completeContent
	 * @param fieldInHead
	 * 
	 * Konstruktor um Feld anzulegen, 
	 * �ber den Parameter fieldInHead wird festgelegt ob das Object ein Kopf oder Datenfeld ist.   
	 * 
	 */
		
	public Feld(String completeContent, Boolean fieldToCopy , Integer col) {
		super();
		this.completeContent = completeContent;
		this.fieldtoCopy = fieldToCopy;
		this.colNumber = col;
		this.key = "";
		this.error = "";
		this.name = "";
		this.abasTyp = "";
		if (fieldToCopy) {
	//		Es ist ein Kopfffeld und daher wird nicht value, sondern name gef�llt
			this.name = extractValue(completeContent);
			fillOptions(completeContent);
		}else {
	//		Es ist ein normales Feld  und es wird der String in value geschrieben
			this.value = extractValue(completeContent);
		}
		
	}

	/**
	 * @param completeContent
	 * @param headfield
	 * @throws ImportitException
	 * 
	 * Dieser Konstruktor wird verwendet um Daten zu einem Kopffeld zu speichern
	 * 
	 * Falls das �bertragene Feld kein Kopffeld ist, wird eine {@link ImportitException} geworfen
	 * 
	 */
	
	public Feld(String completeContent, Feld headfield)throws ImportitException {
		super();
		if (headfield.getFieldInHead()) {
			
			this.completeContent = completeContent;
			this.fieldtoCopy = false;
			this.name = "";
			this.name = headfield.getName();
			this.value = extractValue(completeContent);
			this.option_modifiable = headfield.getOption_modifiable();
			this.option_notEmpty = headfield.getOption_notEmpty();
			this.option_skip = headfield.getOption_skip();
			this.option_dontChangeIfEqual = headfield.getOption_dontChangeIfEqual();
			this.colNumber = headfield.colNumber;
			this.key = "";
			this.error = "";
			this.abasTyp = "";
			
		}else {
			throw new ImportitException("Es wurde kein Feld mit der Eigenschaft fieldInHead �bergeben!");
		}
			
	}

	/**
	 * @return the option_dontChangeIfEqual
	 */
	public Boolean getOption_dontChangeIfEqual() {
		return option_dontChangeIfEqual;
	}


	/**
	 * @param option_dontChangeIfEqual the option_dontChangeIfEqual to set
	 */
	public void setOption_dontChangeIfEqual(Boolean option_dontChangeIfEqual) {
		this.option_dontChangeIfEqual = option_dontChangeIfEqual;
	}


	/**
	 * 
	 * @return ergebnis
	 * 
	 * Die Funktion pr�ft, ob der Inhalt des Felds den Vorgaben des Abastyps entspricht.  
	 * 
	 * 
	 */


	private void fillOptions(String completeContent2) {
	// falls der String leer ist dann das Feld ignorieren
		if (!completeContent2.isEmpty()) {
	
			this.option_notEmpty   = completeContent2.contains(ImportOptionen.NOTEMPTY.getSearchstring());
			this.option_modifiable = completeContent2.contains(ImportOptionen.MODIFIABLE.getSearchstring());
			this.option_skip       = completeContent2.contains(ImportOptionen.SKIP.getSearchstring());
			this.option_dontChangeIfEqual = completeContent2.contains(ImportOptionen.DONT_CHANGE_IF_EQUAL.getSearchstring());
			
			if (completeContent2.contains(ImportOptionen.KEY.getSearchstring())) {
		//		Es muss in dem FELD @KEY=NAME_OF_KEY
				Integer startPosKey = completeContent2.indexOf(ImportOptionen.MODIFIABLE.getSearchstring() + "=");
				String tempstring = completeContent2.substring(startPosKey);
				Integer tempindex  = tempstring.indexOf("@");
				this.key = tempstring.substring(0, tempindex);
			}
		}else {
			this.option_skip = true;
		}
	}



	private String extractValue(String completeContent2) {
		
		int index = completeContent2.indexOf("@");
		if (index != -1 ) {
			
			return completeContent2.substring(0, index );
		
		}else {
			return completeContent2;
		}
	}



	public String getCompleteContent() {
		return completeContent;
	}

	public Boolean getFieldtoCopy() {
		return fieldtoCopy;
	}

	public Integer getColNumber() {
		return colNumber;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
//		�nderung wenn der Typ B oder Boolean ist muss "ja" in "1"  oder "nein" in "0" umge�ndert werden.
		if (this.abasTyp.equals("B")) {
			if (this.value.equals("ja")|| this.value.equals("yes") || this.value.equals("true")) {
				return "1";
			}else if (this.value.equals("nein")|| this.value.equals("no") || this.value.equals("false")) {
				return "0";
			}else return this.value;
			
		}else return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getOption_notEmpty() {
		return option_notEmpty;
	}
	
	public void setOption_notEmpty(Boolean option_notEmpty) {
		this.option_notEmpty = option_notEmpty;
	}
	
	public Boolean getOption_modifiable() {
		return option_modifiable;
	}
	
	public void setOption_modifiable(Boolean option_modifiable) {
		this.option_modifiable = option_modifiable;
	}
	
	public Boolean getOption_skip() {
		return option_skip;
	}
	
	public void setOption_skip(Boolean option_skip) {
		this.option_skip = option_skip;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}	
	
	public Boolean getFieldInHead() {
		return fieldtoCopy;
	}
	
	/**
	 * @return the abasTyp
	 */
	public String getAbasTyp() {
		return abasTyp;
	}

	/**
	 * @param abasTyp the abasTyp to set
	 */
	public void setAbasTyp(String abasTyp) {
		this.abasTyp = abasTyp;
	}

	/**
	 * @return the abasFieldLength
	 */
	public long getAbasFieldLength() {
		return abasFieldLength;
	}




	/**
	 * @param l the abasFieldLength to set
	 */
	public void setAbasFieldLength(long l) {
		this.abasFieldLength = l;
	}

	/**
	 * @param abasFieldLength the abasFieldLength to set
	 * @throws ImportitException 
	 */
	public void setAbasFieldLength(String abasFieldLengthString) throws ImportitException {
		
		Integer abasFieldLength = 0;
		try {
			abasFieldLength = new Integer(abasFieldLengthString);	
		} catch (NumberFormatException e) {
			throw new ImportitException("Falsches Format der Feldl�nge" , e);
		}
			
		this.abasFieldLength = abasFieldLength;
		
	}
	
}