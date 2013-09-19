/**
 * This belong to NON-MODIFYING SEQUENCE OPS
 * using count and count_if
 */
#include <algorithm>
#include <vector>
#include <iostream>

using namespace std;
int main(){
  vector<int> data = {1,2,3};
  data.push_back(4);
  data.insert(data.begin() +1, 6);
  data.erase(data.begin() + 4);

  for(auto& d : data)
    cout << d << ",";
  cout << endl;
  
  // COUNT
  int how_many_3 = count(data.begin(), data.end(), 3);
  cout << "There is/are " << how_many_3 << " 3s in the data" << endl;
  // COUNT_IF iterator count_if(InputIterator first, InputIterator end, UnaryPredicate p)
  int num_divisible_by_3 = count_if(data.begin(), data.end(), [](int i){ return (i % 3) == 0; });
  cout << "Number divisible by 3:" << num_divisible_by_3;    
}

