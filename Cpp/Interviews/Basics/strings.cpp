/**
 * String
 * http://en.cppreference.com/w/cpp/string
 * - strings are basic_string<char>
 * - basic_string stores element contiguosly. 
 *   -> 1. element can be accessed with operator []
 *      2. pointer to the 
 * - size_t stringA.find(stringB o const char* o char, pos); find the first occourence of stringB in stringA starting from the "pos" char.
 * - converting int to string and viceversa:
 *   - stoi(string,  (from C++0x onwards)  
 * Note: size_t is an unsigned int. it's widely used by STD libs for counts and sizes.
 */

#include <string>
#include <iostream>

using namespace std;
int main(){
  // same as basic_string<char>
  string a("ciao");
  string b="ciao\0\0\0";
  cout << "B has 5 printable chars and 3 null chars. b.size= " << b.size() << endl;
  string c=b;

  basic_string<char> d =c;
  //basic_string<wchar_t> e= d; ERROR copy constructor doesnt' work in this case!!
 
  // STRING AND POINTERS 
  string *p = &a;
  cout << "*p=" << *p << endl; //prints ciao
  cout << "a[0]=" << a[1] << endl; //prints ciao
  (*p)[1] ='X';
  cout << "string a has been modified! " << a << endl;

  // LOOKING UP - use rfind to find backward
  cout << "Find X in a. X has been found at position:" << a.find('X') << endl;
  size_t found =  a.find("XX");
  if (found != string::npos)
    cout << "Find XX in a. XX has been found at position:" << found << endl;
  else
  cout << "XX not found" << endl;

  //CONVERSIONS
  {
    // STRING -> INT
    int i = stoi("-127");
    cout << "i = " << i << endl;

    // INT -> STRING
    string x = to_string(i); 
    cout << "i string = " << x << endl;

   // STRING TO CHAR array
   const char* c = x.c_str();
   x = c;
   cout << " c is " << c << endl;
  }

  {
    // APPENDING
    string x = "ciao";
    const char *a= new char('X');
    x.append(a); // requires pointer to a const char. It takes ownership of obj
    x.append(x);
    x.append(3, 'a'); // appends 'a' 3 times   
    x += 3; // 3 is implicitly converted to char. So this print an unprintable char!! 
    cout << "X is " << x << endl;    
  }

  {
    // CLEAR
    string x = "ciao";
    x.clear();
    cout << "string size " << x.size() << endl;
  }
  
  {
    // String as stack
    string x;
    x.push_back('1');
    cout << "return last char " << x[x.size()-1] << endl;
    x.pop_back(); // remove last char and return void
  }
  {
    // substring
    string x="ciao io mi chiamo gigi";
    cout << "substringing 5,2: "  << x.substr(5,2) << endl;
  }
  return 0;
}
