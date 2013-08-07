#include <iostream>
#include <new>
/*** Oh man this doesn't work if build with g++ 4.7.2 ***/

void nomem(){
  std::cout << "HOLALA!! Running out of mem, man?\n";
}

int main(){
  std::cout << "Testing new handler\n";
  std::new_handler(nomem);

  int i = 10;
  while (i) {
    --i;
    char *a = new char[1024 * 1024 * 1024]; // Try to allocate 1Gb
    
  }
  std::cout << "Exiting";
  return 0;
}
