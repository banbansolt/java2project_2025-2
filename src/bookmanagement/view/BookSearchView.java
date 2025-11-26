package bookmanagement.view;

import bookmanagement.domain.BookVO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class BookSearchView extends JPanel {
    JTable table;
    DefaultTableModel model;
    ArrayList<BookVO> bookVOList;
    String[] header = {"도서번호", "도서명", "출판사", "저자명", "도서가격", "카테고리"};
    JLabel lbl;
    JTextField textSearch;
    JButton btnSearch;
    String searchWord = "";
    JPanel panN;
    JComboBox<String> combo;
    String[] comboStr = {"도서명", "출판사", "저자명"};

    public BookSearchView() {
        setLayout(new BorderLayout());
        combo = new JComboBox(comboStr);
        lbl = new JLabel("검색어: ");
        textSearch = new JTextField(20);
        btnSearch = new JButton("검색");
        panN = new JPanel();
        panN.add(combo);
        panN.add(lbl);
        panN.add(textSearch);
        panN.add(btnSearch);
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

        add(panN, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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

    public String getSearchWord() {
        searchWord = textSearch.getText();
        return searchWord;
    }

    public void setBookVOList(ArrayList<BookVO> bookVOList) {
        this.bookVOList = bookVOList;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JComboBox<String> getCombo() {
        return combo;
    }
}