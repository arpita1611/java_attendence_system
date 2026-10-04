package com.attendance.dto;

import java.util.ArrayList;
import java.util.List;

public class CsvImportResultDto {
    private int totalProcessed;
    private int successCount;
    private int failureCount;
    private List<String> errors = new ArrayList<>();
    private List<String> successMessages = new ArrayList<>();

    public CsvImportResultDto() {
    }

    public void addSuccess(String msg) {
        this.successCount++;
        this.totalProcessed++;
        this.successMessages.add(msg);
    }

    public void addError(String error) {
        this.failureCount++;
        this.totalProcessed++;
        this.errors.add(error);
    }

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getSuccessMessages() {
        return successMessages;
    }

    public void setSuccessMessages(List<String> successMessages) {
        this.successMessages = successMessages;
    }
}
