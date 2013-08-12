#include <iostream>
#include <sstream>

using namespace std;

int main(){
  stringstream ss; //allow input and output. See also istringstream & ostringstream
  int i = 3;
  while (i){
    --i;
    string s;
    cin >> s;
    ss << s << ",";
  }
  cout << ss.str() << endl;

  string s;
  ss >> s;
  cout << s << endl;
  return 0;
}
