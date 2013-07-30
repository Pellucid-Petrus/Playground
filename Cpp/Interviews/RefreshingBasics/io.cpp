#include <fstream>
#include <iostream>

#define FILE_PATH "/tmp/test-icolo"

using namespace std;

int main(){
  // Writing test file
  ofstream out(FILE_PATH);
  out << "CIAO";
  out.close();

  // reading back text file
  ifstream in(FILE_PATH);
  string line;
  while(getline(in, line)){
    cout << line;
  }
  in.close();

  // Writing binary data to file
  
  return 0;
}
