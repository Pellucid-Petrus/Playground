/**
 * http://www.parashift.com/c++-faq-lite/overview-const.html
 * Using const in C++ is art... of confusing people! :P
 * Here all combinations of const with pointers:
 * 
 * 1) const T       *        p - same as 2, old style - p is a ptr to a const T
 * 2)       T const *        p - same as 1, new style - since T is const, it canno be modified via p. only const method can be called
 * 3)       T       * const  p - p is const ptr to T - 
 * 4)       T const * const  p - p is const ptr to a const T
 */

#include <iostream>

using namespace std;

class foo{
  int mA;
  public:
  void setA (int a) { mA = a; };
  int getA() const {return mA; };
};

// obj points to a foo constant

void ask(const foo* obj){
  //This discard the const qualifier  
  //obj->setA(3);
  obj->getA(); //OK
  obj = new foo;
  delete obj;
}

// obj points to a constant foo object
// the foo object cannot be changed via p
void ask2(foo const * obj){
  //This discard the const qualifier  
  //obj->setA(3);
  obj->getA(); //OK
  obj = new foo;
}

// obj is a const pointer to foo
// you cannot change the pointer, but you can change the obj
void ask3(foo * const obj){
  obj->setA(3);
  obj->getA(); //OK
  //obj = new foo; //ERROR READ ONLY PARAMETER
}

// obj is a const pointer to a const foo obj
void ask4(foo const * const obj) {
  //obj->setA(3); DISCARD CONST QUALIFIER
  obj->getA(); //OK
  //obj = new foo; READ ONLY PARAM
}

int main(){
  foo a;
  ask(&a);
  ask2(&a); 
  return 0;
}
