package bookmanagement.controller;

import bookmanagement.domain.BookVO;
import bookmanagement.repository.BookRepository;
import bookmanagement.view.BookSearchView;
import center_frame.CenterFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BookController extends JFrame {
    BookSearchView searchPan;
    BookRepository bookRepository;
    ArrayList<BookVO> bookVOList;
    JComboBox<String> combo;
    JButton btnSearch;
    JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);

    public BookController() {
        searchPan = new BookSearchView();
        combo = searchPan.getCombo();
        bookRepository = new BookRepository();
        bookVOList = bookRepository.select("", combo.getSelectedIndex());
        searchPan.setBookVOList(bookVOList);
        searchPan.initView();
        searchPan.pubSearchResult();
        btnSearch = searchPan.getBtnSearch();
        btnSearch.addActionListener(btnSearchL);

        tab.add("도서검색", searchPan);
        add(tab);

        setTitle("도서관리시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CenterFrame cf = new CenterFrame(600, 500);
        cf.centerXY();
        setBounds(cf.getX(), cf.getY(), cf.getFw(), cf.getFh());
        setVisible(true);
    }

    ActionListener btnSearchL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            bookVOList = bookRepository.select(searchPan.getSearchWord(), combo.getSelectedIndex());
            searchPan.setBookVOList(bookVOList);
            searchPan.pubSearchResult();
        }
    };

    public static void main(String[] args) {
        new BookController();
    }
}