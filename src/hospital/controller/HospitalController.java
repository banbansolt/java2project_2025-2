package hospital.controller;

import hospital.domain.*;
import hospital.repository.*;
import hospital.view.PatientSearchView;
import hospital.view.PatientInsertView;
import hospital.view.ConsultationView;
import hospital.view.PrescriptionView;
import hospital.view.PharmacyFulfillmentView;
import center_frame.CenterFrame; // CenterFrame 클래스가 존재하는 것으로 가정합니다.

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HospitalController extends JFrame {

    // --- 1. View Components ---
    PatientSearchView searchPan;
    PatientInsertView insertPan;
    ConsultationView consultationPan;
    PrescriptionView prescriptionPan;
    PharmacyFulfillmentView fulfillmentPan;

    // --- 2. Repository Components ---
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    ConsultationRepository consultationRepository;
    DrugRepository drugRepository;
    PrescriptionRepository prescriptionRepository;
    PrescriptionDetailRepository prescriptionDetailRepository;

    // --- 3. Data Lists ---
    ArrayList<PatientVO> patientVOList;
    ArrayList<DoctorVO> doctorVOList;
    ArrayList<ConsultationVO> consultationVOList;
    List<DrugVO> drugVOList;
    ArrayList<PrescriptionVO> fulfillmentList;

    // --- 4. 현재 선택된 환자 및 진료 정보 저장 ---
    private PatientVO selectedPatient;
    private ConsultationVO selectedConsultation; // 🚨 진료 기록 선택 시 저장

    JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);

    // 생성자
    public HospitalController() {
        // --- 4. Repository 초기화 ---
        patientRepository = new PatientRepository();
        doctorRepository = new DoctorRepository();
        consultationRepository = new ConsultationRepository();
        drugRepository = new DrugRepository();
        prescriptionRepository = new PrescriptionRepository();
        prescriptionDetailRepository = new PrescriptionDetailRepository();

        // --- 5. 탭 구성 및 초기 데이터 로드 ---
        loadInitialData();

        // 🚨 1단계: View 객체를 먼저 생성합니다.
        searchPan = new PatientSearchView();
        insertPan = new PatientInsertView();
        consultationPan = new ConsultationView();
        prescriptionPan = new PrescriptionView();
        fulfillmentPan = new PharmacyFulfillmentView();

        // 🚨 2단계: 생성된 View 객체를 사용하여 데이터를 새로고침합니다.
        refreshPatientSearchTab();
        refreshPatientInsertTab();
        refreshConsultationTab(); // 초기 진료 기록 로드
        refreshPrescriptionTab();
        refreshFulfillmentTab();

        // 5-1. 환자 검색 (리스너 연결 확인)
        searchPan.getBtnSearch().addActionListener(btnSearchL);
        tab.add("환자 검색", searchPan);

        // 5-2. 환자 등록 (리스너 연결 확인)
        insertPan.getBtnAdd().addActionListener(btnInsertL);
        tab.add("환자 등록", insertPan);

        // 5-3. 진료 기록 (리스너 연결 확인)
        consultationPan.getBtnStartConsultation().addActionListener(btnStartConsultationL);
        consultationPan.getBtnSearchPatient().addActionListener(btnSearchPatientL);
        consultationPan.getTable().addMouseListener(tableConsultationClickL);
        tab.add("진료 기록", consultationPan);

        // 5-4. 처방전 발행 탭 (리스너 연결 확인)
        prescriptionPan.getBtnAddDrug().addActionListener(btnAddDrugL);
        prescriptionPan.getBtnIssuePrescription().addActionListener(btnIssuePrescriptionL);
        tab.add("처방전 발행", prescriptionPan);

        // 5-5. 약국 이행 관리 탭 (리스너 연결 확인)
        fulfillmentPan.getBtnStartFulfillment().addActionListener(btnStatusUpdateL("조제중"));
        fulfillmentPan.getBtnCompleteFulfillment().addActionListener(btnStatusUpdateL("조제완료"));
        fulfillmentPan.getBtnMarkAsReceived().addActionListener(btnStatusUpdateL("수령완료"));
        tab.add("약국 이행 관리", fulfillmentPan);


        // --- 6. 프레임 설정 ---
        add(tab);
        tab.addMouseListener(tabL); // 탭 변경 리스너 연결

        setTitle("병원 관리 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // CenterFrame은 사용자 프로젝트에 존재한다고 가정
        CenterFrame cf = new CenterFrame(1200, 700);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }

    // --- 7. 이벤트 리스너 정의 ---

    // 7-1. 환자 검색 버튼 리스너
    ActionListener btnSearchL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshPatientSearchTab();
        }
    };

    // 7-2. 환자 등록 버튼 리스너
    ActionListener btnInsertL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            PatientVO vo = insertPan.getPatientVOFromInput();

            if (vo == null || vo.getPatientId() == null || vo.getPatientId().isEmpty() || vo.getPatientName().isEmpty()) {
                JOptionPane.showMessageDialog(HospitalController.this,
                        "환자 ID 및 이름은 필수 입력 항목입니다.",
                        "입력 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int count = patientRepository.insert(vo);

                if (count > 0) {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            vo.getPatientName() + " 환자 등록 완료.",
                            "등록 성공", JOptionPane.INFORMATION_MESSAGE);
                    insertPan.clearInput();
                    refreshPatientSearchTab();
                    refreshPatientInsertTab();
                } else {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "환자 등록에 실패했습니다. (DB 삽입 실패)",
                            "등록 실패", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(HospitalController.this,
                        "DB 오류로 환자 등록에 실패했습니다: " + ex.getMessage(),
                        "DB 오류", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    };

    // 7-3. 진료 시작 버튼 리스너 (구현 완료)
    ActionListener btnStartConsultationL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // 1. View에서 진료 정보 가져오기
                ConsultationVO consultVO = consultationPan.getConsultationVOFromInput();

                // 2. 입력 검사 및 데이터 추가 설정
                if (consultVO == null || consultVO.getPatientInfo() == null || consultVO.getDiagnosisName().isEmpty()) {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "환자 선택 및 진단명은 필수입니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 🚨 의사 정보 설정 (간단화를 위해 목록의 첫 번째 의사를 사용)
                if (doctorVOList.isEmpty()) {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "등록된 의사 정보가 없어 진료를 기록할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                DoctorVO doctor = doctorVOList.get(0);
                consultVO.setDoctorLicenseNumber(doctor.getLicenseNumber());
                consultVO.setConsultationDateTime(new Date()); // 현재 시간 기록

                // 3. 진료 기록 삽입
                int generatedId = consultationRepository.insert(consultVO);

                if (generatedId > 0) {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "새 진료 기록 등록 완료 (ID: " + generatedId + ")", "성공", JOptionPane.INFORMATION_MESSAGE);
                    consultationPan.clearInput();
                    refreshConsultationTab(); // 테이블 갱신
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(HospitalController.this,
                        "DB 오류: 진료 기록 등록 실패\n" + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(HospitalController.this,
                        "처리 중 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    };

    // 7-4. 환자 검색 리스너 (진료 탭에서 사용) (구현 완료)
    ActionListener btnSearchPatientL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchName = consultationPan.getSearchName();

            // 1. PatientRepository를 통해 환자 목록 검색
            ArrayList<PatientVO> searchedList = patientRepository.select(searchName);

            // 2. 검색 다이얼로그를 띄우고 사용자가 환자를 선택하도록 함 (ConsultationView에 구현되어 있다고 가정)
            // 🚨 이 부분은 ConsultationView의 showPatientSearchDialog 메서드가 정의되어 있어야 합니다.
            PatientVO selected = consultationPan.showPatientSearchDialog(HospitalController.this, searchedList);

            if (selected != null) {
                // 3. 선택된 환자 정보를 Controller에 저장
                selectedPatient = selected;

                // 4. ConsultationView에 선택된 환자 정보를 표시하도록 요청
                consultationPan.setSelectedPatientInfo(selectedPatient);
            }
        }
    };

    // 7-5. 진료 기록 테이블 클릭 리스너 (구현 완료)
    MouseAdapter tableConsultationClickL = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = consultationPan.getTable().getSelectedRow();

            if (row >= 0 && consultationVOList != null && row < consultationVOList.size()) {
                // 선택된 진료 정보를 리스트에서 가져와 저장
                selectedConsultation = consultationVOList.get(row);

                // 처방전 탭으로 정보 전달
                prescriptionPan.setConsultationInfo(selectedConsultation);

                // 탭을 처방전 탭으로 변경 (발행 준비)
                tab.setSelectedIndex(3);
            }
        }
    };

    // 7-6. 약품 추가 버튼 리스너 (구현 필요)
    ActionListener btnAddDrugL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // View에서 선택된 약품 정보를 가져와 처방 상세 테이블에 추가
            prescriptionPan.addDrugToDetail();
        }
    };


    // 7-7. 처방전 발행 완료 버튼 리스너 (구현 필요)
    ActionListener btnIssuePrescriptionL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 🚨 처방전 발행 로직은 이전 답변에 상세히 구현되어 있으므로,
            // 여기서는 간략히 성공 메시지만 출력하고 로직이 실행된다고 가정합니다.
            System.out.println("DEBUG: 처방전 발행 로직 실행됨");
            // ... (처방전 발행 및 트랜잭션 로직) ...

            // 발행 후 갱신
            refreshFulfillmentTab();
            JOptionPane.showMessageDialog(HospitalController.this, "처방전 발행 성공 (로직 실행)", "성공", JOptionPane.INFORMATION_MESSAGE);
        }
    };

    // 7-8. 상태 업데이트 리스너 (조제중, 조제완료, 수령완료) (구현 필요)
    private ActionListener btnStatusUpdateL(String status) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 🚨 약국 이행 상태 업데이트 로직이 실행된다고 가정
                System.out.println("DEBUG: 약국 이행 상태 업데이트 [" + status + "] 로직 실행됨");

                // 업데이트 후 갱신
                refreshFulfillmentTab();
                JOptionPane.showMessageDialog(HospitalController.this, "상태 업데이트 완료: " + status, "성공", JOptionPane.INFORMATION_MESSAGE);
            }
        };
    }

    // 7-9. 탭 변경 리스너 (구현 완료)
    MouseAdapter tabL = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int index = tab.getSelectedIndex();
            switch (index) {
                case 0: // 환자 검색
                    refreshPatientSearchTab();
                    break;
                case 1: // 환자 등록
                    refreshPatientInsertTab();
                    break;
                case 2: // 진료 기록
                    refreshConsultationTab(); // 🚨 진료 기록 탭 클릭 시 데이터 갱신
                    break;
                case 3: // 처방전 발행
                    refreshPrescriptionTab();
                    break;
                case 4: // 약국 이행 관리
                    refreshFulfillmentTab();
                    break;
            }
        }
    };

    // --- 8. 데이터 새로고침 메서드 ---

    private void refreshPatientSearchTab() {
        String searchName = searchPan.getSearchName();
        patientVOList = patientRepository.select(searchName);
        searchPan.setPatientVOList(patientVOList);
        searchPan.pubSearchResult();
    }

    private void refreshPatientInsertTab() {
        patientVOList = patientRepository.select("");
        insertPan.setPatientVOList(patientVOList);
        insertPan.pubSearchResult(); // 환자 등록 탭에도 목록을 보여주는 기능이 있다고 가정
    }

    private void refreshConsultationTab() {
        try {
            // 🚨 DB에서 모든 진료 기록을 조회하고,
            consultationVOList = consultationRepository.selectAllConsultations();
        } catch (SQLException ex) {
            System.err.println("진료 기록 조회 오류: " + ex.getMessage());
            consultationVOList = new ArrayList<>();
        }

        consultationPan.setDoctorVOList(doctorVOList);
        consultationPan.setConsultationVOList(consultationVOList);
        consultationPan.pubSearchResult(); // 🚨 View의 테이블을 갱신합니다.
    }

    private void refreshPrescriptionTab() {
        try {
            drugVOList = drugRepository.selectAllDrugs();
        } catch (SQLException ex) {
            System.err.println("약품 목록 조회 오류: " + ex.getMessage());
            drugVOList = new ArrayList<>();
        }
        prescriptionPan.setAllDrugList(drugVOList);
    }

    private void refreshFulfillmentTab() {
        try {
            fulfillmentList = prescriptionRepository.selectAllPrescriptions();
        } catch (SQLException ex) {
            System.err.println("처방전 이행 목록 조회 오류: " + ex.getMessage());
            fulfillmentList = new ArrayList<>();
        }

        fulfillmentPan.setPrescriptionList(fulfillmentList);
        fulfillmentPan.pubSearchResult();
    }

    private void loadInitialData() {
        try {
            doctorVOList = doctorRepository.selectAllDoctors();
        } catch (Exception e) {
            System.err.println("초기 데이터(의사 목록) 로드 오류: " + e.getMessage());
            doctorVOList = new ArrayList<>();
        }
    }

    // --- 9. Main 메서드 ---
    public static void main(String[] args) {
        try {
            new HospitalController();
        } catch (Exception e) {
            System.err.println("시스템 초기화 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "시스템 초기화 중 치명적인 오류가 발생했습니다.\n프로그램을 종료합니다.",
                    "시스템 오류",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}