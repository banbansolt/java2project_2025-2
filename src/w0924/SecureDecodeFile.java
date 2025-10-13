package w0924;

import java.io.*;

public class SecureDecodeFile {
    public static void main(String[] args) {
        // 암호화된 파일 경로 (SecureFileTest 에서 만든 경로와 동일하게 맞춤)
        String inputPath = "D:\\File test/secure1.txt";
        String outputPath = "D:\\File test/DecodingSecureFile.txt";

        try (
                FileReader fr = new FileReader(inputPath);
                BufferedReader br = new BufferedReader(fr);
                FileWriter fw = new FileWriter(outputPath)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                StringBuilder decodedLine = new StringBuilder();

                for (int i = 0; i < line.length(); i++) {
                    int code = line.charAt(i);
                    code -= 100;  // 암호화 때 +100 했으니 -100 해서 복호화
                    decodedLine.append((char) code);
                }

                fw.write(decodedLine.toString() + "\n"); // 줄바꿈 유지
            }

            System.out.println("복호화 완료: " + outputPath);

        } catch (FileNotFoundException e) {
            System.out.println("암호화된 파일을 찾을 수 없습니다: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("파일 읽기/쓰기 오류: " + e.getMessage());
        }
    }
}
