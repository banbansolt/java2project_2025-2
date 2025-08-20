package w0820;

public class Arra2Test {
    public static void main(String[] args) {
        int[][] arr1 =new int[2][3];
        int cnt = 1;

        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr1[i].length; j++) {
                arr1[i][j] = cnt++;
            }
        }
            for (int i = 0; i < arr1.length; i++) {
                for (int j = 0; j < arr1[i].length; j++) {
                    System.out.print(arr1[i][j] + "\t");
                }
                System.out.println();
        }
            int[][] arr2 = {{1,3,5},{7,8,9}};
            for (int i = 0; i < arr2.length; i++) {
                for (int j = 0; j < arr2[i].length; j++) {
                    System.out.print(arr2[i][j] + "\t");
                }
                System.out.println();
            }



    }
}
