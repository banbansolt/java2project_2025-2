package hospital.view;

import hospital.domain.PatientVO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class PatientSearchView extends JPanel {

    // --- 1. 컴포넌트 선언 ---
    private JTextField searchNameField;
    private JButton btnSearch;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    // --- 2. 데이터 필드 ---
    private ArrayList<PatientVO> patientVOList;

    // 🚨 수정: "주민번호" 컬럼 제거 (총 4개 컬럼)
    private final String[] columnNames = {"ID", "이름", "생년월일", "주소"};

    public PatientSearchView() {
        setLayout(new BorderLayout());

        // --- 3. 검색 패널 구성 ---
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        searchNameField = new JTextField(20);
        btnSearch = new JButton("환자 검색");

        searchPanel.add(new JLabel("환자 이름:"));
        searchPanel.add(searchNameField);
        searchPanel.add(btnSearch);

        add(searchPanel, BorderLayout.NORTH);

        // --- 4. 테이블 구성 ---
        // 🚨 columnNames가 4개로 변경됨
        tableModel = new DefaultTableModel(columnNames, 0) {
            // 테이블 셀 수정 방지
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        patientTable = new JTable(tableModel);
        scrollPane = new JScrollPane(patientTable);

        add(scrollPane, BorderLayout.CENTER);
    }

    // --- 5. Controller 연동 메서드 (생략) ---
    public String getSearchName() {
        return searchNameField.getText().trim();
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public void setPatientVOList(ArrayList<PatientVO> list) {
        this.patientVOList = list;
    }

    /**
     * 내부 데이터(patientVOList)를 기반으로 테이블을 갱신합니다.
     */
    public void pubSearchResult() {
        // 기존 데이터 삭제
        tableModel.setRowCount(0);

        if (patientVOList == null || patientVOList.isEmpty()) {
            return;
        }

        for (PatientVO vo : patientVOList) {
            Vector<Object> rowData = new Vector<>();

            // 0번 인덱스: ID
            rowData.add(vo.getPatientId());

            // 1번 인덱스: 이름
            rowData.add(vo.getPatientName());

            // 🚨 제거: 주민번호 (DB에 없으므로)
            // rowData.add(vo.getResidentId());

            // 2번 인덱스: 생년월일
            rowData.add(vo.getBirthDate());

            // 3번 인덱스: 주소
            rowData.add(vo.getAddress());

            // 총 4개 데이터 (4개 컬럼과 일치)
            tableModel.addRow(rowData);
        }
    }
}