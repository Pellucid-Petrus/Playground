#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main(){
  {
  char *a = "a";
  char *g = "ä";
  // next lines sizeof will print 4 because 'a' and 'ä' are constant and seen as 32-bit integers
  printf("a= %s len=%d sizeof=%d\n", a, strlen(a), sizeof(a)); //strlen 1
  printf("g= %s len=%d sizeof=%d\n", g, strlen(g), sizeof(g)); //strlen 2 
  printf("int size=%d, char size=%d\n", sizeof(int), sizeof(char));
  }

  {   
  // size of an array statically allocated
  int a[] = {1,2,3,4,5};
  printf("array of char size=%d\n", sizeof(a)/sizeof(int));
  }
  {
  // size of an array statically allocated
  char a[5] = {1,2,3,4,5};
  printf("array of char size=%d\n", sizeof(a)/sizeof(int));
  }
  {
  // size of an string statically allocated
  char *a = "ciao";
  printf("string %p size=%d\n", a, strlen(a));
  a = "ciao2";
  printf("string %p size=%d\n", a, strlen(a));
  
  }

}
