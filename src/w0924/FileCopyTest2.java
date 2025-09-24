package w0924;

 import java.io.*;

public class FileCopyTest2 {

    public static void copyFile() throws FileNotFoundException, IOException {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        fr = new FileReader("D:\\File test/MyData1.txt");
        br = new BufferedReader(fr);
        fw = new FileWriter("D:\\File test/CopyFile1.txt");
        String line;

            while ((line = br.readLine()) != null) {
                fw.write(line + "\n");
            }

            br.close();
            fw.close();
            System.out.println("복사가 완료되었습니다.");

        }
        public static void main(String[] args) {
            try {
                copyFile();
            }catch (FileNotFoundException e){
                System.out.println("File Not Found");
            }catch(Exception e){
                System.out.println("File Write Error");
            }
    }

}
