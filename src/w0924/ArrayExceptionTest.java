package w0924;
//RunTime(Unchecked) Exception
//ArrayIndexOutOfBoundsException
public class ArrayExceptionTest {

    public static void main(String[] args) {
        int[] arr = {10, 20, 30};

        try{
            arr[3] = 40;

            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i] + '\t');
            }
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("배열의 인덱스 번호가 범위를 벗어난 것 같습니다.");
        }



    }
}