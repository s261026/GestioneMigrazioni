/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryAndNumber;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxNazione"
    private ComboBox<Country> boxNazione; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	String annoS = txtAnno.getText();
		try {
			int anno = Integer.parseInt(annoS);

			model.creaGrafo(anno);
			
			boxNazione.getItems().addAll(this.model.getCountries());
			
			//calcola numero di confini
			List<CountryAndNumber> result = this.model.getCountryAndNumbers();
			
			for(CountryAndNumber c: result)
				txtResult.appendText(c.toString()+"\n");
			
			
		} catch (NumberFormatException e) {
			txtResult.appendText("Errore di formattazione dell'anno\n");
			return;
		}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    	txtResult.clear();
    	Country partenza = boxNazione.getValue();
    	if(partenza == null) {
    		txtResult.setText("SELEZIONA STATO!\n");
    		return;
    	}
    	txtResult.appendText("SIMULAZIONE A PARTIRE DA: " + partenza +"\n\n");
    	
    	this.model.simula(partenza);
    	txtResult.appendText("Numero di passi simulati: "+this.model.getT()+"\n");
    	
    	for(CountryAndNumber cn: this.model.getStanziali())
    		txtResult.appendText(cn.toString()+"\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxNazione != null : "fx:id=\"boxNazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
