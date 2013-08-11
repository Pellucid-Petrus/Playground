#include <thread>
#include <iostream>

using namespace std;

void ciao(int& a){
  a = 40;
}

int main(){
  int a = 3;
  ciao(a);
  cout << "a=" << a;
  
  a=3;
  //The following line won't compile
  // error: invalid initialization of non-const reference of type ‘int&’ from an rvalue of type ‘int’
  // thread t1(ciao, a);
  // solution
  thread t1(ciao, ref(a));
  t1.join();
  cout << "a=" << a << endl;
  return 0;
}
