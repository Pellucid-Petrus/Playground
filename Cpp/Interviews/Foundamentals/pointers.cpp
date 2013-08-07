#include <iostream>

using namespace std;

// Passingvalue to function by pointer
void test1(int* a){
  cout << "TEST1" << *a << endl;
  ++(*a);
}

// Passing values by reference
void test2(int& a){
  cout << "TEST2" << a << endl;
  int* x = &a;
  *x = 50;
}

// Copying value
void test3(int a){
  cout << "TEST3" << a << endl;
  int* x = &a;
  *x = 100; // WARNING Modifing local copy!!
}


//
void test4(int* a){
  cout << "TEST4 second element is " << a[1]  << endl;
}

int main(){
  int i = 1;
  test1(&i);
  test1(&i);
  test1(&i);

  test2(i);
  test2(i);

  test3(i);
  test3(i);

  // Array!!
  int a[3] = {0,1,2};
  int* _a = &a[0];
  
  test4(_a);

  return 0;
}
