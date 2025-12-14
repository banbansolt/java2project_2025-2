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

    // 1-1. 처방전 목록 테이블 (상단)
    private JTable fulfillmentTable;
    private DefaultTableModel model; // 처방전 목록 모델

    // 1-2. 🚨 [수정/추가] 약품 상세 정보 테이블 (하단)
    private JTable detailTable;
    private DefaultTableModel detailModel; // 약품 상세 모델
    private final String[] detailHeader = {"약품 코드", "약품명", "수량", "용법"}; // 상세 테이블 헤더

    // 1-3. 상태 관리 버튼
    private JButton btnStartFulfillment = new JButton("조제 시작");
    private JButton btnCompleteFulfillment = new JButton("조제 완료");
    private JButton btnMarkAsReceived = new JButton("수령 완료");

    // 1-4. 조회 및 데이터
    private JTextField searchNameField;       // 환자 이름 입력 필드
    private JButton btnRetrieveByName;        // 이름으로 조회 버튼
    private ArrayList<PrescriptionVO> currentPrescriptionList;
    private final String[] header = {"ID", "진료ID", "환자 이름", "발행일", "약국ID", "이행 상태"};


    public PharmacyFulfillmentView() {
        setLayout(new BorderLayout());

        // --- 2. 상단 패널: 조회 및 버튼 ---

        JPanel topPanel = new JPanel(new BorderLayout());

        // 2-1. 환자 이름 기반 조회 패널 (WEST)
        JPanel retrievePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        retrievePanel.setBorder(BorderFactory.createTitledBorder("환자 이름으로 처방전 조회"));

        searchNameField = new JTextField(15);
        btnRetrieveByName = new JButton("이름으로 조회");

        retrievePanel.add(new JLabel("환자 이름:"));
        retrievePanel.add(searchNameField);
        retrievePanel.add(btnRetrieveByName);

        topPanel.add(retrievePanel, BorderLayout.WEST);

        // 2-2. 이행 상태 관리 버튼 패널 (EAST로 배치)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("처방전 이행 상태 관리"));

        buttonPanel.add(btnStartFulfillment);
        buttonPanel.add(btnCompleteFulfillment);
        buttonPanel.add(btnMarkAsReceived);

        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // --- 3. 중앙 및 남쪽 패널: 목록 및 상세 정보 ---

        // 3-1. 처방전 목록 테이블 설정 (상단)
        model = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 테이블 편집 불가 설정
            }
        };
        fulfillmentTable = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(fulfillmentTable);

        // 테이블 클릭 리스너 연결 (상세 정보 표시용)
        fulfillmentTable.addMouseListener(tableClickL);

        // 3-2. 🚨 [추가] 상세 약품 테이블 설정 (하단)
        detailModel = new DefaultTableModel(detailHeader, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 테이블 편집 불가 설정
            }
        };
        detailTable = new JTable(detailModel);
        JScrollPane detailTableScrollPane = new JScrollPane(detailTable);

        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBorder(BorderFactory.createTitledBorder("선택된 처방전 약품 상세 내역"));
        detailPanel.add(detailTableScrollPane, BorderLayout.CENTER);


        // 3-3. SplitPane으로 목록 테이블과 상세 테이블 영역 분할
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, detailPanel);
        splitPane.setDividerLocation(300); // 초기 분할 위치 설정
        splitPane.setResizeWeight(0.7); // 목록 테이블이 더 크게 설정

        add(splitPane, BorderLayout.CENTER);
    }

    // --- 4. Controller 연동을 위한 필수 Getter/Setter/Method ---

    public void setPrescriptionList(ArrayList<PrescriptionVO> list) {
        this.currentPrescriptionList = list;
        // 목록이 새로 로드되면 상세 테이블 초기화
        detailModel.setRowCount(0);
    }

    /**
     * 처방전 목록 JTable 자체를 Controller에 넘겨주어 MouseListener를 연결할 수 있도록 합니다.
     */
    public JTable getTable() {
        return fulfillmentTable;
    }

    /**
     * 현재 선택된 처방전 VO 객체를 반환합니다.
     */
    public PrescriptionVO getSelectedPrescription() {
        int row = fulfillmentTable.getSelectedRow();
        if (row == -1 || currentPrescriptionList == null || row >= currentPrescriptionList.size()) {
            return null;
        }
        return currentPrescriptionList.get(row);
    }

    /**
     * Controller에서 조회된 목록을 기반으로 처방전 목록 JTable을 갱신합니다.
     */
    public void pubSearchResult() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        model.setRowCount(0); // 기존 데이터 삭제

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
        // 목록 갱신 시 상세 정보 초기화
        detailModel.setRowCount(0);
    }

    /**
     * 🚨 [수정] 선택된 처방전의 상세 정보를 하단 JTable에 표시합니다.
     * @param vo 표시할 처방전 VO (null이면 초기화)
     */
    public void displayDetails(PrescriptionVO vo) {
        detailModel.setRowCount(0); // 기존 상세 데이터 삭제

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
            // 상세 정보가 없을 경우 메시지를 추가
            detailModel.addRow(new Object[]{"", "[처방된 약품 없음]", "", ""});
        }
    }

    /**
     * 상태 업데이트 버튼 클릭 시, JTable의 상태 컬럼만 즉시 갱신합니다.
     * @param vo 업데이트된 PrescriptionVO 객체
     * @param newStatus 새로운 상태 문자열
     */
    public void updateDetailInfo(PrescriptionVO vo, String newStatus) {
        // 이 메서드는 상태 업데이트 후 목록을 갱신하기 위해 Controller에서 호출됩니다.
        // pubSearchResult가 전체 목록을 갱신하므로 이 메서드는 테이블 목록의 상태만 갱신하는 용도로 변경합니다.

        if (vo == null || newStatus == null) return;

        // 목록 테이블에서 해당 ID를 찾아 상태만 변경
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(String.valueOf(vo.getPrescriptionId()))) {
                model.setValueAt(newStatus, i, 5); // 5번째 컬럼(이행 상태) 업데이트
                break;
            }
        }
    }


    // 테이블 클릭 리스너: 선택된 처방전 정보를 하단 영역에 표시
    MouseAdapter tableClickL = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            PrescriptionVO selected = getSelectedPrescription();
            if (selected != null) {
                // 🚨 JTable에 상세 정보를 표시하는 메서드 호출
                displayDetails(selected);
            }
        }
    };


    // --- 5. Controller 연동용 Getter 메서드 ---

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