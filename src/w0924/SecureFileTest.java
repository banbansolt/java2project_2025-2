package w0924;

 import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SecureFileTest {
    //    콘솔(키보드) 입력 받은 메시지를 암호화시켜서 파일로 저장
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FileWriter fw = null;
        String line = "";
        String secureOutStr = "";

        try {
            fw = new FileWriter("D:\\File test/secure1.txt");
            while (true){
                System.out.print("Enter Message:");
                line = sc.nextLine();
                if(line.equals(""))
                    break;

                for (int i = 0; i < line.length(); i++) {
                    int code = (int)line.charAt(i);
                    code += 100;
                    secureOutStr  += (char)code;
                }
                fw.write(secureOutStr + "\n");
            }

        } catch (IOException e) {
            System.out.println("File Write Error");
        }

        try {
            fw.close();
            System.out.println("Generated Secure File");
            sc.close();
        } catch (IOException e) {
            System.out.println("Closing File Error");
        }
    }
}
