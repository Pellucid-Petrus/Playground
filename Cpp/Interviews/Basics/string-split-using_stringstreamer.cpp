#include <iostream>
#include <sstream>

/*
 * The trick here is to use getline a input string streamer
 */

using namespace std;
int main(){
  istringstream ss("ciao,io,sono,gigi");
  string s;
  while(getline(ss, s, ',')){
    cout << s << endl;
  }
  return 0;
}
