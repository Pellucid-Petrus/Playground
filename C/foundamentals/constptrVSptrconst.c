#include <stdio.h>
#include <stdlib.h>

// const ptr - protects value of the pointer 
void test1(const char* a){
  //*a = 'q'; error: assignment of read-only location ‘*a’
  char *b = malloc(sizeof(char));
  *b = 'q';
  a = b; // of course this won't change char a in the main!!
  printf("in TEST1 %c\n", *b);
  printf(" *a=%p *b=%p \n ", a, b);
}

//
void test2(char* const a){
  char *b = malloc(sizeof(char));
  //a = b; // error: assignment of read-only parameter ‘a’
  free(b);
}

int main(){
  char a = 'z';
  printf(" *a=%p\n ", &a);
  test1(&a);
  printf("TEST1 %c\n", a);
  printf(" *a=%p\n ", &a);
}
