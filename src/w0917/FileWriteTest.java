package w0917;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriteTest {
    public static void main(String[] args) {
        FileWriter fw = null;
        String line = null;
        try {
            fw = new FileWriter("D:\\File test/MyData2.txt");
            line = "아이브 원영";
            fw.write(line + "\n");

            line = "아이브 리즈";
            fw.write(line + "\n");

            line = "아이브 가을";
            fw.write(line + "\n");

        } catch (IOException e) {
            System.out.println("FileWriter Generation Error");
        }

        try {
            fw.close();
        } catch (IOException e) {
            System.out.println("FileWriter Close Error");
        }
    }
}