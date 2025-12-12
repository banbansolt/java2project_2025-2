package hospital.domain;

import java.util.Date;

public class PrescriptionVO {

    private int prescriptionId;          // 처방전ID (PK)
    private int consultationId;          // 진료ID (FK, ConsultationVO 연결)
    private String pharmacyId;           // 약국ID (FK, PharmacyVO 연결 - String 타입)
    private Date issueDate;              // 발행일자
    private String fulfillmentStatus;    // 조제 상태 (예: 발행, 조제중, 조제완료, 수령완료)

    // 🚨 환자 이름을 담기 위한 필드 추가
    private String patientName;          // 환자 이름 (JOIN을 통해 조회)

    // 기본 생성자
    public PrescriptionVO() {}

    // 모든 필드를 포함하는 생성자 (선택 사항)
    public PrescriptionVO(int prescriptionId, int consultationId, String pharmacyId, Date issueDate, String fulfillmentStatus, String patientName) {
        this.prescriptionId = prescriptionId;
        this.consultationId = consultationId;
        this.pharmacyId = pharmacyId;
        this.issueDate = issueDate;
        this.fulfillmentStatus = fulfillmentStatus;
        this.patientName = patientName; // 추가된 필드
    }

    // --- Getter and Setter ---

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(int consultationId) {
        this.consultationId = consultationId;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    public void setFulfillmentStatus(String fulfillmentStatus) {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    // 🚨 추가된 getPatientName() 메서드
    public String getPatientName() {
        return patientName;
    }

    // 🚨 추가된 setPatientName() 메서드
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    @Override
    public String toString() {
        return "PrescriptionVO{" +
                "prescriptionId=" + prescriptionId +
                ", consultationId=" + consultationId +
                ", pharmacyId='" + pharmacyId + '\'' +
                ", issueDate=" + issueDate +
                ", fulfillmentStatus='" + fulfillmentStatus + '\'' +
                ", patientName='" + patientName + '\'' + // 추가된 필드
                '}';
    }
}