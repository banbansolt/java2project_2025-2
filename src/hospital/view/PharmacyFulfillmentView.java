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

    // --- 1. 필드 선언 ---

    // 처방전 목록 테이블
    private JTable fulfillmentTable;
    private DefaultTableModel model;

    // 상세 정보 영역
    private JTextArea detailInfoArea;

    // 이행 상태 관리 버튼
    private JButton btnStartFulfillment = new JButton("조제 시작");
    private JButton btnCompleteFulfillment = new JButton("조제 완료");
    private JButton btnMarkAsReceived = new JButton("수령 완료");

    // 데이터 목록
    private ArrayList<PrescriptionVO> currentPrescriptionList;

    // 테이블 헤더
    private final String[] header = {"ID", "진료ID", "환자 이름", "발행일", "약국ID", "이행 상태"};

    public PharmacyFulfillmentView() {
        setLayout(new BorderLayout());

        // --- 2. 북쪽 패널: 이행 상태 관리 버튼 ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("처방전 이행 상태 관리"));

        buttonPanel.add(btnStartFulfillment);
        buttonPanel.add(btnCompleteFulfillment);
        buttonPanel.add(btnMarkAsReceived);

        add(buttonPanel, BorderLayout.NORTH);

        // --- 3. 중앙 및 남쪽 패널: 목록 및 상세 정보 ---

        // 3-1. 테이블 설정
        model = new DefaultTableModel(header, 0);
        fulfillmentTable = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(fulfillmentTable);

        // 테이블 클릭 리스너 연결 (상세 정보 표시용)
        fulfillmentTable.addMouseListener(tableClickL);

        // 3-2. 상세 정보 영역 설정
        JPanel detailPanel = new JPanel(new BorderLayout());

        detailInfoArea = new JTextArea("처방전 상세 정보가 여기에 표시됩니다.");
        detailInfoArea.setEditable(false);
        detailInfoArea.setPreferredSize(new Dimension(800, 150));
        detailInfoArea.setBorder(BorderFactory.createTitledBorder("선택된 처방전 상세 정보"));

        detailPanel.add(new JScrollPane(detailInfoArea), BorderLayout.CENTER);

        // 3-3. SplitPane으로 테이블과 상세 정보 영역 분할
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, detailPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(1.0);

        add(splitPane, BorderLayout.CENTER);
    }

    // --- 4. Controller 연동을 위한 필수 Getter/Setter/Method ---

    public void setPrescriptionList(ArrayList<PrescriptionVO> list) {
        this.currentPrescriptionList = list;
        updateDetailInfo(null, null);
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
                String prescriptionIdStr = String.valueOf(vo.getPrescriptionId());
                String consultationIdStr = String.valueOf(vo.getConsultationId());

                model.addRow(new Object[]{
                        prescriptionIdStr,
                        consultationIdStr,
                        vo.getPatientName(),
                        sdf.format(vo.getIssueDate()),
                        pharmacyIdStr,
                        vo.getFulfillmentStatus()
                });
            }
        }
    }

    /**
     * 선택된 처방전의 상세 정보를 하단 JTextArea에 표시합니다.
     * @param vo 표시할 처방전 VO (null이면 초기화)
     * @param newStatus (선택 사항) 상태 업데이트 버튼 클릭 시 새로운 상태
     */
    public void updateDetailInfo(PrescriptionVO vo, String newStatus) {
        if (vo == null) {
            detailInfoArea.setText("처방전 상세 정보가 여기에 표시됩니다. 목록에서 처방전을 선택해주세요.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String status = (newStatus != null) ? newStatus : vo.getFulfillmentStatus();

        // ------------------ 1. 기본 정보 포맷팅 ------------------
        String info = String.format(
                "--- 처방전 기본 정보 ---\n" +
                        "처방전 ID: %d | 진료 ID: %d\n" +
                        "환자명: %s | 발행일: %s\n" +
                        "지정 약국 ID: %s\n" +
                        "현재 이행 상태: %s\n" +
                        "\n" +
                        "--- 처방된 약품 내역 ---",
                vo.getPrescriptionId(),
                vo.getConsultationId(),
                vo.getPatientName(),
                sdf.format(vo.getIssueDate()),
                (vo.getPharmacyId() != null ? vo.getPharmacyId() : "미지정"),
                status
        );

        // ------------------ 2. 약품 상세 정보 포맷팅 ------------------
        StringBuilder drugDetails = new StringBuilder();
        List<PrescriptionDetailVO> details = vo.getDrugDetails();

        if (details != null && !details.isEmpty()) {
            for (int i = 0; i < details.size(); i++) {
                PrescriptionDetailVO detail = details.get(i);

                String drugName = detail.getDrugName();
                int quantity = detail.getQuantity();
                String dosage = detail.getDosage(); // String 타입

                drugDetails.append(String.format(
                        // 🚨 오류 수정 완료: dosage를 문자열 (%s)로 포맷팅
                        "\n  %d. %s - 수량: %d개, 용법: %s",
                        (i + 1), drugName, quantity, dosage
                ));
            }
        } else {
            drugDetails.append("\n  [처방된 약품 정보 없음]");
        }

        // ------------------ 3. 최종 결합 및 출력 ------------------
        detailInfoArea.setText(info + drugDetails.toString());
    }

    // 테이블 클릭 리스너: 선택된 처방전 정보를 하단 영역에 표시
    MouseAdapter tableClickL = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            PrescriptionVO selected = getSelectedPrescription();
            if (selected != null) {
                updateDetailInfo(selected, null); // 테이블 클릭 시 상태 변경 없이 정보만 표시
            }
        }
    };


    // 상태 변경 버튼 리스너 연결용
    public JButton getBtnStartFulfillment() {
        return btnStartFulfillment;
    }

    public JButton getBtnCompleteFulfillment() {
        return btnCompleteFulfillment;
    }

    public JButton getBtnMarkAsReceived() {
        return btnMarkAsReceived;
    }

}