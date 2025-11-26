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
    String[] header = {"도서번호", "도서명", "출판사", "저자명", "도서가격", "카테고리"};
    String[] categoryNames = {"IT도서", "소설", "비소설", "경제", "사회"};

    JPanel panS;
    JLabel[] lbls = new JLabel[header.length];
    JTextField[] tf = new JTextField[header.length - 1];
    JComboBox<String> categoryCombo;
    JButton btnAdd;

    public BookInsertView() {
        setLayout(new BorderLayout());
        categoryCombo = new JComboBox(categoryNames);
        btnAdd = new JButton("추가");
        panS = new JPanel(new GridLayout(4, 4));
        for (int i = 0; i < header.length; i++) {
            lbls[i] = new JLabel(header[i]);
            panS.add(lbls[i]);
            if(i < header.length - 1) {
                tf[i] = new JTextField();
                panS.add(tf[i]);
            } else {
                panS.add(categoryCombo);
            }
        }

        for(int i=0; i<3; i++) {
            panS.add(new JLabel(""));
        }
        panS.add(btnAdd);
    }

    public void initView() {
        model = new DefaultTableModel(header, bookVOList.size()){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setPreferredWidth(70);

        JScrollPane scrollPane = new JScrollPane(table);

        pubSearchResult();

        add(scrollPane, BorderLayout.CENTER);
        add(panS, BorderLayout.SOUTH);
    }

    public void pubSearchResult() {
        model.setRowCount(bookVOList.size());
        BookVO vo = null;
        for (int i=0; i<bookVOList.size(); i++) {
            vo = bookVOList.get(i);
            model.setValueAt(vo.getIsbn(), i, 0);
            model.setValueAt(vo.getName(), i, 1);
            model.setValueAt(vo.getPublish(), i, 2);
            model.setValueAt(vo.getAuthor(), i, 3);
            model.setValueAt(vo.getPrice(), i, 4);
            model.setValueAt(vo.getCategoryName(), i, 5);
        }
    }
}