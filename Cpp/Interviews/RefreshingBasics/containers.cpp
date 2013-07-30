// http://en.cppreference.com/w/cpp/container
// Understanding STL allocators: http://www.drdobbs.com/the-standard-librarian-what-are-allocato/184403759
// build with g++ filename.c -std=c++11
#include <array>
#include <vector>
#include <forward_list>
#include <list>

#include <set>
#include <map>

#include <unordered_set>
#include <unordered_map>

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
// constant time insertion/deletion
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

// Sets are associative data structure
// Stores sorted unique keys.
// It's implemented as a red-black tree
// which ensure logarithmic complexity for searching, adding, removing
// Has iterators which go in both ways

ostream& operator<<(ostream &str,set<int>&s){
  for (auto& e : s)
    str << ", " << e;
  return str;
}

void testSet(){
  set<int> s;
  s.insert(1);
  s.insert(2);
  cout << "SET" << s << endl;
  list<int> l;
  copy(s.begin(), s.end(), back_inserter(l)); //back_inserter performes push_back
  cout << "LIST copied from SET " << l << endl;
  cout << "Finding " << *s.find(1) << endl;
  cout << "Finding 55: found? " << (s.find(55) != s.end()) << endl; //Find returns end() interator if not found
}

// MAP
// Associative container which contains Key-Value pairs
// Sorted by key
// Implemented using a red-black tree
// Search, removal and insertion are logarithimic operations
void testMap(){
  map<string, int> m;
  m["ciao"] = 4;
  m.clear();
  cout << "is map empty? " << m.empty() << endl;
  m["figo"] = string("figo").size();
  map<string, int> m2;
  swap(m, m2);
  cout << "M2 size=" << m2.size() << endl;
  cout << "M size=" << m.size() << endl;

}
// Unordered set, implemented as hashmap 
void testUnorderedSet() {
  unordered_set<int> us = {1,2,3,1,1,1};
  for (auto& i : us)
    cout << "," << i;
  cout << endl;
  unordered_set<int>::hasher fn = us.hash_function(); 
  us.insert(4);
  cout << "initial value=" << 1  << "hashed value=" << fn(1) << "\n";
  cout << "Set has " << us.size() << " elements\n";
}

// Unordered MAP, here is another hashmap 
void testUnorderedMap(){
  unordered_map<string,int> um{{"uno",1}, {"due",2}};
  um["test"] = 3;
  um["ciao"] = -12;
  for (auto& pair : um)
    cout << "(key=" <<pair.first << ",value= " << pair.second <<")";
  cout << endl;
  um.clear();
  cout << "XX " << (um["xx"] ? "it's in the map" : "it's not in the map") << endl;
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
  testSet();  
  testMap();

  /** unordered associative containers **/
  testUnorderedSet();
  testUnorderedMap();  
}
