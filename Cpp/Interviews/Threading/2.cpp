#include <iostream>
#include <thread>

using namespace std;

class greetings{
  string mMsg;
  const string* _mMsg;

  public:
  greetings(const string& msg) {
    mMsg = msg; //Creates a copy
    _mMsg = &msg; // Force thread to use resource created on the main thread 
    cout << "object created! mmMsg=" << &mMsg 
         << " and msg=" << &msg 
         << endl; 
  }

  // invoked by the thread
  greetings operator()(){
    // This shows that mMsg is not the one created in the costructor.
    cout << "Printing out var s:" << mMsg << ", mem address:" << &mMsg << endl;

    // Here printed out the var in the main thread
    cout << "Printing out var s in the main thread:" << *_mMsg << ", mem address:" << _mMsg << endl;
    return *this;
  }
};

int main(){
  string s;
  cout << "What's your name?\n";
  cin >> s;
  cout << "S is store in " << &s << endl;

  greetings g(s);

  // function object is copied into the internal storage
  // accessible to the new thread. Threads invokes operator()
  thread t1(g);
  t1.join();
}
