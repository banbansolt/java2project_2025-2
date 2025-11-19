package bookmanagement.test;

import bookmanagement.domain.BookVO;
import bookmanagement.repository.BookRepository;

import java.util.ArrayList;

public class ArrayListTest {
    public static void main(String[] args) {
        BookRepository bookRepository = new BookRepository();
        ArrayList<BookVO> bookVOList = bookRepository.select("", 0);
        for (BookVO bookVO : bookVOList) {
            System.out.println("도서번호: " + bookVO.getIsbn());
            System.out.println("도서명: " + bookVO.getName());
            System.out.println("출판사: " + bookVO.getPublish());
            System.out.println("저자명: " + bookVO.getAuthor());
            System.out.println("=========================================");
        }
    }
}