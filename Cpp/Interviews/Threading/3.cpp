#include <iostream>

using namespace std;

int main(){
  int x,y;
  cout << "Enter 2 numbers:" << endl;
  cin >> x >> y;
  if (cin.fail()) {
     cout << "Input not valid" << endl;
     return -1;
  }
  cout << "X=" << x << "Y=" << y << endl;
  return 0;
}
