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

    // --- 1. 필드 선언 ---

    // 진료 시작/기록 입력 필드
    private JTextField tfDiagnosis = new JTextField(40);
    private JButton btnStartConsultation = new JButton("진료 기록");

    // 환자 찾기 기능 필드
    private JTextField tfPatientId = new JTextField(10);
    private JTextField tfPatientName = new JTextField(15);
    private JButton btnSearchPatient = new JButton("환자 찾기");

    // 의사 선택 콤보박스 (현재 사용하지 않지만 구조상 포함)
    private JComboBox<String> comboDoctor;

    // 진료 기록 테이블
    private JTable consultationTable;
    private DefaultTableModel model;

    // 데이터 목록
    private ArrayList<ConsultationVO> consultationVOList;
    private ArrayList<DoctorVO> doctorVOList;

    // 테이블 헤더
    private final String[] header = {
            "진료 ID", "환자 ID", "환자 이름",
            "의사 면허", "의사 이름", "진단명", "진료 일시"
    };

    public ConsultationView() {
        setLayout(new BorderLayout());

        // --- 2. 북쪽 입력 패널 ---
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("새 진료 기록 및 검색"));

        // 2-1. 환자 검색 입력 영역
        JPanel patientSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfPatientName.setEditable(false);

        patientSearchPanel.add(new JLabel("환자 ID:"));
        patientSearchPanel.add(tfPatientId);
        patientSearchPanel.add(btnSearchPatient);

        patientSearchPanel.add(new JLabel("선택된 환자:"));
        patientSearchPanel.add(tfPatientName);

        // 2-2. 진단명 입력 + 진료 버튼
        JPanel combinedInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        combinedInputPanel.add(new JLabel("진단명:"));
        combinedInputPanel.add(tfDiagnosis);
        combinedInputPanel.add(btnStartConsultation);

        inputPanel.add(patientSearchPanel, BorderLayout.NORTH);
        inputPanel.add(combinedInputPanel, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);

        // --- 3. 중앙 테이블 ---
        model = new DefaultTableModel(header, 0);
        consultationTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(consultationTable);

        add(scrollPane, BorderLayout.CENTER);

        // 콤보박스(사용 안 함)
        comboDoctor = new JComboBox<>();
    }

    // --- 4. Controller 연동 메서드 ---

    public void setDoctorVOList(ArrayList<DoctorVO> doctorVOList) {
        this.doctorVOList = doctorVOList;
    }

    public void setConsultationVOList(ArrayList<ConsultationVO> consultationVOList) {
        this.consultationVOList = consultationVOList;
    }

    // 선택된 환자 정보 표시
    public void setSelectedPatientInfo(PatientVO vo) {
        if (vo != null) {
            tfPatientId.setText(vo.getPatientId());
            tfPatientName.setText(vo.getPatientName());
        } else {
            tfPatientId.setText("");
            tfPatientName.setText("");
        }
    }

    // 환자 검색 입력값 반환
    public String getSearchName() {
        return tfPatientId.getText().trim();
    }

    // 진료 입력값 반환
    public ConsultationVO getConsultationVOFromInput() {
        ConsultationVO vo = new ConsultationVO();

        if (tfPatientId.getText().trim().isEmpty() || tfDiagnosis.getText().trim().isEmpty()) {
            return null;
        }

        vo.setPatientInfo(tfPatientId.getText().trim());
        vo.setDiagnosisName(tfDiagnosis.getText().trim());

        return vo;
    }

    // 테이블 출력
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

    // 버튼 및 테이블 Getter
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

    // ============================
    // 🔥 showPatientSearchDialog 추가됨 🔥
    // ============================
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
