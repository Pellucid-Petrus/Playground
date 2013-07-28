/*
 * Compile with g++ 1.cpp -std=c++11 -pthread
 */

#include <thread>
#include <iostream>

using namespace std;

void myFunc(string s){
  cout << "Hello " << s << " from a thread!" << endl;
}

int main(){
  cout << "What's your name?" << endl;
  string s;
  cin >> s;
  thread t1(myFunc, s);    
  // main waits for t1 to finish
  t1.join();
  return 0;
}

