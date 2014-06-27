/**
 * Dynamic Connectivity problem - weighted Quick Union
 */
import java.util.Arrays;

public class DynamicConnectivityQU{
  
  public class QU{
    int[] id;
    int[] sz;
    public QU(int N){
      id = new int[N];
      sz = new int[N];
      for (int i=0; i < N; i++) {
        id[i] = i;
        sz[i] = 1; 
      }
      System.out.println("Created array" + Arrays.toString(id));
      System.out.println("Created array" + Arrays.toString(sz));
    }

    private int root(int i){
      while(id[i] != i) {
        i = id[i];
      }
      return i;
    }

    public void union(int p, int q){
      // check bigger tree
      int rP = root(id[p]); 
      int rQ = root(id[q]);
      if (rP == rQ) return;
      if (sz[rP] >= sz[rQ]){
        id[rQ] = id[rP];
        sz[rP] += sz[rQ];
      } else {
        id[rP] = id[rQ];
        sz[rQ] += sz[rP];
      }
      System.out.println("Union array" + Arrays.toString(id));
    }

    public boolean connected(int p, int q){
      return root(id[q]) == root(id[p]);
    }
  }

  public static void main(String[] args){
    System.out.println("This solves Dynamic Connectivity problems");
    DynamicConnectivityQU dc = new DynamicConnectivityQU();
    int N = StdIn.readInt();
    QU uf = dc.new QU(N);
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
