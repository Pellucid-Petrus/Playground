/**
 *
 * STD::Find 
 * This belong to NON-MODIFYING SEQUENCE OPS
 * InputIt find(InputIt first, InputIt last, const T& value)
 */

#include <algorithm>
#include <iostream>

using namespace std;

void testValue(const vector<int>::iterator& iV, const vector<int>& v){
  if (iV != v.end())
    cout << "Item is at position " << iV - v.begin() << endl;
  else
    cout << "Value not found" << endl;
}

int main(){
  vector<int> v = {1,2,3};

  cout << "Looking for 2\n";
  vector<int>::iterator iV= find(v.begin(), v.end(), 2);
  testValue(iV, v);
  
  cout << "Looking for 5\n";
  auto iV2 = find(v.begin(), v.end(), 5);
  testValue(iV2, v);
  
  return 0;
}
