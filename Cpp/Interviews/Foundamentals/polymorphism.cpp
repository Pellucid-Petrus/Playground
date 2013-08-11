/*
 *  Polymorphism = "multiple forms"
 *  Polymorphism is a concept. 
 *   
 *   Kinf of polymorphism:
 *   1. Adhoc
 *       Function overloading =====> void foo(A a); void foo(B b);
 *       Template specializations => template<typename T> class A;
 *   2. Subtype
 *       subclassing and virtual methods
 *   3. Parametric
 *       C++ templates
 *       C# and Java Generics
 *       Polymorphic functions in ML
 */


#include <iostream>

using namespace std;

class base{
public:
  base(){ cout << "b obj created" << endl;}
  virtual ~base(){ cout << "b destroyed" << endl;}
  void foo(int i){ cout << "foo(int)" << endl; }
  void foo(float f) { cout << "foo(float)" << endl; }
};

class derived : public base {
public:
  derived(): base(){ cout << "d obj created" << endl;}
  ~derived(){ cout << "d destroyed" << endl;}; 
};

template <class T>
T getMax(T a, T b){
  return (a > b ? a : b);
}
 
int main(){
  //1a. adhoc - function overloading
  {
    base b;
    b.foo(1);
    b.foo(1.0f);
  }
  //1b. adhoc - template specialization
  cout << "template test: getMax(double,double)" << getMax(0.11L,0.2L) << endl; 

  //2. subtype polymorphism
  base *b = new derived();
  delete b;

  return 0;
}
