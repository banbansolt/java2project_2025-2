package hospital.view;

import hospital.domain.ConsultationVO;
import hospital.domain.DoctorVO;
import hospital.domain.PatientVO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ConsultationView extends JPanel {

    private JTextField tfDiagnosis = new JTextField(40);
    private JButton btnStartConsultation = new JButton("진료 기록");

    private JTextField tfPatientId = new JTextField(10);
    private JTextField tfPatientName = new JTextField(15);
    private JButton btnSearchPatient = new JButton("환자 찾기");

    private JComboBox<String> comboDoctor;

    private JTable consultationTable;
    private DefaultTableModel model;


    private ArrayList<ConsultationVO> consultationVOList;
    private ArrayList<DoctorVO> doctorVOList;


    private final String[] header = {
            "진료 ID", "환자 ID", "환자 이름",
            "의사 면허", "의사 이름", "진단명", "진료 일시"
    };

    public ConsultationView() {
        setLayout(new BorderLayout());


        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("새 진료 기록 및 검색"));


        JPanel patientSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfPatientName.setEditable(false);

        patientSearchPanel.add(new JLabel("환자 ID:"));
        patientSearchPanel.add(tfPatientId);
        patientSearchPanel.add(btnSearchPatient);

        patientSearchPanel.add(new JLabel("선택된 환자:"));
        patientSearchPanel.add(tfPatientName);


        JPanel combinedInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        combinedInputPanel.add(new JLabel("진단명:"));
        combinedInputPanel.add(tfDiagnosis);
        combinedInputPanel.add(btnStartConsultation);

        inputPanel.add(patientSearchPanel, BorderLayout.NORTH);
        inputPanel.add(combinedInputPanel, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);


        model = new DefaultTableModel(header, 0);
        consultationTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(consultationTable);

        add(scrollPane, BorderLayout.CENTER);


        comboDoctor = new JComboBox<>();
    }



    public void setDoctorVOList(ArrayList<DoctorVO> doctorVOList) {
        this.doctorVOList = doctorVOList;
    }

    public void setConsultationVOList(ArrayList<ConsultationVO> consultationVOList) {
        this.consultationVOList = consultationVOList;
    }


    public void setSelectedPatientInfo(PatientVO vo) {
        if (vo != null) {
            tfPatientId.setText(vo.getPatientId());
            tfPatientName.setText(vo.getPatientName());
        } else {
            tfPatientId.setText("");
            tfPatientName.setText("");
        }
    }


    public String getSearchName() {
        return tfPatientId.getText().trim();
    }


    public ConsultationVO getConsultationVOFromInput() {
        ConsultationVO vo = new ConsultationVO();

        if (tfPatientId.getText().trim().isEmpty() || tfDiagnosis.getText().trim().isEmpty()) {
            return null;
        }

        vo.setPatientInfo(tfPatientId.getText().trim());
        vo.setDiagnosisName(tfDiagnosis.getText().trim());

        return vo;
    }

    public void pubSearchResult() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        model.setRowCount(0);

        if (consultationVOList != null) {
            for (ConsultationVO vo : consultationVOList) {
                model.addRow(new Object[]{
                        vo.getConsultationId(),
                        vo.getPatientInfo(),
                        vo.getPatientName(),
                        vo.getDoctorLicenseNumber(),
                        vo.getDoctorName(),
                        vo.getDiagnosisName(),
                        sdf.format(vo.getConsultationDateTime())
                });
            }
        }
    }


    public JButton getBtnStartConsultation() {
        return btnStartConsultation;
    }

    public JButton getBtnSearchPatient() {
        return btnSearchPatient;
    }

    public JTable getTable() {
        return consultationTable;
    }

    // 입력 초기화
    public void clearInput() {
        tfPatientId.setText("");
        tfPatientName.setText("");
        tfDiagnosis.setText("");
    }


    public PatientVO showPatientSearchDialog(JFrame parent, ArrayList<PatientVO> list) {

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "검색된 환자가 없습니다.");
            return null;
        }

        String[] names = list.stream()
                .map(vo -> vo.getPatientName() + " (" + vo.getPatientId() + ")")
                .toArray(String[]::new);

        String selectedValue = (String) JOptionPane.showInputDialog(
                parent,
                "환자를 선택하세요:",
                "환자 검색",
                JOptionPane.PLAIN_MESSAGE,
                null,
                names,
                names[0]
        );

        if (selectedValue == null) return null;

        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(selectedValue)) {
                return list.get(i);
            }
        }

        return null;
    }
}
