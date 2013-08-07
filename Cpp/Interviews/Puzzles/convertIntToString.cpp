#include <iostream>
#include <cmath>

using namespace std;

const int asciiCode = (int) '0';

string toString(unsigned int i){
  string s;
  // cout << sizeof(int); 4 -> 2^32 MAX
  unsigned int d = 1000000000; 
  //We could use here sprintf instead.
  while(d >= 1){
    int n = i/d;
    if (n) {
      char *c = new char( asciiCode + n);
      i -= n * d;
      s.append(c);
    }
    d /=10;
  }
  return s;
}

int main(){
  // test1
  unsigned int i;
  cout << "Insert a number" << endl;
  cin >> i;
   
  cout << "String: " << toString(i) << endl;
  
  //Test 2
  
}
