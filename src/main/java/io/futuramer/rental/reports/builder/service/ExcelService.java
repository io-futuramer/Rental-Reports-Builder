package io.futuramer.rental.reports.builder.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class handler of Excel parsing
 */
class ExcelService {

    /**
     * Method to parse the selected Excel file, containing report of rental units
     * @param file selected excel file
     * @return list of rental units dto
     * @throws ReportException
     */
    List<RentalUnitDto> parseExcel(File file) throws ReportException {
        List<RentalUnitDto> rental_unit_dto_list = new ArrayList<>();

        try (InputStream inp = new FileInputStream(file)) {
            XSSFWorkbook workbook = new XSSFWorkbook(inp);
            XSSFSheet sheet = workbook.getSheet("Sheet1");

            int number_of_rows = sheet.getLastRowNum() + 1;
            for (int row_number = 0; row_number < number_of_rows; row_number++) {

                Row row = sheet.getRow(row_number);
                if (isRowValid(row)) {
                    handleRow(rental_unit_dto_list, row, row_number);
                }
            }
        }
        catch (IOException e) {
            throw new ReportException("Unable to open file " + file.getAbsolutePath());
        }
        return rental_unit_dto_list;
    }

    /**
     * Method to check if the row is valid, considering the fact that the value of first cell must be a valid date
     * @param row one single row of the excel sheet
     * @return true if valid, otherwise false
     */
    private boolean isRowValid(Row row) {
        boolean valid;
        try {
            valid = row.getCell(0).getDateCellValue() != null; // it returns java.util.Date. Consider row is valid if no exception and it is not null.
        }
        catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    /**
     *
     * @param rental_unit_dto_list collection of rental_unit DTOs
     * @param row one single row of the excel sheet
     * @param row_number number of the row
     */
    private void handleRow(List<RentalUnitDto> rental_unit_dto_list, Row row, int row_number) {
        try {
            RentalUnitDto rental_unit_dto = new RentalUnitDto();

            rental_unit_dto.setCreated_on(getDateValue(row, 0)); // Date
            rental_unit_dto.setCreated_by(getStringValue(row, 1)); // String
            rental_unit_dto.setRental_unit(getStringValue(row, 2)); // String
            rental_unit_dto.setLocation(getStringValue(row, 3)); // String
            rental_unit_dto.setProperty_status(getStringValue(row, 4)); // String
            rental_unit_dto.setApplication_status(getStringValue(row, 5)); // String
            rental_unit_dto.setApplicant_name(getStringValue(row, 6)); // String
            rental_unit_dto.setRent_pcm_amount(getIntValue(row, 7)); // int
            rental_unit_dto.setRent_pcm_currency(getStringValue(row, 8)); // String
            rental_unit_dto.setDeposit_amount(getIntValue(row, 9)); // int
            rental_unit_dto.setDeposit_currency(getStringValue(row, 10)); // String
            rental_unit_dto.setCheck_in_fee_amount(getIntValue(row, 11)); // int
            rental_unit_dto.setCheck_in_fee_currency(getStringValue(row, 12)); // String
            rental_unit_dto.setAvailable_start(getDateValue(row, 13)); // Date
            rental_unit_dto.setAvailable_end(getDateValue(row, 14)); // Date
            rental_unit_dto.setGl_id(getStringValue(row, 15)); // String
            rental_unit_dto.setShort_description(getStringValue(row, 16)); // String
            rental_unit_dto.setDescription(getStringValue(row, 17)); // String
            rental_unit_dto.setViewing_notification(getStringValue(row, 18)); // String
            rental_unit_dto.setKey_numbers(getStringValue(row, 19)); // String
            rental_unit_dto.setListing(getStringValue(row, 20)); // String
            rental_unit_dto.setTags(getStringValue(row, 21)); // String

            rental_unit_dto_list.add(rental_unit_dto);
        }
        catch (Exception e) {
            System.out.println("Error parsing row number " + row_number);
        }
    }

    /**
     * Getting Date value of given excel cell
     * @param row single excel row
     * @param cell_index index of cell to extract
     * @return Date if success, otherwise null
     */
    private Date getDateValue(Row row, int cell_index) {
        Cell cell = row.getCell(cell_index);
        return cell == null ? null : cell.getDateCellValue();
    }

    /**
     * Getting String value of given excel cell
     * @param row single excel row
     * @param cell_index index of cell to extract
     * @return String if success, otherwise null
     */
    private String getStringValue(Row row, int cell_index) {
        Cell cell = row.getCell(cell_index);
        return cell == null ? null : cell.getStringCellValue();
    }

    /**
     * Getting Integer value of given excel cell
     * @param row single excel row
     * @param cell_index index of cell to extract
     * @return Integer if success, otherwise null
     */
    private Integer getIntValue(Row row, int cell_index) {
        Cell cell = row.getCell(cell_index);
        return cell == null ? null : (int)cell.getNumericCellValue();
    }
}
