package hospital.domain;

import java.util.Date;

public class ConsultationVO {
    private int consultationId; // 진료ID (PK)
    private String patientInfo; // 환자정보 (FK)
    private String doctorLicenseNumber; // 의사면허번호 (FK)
    private Date consultationDateTime; // 진료일시
    private String diagnosisName; // 진단명

    // 조회를 위한 추가 필드
    private String patientName;
    private String doctorName;

    // Getters and Setters

    public int getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(int consultationId) {
        this.consultationId = consultationId;
    }

    public String getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(String patientInfo) {
        this.patientInfo = patientInfo;
    }

    public String getDoctorLicenseNumber() {
        return doctorLicenseNumber;
    }

    public void setDoctorLicenseNumber(String doctorLicenseNumber) {
        this.doctorLicenseNumber = doctorLicenseNumber;
    }

    public Date getConsultationDateTime() {
        return consultationDateTime;
    }

    public void setConsultationDateTime(Date consultationDateTime) {
        this.consultationDateTime = consultationDateTime;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}