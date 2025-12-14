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
import java.text.SimpleDateFormat; // 날짜 포매팅을 위해 추가

public class PrescriptionView extends JPanel {

    // --- UI Components ---
    private JTextArea consultationInfoArea; // 선택된 진료 정보 표시
    private JComboBox<String> drugComboBox; // 약품 선택 콤보박스
    private JTextField dosageField; // 용량 입력 필드
    private JTextField quantityField; // 수량 입력 필드
    private JButton btnAddDrug; // 약품 추가 버튼
    private JButton btnIssuePrescription; // 처방전 발행 완료 버튼
    private JTable detailTable; // 처방 상세 내역 표시 테이블
    private DefaultTableModel detailTableModel;
    private JLabel totalAmountLabel; // 합계 금액 표시 레이블
    private JLabel pharmacyIdLabel; // 지정 약국 ID 표시

    // --- Data Fields ---
    private ConsultationVO selectedConsultation; // 현재 선택된 진료 기록 VO
    private List<DrugVO> allDrugList; // Controller에서 받은 전체 약품 목록
    private List<PrescriptionDetailVO> currentPrescriptionDetails; // 현재 처방할 상세 내역 리스트

    public PrescriptionView() {
        setLayout(new BorderLayout(10, 10));

        // 1. Data Fields 초기화
        allDrugList = new ArrayList<>();
        currentPrescriptionDetails = new ArrayList<>();

        // 2. 상단 패널 (선택된 진료 정보)
        consultationInfoArea = new JTextArea("선택된 진료 정보가 없습니다.");
        consultationInfoArea.setEditable(false);
        consultationInfoArea.setPreferredSize(new Dimension(800, 100));
        consultationInfoArea.setBorder(BorderFactory.createTitledBorder("선택된 진료 정보"));
        add(new JScrollPane(consultationInfoArea), BorderLayout.NORTH);

        // 3. 중앙 패널 (약품 선택 및 추가 및 상세 테이블)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // 3-1. 약품 입력 필드 패널
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        drugComboBox = new JComboBox<>();
        dosageField = new JTextField("1", 5); // 용량 기본값
        quantityField = new JTextField("1", 5); // 수량 기본값
        btnAddDrug = new JButton("약품 추가");

        inputPanel.add(new JLabel("약품 선택:"));
        inputPanel.add(drugComboBox);
        inputPanel.add(new JLabel("용량:"));
        inputPanel.add(dosageField);
        inputPanel.add(new JLabel("수량:"));
        inputPanel.add(quantityField);
        inputPanel.add(btnAddDrug);

        centerPanel.add(inputPanel);

        // 3-2. 상세 테이블 설정
        String[] columnNames = {"약품코드", "약품명", "용량", "수량", "단위가격", "합계"};
        detailTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 테이블 편집 불가 설정
            }
        };
        detailTable = new JTable(detailTableModel);
        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        // 4. 하단 패널 (합계 및 발행 버튼)
        JPanel southPanel = new JPanel(new BorderLayout());

        // 4-1. 합계 표시 패널
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalAmountLabel = new JLabel("총 합계: 0.0원");
        totalPanel.add(totalAmountLabel);

        // 4-2. 발행 버튼 및 약국 ID 표시 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pharmacyIdLabel = new JLabel("지정 약국 ID:[1]"); // 임시로 ID 1 고정
        btnIssuePrescription = new JButton("처방전 발행 완료");

        buttonPanel.add(pharmacyIdLabel);
        buttonPanel.add(btnIssuePrescription);

        southPanel.add(totalPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
    }

    // --- 1. Controller가 접근하는 Getter/Setter 메서드 ---

    public JButton getBtnIssuePrescription() {
        return btnIssuePrescription;
    }

    public JButton getBtnAddDrug() {
        return btnAddDrug;
    }

    public ConsultationVO getSelectedConsultation() {
        return selectedConsultation;
    }

    // 🚨 Controller에서 약품 추가 후 입력 필드를 초기화하기 위한 Getter 추가
    public JTextField getDosageField() {
        return dosageField;
    }

    // 🚨 Controller에서 약품 추가 후 입력 필드를 초기화하기 위한 Getter 추가
    public JTextField getQuantityField() {
        return quantityField;
    }

    public void setSelectedConsultation(ConsultationVO consultation) {
        this.selectedConsultation = consultation;
        updateConsultationInfoArea(); // 정보 영역 업데이트
        // 새 진료 선택 시 기존 처방 내역 초기화
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

    // --- 2. UI 업데이트 및 헬퍼 메서드 ---

    private void updateConsultationInfoArea() {
        if (selectedConsultation != null) {
            // 날짜 포맷팅을 위해 SimpleDateFormat을 사용합니다.
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
                // 약품명과 코드를 함께 표시
                drugComboBox.addItem(drug.getDrugName() + " (" + drug.getDrugCode() + ")");
            }
        }
    }

    // 약품 추가 로직 (Controller에서 호출)
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

            // 콤보박스에서 약품 코드 추출
            String drugCode = selectedItem.substring(
                    selectedItem.indexOf("(") + 1,
                    selectedItem.indexOf(")")
            );

            // 해당 약품 정보를 allDrugList에서 찾기 (Java 8 Optional 사용)
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
            // VO에 단위 가격 및 이름 임시 저장 (테이블 표시용)
            detail.setDrugPrice(selectedDrug.getUnitPrice());
            detail.setDrugName(selectedDrug.getDrugName());

            return detail;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "수량은 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // 처방 상세 내역 추가 및 테이블 업데이트
    public void addDetail(PrescriptionDetailVO detail) {
        // 중복 추가 방지
        boolean exists = currentPrescriptionDetails.stream()
                .anyMatch(d -> d.getDrugCode().equals(detail.getDrugCode()));

        if (exists) {
            JOptionPane.showMessageDialog(this, "이미 추가된 약품입니다. 수정하려면 기존 항목을 삭제 후 다시 추가해주세요.", "중복 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentPrescriptionDetails.add(detail);

        // 테이블에 행 추가
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

        updateTotalAmount(); // 합계 업데이트
    }

    // 총 합계 금액 업데이트
    private void updateTotalAmount() {
        double total = currentPrescriptionDetails.stream()
                .mapToDouble(d -> (double) d.getDrugPrice() * d.getQuantity())
                .sum();

        totalAmountLabel.setText(String.format("총 합계: %.1f원", total));
    }


    // --- 3. 필수 초기화/클리어 메서드 ---

    /**
     * 처방전 상세 목록 (currentPrescriptionDetails)과 테이블을 초기화합니다.
     * HospitalController에서 처방전 발행 완료 후 또는 새 진료 기록 선택 시 호출됩니다.
     */
    public void clearDetails() {
        // 1. 내부 데이터 리스트 초기화
        currentPrescriptionDetails.clear();

        // 2. 화면에 보이는 테이블 데이터 초기화
        detailTableModel.setRowCount(0);

        // 3. UI 컴포넌트 초기화
        dosageField.setText("1");
        quantityField.setText("1");
        updateTotalAmount(); // 합계 0으로 리셋
    }

    /**
     * 전체 탭 새로고침 시 필요한 초기화 (Controller에서 탭 전환 시 호출)
     */
    public void refreshPrescriptionTab() {
        updateConsultationInfoArea();
    }

}