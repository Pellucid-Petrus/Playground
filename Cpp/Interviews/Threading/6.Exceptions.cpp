#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
/*
 * Exceptions thrown in a thread are not propagated to
 * the main thread.
 * Solution: catch exceptions in the child thread and store them
 * Read more at http://binglongx.wordpress.com/2010/01/03/handling-c-exceptions-thrown-from-worker-thread-in-the-main-thread/
 */

/*
 The TRY/CATCH clausole won't catch any exception!!
using namespace std;

#define EXCEPTION -1

void badFunction(){
  throw EXCEPTION;
}

int main(){
  try{
    thread t1(badFunction);
    t1.join();
  } catch(const int&  e){
    cout << "Exception " << e << endl;
  }
  return 0;
}
*/

#define EXCEPTION -1
using namespace std;
void badFunction(vector<int>& exps, mutex *m){
  try {
    throw EXCEPTION;
  } catch (int e) {
    lock_guard<mutex> l(*m);
    exps.push_back(e);
  }
}

int main(){
  vector<int> exps;
  mutex m;

  exps.clear();

  thread t1(badFunction, ref(exps), &m);
  t1.join();
  for(auto &e : exps){
    cout << "Exception " << e << endl;
  }
  return 0;
}

