package io.futuramer.rental.reports.builder.service;

import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Main class containing business logic of application
 */
public class ReportEngine {

    /**
     * Database service
     */
    private DatabaseService database_service;

    /**
     * Excel parsing service
     */
    private ExcelService excel_service;

    /**
     * E-Mail service
     */
    private MailService mail_service;

    /**
     * Constructor, initializing fields
     */
    public ReportEngine() {
        database_service = new DatabaseService();
        excel_service = new ExcelService();
        mail_service = new MailService();
    }

    /**
     * Main method to handle file
     * @param file Excel filename to be parsed
     * @return Message to be displayed regarding the results of the handling of the file. Will contain reason of error in case of exception.
     */
    public String handle(File file) {
        String report_status = "Completed successfully";

        try {
            database_service.createConnection();
            List<RentalUnitDto> rental_unit_new_list = excel_service.parseExcel(file);
            List<RentalUnitDto> rental_unit_old_list = database_service.getRentalUnitsOfLastReport();
            database_service.persistRentalUnits(rental_unit_new_list);
            String email_report_body = createReport(rental_unit_new_list, rental_unit_old_list);
            Map<String, String> message_recipients_map = database_service.getMessageRecipients();
            mail_service.sendReports(message_recipients_map, email_report_body);
            database_service.commit();
        }
        catch (ReportException e) {
            report_status = e.getDescription();
            database_service.tryRollback();
        }
        finally {
            database_service.close();
        }
        return report_status;
    }

    /**
     * Method generating the message of body of the generated report to send
     * @param rental_unit_new_list list of existing rental units
     * @param rental_unit_old_list list of newly parsed rental units
     * @return the message of body of the generated report to send
     * @throws ReportException in case of various issues
     */
    private String createReport(List<RentalUnitDto> rental_unit_new_list, List<RentalUnitDto> rental_unit_old_list) throws ReportException {
        StringBuilder string_builder = new StringBuilder();
        List<String> notes_list = database_service.getNotes();

        generateHeader(string_builder);
        generateRentalUnitsBlock(string_builder, rental_unit_new_list);
        generateChangesSinceLastReportBlock(string_builder);
        generateUrgentBlock(string_builder);
        generateNewBlock(string_builder);
        generateNotesBlock(string_builder, notes_list);
        generateFooter(string_builder);

        return string_builder.toString();
    }

    private void generateHeader(StringBuilder string_builder) {
        string_builder.append("<html><head></head><body><font face='Times New Roman, Times, serif' size='3'>");
    }

    private void generateRentalUnitsBlock(StringBuilder string_builder, List<RentalUnitDto> rental_unit_dto_list) {
        // TODO
    }

    private void generateChangesSinceLastReportBlock(StringBuilder string_builder) {
        // TODO
    }

    private void generateUrgentBlock(StringBuilder string_builder) {
        // TODO
    }

    private void generateNewBlock(StringBuilder string_builder) {
        // TODO
    }

    private void generateNotesBlock(StringBuilder string_builder, List<String> notes_list) {
        string_builder.append("<div><font face='Arial, Helvetica, sans-serif' size='4' color='#0000FF'>Notes</font></div>");
        string_builder.append("<div>&nbsp;</div>");
        if (CollectionUtils.isNotEmpty(notes_list)) {
            for (String note : notes_list) {
                string_builder.append("<div>").append(note).append("</div>");
            }
        }
    }

    private void generateFooter(StringBuilder string_builder) {
        string_builder.append("</font></body></html>");
    }
}
