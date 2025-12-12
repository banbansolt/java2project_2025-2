package hospital.domain;

public class PrescriptionDetailVO {

    // 처방전 테이블의 외래키
    private int prescriptionId;

    // 약품 테이블의 외래키 (주 키의 일부)
    private String drugCode;

    private String dosage; // 용량 (예: 1일 3회, 10mg)
    private int quantity; // 수량 (약품 개수)

    // 🚨 필수 추가: 테이블에 약품명을 표시하거나 로직에 사용하기 위해 VO에 임시로 저장
    private String drugName;

    // 🚨 필수 추가: 계산 로직을 위해 DrugVO에서 가져온 단위 가격을 저장
    private int drugPrice;

    // --- Getter and Setter Methods ---

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

    // 🚨 새로 추가된 메서드 1: 약품명
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

    // --- (선택 사항) toString() 오버라이드 ---
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