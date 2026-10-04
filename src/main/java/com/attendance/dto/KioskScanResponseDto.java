package com.attendance.dto;

public class KioskScanResponseDto {
    private boolean success;
    private String status; // "SUCCESS", "ALREADY_RECORDED", "NOT_FOUND", "ERROR"
    private String message;
    private String entityType; // "STUDENT" or "EMPLOYEE"
    private String identifier;
    private String name;
    private String department;
    private String details;
    private String time;
    private Double currentPercentage;
    private String audioCue; // "success", "warn", "error"

    public KioskScanResponseDto() {
    }

    public static KioskScanResponseDto success(String entityType, String identifier, String name,
                                              String department, String details, String time,
                                              Double currentPercentage, String message) {
        KioskScanResponseDto res = new KioskScanResponseDto();
        res.setSuccess(true);
        res.setStatus("SUCCESS");
        res.setEntityType(entityType);
        res.setIdentifier(identifier);
        res.setName(name);
        res.setDepartment(department);
        res.setDetails(details);
        res.setTime(time);
        res.setCurrentPercentage(currentPercentage);
        res.setMessage(message);
        res.setAudioCue("success");
        return res;
    }

    public static KioskScanResponseDto notFound(String code, String message) {
        KioskScanResponseDto res = new KioskScanResponseDto();
        res.setSuccess(false);
        res.setStatus("NOT_FOUND");
        res.setIdentifier(code);
        res.setMessage(message);
        res.setAudioCue("error");
        return res;
    }

    public static KioskScanResponseDto error(String message) {
        KioskScanResponseDto res = new KioskScanResponseDto();
        res.setSuccess(false);
        res.setStatus("ERROR");
        res.setMessage(message);
        res.setAudioCue("error");
        return res;
    }

    // Getters and Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getCurrentPercentage() {
        return currentPercentage;
    }

    public void setCurrentPercentage(Double currentPercentage) {
        this.currentPercentage = currentPercentage;
    }

    public String getAudioCue() {
        return audioCue;
    }

    public void setAudioCue(String audioCue) {
        this.audioCue = audioCue;
    }
}
