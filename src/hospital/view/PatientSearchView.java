package hospital.view;

import hospital.domain.PatientVO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class PatientSearchView extends JPanel {

    private JTextField searchNameField;
    private JButton btnSearch;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private ArrayList<PatientVO> patientVOList;

    private final String[] columnNames = {"ID", "이름", "생년월일", "주소"};

    public PatientSearchView() {
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        searchNameField = new JTextField(20);
        btnSearch = new JButton("환자 검색");

        searchPanel.add(new JLabel("환자 이름:"));
        searchPanel.add(searchNameField);
        searchPanel.add(btnSearch);

        add(searchPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        patientTable = new JTable(tableModel);
        scrollPane = new JScrollPane(patientTable);

        add(scrollPane, BorderLayout.CENTER);
    }

    public String getSearchName() {
        return searchNameField.getText().trim();
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public void setPatientVOList(ArrayList<PatientVO> list) {
        this.patientVOList = list;
    }

    public void pubSearchResult() {
        tableModel.setRowCount(0);

        if (patientVOList == null || patientVOList.isEmpty()) {
            return;
        }

        for (PatientVO vo : patientVOList) {
            Vector<Object> rowData = new Vector<>();

            rowData.add(vo.getPatientId());
            rowData.add(vo.getPatientName());
            rowData.add(vo.getBirthDate());
            rowData.add(vo.getAddress());

            tableModel.addRow(rowData);
        }
    }
}