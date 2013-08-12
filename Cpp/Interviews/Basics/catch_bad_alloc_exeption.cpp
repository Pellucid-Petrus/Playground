#include <iostream>

int main(){
  std::cout << "Trying to allocate 1Gb of RAM" << std::endl;
  char *a = NULL;
  int i = 0;
  while (i < 10) {
    ++i;
    try {
      a = new char[1024 * 1024 * 1024];
    } catch (std::bad_alloc &e) {
      std::cout << "NO WAY\n";
      continue;
    }
    std::cout << "Ok\n";
  }
  //SORRY MAN! :P delete[] a;
  return 0;
}
