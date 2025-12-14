package hospital.domain;

public class PrescriptionDetailVO {


    private int prescriptionId;


    private String drugCode;

    private String dosage;
    private int quantity;


    private String drugName;

    private int drugPrice;



    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    // 🚨 새로 추가된 메서드 2: 단위 가격
    public int getDrugPrice() {
        return drugPrice;
    }

    public void setDrugPrice(int drugPrice) {
        this.drugPrice = drugPrice;
    }


    @Override
    public String toString() {
        return "PrescriptionDetailVO{" +
                "prescriptionId=" + prescriptionId +
                ", drugCode='" + drugCode + '\'' +
                ", drugName='" + drugName + '\'' +
                ", dosage='" + dosage + '\'' +
                ", quantity=" + quantity +
                ", drugPrice=" + drugPrice +
                '}';
    }
}