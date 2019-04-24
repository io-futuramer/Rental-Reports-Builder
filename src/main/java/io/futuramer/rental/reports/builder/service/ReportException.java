package io.futuramer.rental.reports.builder.service;

/**
 * Exception, encapsulating the exact reason of failed handling stage
 */
class ReportException extends Exception {

    /**
     * Description of the failed handling stage
     */
    private String description;

    /**
     * @param description Description of the failed handling stage
     */
    ReportException(String description) {
        this.description = description;
    }

    /**
     * @return description of failure
     */
    String getDescription() {
        return description;
    }
}
