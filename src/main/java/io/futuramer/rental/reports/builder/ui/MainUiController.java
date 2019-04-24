package io.futuramer.rental.reports.builder.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import io.futuramer.rental.reports.builder.service.ReportEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

/**
 * Class controller for FX workaround: 'rentalReportsBuilder.fxml'
 */
public class MainUiController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="selectExcelFileButton"
    private Button select_excel_file_button; // Value injected by FXMLLoader

    @FXML // fx:id="buildReportButton"
    private Button build_report_button; // Value injected by FXMLLoader

    @FXML // fx:id="status_label"
    private Label status_label;

    // Excel file containing report data
    private File selectedFile;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert select_excel_file_button != null : "fx:id=\"selectExcelFileButton\" was not injected: check your FXML file 'rentalReportsBuilder.fxml'.";
        assert build_report_button != null : "fx:id=\"buildReportButton\" was not injected: check your FXML file 'rentalReportsBuilder.fxml'.";
        build_report_button.setDisable(true);
    }

    @FXML
    void selectExcelFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open excel rental report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MS Excel files (*.xlsx)", "*.xlsx"));
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            build_report_button.setDisable(false);
            select_excel_file_button.setDisable(true);
            status_label.setText("Push build button to proceed");
        }
    }

    @FXML
    void buildReportAction(ActionEvent event) {
        build_report_button.setDisable(true);
        select_excel_file_button.setDisable(false);
        String status = new ReportEngine().handle(selectedFile);
        status_label.setText(status);
    }

}
