/**
 * Exercise: Implementing a stack in C or C++ using either a linked list or a dynamic array.
 * Design the interface to your stack to be complete consistent and easy to use.
 */

#include <iostream>

using namespace std;


/**
 * We can implement a stack using 
 * - Dinamic array - implementation could be not fit in an interview session. cons
 *                    resizing array means coping to a new location. slow if there is no
 *                    euristic involved wihch minimize resize ops.
 * - Linked Lists - easy, but not performant if a lot of small elements are allocated.
 *                  a lot of overhead involved in allocating new elements.
 */

template <class T>
class stack {
  public:

  stack (){
  }
  
  bool push(T* obj){
    
  }

  T* pop() {
    
  } 
};

int main(){
  cout << "Here is my stack!! TATAAAAA!\a";
  stack<int> *s = new(nothrow) stack<int>();
  if (!s)
    return -1;
  
  
  return 0;
}
