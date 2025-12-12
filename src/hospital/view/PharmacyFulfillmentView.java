package hospital.view;

import hospital.domain.PrescriptionVO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class PharmacyFulfillmentView extends JPanel {

    // --- 1. 필드 선언 ---

    // 처방전 목록 테이블
    private JTable fulfillmentTable;
    private DefaultTableModel model;

    // 이행 상태 관리 버튼
    private JButton btnStartFulfillment = new JButton("조제 시작");
    private JButton btnCompleteFulfillment = new JButton("조제 완료");
    private JButton btnMarkAsReceived = new JButton("수령 완료");

    // 데이터 목록
    private ArrayList<PrescriptionVO> currentPrescriptionList;

    // 테이블 헤더: Controller에서 JOIN하여 가져온 정보 포함
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

        // --- 3. 중앙 패널: 처방전 목록 ---
        model = new DefaultTableModel(header, 0);
        fulfillmentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(fulfillmentTable);

        add(scrollPane, BorderLayout.CENTER);
    }

    // --- 4. Controller 연동을 위한 필수 Getter/Setter/Method ---

    // Controller가 조회 결과를 설정
    public void setPrescriptionList(ArrayList<PrescriptionVO> list) {
        this.currentPrescriptionList = list;
    }

    // 🚨 필수 메서드 1: Controller가 호출하여 현재 선택된 처방전 VO 가져오기
    public PrescriptionVO getSelectedPrescription() {
        int row = fulfillmentTable.getSelectedRow();
        if (row == -1 || currentPrescriptionList == null) {
            JOptionPane.showMessageDialog(this, "목록에서 처방전을 선택하세요.", "선택 오류", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // 테이블 인덱스를 실제 VO 목록 인덱스로 변환하여 반환
        // (현재는 목록이 정렬되어 있지 않다고 가정하고 인덱스 그대로 사용)
        return currentPrescriptionList.get(row);
    }

    // 🚨 필수 메서드 2: Controller가 호출하여 검색/업데이트 결과를 UI에 표시
    public void pubSearchResult() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        model.setRowCount(0); // 기존 데이터 지우기

        if (currentPrescriptionList != null) {
            for (PrescriptionVO vo : currentPrescriptionList) {
                // 약국 ID는 Integer 타입이므로 null 처리 필요
                String pharmacyIdStr = (vo.getPharmacyId() != null) ? String.valueOf(vo.getPharmacyId()) : "미지정";

                model.addRow(new Object[]{
                        vo.getPrescriptionId(),
                        vo.getConsultationId(),
                        vo.getPatientName(), // PrescriptionRepository에서 JOIN하여 가져온 환자 이름
                        sdf.format(vo.getIssueDate()),
                        pharmacyIdStr,
                        vo.getFulfillmentStatus()
                });
            }
        }
    }

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