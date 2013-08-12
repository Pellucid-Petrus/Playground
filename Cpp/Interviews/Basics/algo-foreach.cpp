/*
 * This belong to NON-MODIFYING SEQUENCE OPS
 * for_each is available C++<11
 */
#include <algorithm>
#include <vector>
#include <iostream>

using namespace std;

int main(){
  vector<int> numbers = {1,2,3}; // c++11 initialization
  for (auto& n : numbers)
    cout << n << ","; 
}
