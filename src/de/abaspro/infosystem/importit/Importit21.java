package de.abaspro.infosystem.importit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.abas.eks.jfop.annotation.Stateful;
import de.abas.eks.jfop.remote.FOe;
import de.abas.erp.api.gui.TextBox;
import de.abas.erp.axi.event.ButtonEvent;
import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.event.EventHandler;
import de.abas.erp.axi.event.ObjectEventHandler;
import de.abas.erp.axi.event.listener.ButtonListenerAdapter;
import de.abas.erp.db.SelectableObject;
import de.abas.erp.db.infosystem.custom.owjava.InfosystemImportit;
import de.abas.erp.db.infosystem.custom.owjava.InfosystemImportit.Row;
import de.abas.erp.db.selection.Selection;
@Stateful
public class Importit21 extends EventHandler<InfosystemImportit> {
	
	ArrayList<Datensatz> datensatzList;
	EdpProcessing edpProcessing;
	private final static Logger log = Logger.getLogger( Importit21.class );
	
	public Importit21() throws IOException {
		super(InfosystemImportit.class);
	}

	@Override
	  protected void configureEventHandler(ObjectEventHandler<InfosystemImportit> objectHandler) {
	    super.configureEventHandler(objectHandler);
	    // add user defined listener
	    objectHandler.addListener(InfosystemImportit.META.ypruefstrukt, new PruefStrukturButtonListener());
	    objectHandler.addListener(InfosystemImportit.META.yintabladen, new IntabladenButtonListener());
	    objectHandler.addListener(InfosystemImportit.META.ypruefdat, new PruefDatenButtonListener());
	    objectHandler.addListener(InfosystemImportit.META.yimport, new ImportButtonListener());
	    
	  }
	
	/**
	 * @author tkellermann
	 * 
	 * Unterklasse f�r den Start des Datenimports
	 *
	 */
	class ImportButtonListener extends ButtonListenerAdapter<InfosystemImportit>{

	    @Override
	    public void after(ButtonEvent<InfosystemImportit> event) throws EventException {
	      super.after(event);
	      yimportButtonInvoked(event);
	    }
	
		/**
		 * @param event
		 * 
		 * Daten importieren
		 * 
		 */
		private void yimportButtonInvoked(
				ButtonEvent<InfosystemImportit> event) {
			
			InfosystemImportit infosysImportit = event.getSourceRecord();
			
			try {
				log.debug("Start import der Daten");
				edpProcessing.importDatensatzList(datensatzList);
				log.debug("Ende import der Daten");
			} catch (ImportitException e) {
				AbasExceptionOutput(e);
			}
			
			infosysImportit.setYok(getimportitDatasets(datensatzList)); 
			infosysImportit.setYfehler(geterrorDatasets(datensatzList));
			
		}


	}
	
	/**
	 * @author tkellermann
	 *
	 * Unterklasse um die Pr�fung der Daten aus der Liste datensatzList auf Inhaltliche Fehler pr�fen.
	 * 
	 * -Feldl�nge
	 * -Datum
	 * -Zahlformate
	 * 
	 * -nicht pr�fen der Verweisfelder
	 *  
	 *
	 */
	class PruefDatenButtonListener extends ButtonListenerAdapter<InfosystemImportit>{

	    @Override
	    public void after(ButtonEvent<InfosystemImportit> event) throws EventException {
	      super.after(event);
	      log.debug("Start pr�fen der Daten");
	      String name = log.getName();
	      Level loglevel = log.getLevel();
	      ypruefdatButtonInvoked(event);
	      log.debug("Ende pr�fen der Daten");
	    }
	
	
	
	
		private void ypruefdatButtonInvoked(
				ButtonEvent<InfosystemImportit> event) {
			InfosystemImportit infosysImportit = event.getSourceRecord();
			try {
				
				edpProcessing.checkDatensatzListValues(datensatzList);
				
			} catch (ImportitException e) {
				
				AbasExceptionOutput(e);
			}
			
			
			
			
		}


	}

		class IntabladenButtonListener extends ButtonListenerAdapter<InfosystemImportit>{

	    @Override
	    public void after(ButtonEvent<InfosystemImportit> event) throws EventException {
	      super.after(event);
	      yintabladenButtonInvoked(event);
	    }
	    
	    public void yintabladenButtonInvoked(ButtonEvent<InfosystemImportit> event) {
//			Ausgabe der Datens�tze in der Tabelle
			
			InfosystemImportit infosysImportit = event.getSourceRecord();
			
			infosysImportit.table().clear();
				try {
					for (Datensatz datensatz : datensatzList) {
						if (datensatz.getTippkommando()== null) {
							
//					Es ist ein Datenbank-Kommando
							if ((datensatz.getError() != null & infosysImportit.getYshowonlyerrorline()) || 
									(!infosysImportit.getYshowonlyerrorline()) ) {
//								Alles ausgeben oder wenn yshowonlyerrorline gesetzt nur die fehlerhaften Datens�tze 
								Row row = infosysImportit.table().appendRow();
								row.setYsel(datensatz.getValueOfKeyfield());
								if (datensatz.getAbasId() != null) {
									row.setString(row.META.ydatensatz, datensatz.getAbasId());
								}
								if (datensatz.getError() == null) {
									row.setYicon("icon:ok");
								}else {
									row.setYicon("icon:stop");
									row.setYtfehler(datensatz.getError().substring(0, 70));
									StringReader reader = new StringReader(datensatz.getError());
									row.setYkomtext(reader);
								}
							}
							
							
						}else {
//							Tippkommando
							Row row = infosysImportit.table().appendRow();
							row.setYsel("Tippkommando " + datensatz.getTippkommando() + " "  + "Datensatznummer " + datensatzList.indexOf(datensatz));
							if (datensatz.getError() == null) {
								row.setYicon("icon:ok");
							}else {
								row.setYicon("icon:stop");
								row.setYtfehler(datensatz.getError().substring(0, 70));
								StringReader reader = new StringReader(datensatz.getError());
								row.setYkomtext(reader);
							}
						}
						
					}
				} catch (ImportitException e) {

					AbasExceptionOutput(e);
				} catch (IOException e) {
					AbasExceptionOutput(e);
				}
			}
			
		}
		
		
		
		
	    class PruefStrukturButtonListener extends ButtonListenerAdapter<InfosystemImportit>{

	    @Override
	    public void after(ButtonEvent<InfosystemImportit> event) throws EventException {
	      super.after(event);
	      ypruefstruktButtonInvoked(event);
	    }

	    private void ypruefstruktButtonInvoked(ButtonEvent<InfosystemImportit> event) {
	    	
	    	InfosystemImportit infosysImportit = event.getSourceRecord();
	    	try {
//	    		pr�fe noch ob passwort eingeben wurde 
	    		
				ExcelProcessing excelProcessing = new ExcelProcessing(infosysImportit.getYdatafile());
				datensatzList = excelProcessing.getDatensatzList();
				
				edpProcessing = new EdpProcessing(infosysImportit.getYserver(), infosysImportit.getYport(), infosysImportit.getYmandant(), infosysImportit.getYpasswort());
				edpProcessing.checkDatensatzList(datensatzList);
				
				String daten = "test2"; 
				
				
			} catch (ImportitException e) {
				AbasExceptionOutput(e);
			}
	      
	    }
	  }

	

	private int geterrorDatasets(ArrayList<Datensatz> datensatzList2) {
		int numberOfError = 0;
		for (Datensatz datensatz : datensatzList2) {
			String error = datensatz.getError();
			if (error != null) {
				if (!datensatz.getError().isEmpty()) {
					numberOfError++;
				}
			}
		}
		return numberOfError;
	}

	private int getimportitDatasets(ArrayList<Datensatz> datensatzList2) {
		int numberOfOk = 0;
		for (Datensatz datensatz : datensatzList2) {
			String error = datensatz.getError();
			if (error != null) {
				if (datensatz.getError().isEmpty()) {
					numberOfOk++;
				}
			}else {
				numberOfOk++;
			}
		}
		
		return numberOfOk;
	}

	private void AbasExceptionOutput(Exception e){
		TextBox textBox = new TextBox(getContext(), "Fehler", e.toString());
		textBox.show();
		
	}
	
}
