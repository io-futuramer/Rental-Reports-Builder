package io.futuramer.rental.reports.builder.service;

import java.util.Date;

/**
 * Class DTO of Rental Units containing all fields to be parsed from Excel and to we saved in DB
 */
public class RentalUnitDto {

    /*
     * Fields
     */
    private Integer run_id;
    private Date created_on;
    private String created_by;
    private String rental_unit;
    private String location;
    private String property_status;
    private String application_status;
    private String applicant_name;
    private Integer rent_pcm_amount;
    private String rent_pcm_currency;
    private Integer deposit_amount;
    private String deposit_currency;
    private Integer check_in_fee_amount;
    private String check_in_fee_currency;
    private Date available_start;
    private Date available_end;
    private String gl_id;
    private String short_description;
    private String description;
    private String viewing_notification;
    private String key_numbers;
    private String listing;
    private String tags;

    @Override
    public String toString() {
        return "RentalUnitDto{" +
                "run_id=" + run_id +
                ", created_on=" + created_on +
                ", created_by='" + created_by + '\'' +
                ", rental_unit='" + rental_unit + '\'' +
                ", location='" + location + '\'' +
                ", property_status='" + property_status + '\'' +
                ", application_status='" + application_status + '\'' +
                ", applicant_name='" + applicant_name + '\'' +
                ", rent_pcm_amount=" + rent_pcm_amount +
                ", rent_pcm_currency='" + rent_pcm_currency + '\'' +
                ", deposit_amount=" + deposit_amount +
                ", deposit_currency='" + deposit_currency + '\'' +
                ", check_in_fee_amount=" + check_in_fee_amount +
                ", check_in_fee_currency='" + check_in_fee_currency + '\'' +
                ", available_start=" + available_start +
                ", available_end=" + available_end +
                ", gl_id='" + gl_id + '\'' +
                ", short_description='" + short_description + '\'' +
                ", description='" + description + '\'' +
                ", viewing_notification='" + viewing_notification + '\'' +
                ", key_numbers='" + key_numbers + '\'' +
                ", listing='" + listing + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }

    /*
     * ================================================================================
     * Setters
     */
    void setRun_id(Integer run_id) {
        this.run_id = run_id;
    }

    void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    void setRental_unit(String rental_unit) {
        this.rental_unit = rental_unit;
    }

    void setLocation(String location) {
        this.location = location;
    }

    void setProperty_status(String property_status) {
        this.property_status = property_status;
    }

    void setApplication_status(String application_status) {
        this.application_status = application_status;
    }

    void setApplicant_name(String applicant_name) {
        this.applicant_name = applicant_name;
    }

    void setRent_pcm_amount(Integer rent_pcm_amount) {
        this.rent_pcm_amount = rent_pcm_amount;
    }

    void setRent_pcm_currency(String rent_pcm_currency) {
        this.rent_pcm_currency = rent_pcm_currency;
    }

    void setDeposit_amount(Integer deposit_amount) {
        this.deposit_amount = deposit_amount;
    }

    void setDeposit_currency(String deposit_currency) {
        this.deposit_currency = deposit_currency;
    }

    void setCheck_in_fee_amount(Integer check_in_fee_amount) {
        this.check_in_fee_amount = check_in_fee_amount;
    }

    void setCheck_in_fee_currency(String check_in_fee_currency) {
        this.check_in_fee_currency = check_in_fee_currency;
    }

    void setAvailable_start(Date available_start) {
        this.available_start = available_start;
    }

    void setAvailable_end(Date available_end) {
        this.available_end = available_end;
    }

    void setGl_id(String gl_id) {
        this.gl_id = gl_id;
    }

    void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setViewing_notification(String viewing_notification) {
        this.viewing_notification = viewing_notification;
    }

    void setKey_numbers(String key_numbers) {
        this.key_numbers = key_numbers;
    }

    void setListing(String listing) {
        this.listing = listing;
    }

    void setTags(String tags) {
        this.tags = tags;
    }

    /*
     * ================================================================================
     * Getters
     */
    public Integer getRun_id() {
        return run_id;
    }

    Date getCreated_on() {
        return created_on;
    }

    String getCreated_by() {
        return created_by;
    }

    String getRental_unit() {
        return rental_unit;
    }

    String getLocation() {
        return location;
    }

    String getProperty_status() {
        return property_status;
    }

    String getApplication_status() {
        return application_status;
    }

    String getApplicant_name() {
        return applicant_name;
    }

    Integer getRent_pcm_amount() {
        return rent_pcm_amount;
    }

    String getRent_pcm_currency() {
        return rent_pcm_currency;
    }

    Integer getDeposit_amount() {
        return deposit_amount;
    }

    String getDeposit_currency() {
        return deposit_currency;
    }

    Integer getCheck_in_fee_amount() {
        return check_in_fee_amount;
    }

    String getCheck_in_fee_currency() {
        return check_in_fee_currency;
    }

    Date getAvailable_start() {
        return available_start;
    }

    Date getAvailable_end() {
        return available_end;
    }

    String getGl_id() {
        return gl_id;
    }

    String getShort_description() {
        return short_description;
    }

    String getDescription() {
        return description;
    }

    String getViewing_notification() {
        return viewing_notification;
    }

    String getKey_numbers() {
        return key_numbers;
    }

    String getListing() {
        return listing;
    }

    String getTags() {
        return tags;
    }
}
