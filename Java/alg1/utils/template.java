import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class template{

  public static BufferedReader stdinBuffer() {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    return br;
  }

  public static void main(String[] args) throws IOException{
    System.out.println("template");
    
    String input;
    BufferedReader stdinBr = template.stdinBuffer();
    while((input =  stdinBr.readLine()) != null){
      System.out.println(input);
    }
  }
}
