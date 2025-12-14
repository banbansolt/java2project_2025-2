package hospital.controller;

import hospital.domain.*;
import hospital.repository.*;
import hospital.view.PatientSearchView;
import hospital.view.PatientInsertView;
import hospital.view.ConsultationView;
import hospital.view.PrescriptionView;
import hospital.view.PharmacyFulfillmentView;
import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HospitalController extends JFrame {

    PatientSearchView searchPan;
    PatientInsertView insertPan;
    ConsultationView consultationPan;
    PrescriptionView prescriptionPan;
    PharmacyFulfillmentView fulfillmentPan;

    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    ConsultationRepository consultationRepository;
    DrugRepository drugRepository;
    PrescriptionRepository prescriptionRepository;
    PrescriptionDetailRepository prescriptionDetailRepository;

    ArrayList<PatientVO> patientVOList;
    ArrayList<DoctorVO> doctorVOList;
    ArrayList<ConsultationVO> consultationVOList;
    List<DrugVO> drugVOList;
    ArrayList<PrescriptionVO> fulfillmentList;

    private PatientVO selectedPatient;
    private ConsultationVO selectedConsultation;

    JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);

    public HospitalController() {
        prescriptionDetailRepository = new PrescriptionDetailRepository();
        patientRepository = new PatientRepository();
        doctorRepository = new DoctorRepository();
        consultationRepository = new ConsultationRepository();
        drugRepository = new DrugRepository();
        prescriptionRepository = new PrescriptionRepository(prescriptionDetailRepository);

        loadInitialData();

        searchPan = new PatientSearchView();
        insertPan = new PatientInsertView();
        consultationPan = new ConsultationView();
        prescriptionPan = new PrescriptionView();
        fulfillmentPan = new PharmacyFulfillmentView();

        refreshPatientSearchTab();
        refreshPatientInsertTab();
        refreshConsultationTab();
        refreshPrescriptionTab();
        refreshFulfillmentTab();

        searchPan.getBtnSearch().addActionListener(btnSearchL);
        tab.add("환자 검색", searchPan);

        insertPan.getBtnAdd().addActionListener(btnInsertL);
        tab.add("환자 등록", insertPan);

        consultationPan.getBtnStartConsultation().addActionListener(btnStartConsultationL);
        consultationPan.getBtnSearchPatient().addActionListener(btnSearchPatientL);
        consultationPan.getTable().addMouseListener(tableConsultationClickL);
        tab.add("진료 기록", consultationPan);

        prescriptionPan.getBtnAddDrug().addActionListener(btnAddDrugL);
        prescriptionPan.getBtnIssuePrescription().addActionListener(btnIssuePrescriptionL);
        tab.add("처방전 발행", prescriptionPan);

        fulfillmentPan.getBtnStartFulfillment().addActionListener(btnStatusUpdateL("조제중"));
        fulfillmentPan.getBtnCompleteFulfillment().addActionListener(btnStatusUpdateL("조제완료"));
        fulfillmentPan.getBtnMarkAsReceived().addActionListener(btnStatusUpdateL("수령완료"));
        fulfillmentPan.getBtnRetrieveByName().addActionListener(btnRetrieveByNameL);
        tab.add("약국 이행 관리", fulfillmentPan);


        add(tab);
        tab.addMouseListener(tabL);

        setTitle("병원 관리 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(1200, 700);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }

    ActionListener btnSearchL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshPatientSearchTab();
        }
    };

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

    ActionListener btnStartConsultationL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ConsultationVO consultVO = consultationPan.getConsultationVOFromInput();

                if (consultVO == null || consultVO.getPatientInfo() == null || consultVO.getDiagnosisName().isEmpty()) {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "환자 선택 및 진단명은 필수입니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (doctorVOList.isEmpty()) {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "등록된 의사 정보가 없어 진료를 기록할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                DoctorVO doctor = doctorVOList.get(0);
                consultVO.setDoctorLicenseNumber(doctor.getLicenseNumber());
                consultVO.setConsultationDateTime(new Date());

                int generatedId = consultationRepository.insert(consultVO);

                if (generatedId > 0) {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "새 진료 기록 등록 완료 (ID: " + generatedId + ")", "성공", JOptionPane.INFORMATION_MESSAGE);
                    consultationPan.clearInput();
                    refreshConsultationTab();
                } else {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "진료 기록 등록에 실패했습니다. (DB 삽입 실패)", "오류", JOptionPane.ERROR_MESSAGE);
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

    ActionListener btnSearchPatientL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchName = consultationPan.getSearchName();
            ArrayList<PatientVO> searchedList = patientRepository.select(searchName);
            PatientVO selected = consultationPan.showPatientSearchDialog(HospitalController.this, searchedList);

            if (selected != null) {
                selectedPatient = selected;
                consultationPan.setSelectedPatientInfo(selectedPatient);
            }
        }
    };

    MouseAdapter tableConsultationClickL = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = consultationPan.getTable().getSelectedRow();

            if (row >= 0 && consultationVOList != null && row < consultationVOList.size()) {
                selectedConsultation = consultationVOList.get(row);
                prescriptionPan.setSelectedConsultation(selectedConsultation);
                tab.setSelectedIndex(3);
            }
        }
    };

    ActionListener btnAddDrugL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            PrescriptionDetailVO detail = prescriptionPan.createPrescriptionDetail();

            if (detail != null) {
                prescriptionPan.addDetail(detail);
                prescriptionPan.getQuantityField().setText("1");
                prescriptionPan.getDosageField().setText("1");
            }
        }
    };


    ActionListener btnIssuePrescriptionL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ConsultationVO consultation = prescriptionPan.getSelectedConsultation();
            List<PrescriptionDetailVO> details = prescriptionPan.getCurrentPrescriptionDetails();

            if (consultation == null) {
                JOptionPane.showMessageDialog(HospitalController.this,
                        "처방전 발행을 위한 진료 기록이 선택되지 않았습니다.", "발행 실패", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (details.isEmpty()) {
                JOptionPane.showMessageDialog(HospitalController.this,
                        "처방할 약품을 최소 하나 이상 추가해야 합니다.", "발행 실패", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PrescriptionVO prescription = new PrescriptionVO();
            prescription.setConsultationId(consultation.getConsultationId());
            prescription.setPharmacyId(String.valueOf(prescriptionPan.getPharmacyId()));
            prescription.setIssueDate(new Date());
            prescription.setFulfillmentStatus("대기");

            try {
                int generatedId = prescriptionRepository.issuePrescription(prescription, details);

                if (generatedId > 0) {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "처방전 발행 성공 (ID: " + generatedId + ")", "성공", JOptionPane.INFORMATION_MESSAGE);

                    prescriptionPan.clearDetails();
                    selectedConsultation = null;

                    refreshFulfillmentTab();

                    tab.setSelectedIndex(4);

                } else {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "처방전 발행에 실패했습니다. (DB 삽입 실패)", "발행 실패", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(HospitalController.this,
                        "DB 오류: 처방전 발행 실패\n" + ex.getMessage(), "DB 오류", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    };

    private ActionListener btnStatusUpdateL(String status) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrescriptionVO selectedPrescription = fulfillmentPan.getSelectedPrescription();

                if (selectedPrescription == null) {
                    JOptionPane.showMessageDialog(HospitalController.this,
                            "목록에서 상태를 변경할 처방전을 먼저 선택해주세요.",
                            "선택 오류", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(HospitalController.this,
                        "처방전 ID [" + selectedPrescription.getPrescriptionId() + "]의 상태를 '" + status + "'(으)로 변경하시겠습니까?",
                        "상태 변경 확인", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        selectedPrescription.setFulfillmentStatus(status);
                        int count = prescriptionRepository.updateFulfillmentStatus(selectedPrescription);

                        if (count > 0) {
                            JOptionPane.showMessageDialog(HospitalController.this,
                                    "처방전 ID [" + selectedPrescription.getPrescriptionId() + "] 상태 업데이트 완료: " + status,
                                    "성공", JOptionPane.INFORMATION_MESSAGE);

                            refreshFulfillmentTab();
                            fulfillmentPan.updateDetailInfo(selectedPrescription, status);
                        } else {
                            JOptionPane.showMessageDialog(HospitalController.this,
                                    "상태 업데이트에 실패했습니다. (DB 삽입 실패)",
                                    "업데이트 실패", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(HospitalController.this,
                                "DB 오류: 상태 업데이트 실패\n" + ex.getMessage(),
                                "오류", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        };
    }

    MouseAdapter tabL = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int index = tab.getSelectedIndex();
            switch (index) {
                case 0:
                    refreshPatientSearchTab();
                    break;
                case 1:
                    refreshPatientInsertTab();
                    break;
                case 2:
                    refreshConsultationTab();
                    break;
                case 3:
                    refreshPrescriptionTab();
                    break;
                case 4:
                    refreshFulfillmentTab();
                    break;
            }
        }
    };


    ActionListener btnRetrieveByNameL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchName = fulfillmentPan.getSearchName();

            try {
                fulfillmentList = prescriptionRepository.selectPrescriptionsByPatientName(searchName);

                fulfillmentPan.setPrescriptionList(fulfillmentList);
                fulfillmentPan.pubSearchResult();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(HospitalController.this,
                        "처방전 조회 오류: " + ex.getMessage(), "DB 오류", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    };


    private void refreshPatientSearchTab() {
        String searchName = searchPan.getSearchName();
        patientVOList = patientRepository.select(searchName);
        searchPan.setPatientVOList(patientVOList);
        searchPan.pubSearchResult();
    }

    private void refreshPatientInsertTab() {
        patientVOList = patientRepository.select("");
        insertPan.setPatientVOList(patientVOList);
        insertPan.pubSearchResult();
    }

    private void refreshConsultationTab() {
        try {
            consultationVOList = consultationRepository.selectAllConsultations();
        } catch (SQLException ex) {
            System.err.println("진료 기록 조회 오류: " + ex.getMessage());
            consultationVOList = new ArrayList<>();
        }

        consultationPan.setDoctorVOList(doctorVOList);
        consultationPan.setConsultationVOList(consultationVOList);
        consultationPan.pubSearchResult();
    }

    private void refreshPrescriptionTab() {
        try {
            drugVOList = drugRepository.selectAllDrugs();
        } catch (SQLException ex) {
            System.err.println("약품 목록 조회 오류: " + ex.getMessage());
            drugVOList = new ArrayList<>();
        }
        prescriptionPan.setAllDrugList(drugVOList);
        prescriptionPan.refreshPrescriptionTab();
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