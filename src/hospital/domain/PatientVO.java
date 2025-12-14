package hospital.domain;

import java.util.Date;

public class PatientVO {


    private String patientId;
    private String patientName;
    private Date birthDate;
    private String address;


    public PatientVO() {}



    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }



    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getInfo() {
        return "[" + this.patientId + "] " + this.patientName;
    }


    @Override
    public String toString() {
        return "PatientVO{" +
                "patientId='" + patientId + '\'' +
                ", patientName='" + patientName + '\'' +
                // "residentId는 DB에 없어 제외합니다." +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                '}';
    }
}