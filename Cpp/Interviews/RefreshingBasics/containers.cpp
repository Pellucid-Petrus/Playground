// http://en.cppreference.com/w/cpp/container
// Understanding STL allocators: http://www.drdobbs.com/the-standard-librarian-what-are-allocato/184403759
// build with g++ filename.c -std=c++11
#include <array>
#include <vector>
#include <forward_list>
#include <list>

#include <iostream>

using namespace std;

void testArray(){
  array<int, 4> a = {1,2,3};
  cout << "ARRAY: ";
  for (auto &i : a)
    cout << i << ',';
  cout << "it contains " << a.size() << "MAX SIZE" << a.max_size()  << endl;  
  a[2] = 2;
  a[3] = 3;
  array<int,4> b;
  b.fill(10);
  a.swap(b); // b.size() == a.size() or it won't compile, if statically declared!!
}

void testVector(){
  // encapsulate dinamic size arrays
  // elements stored contiguosly
  // occupy more space than arrays
  // - capacity() <-- shows ammount of allocated memory
  // - shrink_to_fit() <-- frees extra space
  // - reserve() <-- avoid re-allocation if we know final size beforehand
  // Vector<T> where T has to be CopyAssignable and CopyConstructible.
  vector<int> v;
  v.push_back(2);
  v.pop_back();
  v.push_back(3);
  v.insert(v.begin(),10);
  cout << "SIZE " << v.size() <<" CAPACITY=" << v.capacity() << endl;
  for (auto &i : v)
    cout << i << ",";
  cout << endl;
}

// Singly-linked list 
// - Fast for adding/removing elements from any position
// - iterator invalidated only when element deleted
// - T required to be  MoveConstructible and MoveAssignable
void testForwardList(){
  forward_list<int> l = {1,2,3};  
  l.push_front(4);
  l.pop_front();
  forward_list<int>::iterator i = l.begin();
  while (i != l.end()){
    cout << *i << ",";
    ++i;
  }
  cout << endl;
}


// Doubly-linked list
// constant time insertion
// as forward_list, iterator is not invalidated while adding/removing/moving happens
// it's invalidate only when the corresponding element is deleted
ostream& operator<<(ostream &str, const list<int>& l){
  for(auto& i : l)
    str << " " << i;
  return str;
}

void testList(){
  list<int> l {1,2,3};  
  l.front(); // return first node
  l.back();// return last node
  l.push_back(5);
  l.pop_back();
  l.push_front(5);
  l.pop_front();
  list<int>::iterator i = l.begin();
  cout << "LIST" << l << "\n";
}

int main() {
  /** Sequence containers **/
  testArray(); // C++11
  //testDynArray(); // since C++14;
  
  testVector();
  testForwardList();
  testList();
  //testDeque();

  /** Associative containers **/
  
}
