package hospital.view;

import hospital.domain.ConsultationVO;
import hospital.domain.DrugVO;
import hospital.domain.PrescriptionDetailVO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.text.SimpleDateFormat;

public class PrescriptionView extends JPanel {

    private JTextArea consultationInfoArea;
    private JComboBox<String> drugComboBox;
    private JTextField dosageField;
    private JTextField quantityField;
    private JButton btnAddDrug;
    private JButton btnIssuePrescription;
    private JTable detailTable;
    private DefaultTableModel detailTableModel;
    private JLabel totalAmountLabel;
    private JLabel pharmacyIdLabel;

    private ConsultationVO selectedConsultation;
    private List<DrugVO> allDrugList;
    private List<PrescriptionDetailVO> currentPrescriptionDetails;

    public PrescriptionView() {
        setLayout(new BorderLayout(10, 10));

        allDrugList = new ArrayList<>();
        currentPrescriptionDetails = new ArrayList<>();

        consultationInfoArea = new JTextArea("선택된 진료 정보가 없습니다.");
        consultationInfoArea.setEditable(false);
        consultationInfoArea.setPreferredSize(new Dimension(800, 100));
        consultationInfoArea.setBorder(BorderFactory.createTitledBorder("선택된 진료 정보"));
        add(new JScrollPane(consultationInfoArea), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        drugComboBox = new JComboBox<>();
        dosageField = new JTextField("1", 5);
        quantityField = new JTextField("1", 5);
        btnAddDrug = new JButton("약품 추가");

        inputPanel.add(new JLabel("약품 선택:"));
        inputPanel.add(drugComboBox);
        inputPanel.add(new JLabel("용량:"));
        inputPanel.add(dosageField);
        inputPanel.add(new JLabel("수량:"));
        inputPanel.add(quantityField);
        inputPanel.add(btnAddDrug);

        centerPanel.add(inputPanel);

        String[] columnNames = {"약품코드", "약품명", "용량", "수량", "단위가격", "합계"};
        detailTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        detailTable = new JTable(detailTableModel);
        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalAmountLabel = new JLabel("총 합계: 0.0원");
        totalPanel.add(totalAmountLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pharmacyIdLabel = new JLabel("지정 약국 ID:[1]");
        btnIssuePrescription = new JButton("처방전 발행 완료");

        buttonPanel.add(pharmacyIdLabel);
        buttonPanel.add(btnIssuePrescription);

        southPanel.add(totalPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
    }

    public JButton getBtnIssuePrescription() {
        return btnIssuePrescription;
    }

    public JButton getBtnAddDrug() {
        return btnAddDrug;
    }

    public ConsultationVO getSelectedConsultation() {
        return selectedConsultation;
    }

    public JTextField getDosageField() {
        return dosageField;
    }

    public JTextField getQuantityField() {
        return quantityField;
    }

    public void setSelectedConsultation(ConsultationVO consultation) {
        this.selectedConsultation = consultation;
        updateConsultationInfoArea();
        clearDetails();
    }

    public void setAllDrugList(List<DrugVO> drugList) {
        this.allDrugList = drugList;
        updateDrugComboBox();
    }

    public List<PrescriptionDetailVO> getCurrentPrescriptionDetails() {
        return currentPrescriptionDetails;
    }

    public int getPharmacyId() {
        return 1;
    }

    private void updateConsultationInfoArea() {
        if (selectedConsultation != null) {
            Date consultationDate = selectedConsultation.getConsultationDateTime();
            String dateStr = (consultationDate != null) ?
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(consultationDate) : "날짜 정보 없음";

            String info = String.format(
                    "진료 ID: %d | 환자: %s(%s) | 의사: %s(%s) | 진단명: %s | 진료 일시: %s",
                    selectedConsultation.getConsultationId(),
                    selectedConsultation.getPatientName(),
                    selectedConsultation.getPatientInfo(),
                    selectedConsultation.getDoctorName(),
                    selectedConsultation.getDoctorLicenseNumber(),
                    selectedConsultation.getDiagnosisName(),
                    dateStr
            );
            consultationInfoArea.setText(info);
        } else {
            consultationInfoArea.setText("선택된 진료 정보가 없습니다.");
        }
    }

    private void updateDrugComboBox() {
        drugComboBox.removeAllItems();
        if (allDrugList != null) {
            for (DrugVO drug : allDrugList) {
                drugComboBox.addItem(drug.getDrugName() + " (" + drug.getDrugCode() + ")");
            }
        }
    }

    public PrescriptionDetailVO createPrescriptionDetail() {
        if (selectedConsultation == null) {
            JOptionPane.showMessageDialog(this, "먼저 진료 기록을 선택해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String selectedItem = (String) drugComboBox.getSelectedItem();
        if (selectedItem == null || selectedItem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "약품을 선택해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText().trim());
            String dosage = dosageField.getText().trim();

            if (quantity <= 0 || dosage.isEmpty()) {
                JOptionPane.showMessageDialog(this, "용량과 수량을 정확히 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            String drugCode = selectedItem.substring(
                    selectedItem.indexOf("(") + 1,
                    selectedItem.indexOf(")")
            );

            Optional<DrugVO> drugOpt = allDrugList.stream()
                    .filter(d -> d.getDrugCode().equals(drugCode))
                    .findFirst();

            if (drugOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "선택된 약품 코드를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            DrugVO selectedDrug = drugOpt.get();

            PrescriptionDetailVO detail = new PrescriptionDetailVO();
            detail.setDrugCode(drugCode);
            detail.setDosage(dosage);
            detail.setQuantity(quantity);
            detail.setDrugPrice(selectedDrug.getUnitPrice());
            detail.setDrugName(selectedDrug.getDrugName());

            return detail;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "수량은 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void addDetail(PrescriptionDetailVO detail) {
        boolean exists = currentPrescriptionDetails.stream()
                .anyMatch(d -> d.getDrugCode().equals(detail.getDrugCode()));

        if (exists) {
            JOptionPane.showMessageDialog(this, "이미 추가된 약품입니다. 수정하려면 기존 항목을 삭제 후 다시 추가해주세요.", "중복 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentPrescriptionDetails.add(detail);

        double unitPrice = (double) detail.getDrugPrice();
        double totalPrice = unitPrice * detail.getQuantity();

        detailTableModel.addRow(new Object[]{
                detail.getDrugCode(),
                detail.getDrugName(),
                detail.getDosage(),
                detail.getQuantity(),
                unitPrice,
                totalPrice
        });

        updateTotalAmount();
    }

    private void updateTotalAmount() {
        double total = currentPrescriptionDetails.stream()
                .mapToDouble(d -> (double) d.getDrugPrice() * d.getQuantity())
                .sum();

        totalAmountLabel.setText(String.format("총 합계: %.1f원", total));
    }


    public void clearDetails() {
        currentPrescriptionDetails.clear();

        detailTableModel.setRowCount(0);

        dosageField.setText("1");
        quantityField.setText("1");
        updateTotalAmount();
    }

    public void refreshPrescriptionTab() {
        updateConsultationInfoArea();
    }

}