#include <iostream>

using namespace std;

int fibonacci(int i){
  switch (i) {
    case 1: return 1;
    case 0: return 0;
  }
  return fibonacci(i-1) + fibonacci(i-2); 
}

int main(){
  int i;
  cout << "Insert a number" << endl;
  cin >> i;
  cout << "Fibonacci: " << fibonacci(i) <<endl;
}
