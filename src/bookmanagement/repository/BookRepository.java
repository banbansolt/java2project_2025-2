package bookmanagement.repository;

import bookmanagement.domain.BookVO;
import oracle.jdbc.proxy.annotation.Pre;

import java.awt.print.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// 책 정보를 불러오고/입력하는 등의 기능을 이곳에 정의.
public class BookRepository {
    ArrayList<BookVO> bookVOList;

    public ArrayList<BookVO> select(String searchWord, int selectedIndex) {
        Connection con = JDBCConnector.getConnection();
        bookVOList = new ArrayList<BookVO>();
        ResultSet rs = null;
        PreparedStatement psmt = null;
        String[] columnName = {"name", "publish", "author"};

        // ----------------------------------------------------------------------
        // ★ ORA-00904 최종 수정: 컬럼 이름이 소문자로 생성되었을 가능성을 고려하여
        // "category_name" (큰따옴표로 감싼 소문자)로 변경
        // ----------------------------------------------------------------------
        String sql = "select isbn, name, publish, author, price, category as category_name from book, category where book.category = category.category_id and "+ columnName[selectedIndex] +" like ?";

        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, "%"+searchWord+"%");
            rs = psmt.executeQuery();
            while(rs.next()){
                BookVO bookVO = new BookVO();
                bookVO.setIsbn(rs.getInt("isbn"));
                bookVO.setName(rs.getString("name"));
                bookVO.setPublish(rs.getString("publish"));
                bookVO.setAuthor(rs.getString("author"));
                bookVO.setPrice(rs.getInt("price"));
                bookVO.setCategoryName(rs.getString("category_name"));
                bookVOList.add(bookVO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("오류 발생 "+e.getMessage());
        } finally {
            try {
                if(rs != null) {
                    rs.close();
                }

                if(psmt != null) {
                    psmt.close();
                }

                if(con != null) {
                    con.close();
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

        return bookVOList;
    }

    // ... (insert, update, delete 메서드 생략 - 오류와 무관함)
    public void insert(BookVO vo) {
        Connection con = JDBCConnector.getConnection();
        String sql = "insert into book values(?,?,?,?,?,?)";
        PreparedStatement pstmt = null;


        try {
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, vo.getIsbn());
            pstmt.setString(2, vo.getName());
            pstmt.setString(3, vo.getPublish());
            pstmt.setString(4, vo.getAuthor());
            pstmt.setInt(5, vo.getPrice());

            int categoryId = 0; // 카테고리 아이디는 정해진 카테고리 내에서만 입력해야 하므로,

            switch(vo.getCategoryName()) {
                case "IT도서":
                    categoryId = 10;
                    break;
                case "소설":
                    categoryId = 20;
                    break;
                case "비소설":
                    categoryId = 30;
                    break;
                case "경제":
                    categoryId = 40;
                    break;
                case "사회":
                    categoryId = 50;
                    break;
            }

            pstmt.setInt(6, categoryId);
            pstmt.executeUpdate(); //executeQuery는 select할 때 쓰는거임.
        } catch (SQLException e) {
            System.out.println("오류 발생: "+e.getMessage());
        } finally {
            try{
                if(pstmt != null)
                    pstmt.close();

                if(con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("insert close 문제 발생");
                e.printStackTrace();
            }
        }
    }

    public void update(BookVO vo) {
        Connection con = JDBCConnector.getConnection();
        String sql = "update book set name=?, publish=?, author=?, price=?, category=? where isbn=?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, vo.getName());
            pstmt.setString(2, vo.getPublish());
            pstmt.setString(3, vo.getAuthor());
            pstmt.setInt(4, vo.getPrice());

            int categoryId = 0;
            switch(vo.getCategoryName()) {
                case "IT도서":
                    categoryId = 10;
                    break;
                case "소설":
                    categoryId = 20;
                    break;
                case "비소설":
                    categoryId = 30;
                    break;
                case "경제":
                    categoryId = 40;
                    break;
                case "사회":
                    categoryId = 50;
                    break;
            }

            pstmt.setInt(5, categoryId);
            pstmt.setInt(6, vo.getIsbn());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(pstmt != null)
                    pstmt.close();

                if(con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("update close 문제 발생");
                e.printStackTrace();
            }
        }
    }

    public void delete(BookVO vo) {
        Connection con = JDBCConnector.getConnection();
        String sql = "DELETE FROM BOOK WHERE ISBN = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, vo.getIsbn());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(pstmt != null)
                    pstmt.close();

                if(con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("delete close 문제 발생");
                e.printStackTrace();
            }
        }
    }
}