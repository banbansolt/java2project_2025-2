package hospital.domain;

public class DrugVO {

    private String drugCode;    // 약품코드 (PRIMARY KEY)
    private String drugName;    // 🚨 필수 추가: 약품명
    private String manufacturer; // 제조사
    private int unitPrice;      // 단위가격

    // --- Getter and Setter Methods ---

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    // 🚨 새로 추가된 Getter/Setter
    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    // --- (선택 사항) toString() 오버라이드 ---
    @Override
    public String toString() {
        return "DrugVO{" +
                "drugCode='" + drugCode + '\'' +
                ", drugName='" + drugName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }
}