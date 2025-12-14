package hospital.domain;

public class DrugVO {

    private String drugCode;
    private String drugName;
    private String manufacturer;
    private int unitPrice;



    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }


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