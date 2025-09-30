package w0924;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class SecureDecodeFile {// 암호화된 파일을 읽어서 해독 후 콘솔에 출력
    public static void main(String[] args) {
        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new FileReader("D:\\File test/secure1.txt"));
            while ((line = br.readLine()) != null) {
                String originalStr = "";
                for (int i = 0; i < line.length(); i++) {
                    int code = (int) line.charAt(i);
                    code -= 100; // 암호화할 때 +100 했으니, 해독할 때 -100
                    originalStr += (char) code;
                }
                System.out.println("Decoding: " + originalStr);
            }
        } catch (IOException e) {
            System.out.println("File Read Error");
        } finally {
            try {
                if (br != null) br.close();
                System.out.println("Decoding Finished");
            } catch (IOException e) {
                System.out.println("Closing File Error");
            }
        }
    }
}
