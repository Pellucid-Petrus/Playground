#include <thread>
#include <iostream>

using namespace std;
int main(int argc, char** argv){
  unsigned int n = thread::hardware_concurrency();
  cout << "CPU threads " << n << endl;
}
