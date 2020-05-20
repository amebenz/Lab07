package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class FXMLController {

	private Model model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Nerc> comboNerc;

    @FXML
    private TextField txtYears;

    @FXML
    private TextField txtHours;

    @FXML
    private Button btnWorst;

    @FXML
    private TextArea txtResult;

    @FXML
    private ImageView imgMap;

    @FXML
    void doWorstCase(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	try {
    	
    	Nerc nercTemp = comboNerc.getSelectionModel().getSelectedItem();
    	
    	if(nercTemp==null) {
    		txtResult.appendText("Seleziona un NERC!");
    		return;
    	}
    	
    	int year = Integer.parseInt(txtYears.getText());
    	
    	if(year<0 || year>model.getYearList().size()) {
    		txtResult.appendText(String.format("Scegli un numero di anni maggiore di 0 oppure minore di %d", model.getYearList().size()));
    		return;
    	}
    	
    	int hours = Integer.parseInt(txtHours.getText());
    	
    	if(hours<=0) {
    		txtResult.appendText("Scegli un numero di ore positivo");
    		return;
    	}
    	
    	List<PowerOutages> listaTemp = model.getWorstCase(year, hours, nercTemp);
    	
    	for(PowerOutages p : listaTemp) {
    		txtResult.appendText(p.toString());
    	}

    	} catch (NumberFormatException e) {
			txtResult.setText("Insert a valid number of years and of hours");
		}
    }
    
    public void setComboItems() {
    	
    	List<Nerc> lista = new ArrayList<>(this.model.getNercList());
    	
    	comboNerc.getItems().addAll(lista);
    	
    }

    @FXML
    void initialize() {
        assert comboNerc != null : "fx:id=\"comboNerc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnWorst != null : "fx:id=\"btnWorst\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert imgMap != null : "fx:id=\"imgMap\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model=model;
    	setComboItems();
    }
}
