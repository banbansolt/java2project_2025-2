package bookmanagement.view;

import bookmanagement.domain.BookVO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class BookInsertView extends JPanel {
    JTable table;
    DefaultTableModel model;
    ArrayList<BookVO> bookVOList;
    String[] header = {"도서번호", "도서명", "출판사", "저자명","도서가격","카테고리"};
    String[] categoryNames = {"IT도서", "소설", "비소설", "경제", "사회"};

    JPanel panS;
    JLabel[] lbls = new JLabel[header.length];
    JTextField[] tf = new JTextField[header.length - 1];
    JComboBox<String> categoryCombo;
    JButton btnAdd;

    public BookInsertView(){
        setLayout(new BorderLayout());
        categoryCombo = new JComboBox(categoryNames);
        btnAdd = new JButton("도서추가");
        panS = new JPanel(new GridLayout(4, 4));
        for (int i = 0; i < header.length; i++) {
            lbls[i] = new JLabel(header[i]);
            panS.add(lbls[i]);
            if(i < header.length - 1){
                tf[i] = new JTextField();
                panS.add(tf[i]);
            }else{
                panS.add(categoryCombo);
            }
        }

        for (int i = 0; i < 3 ; i++) {
            panS.add(new JLabel(" "));
        }
        panS.add(btnAdd);
    }

    //    JTable과 DefaultTableModel을 연결하고 테이블과 관련된 내용을 초기화
    public void initView(){
        model = new DefaultTableModel(header,bookVOList.size()){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
//        테이블 컬럼너비
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setPreferredWidth(70);

//        테이블의 행의 개수가 많을 때는 스크롤바가 필요
        JScrollPane scrollPane = new JScrollPane(table);

//        각 셀에 리스트에 저장된 BookVO객체가 가지고 있는 값들을 설정
        pubSearchResult();

//        현재패널에 Center에는 스크롤바가 있는 테이블, South에는 도서추가 패널
        add(scrollPane, BorderLayout.CENTER);
        add(panS, BorderLayout.SOUTH);
    }

    //    DefaultTableModel에 도서정보들을 설정한다.
    public void pubSearchResult(){
//        model에 행개수 설정
        model.setRowCount(bookVOList.size());
        BookVO vo = null;
        for (int i = 0; i < bookVOList.size(); i++) {
            vo = bookVOList.get(i);
            model.setValueAt(vo.getIsbn(), i,0);
            model.setValueAt(vo.getName(), i,1);
            model.setValueAt(vo.getPublish(), i,2);
            model.setValueAt(vo.getAuthor(), i,3);
            model.setValueAt(vo.getPrice(), i,4);
            model.setValueAt(vo.getCategoryName(), i,5);
        }
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public void setBookVOList(ArrayList<BookVO> bookVOList) {
        this.bookVOList = bookVOList;
    }

    public BookVO neededInsertData() {
        BookVO vo = new BookVO();
        vo.setIsbn(Integer.parseInt(tf[0].getText()));
        vo.setName(tf[1].getText());
        vo.setPublish(tf[2].getText());
        vo.setAuthor(tf[3].getText());
        vo.setPrice(Integer.parseInt(tf[4].getText()));
        vo.setCategoryName((String)categoryCombo.getSelectedItem());
        return vo;
    }

    public void initInsertData(){
        for (int i = 0; i < tf.length; i++) {
            tf[i].setText("");
        }
        categoryCombo.setSelectedIndex(0);
    }
}