

public class binarysearch{
  public static void main(String[] args){
    System.out.println("BinarySearch");
    int[] a = new int[]{0,1,2,3,4,5,6,7,8,9};
    int res = algo(a, 2);
    System.out.println(res);
  }

  static int algo(int a[], int key){
    int lo = 0;
    int hi = a.length -1;
    
    while (lo < hi) {
      int mi = ((hi - lo) / 2) + lo;
      if (key < a[mi]){ hi = mi -1;}
      else if (key > a[mi]){ lo = mi +1;}
      else if (key == a[mi]){ return mi;}
    }

    return -1;
  }
}
