/*
 * Have a look at http://c.comsci.us/etymology/literals.html
 */
#include <iostream>
#include <string>

using namespace std;
int main(){
  // boolean
  bool b = true;

  //chars
  char c = 'c'; //unqualified - used to rapresent char in strings. Max chars 2^8 =256
  unsigned char uc = 'c'; // some system can define char as not positive this force all chars to be in the range 0-255 
  signed char sc = 'c';
  wchar_t wc = 'c';
  //char16_t c16= 'ä';
  //char32_t c32 = 'ö';
  
  //integers
  //  short, long, long long
  // unsigned, signed 
  int i = 1;
  unsigned ui = 1u;
  long l = 1l;
 
  // floats
  float f = 1.0f; //f or F
  double d = 1.0; //64bit
  long double ld = 1.0L;//96bit
  cout << "float=" << sizeof(float) 
       << " double=" << sizeof(double) 
       << " long double=" << sizeof(ld)
       << "short=" << sizeof(short) 
       << "sizeof int " << sizeof(int) 
       << "sizeof long " << sizeof(long) 
       << "size of a ptr" << sizeof (void*);
  return 0;
}
