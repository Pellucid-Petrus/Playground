/**
 * popen/pclose - pipe stream to or from a process
 * see man popen
 * FILE *popen(const char *command, const char *type);
 * int pclose(FILE *stream);
 */

#include <iostream>
#include <stdio.h>
#include <sstream>

using namespace std;

int main(){
  stringstream ss;
  FILE* test = popen("ls /","r");
  if (!test)
    return -1;
  
  char buff[512];
  while(fgets(buff,sizeof(buff),test)) {
    ss << buff;
  }
  
  cout << ss.str();
  pclose(test);  
}
