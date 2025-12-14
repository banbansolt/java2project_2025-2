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


    private JTextField idField;
    private JTextField nameField;
    private JTextField birthDateField;
    private JTextField addressField;
    private JButton btnAdd;

    private JTable patientTable;
    private DefaultTableModel tableModel;

    private ArrayList<PatientVO> patientVOList;


    private final String[] columnNames = {"ID", "이름", "생년월일", "주소"};

    public PatientInsertView() {
        setLayout(new BorderLayout());


        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        idField = new JTextField(15);
        nameField = new JTextField(15);
        birthDateField = new JTextField(15); // 예시: "YYYY-MM-DD"
        addressField = new JTextField(15);
        btnAdd = new JButton("환자 등록");

        inputPanel.add(new JLabel("환자 ID (정보):"));
        inputPanel.add(idField);

        inputPanel.add(new JLabel("이름:"));
        inputPanel.add(nameField);


        inputPanel.add(new JLabel("생년월일 (YYYY-MM-DD):"));
        inputPanel.add(birthDateField);

        inputPanel.add(new JLabel("주소:"));
        inputPanel.add(addressField);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnAdd);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);


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


    public PatientVO getPatientVOFromInput() {
        PatientVO vo = new PatientVO();
        vo.setPatientId(idField.getText().trim());
        vo.setPatientName(nameField.getText().trim());
        vo.setAddress(addressField.getText().trim());


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
                vo.setBirthDate(null);
            }
        }
        return vo;
    }


    public void clearInput() {
        idField.setText("");
        nameField.setText("");
        birthDateField.setText("");
        addressField.setText("");
    }



    public JButton getBtnAdd() {
        return btnAdd;
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