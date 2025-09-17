package w0917;

import java.io.*;

public class FileReadTest02 {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = null;
        String line = null;
        try {
            br = new BufferedReader(new FileReader(new File("D:\\File test/MyData1.txt")));
            while (true){
                line = br.readLine();
                if (line == null)
                    break;
                sb.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Line read Error");
        }

        try {
            br.close();
        } catch (IOException e) {
            System.out.println("Closing file Error");
        }

        sb.reverse();
        System.out.println(sb);

    }
}