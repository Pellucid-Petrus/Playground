/**
 *   String combination:
 *     Rules:
 *     - length: 1 -> string length
 *     - "12" == "21" for a string "123"
 *
 *          w              x           y        z
 *     x    y    z       y   z        z
 *    y z    z          z 
 *   z  
 *
 *   the k-combinations of a set of n numbers = n!/k!(n-k)! 
*/
#include <string>
#include <iostream>
#include <unordered_set>
using namespace std;


void findCombinations(const string& s, string p = "", int pos=0){
  //base case
  if (pos == s.size()){
    return;
  }
  //recursive case
  for (int i=pos; i < s.size(); ++i){
    findCombinations(s, p, i+1);
    p += s[i];
    cout << p << endl;
  }
}

int main(int argc, char **argv){
  if (argc != 2){
    cout << "please pass a string as argument" << endl;
    return -1;
  }
  string s = argv[1];
  findCombinations(s);

  return 0;
}
