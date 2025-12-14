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
    private final String[] header = {"ID", "진료ID", "환자 이름", "발행일", "약국ID", "이행 상태"};

    // 1-2. 약품 상세 정보 테이블 (하단)
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
    // Controller로부터 받은 현재 표시 중인 처방전 목록
    private ArrayList<PrescriptionVO> currentPrescriptionList;


    // --- 생성자 ---
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

        // 3-2. 상세 약품 테이블 설정 (하단)
        detailModel = new DefaultTableModel(detailHeader, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 테이블 편집 불가 설정
            }
        };
        detailTable = new JTable(detailModel);
        JScrollPane detailTableScrollPane = new JScrollPane(detailTable);

        JPanel detailPanel = new JPanel(new BorderLayout());

        // ************************************************
        // ******************* 수정된 부분 *******************
        // "선택된 처방전 약품 상세 내역"이라는 제목이 있는 TitledBorder를 제거합니다.
        // detailPanel.setBorder(BorderFactory.createTitledBorder("선택된 처방전 약품 상세 내역")); // 원래 코드

        // 단순히 TitledBorder를 없애고 일반 Border나 아무것도 설정하지 않을 수 있습니다.
        // 여기서는 TitledBorder 대신 일반 LineBorder를 설정하여 영역은 구분하되 제목은 없애겠습니다.
        // 만약 경계선 자체를 완전히 없애고 싶다면, 아래 라인을 주석 처리하거나 제거하세요.
        detailPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        // ************************************************

        detailPanel.add(detailTableScrollPane, BorderLayout.CENTER);


        // 3-3. SplitPane으로 목록 테이블과 상세 테이블 영역 분할
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, detailPanel);
        splitPane.setDividerLocation(300); // 초기 분할 위치 설정
        splitPane.setResizeWeight(0.7); // 목록 테이블이 더 크게 설정

        add(splitPane, BorderLayout.CENTER);
    }

    // --- 4. Controller 연동을 위한 필수 Getter/Setter/Method ---

    /**
     * Controller로부터 처방전 목록을 받아 내부 변수에 저장합니다.
     * @param list DB에서 조회된 처방전 목록 (PrescriptionVO)
     */
    public void setPrescriptionList(ArrayList<PrescriptionVO> list) {
        this.currentPrescriptionList = list;
        // 목록이 새로 로드되면 상세 테이블 초기화
        detailModel.setRowCount(0);
    }

    /**
     * 처방전 목록 JTable 자체를 Controller에 넘겨주어 MouseListener를 연결할 수 있도록 합니다.
     * (현재는 View 내부에서 MouseListener를 처리하므로 주로 선택된 VO를 얻는 용도로 사용됨)
     */
    public JTable getTable() {
        return fulfillmentTable;
    }

    /**
     * 현재 선택된 처방전 VO 객체를 반환합니다.
     */
    public PrescriptionVO getSelectedPrescription() {
        int row = fulfillmentTable.getSelectedRow();

        // 선택된 행이 없고, 목록이 유효한지 확인
        if (row == -1 || currentPrescriptionList == null || row >= currentPrescriptionList.size()) {
            return null;
        }

        // JTable의 선택 인덱스가 currentPrescriptionList의 인덱스와 일치한다고 가정
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
                // 약국ID가 null일 경우 '미지정'으로 표시 (DB 스키마에 따라 처리)
                String pharmacyIdStr = (vo.getPharmacyId() != null) ? String.valueOf(vo.getPharmacyId()) : "미지정";

                model.addRow(new Object[]{
                        String.valueOf(vo.getPrescriptionId()),
                        String.valueOf(vo.getConsultationId()),
                        vo.getPatientName(), // PrescriptionVO에 환자 이름 필드가 있어야 함
                        sdf.format(vo.getIssueDate()),
                        pharmacyIdStr,
                        vo.getFulfillmentStatus()
                });
            }
        }
        // 목록 갱신 시 상세 정보 테이블 초기화 (데이터 무결성 유지)
        detailModel.setRowCount(0);
    }

    /**
     * 선택된 처방전의 상세 정보를 하단 JTable에 표시합니다.
     * @param vo 표시할 처방전 VO (내부에 List<PrescriptionDetailVO> 포함)
     */
    public void displayDetails(PrescriptionVO vo) {
        detailModel.setRowCount(0); // 기존 상세 데이터 삭제

        if (vo == null) {
            return;
        }

        // PrescriptionVO에 getDrugDetails() 메서드가 구현되어 있다고 가정
        List<PrescriptionDetailVO> details = vo.getDrugDetails();

        if (details != null && !details.isEmpty()) {
            for (PrescriptionDetailVO detail : details) {
                detailModel.addRow(new Object[]{
                        detail.getDrugCode(),
                        detail.getDrugName(), // PrescriptionDetailVO에 약품명이 포함되어야 함
                        detail.getQuantity(),
                        detail.getDosage()
                });
            }
        } else {
            // 상세 정보가 없을 경우 안내 메시지 추가
            detailModel.addRow(new Object[]{"", "[처방된 약품 없음]", "", ""});
        }
    }

    /**
     * 상태 업데이트 버튼 클릭 시, JTable의 상태 컬럼만 즉시 갱신합니다.
     * (Controller의 pubSearchResult 호출이 완료된 후에만 사용하는 것이 권장됨)
     * @param vo 업데이트된 PrescriptionVO 객체
     * @param newStatus 새로운 상태 문자열
     */
    public void updateDetailInfo(PrescriptionVO vo, String newStatus) {
        if (vo == null || newStatus == null) return;

        // 목록 테이블에서 해당 처방전 ID를 찾아 상태 컬럼(인덱스 5)만 변경
        for (int i = 0; i < model.getRowCount(); i++) {
            // 모델의 0번째 컬럼(처방전 ID)과 업데이트 대상 ID 비교
            if (model.getValueAt(i, 0).equals(String.valueOf(vo.getPrescriptionId()))) {
                model.setValueAt(newStatus, i, 5); // 5번째 컬럼(이행 상태) 업데이트
                break;
            }
        }
    }


    // --- 5. 이벤트 리스너 정의 ---

    // 테이블 클릭 리스너: 선택된 처방전 정보를 하단 영역에 표시
    MouseAdapter tableClickL = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            // 더블 클릭 방지 로직은 생략하고, 싱글 클릭 시 바로 처리
            PrescriptionVO selected = getSelectedPrescription();
            if (selected != null) {
                // 선택된 처방전의 상세 정보를 하단 테이블에 표시
                displayDetails(selected);
            } else {
                // 선택 해제 시 상세 테이블 초기화
                detailModel.setRowCount(0);
            }
        }
    };


    // --- 6. Controller 연동용 Getter 메서드 ---

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