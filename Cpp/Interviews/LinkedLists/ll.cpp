#include <iostream>
#include <stdio.h>
//#include <string>

#include <stdlib.h> // malloc

using namespace std;

class linkedList {

  typedef struct node {
    int data;
    node* link;
  } node;

  node *mHead;

  public:
  linkedList(): mHead(NULL){
  
  }
  
  ~linkedList() {
    //clean memory
    clean();
  }

  void clean() {
    cout << "Cleaning data\n";
    node *n = mHead;
    while(n) {
      cout << "Item Deleted\n";
      n = n->link;
      delete n;
    }
  }

  void append(int data){
    // create new node
    node* n = new node(nothrow);
    n->data = data;
    n->link = NULL;

    // update previous tail link or head if there are no elements
    node* tail = getTail();
    if (tail)
      tail->link = n;
    else
      mHead = n; 
  }

  void prepend(int data){
    // create a new node
    node *n = new(nothrow) node(); // avoid throwing std::bad_alloc exceptions
    if (!n)
      return;

    // pupulate node data
    n->data = data;
    n->link = mHead;
 
    // update head
    mHead = n;
  }

  void insert(int data, int pos) {
    //TODO :P    
  }
  
  //For a better design: do not expose node pointers!
  node* getHead() {
    return mHead;
  }

  int count() {
    int c = 0;
    node *n = mHead;

    while (1) {
      if (!n)
        break;
      n = n->link;
      // since c is an integer there are no real diff betweeen pre or post-increment
      // for no simple types
      // Foo& Foo::operator++()  <-- called by ++i
      // Foo Foo::operator++(int ignored) <-- i++
      ++c;
    }
    
    return c;
  }
  
  //For a better design: do not expose node pointers!
  node* getTail(){
    node *n = mHead;
    while(1){
      if (n && n->link)
        n = n->link;
      else
        return n;
    }
  }

  // This is method has been written just as exercise!!
  // If something goes wrong we get a nice buffer overflow! :D some people may like it! :P
  const char* toString() {
    char *s = reinterpret_cast<char*>(malloc(( 2 * count()) + 2 * sizeof(char)));
    char *_s = s;
    // Add fist char to string
    *_s = '[';
    ++_s;
    
    // add chars
    node* n = getHead();
    while (n) {
      // let's assume we have integers < 10
      int i = n->data < 10 ? n->data : 9;
      int asciiOffset = static_cast<int>('0'); //It's 48, see ascii table
      char c = static_cast<char>(i + asciiOffset);
      *_s = c;
      ++_s;
      *_s =',';
      ++_s;

      // next one!
      n = n->link;
    }    
    // close square brakets
    *_s = ']';
    ++_s;

    // terminate the string
    *_s = '\0';
    return s;
  }

 /* or you can simply use C++ version!
  string toString() {
    ...
  }
 */

};

int main() {
  /* iostram basics
  int a;
  cin >> a;
  cout << "hello world " << a << "\n"; // IOSTREAM
  */

  // Playing with Linked lists
  linkedList* ll = new linkedList();
  ll->prepend(5);
  printf("Head contains %d\n", ll->getHead()->data);
  printf("Linked list contains %d\n", ll->count());
  printf("Adding 3 elements\n");
  ll->prepend(7);
  ll->prepend(6);
  ll->append(11);
  printf("Linked list contains %d\n", ll->count());
  const char * llDump = ll->toString();
  printf("Linked list = %s\n", llDump);
  free(static_cast<const char*>(llDump)); //Free the buffer

  // Clean
  delete ll;

  return 0;
}
