package hospital.view;

import hospital.domain.PrescriptionVO;
import hospital.domain.PrescriptionDetailVO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

public class PharmacyFulfillmentView extends JPanel {

    private JTable fulfillmentTable;
    private DefaultTableModel model;
    private final String[] header = {"ID", "진료ID", "환자 이름", "발행일", "약국ID", "이행 상태"};

    private JTable detailTable;
    private DefaultTableModel detailModel;
    private final String[] detailHeader = {"약품 코드", "약품명", "수량", "용법"};

    private JButton btnStartFulfillment = new JButton("조제 시작");
    private JButton btnCompleteFulfillment = new JButton("조제 완료");
    private JButton btnMarkAsReceived = new JButton("수령 완료");

    private JTextField searchNameField;
    private JButton btnRetrieveByName;
    private ArrayList<PrescriptionVO> currentPrescriptionList;


    public PharmacyFulfillmentView() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel retrievePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        retrievePanel.setBorder(BorderFactory.createTitledBorder("환자 이름으로 처방전 조회"));

        searchNameField = new JTextField(15);
        btnRetrieveByName = new JButton("이름으로 조회");

        retrievePanel.add(new JLabel("환자 이름:"));
        retrievePanel.add(searchNameField);
        retrievePanel.add(btnRetrieveByName);

        topPanel.add(retrievePanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("처방전 이행 상태 관리"));

        buttonPanel.add(btnStartFulfillment);
        buttonPanel.add(btnCompleteFulfillment);
        buttonPanel.add(btnMarkAsReceived);

        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fulfillmentTable = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(fulfillmentTable);

        fulfillmentTable.addMouseListener(tableClickL);

        detailModel = new DefaultTableModel(detailHeader, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        detailTable = new JTable(detailModel);
        JScrollPane detailTableScrollPane = new JScrollPane(detailTable);

        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        detailPanel.add(detailTableScrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, detailPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.7);

        add(splitPane, BorderLayout.CENTER);
    }

    public void setPrescriptionList(ArrayList<PrescriptionVO> list) {
        this.currentPrescriptionList = list;
        detailModel.setRowCount(0);
    }

    public JTable getTable() {
        return fulfillmentTable;
    }

    public PrescriptionVO getSelectedPrescription() {
        int row = fulfillmentTable.getSelectedRow();

        if (row == -1 || currentPrescriptionList == null || row >= currentPrescriptionList.size()) {
            return null;
        }

        return currentPrescriptionList.get(row);
    }

    public void pubSearchResult() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        model.setRowCount(0);

        if (currentPrescriptionList != null) {
            for (PrescriptionVO vo : currentPrescriptionList) {
                String pharmacyIdStr = (vo.getPharmacyId() != null) ? String.valueOf(vo.getPharmacyId()) : "미지정";

                model.addRow(new Object[]{
                        String.valueOf(vo.getPrescriptionId()),
                        String.valueOf(vo.getConsultationId()),
                        vo.getPatientName(),
                        sdf.format(vo.getIssueDate()),
                        pharmacyIdStr,
                        vo.getFulfillmentStatus()
                });
            }
        }
        detailModel.setRowCount(0);
    }

    public void displayDetails(PrescriptionVO vo) {
        detailModel.setRowCount(0);

        if (vo == null) {
            return;
        }

        List<PrescriptionDetailVO> details = vo.getDrugDetails();

        if (details != null && !details.isEmpty()) {
            for (PrescriptionDetailVO detail : details) {
                detailModel.addRow(new Object[]{
                        detail.getDrugCode(),
                        detail.getDrugName(),
                        detail.getQuantity(),
                        detail.getDosage()
                });
            }
        } else {
            detailModel.addRow(new Object[]{"", "[처방된 약품 없음]", "", ""});
        }
    }

    public void updateDetailInfo(PrescriptionVO vo, String newStatus) {
        if (vo == null || newStatus == null) return;

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(String.valueOf(vo.getPrescriptionId()))) {
                model.setValueAt(newStatus, i, 5);
                break;
            }
        }
    }


    MouseAdapter tableClickL = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            PrescriptionVO selected = getSelectedPrescription();
            if (selected != null) {
                displayDetails(selected);
            } else {
                detailModel.setRowCount(0);
            }
        }
    };


    public JButton getBtnStartFulfillment() {
        return btnStartFulfillment;
    }

    public JButton getBtnCompleteFulfillment() {
        return btnCompleteFulfillment;
    }

    public JButton getBtnMarkAsReceived() {
        return btnMarkAsReceived;
    }

    public String getSearchName() {
        return searchNameField.getText().trim();
    }

    public JButton getBtnRetrieveByName() {
        return btnRetrieveByName;
    }
}