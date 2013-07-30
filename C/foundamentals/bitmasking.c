#include <stdio.h>

// To define binary constants we use gcc extensions 
// http://gcc.gnu.org/onlinedocs/gcc/Binary-constants.html

int main(){
  //Turning bit off (OR)
  unsigned int x = 0b010; // 010
  unsigned int maskOff = 0b101; // maks which shoud turn bit off
  printf("Switching off bit - %d\n", x & maskOff);
  
  //Turning bit on (AND)
  unsigned int maskOn = 0b100; // turns on the 3rd bit
  printf("Switching on bit - %d\n", x | maskOn); // results should be 110 = 6
  
  //Toggling bits (XOR)
  unsigned int maskToggle = 0b001; // toggle only the first bit
  printf("toggling first bit:%d\n", x ^ maskToggle); // result 011 = 3

  //Querying status (AND)
  unsigned int maskQuery = 0b010;
  unsigned int maskQuery2 = 0b110;
  printf("is the 2nd bit on? %s\n", maskQuery == (x & maskQuery) ? "true" : " false");
  printf("is the 3nd bit on? %s\n", maskQuery2 == (x & maskQuery2) ? "true" : "false");  
}
