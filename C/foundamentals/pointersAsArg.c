#include <stdio.h>

//This shows that in C, arguments are passed by value.
//test changes the value of the local copy
void test(int *a){
  int *b = malloc(sizeof(int));
  *b = 13;
  a = b;
}

void test2(int *a) {
  *a = 14;
}

void test3(int **a) {
  int *b = malloc(sizeof(int));
  *b = 15;
  *a = b;
}

int main(){
  int *a = malloc(sizeof(int));
  *a = 12;
  test(a);
  printf("TEST1 %d\n", *a);
  test2(a);
  printf("TEST2 %d\n", *a);
  test3(&a);
  printf("TEST3 %d\n", *a);  
}
