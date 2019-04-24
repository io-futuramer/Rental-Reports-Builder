package io.futuramer.rental.reports.builder.service;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Class providing access to DB and performing the necessary workaround
 */
class DatabaseService {

    /**
     * Database and users` credentials
     */
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/rental_reports";
    private static final String USERNAME = "rental";
    private static final String PASSWORD = "reports";

    private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String SELECT_LAST_RUN_ID = "SELECT `run_id` FROM `run_table` ORDER BY `run_id` DESC LIMIT 1";
    private static final String SELECT_LAST_RUN_RENTAL_UNITS = "SELECT * FROM `rental_units` WHERE `run_id` = ";
    private static final String SELECT_NOTES = "SELECT `note` FROM `notes`";
    private static final String SELECT_RECIPIENTS = "SELECT `name`, `email` FROM `message_recipients`";
    private static final String RUN_TIME_INSERT = "INSERT INTO `run_table` (`run_time`) VALUES (?)";
    private static final String RENTAL_UNITS_INSERT = "INSERT INTO `rental_units`\n" +
            "(`run_id`,\n" +
            "`created_on`,\n" +
            "`created_by`,\n" +
            "`rental_unit`,\n" +
            "`location`,\n" +
            "`property_status`,\n" +
            "`application_status`,\n" +
            "`applicant_name`,\n" +
            "`rent_pcm_amount`,\n" +
            "`rent_pcm_currency`,\n" +
            "`deposit_amount`,\n" +
            "`deposit_currency`,\n" +
            "`check_in_fee_amount`,\n" +
            "`check_in_fee_currency`,\n" +
            "`available_start`,\n" +
            "`available_end`,\n" +
            "`gl_id`,\n" +
            "`short_description`,\n" +
            "`description`,\n" +
            "`viewing_notification`,\n" +
            "`key_numbers`,\n" +
            "`listing`,\n" +
            "`tags`)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private Connection connection;
    private SimpleDateFormat simple_date_format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

    void persistRentalUnits(List<RentalUnitDto> rental_unit_dto_list) throws ReportException {
        // https://www.tutorialspoint.com/jdbc/commit-rollback.htm
        // https://viralpatel.net/blogs/batch-insert-in-java-jdbc/
        int run_id = createRunRecord();
        saveRentalUnitRecords(rental_unit_dto_list, run_id);
    }

    /**
     * Method to create DB connection and to initialise SQL statement
     * @throws ReportException in case if it was unable either to register JDBC driver or to open DB connection
     */
    void createConnection() throws ReportException {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            connection.setAutoCommit(false);
        }
        catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe.getMessage());
            throw new ReportException("Unable to register JDBC driver");
        }
        catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            throw new ReportException("Unable to open DB connection");
        }
    }

    /**
     * Method to create "Run" record in DB
     * @return id of newly created Run record
     * @throws ReportException in case of DB issues
     */
    private int createRunRecord() throws ReportException {
        int run_id;
        try {
            PreparedStatement prepared_statement = connection.prepareStatement(RUN_TIME_INSERT, Statement.RETURN_GENERATED_KEYS);
            Date current_date = Calendar.getInstance().getTime();
            SimpleDateFormat simple_date_format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
            prepared_statement.setString(1, simple_date_format.format(current_date));
            prepared_statement.executeUpdate();

            ResultSet result_set = prepared_statement.getGeneratedKeys();
            result_set.next();
            run_id = result_set.getInt(1);

            result_set.close();
            prepared_statement.close();
        }
        catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            throw new ReportException("Error of creating new Run record");
        }
        return run_id;
    }

    /**
     * Method to save newly parsed Rental Units in DB
     * @param rental_unit_dto_list the list of Rental Units` DTOs to save
     * @param run_id id of Run (import) than these Rental Units belongs to
     * @throws ReportException in case of DB issues
     */
    private void saveRentalUnitRecords(List<RentalUnitDto> rental_unit_dto_list, int run_id) throws ReportException {

        try (PreparedStatement prepared_statement = connection.prepareStatement(RENTAL_UNITS_INSERT)) {
            for (RentalUnitDto rental_unit: rental_unit_dto_list) {

                prepared_statement.setInt(1, run_id);
                prepared_statement.setString(2, getStringByDate(rental_unit.getCreated_on()));
                prepared_statement.setString(3, rental_unit.getCreated_by());
                prepared_statement.setString(4, rental_unit.getRental_unit());
                prepared_statement.setString(5, rental_unit.getLocation());
                prepared_statement.setString(6, rental_unit.getProperty_status());
                prepared_statement.setString(7, rental_unit.getApplication_status());
                prepared_statement.setString(8, rental_unit.getApplicant_name());
                prepared_statement.setInt(9, rental_unit.getRent_pcm_amount());
                prepared_statement.setString(10, rental_unit.getRent_pcm_currency());
                prepared_statement.setInt(11, rental_unit.getDeposit_amount());
                prepared_statement.setString(12, rental_unit.getDeposit_currency());
                prepared_statement.setInt(13, rental_unit.getCheck_in_fee_amount());
                prepared_statement.setString(14, rental_unit.getCheck_in_fee_currency());
                prepared_statement.setString(15, getStringByDate(rental_unit.getAvailable_start()));
                prepared_statement.setString(16, getStringByDate(rental_unit.getAvailable_end()));
                prepared_statement.setString(17, rental_unit.getGl_id());
                prepared_statement.setString(18, rental_unit.getShort_description());
                prepared_statement.setString(19, rental_unit.getDescription());
                prepared_statement.setString(20, rental_unit.getViewing_notification());
                prepared_statement.setString(21, rental_unit.getKey_numbers());
                prepared_statement.setString(22, rental_unit.getListing());
                prepared_statement.setString(23, rental_unit.getTags());

                prepared_statement.addBatch();
            }
            prepared_statement.executeBatch();
        }
        catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            throw new ReportException("Error of saving Rental Units in DB");
        }
    }

    /**
     * Method to convert formatted String representation of java.util.Date
     * Utility for workaround java.util.Date <-> java.sql.Date conversions considering it may be null
     * @param date date to convert
     * @return null in case if param date is null, otherwise formatted String
     */
    private String getStringByDate(Date date) {
        return date == null ? null : simple_date_format.format(date);
    }

    /**
     * Method to convert String received from Date field of resultSet to java.util.Date
     * Implemented since ResultSet#getDate() returns Date without minutes, other conversions could lead to NPE
     * Utility for workaround java.util.Date <-> java.sql.Date conversions considering it may be null
     * @param date to convert
     * @return null in case if param date is null, otherwise new Date
     */
    private Date getDateByString(String date) throws ReportException {
        if (date == null) {
            return null;
        }

        Date parsed_date = null;
        try {
            parsed_date = simple_date_format.parse(date);
        }
        catch (ParseException pe) {
            throw new ReportException("Unexpected error of parsing date field from DB");
        }
        return parsed_date;
    }

    /**
     * Method to get list of notes from DB table 'notes'
     * @return List of notes from DB table 'notes'
     * @throws ReportException in case of DB table error
     */
    List<String> getNotes() throws ReportException {
        List<String> notes_list = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet result_set = statement.executeQuery(SELECT_NOTES)) {

            result_set.beforeFirst();
            while(result_set.next()) {
                notes_list.add(result_set.getString("note")); // Retrieving by column name
            }
        }
        catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            throw new ReportException("Error getting list of notes from database");
        }
        return notes_list;
    }

    /**
     * Method to get last successful run id of application from DB table 'run_table'
     * Implemented in order to handle the following issues:
     * - Continuous numeration could be broken in case of unsuccessful run (DB table autoincrement works)
     * - First run of the application must be handled accordingly
     * @return previous success 'run_id' of 'run_table'
     *         0 in case if no results fetched (first run)
     */
    private int getLastSuccessfulRun() {
        int last_successful_run_id = 0;
        try (Statement statement = connection.createStatement();
            ResultSet result_set = statement.executeQuery(SELECT_LAST_RUN_ID)) {

            result_set.beforeFirst();
            result_set.next();
            last_successful_run_id = result_set.getInt("run_id");
        }
        catch (SQLException sqle) {
            System.out.println(sqle.getMessage()); // may happen if no results fetched. First run?
        }
        return last_successful_run_id;
    }

    /**
     * Method to get Rental Units of last report from DB
     * @return collection of records
     * @throws ReportException in case of DB issues
     */
    List<RentalUnitDto> getRentalUnitsOfLastReport() throws ReportException {
        int last_successful_run_id = getLastSuccessfulRun();
        List<RentalUnitDto> rental_unit_dto_list = new ArrayList<>();
        if (last_successful_run_id != 0) {
            try (PreparedStatement prepared_statement = connection.prepareStatement(SELECT_LAST_RUN_RENTAL_UNITS + last_successful_run_id);
                ResultSet result_set = prepared_statement.executeQuery()) {

                result_set.beforeFirst();
                while (result_set.next()) {
                    RentalUnitDto rental_unit = new RentalUnitDto();

                    rental_unit.setRun_id(result_set.getInt("run_id"));
                    rental_unit.setCreated_on(getDateByString(result_set.getString("created_on")));
                    rental_unit.setCreated_by(result_set.getString("created_by"));
                    rental_unit.setRental_unit(result_set.getString("rental_unit"));
                    rental_unit.setLocation(result_set.getString("location"));
                    rental_unit.setProperty_status(result_set.getString("property_status"));
                    rental_unit.setApplication_status(result_set.getString("application_status"));
                    rental_unit.setApplicant_name(result_set.getString("applicant_name"));
                    rental_unit.setRent_pcm_amount(result_set.getInt("rent_pcm_amount"));
                    rental_unit.setRent_pcm_currency(result_set.getString("rent_pcm_currency"));
                    rental_unit.setDeposit_amount(result_set.getInt("deposit_amount"));
                    rental_unit.setDeposit_currency(result_set.getString("deposit_currency"));
                    rental_unit.setCheck_in_fee_amount(result_set.getInt("check_in_fee_amount"));
                    rental_unit.setCheck_in_fee_currency(result_set.getString("check_in_fee_currency"));
                    rental_unit.setAvailable_start(getDateByString(result_set.getString("available_start")));
                    rental_unit.setAvailable_end(getDateByString(result_set.getString("available_end")));
                    rental_unit.setGl_id(result_set.getString("gl_id"));
                    rental_unit.setShort_description(result_set.getString("short_description"));
                    rental_unit.setDescription(result_set.getString("description"));
                    rental_unit.setViewing_notification(result_set.getString("viewing_notification"));
                    rental_unit.setKey_numbers(result_set.getString("key_numbers"));
                    rental_unit.setListing(result_set.getString("listing"));
                    rental_unit.setTags(result_set.getString("tags"));

                    rental_unit_dto_list.add(rental_unit);
                }
            } catch (SQLException sqle) {
                System.out.println(sqle.getMessage());
                throw new ReportException("Error getting list of rental units of previous run");
            }
        }
        return rental_unit_dto_list;
    }

    /**
     * Method to get the collection of email recipients
     * @return Map<String, String> where K - email of recipient, V - his name
     * @throws ReportException in case of DB table error
     */
    Map<String, String> getMessageRecipients() throws ReportException {
        Map<String, String> message_recipients_map = new HashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet result_set = statement.executeQuery(SELECT_RECIPIENTS)) {

            result_set.beforeFirst();
            while(result_set.next()) {
                message_recipients_map.put(result_set.getString("email"), result_set.getString("name")); // Retrieving by column names
            }
        }
        catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            throw new ReportException("Error getting list of notes from database");
        }
        return message_recipients_map;
    }

    /**
     * Trying to rollback database operations
     */
    void tryRollback(){
        try {
            if (connection != null) {
                connection.rollback();
            }
        }
        catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
    }

    /**
     * Closing and releasing resources
     */
    void close() {
        try {
            if(connection != null) {
                connection.close();
            }
        }
        catch (SQLException se2){
            System.out.println(se2.getMessage());
        }
    }

    /**
     * Commiting all imports in DB
     * @throws ReportException in case if commit failed.
     */
    void commit() throws ReportException {
        try {
            connection.commit();
        }
        catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            throw new ReportException("Error upon Commit to database. No data was saved in DB.");
        }
    }

}
