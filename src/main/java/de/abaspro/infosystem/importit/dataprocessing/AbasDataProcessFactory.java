package de.abaspro.infosystem.importit.dataprocessing;

import java.util.ArrayList;

import de.abaspro.infosystem.importit.AbasDataCheckAndComplete;
import de.abaspro.infosystem.importit.Data;
import de.abaspro.infosystem.importit.ImportitException;

public class AbasDataProcessFactory {

	public AbasDataProcessable createAbasDataProcess(String server, Integer port, String client, String password , ArrayList<Data> dataList) throws ImportitException {
		
		AbasDataProcessable abasDataProcessable = null;
		AbasDataCheckAndComplete aCheckAndComplete = new AbasDataCheckAndComplete(server, port, client, password, dataList);
		
		
//		pruefe welcher datensatz ist datalist
		
		if (aCheckAndComplete.checkandcompleteDataList()) {
			switch (checkTypDataList(dataList)) {
			case 1:
				//			Standard Object
				abasDataProcessable = new AbasDataProcessingStandardObject(
						server, port, client, password);
				break;
			case 2:
				//			Typcommando
				abasDataProcessable = new AbasDataProcessingTypeCommands(server,
						port, client, password);
				break;
			case 3:
				//			CustomerPartProperties
				abasDataProcessable = new AbasDataProzessingCustomerPartProperties(
						server, port, client, password);
				break;
			default:
				break;
			}
		}
		return abasDataProcessable;
		// TODO Auto-generated constructor stub
	}

	private int checkTypDataList(ArrayList<Data> dataList) throws ImportitException{

//		1 : standard 
//		2 : Typcommand
//		3 : CustomerPartProperties
		int auswahl=0; 
		
		Data data = dataList.get(0);		
		
		if (data!=null) {
			
				
		if (data.getDatabase() != null && data.getDatabase() != null ) {
			if (data.getDatabase() == 2
					&& (data.getGroup() == 6 || data.getGroup() == 7)) {
				auswahl = 3;
			} else {
				if (data.getTypeCommandString() != null) {
					if (!data.getTypeCommandString().isEmpty()) {

						auswahl = 2;

					} else {
						auswahl = 1;
					}

				} else {
					auswahl = 1;
				}

			} 
		}else {
			throw new ImportitException (" Database ist " + data.getDatabase() + "Gruppe ist " + data.getGroup() +" sind null :" + data.getDbGroupString() );
		}
		
		
	}else {
		throw new ImportitException (" Daten sind null");	
	}
		return auswahl;
		}   
	
}