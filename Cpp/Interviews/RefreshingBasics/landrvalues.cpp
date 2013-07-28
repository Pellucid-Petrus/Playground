/**
 * LValue & - used to alias a variable and pass a value by reference to functions
 * RValue && - used to exted lifetime of modifable var 
 */

#include <string>
#include <iostream>

using namespace std;

// an array cannot be null
int& first(int (&a)[6]){ //legal, but sucks!
  cout << "&a[]=" << &a  << endl;
  return a[0];
}

char& firstLetter(string& s){
  return s[0];
}

void f(int&& x)
{
  cout << "rvalue reference overload f(" << x << ")\n";
}
/*
void f(int x){
  cout << "f(" << x << ")\n";
}*/

int main(){
  // lvalue as alias
  string s = "ciao";
  string &p = s;
  cout << p << endl;
  
  // passing array as reference to a function and getting back another reference! 
  int a[6] = {0,1,2,3,4,5};
  int *b = &first(a);

  cout << "- &a=" << &a << endl
       << "- &a[0]=" << &a[0] << endl
       << "- b" << b << endl;

  //
  cout << "First char " << firstLetter(s) << endl; 
  
  //RValue
  f(3);
}
