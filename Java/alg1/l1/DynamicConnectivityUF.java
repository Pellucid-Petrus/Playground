/**
 * Dynamic Connectivity problem - Union Find
 */
import java.util.Arrays;

public class DynamicConnectivityUF{
  
  public class UF{
    int[] id;
    public UF(int N){
      id = new int[N];
      for (int i=0; i < N; i++) {
        id[i] = i;
      }
      System.out.println("Created array" + Arrays.toString(id));
    }

    public void union(int p, int q){
      int Pid = id[p];
      int Qid = id[q];
      // update elements
      for (int i=0; i < id.length; i++){
        if (id[i] == Pid)
          id[i] = Qid;
      }
      System.out.println("Union array" + Arrays.toString(id));
    }

    public boolean connected(int p, int q){
      return id[p] == id[q];
    }
  }

  public static void main(String[] args){
    System.out.println("This solves Dynamic Connectivity problems");
    DynamicConnectivityUF dc = new DynamicConnectivityUF();
    int N = StdIn.readInt();
    UF uf = dc.new UF(N);
    while (!StdIn.isEmpty()){
      int p = StdIn.readInt();
      int q = StdIn.readInt();
      if (!uf.connected(p, q)) {
        uf.union(p, q);
        System.out.println(p + " " + q);
      }
    }   
  }
}
