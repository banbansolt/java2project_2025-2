package hospital.view;

import hospital.domain.PatientVO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class PatientInsertView extends JPanel {

    // --- 1. 컴포넌트 선언 ---
    private JTextField idField;       // DB '정보' 컬럼 값 입력용
    private JTextField nameField;
    // private JTextField residentIdField; // 🚨 제거됨
    private JTextField birthDateField;
    private JTextField addressField;
    private JButton btnAdd;

    private JTable patientTable;
    private DefaultTableModel tableModel;

    // --- 2. 데이터 필드 ---
    private ArrayList<PatientVO> patientVOList;

    // 🚨 컬럼 이름에서 '주민번호' 제거 (4개 컬럼 유지)
    private final String[] columnNames = {"ID", "이름", "생년월일", "주소"};

    public PatientInsertView() {
        setLayout(new BorderLayout());

        // --- 3. 입력 패널 구성 ---
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        idField = new JTextField(15);
        nameField = new JTextField(15);
        // residentIdField = new JTextField(15); // 🚨 제거됨
        birthDateField = new JTextField(15); // 예시: "YYYY-MM-DD"
        addressField = new JTextField(15);
        btnAdd = new JButton("환자 등록");

        // 입력 컴포넌트 추가
        inputPanel.add(new JLabel("환자 ID (정보):"));
        inputPanel.add(idField);

        inputPanel.add(new JLabel("이름:"));
        inputPanel.add(nameField);

        // 🚨 주민번호 필드 제거
        // inputPanel.add(new JLabel("주민등록번호:"));
        // inputPanel.add(residentIdField);

        inputPanel.add(new JLabel("생년월일 (YYYY-MM-DD):"));
        inputPanel.add(birthDateField);

        inputPanel.add(new JLabel("주소:"));
        inputPanel.add(addressField);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnAdd);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);

        // --- 4. 테이블 구성 (등록 후 목록 확인용) ---
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        patientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);

        add(scrollPane, BorderLayout.CENTER);
    }

    // --- 5. Controller 연동 메서드 ---

    /**
     * 입력된 데이터를 PatientVO 객체로 변환하여 반환합니다.
     */
    public PatientVO getPatientVOFromInput() {
        PatientVO vo = new PatientVO();
        vo.setPatientId(idField.getText().trim());
        vo.setPatientName(nameField.getText().trim());
        // vo.setResidentId(residentIdField.getText().trim()); // 🚨 제거됨
        vo.setAddress(addressField.getText().trim());

        // 날짜 파싱
        String dateStr = birthDateField.getText().trim();
        if (!dateStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date birthDate = sdf.parse(dateStr);
                vo.setBirthDate(birthDate);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this,
                        "생년월일 형식이 올바르지 않습니다. (YYYY-MM-DD)",
                        "입력 오류", JOptionPane.ERROR_MESSAGE);
                vo.setBirthDate(null); // 날짜 파싱 실패 시 null 설정
            }
        }
        return vo;
    }

    /**
     * 입력 필드를 초기화합니다.
     */
    public void clearInput() {
        idField.setText("");
        nameField.setText("");
        // residentIdField.setText(""); // 🚨 제거됨
        birthDateField.setText("");
        addressField.setText("");
    }

    // --- Controller가 접근할 수 있도록 Getter 제공 ---

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public void setPatientVOList(ArrayList<PatientVO> list) {
        this.patientVOList = list;
    }

    /**
     * 내부 데이터(patientVOList)를 기반으로 테이블을 갱신합니다.
     */
    public void pubSearchResult() {
        tableModel.setRowCount(0);

        if (patientVOList == null || patientVOList.isEmpty()) {
            return;
        }

        for (PatientVO vo : patientVOList) {
            Vector<Object> rowData = new Vector<>();

            rowData.add(vo.getPatientId());
            rowData.add(vo.getPatientName());
            // 🚨 주민번호 필드 제거

            rowData.add(vo.getBirthDate());
            rowData.add(vo.getAddress());

            // 총 4개 데이터 (4개 컬럼과 일치)
            tableModel.addRow(rowData);
        }
    }
}