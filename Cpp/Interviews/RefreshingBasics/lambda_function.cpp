/**
 * Lambda function
 * allow you to write inline, anonymous functor
 * functor are object that can be treated as func o func ptrs
 * How they work? C++ creates a small class for lambda and overload
 * operator(). The class works as function.
 * 
 * Lambda are defined by
 * [] () -> out_type {}
 * [] is the capture group
 * () in argument
 * -> out_type define the output type
 * {} holds the implementation of the anonymous function
 */
#include <iostream>

using namespace std;
int main(){
  // Assign lambda function to var
  auto func = [] () { return "FUNC"; };
  cout << "TEST1" << func();

  // nice tangled way to define and run anonymouse function 
  cout << "TEST =>" << [](){ return "ciao"; }() << endl;
  
  // Force return values
  {
    auto f = [] () -> double { return 0.2f; };
    cout << "DOUBLE TEST " << f() << endl;
  }
  
  // take in args
  {
    auto f = [] (string s) -> int { return s.size(); };
    cout << "CIAO has " << f("ciao") << " chars" << endl;
  }

  // Closures
  // When a lambda function created a class with operator() is instantiated
  // Vars around are passed to the class as member
  // Closure define which and in which way these vars are passed.
  // possible closures
  // [] -> capture nothing
  // [&] -> capture everything as reference
  // [=] -> capture everything as copy
  // [&,=foo] -> capture everything, but pass foo as value
  // [bar] -> capture bar and pass it as value
  // [this] -> pass pointer of this class to lambda
  //
  {
    int i = 15;
    auto f = [&] { i = 16; };
    f();
    cout << "I=" << i << endl; //prints out 16
  }
}

