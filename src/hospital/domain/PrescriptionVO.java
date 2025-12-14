package hospital.domain;

import java.util.Date;
import java.util.List;

public class PrescriptionVO {

    private int prescriptionId;
    private int consultationId;
    private String pharmacyId;
    private Date issueDate;
    private String fulfillmentStatus;

    private String patientName;

    private List<PrescriptionDetailVO> drugDetails;

    public PrescriptionVO() {}

    public PrescriptionVO(int prescriptionId, int consultationId, String pharmacyId, Date issueDate, String fulfillmentStatus, String patientName, List<PrescriptionDetailVO> drugDetails) {
        this.prescriptionId = prescriptionId;
        this.consultationId = consultationId;
        this.pharmacyId = pharmacyId;
        this.issueDate = issueDate;
        this.fulfillmentStatus = fulfillmentStatus;
        this.patientName = patientName;
        this.drugDetails = drugDetails;
    }

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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public List<PrescriptionDetailVO> getDrugDetails() {
        return drugDetails;
    }

    public void setDrugDetails(List<PrescriptionDetailVO> drugDetails) {
        this.drugDetails = drugDetails;
    }


    @Override
    public String toString() {
        return "PrescriptionVO{" +
                "prescriptionId=" + prescriptionId +
                ", consultationId=" + consultationId +
                ", pharmacyId='" + pharmacyId + '\'' +
                ", issueDate=" + issueDate +
                ", fulfillmentStatus='" + fulfillmentStatus + '\'' +
                ", patientName='" + patientName + '\'' +
                ", drugDetails=" + drugDetails +
                '}';
    }
}