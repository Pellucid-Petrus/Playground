/**
 * Compile with gcc > 4.7
 * gcc file.cpp --std=c++11 -pthread
 */

#include <iostream>
#include <thread>
#include <mutex>
#include <unistd.h> //sleep
#include <vector>

#define MAX_SLEEPING_TIME0 // 1sec

using namespace std;
mutex mMutex;

class fakeDownloader{
  //public:  
  //static mutex mMutex;

  public:
  fakeDownloader(){
    cout << "Created new instance" << endl;
  }

  static void workerFunction(){	  
    mMutex.lock();
    cout << "Operation Started" << endl;
    sleep(1);
    cout << "Operation Terminated" << endl;
    mMutex.unlock();
  }

  fakeDownloader& operator()(){
    int i = 10;
    vector<thread*> v;

    while (i) {
      cout << "New thread" << endl;
      // creates thread into the heap
      thread *t = new thread(fakeDownloader::workerFunction);
      v.push_back(t);
      --i;
    }
    cout << "Waiting for the threads to finish before exiting" << endl;
    for (auto &t : v){
      t->join();
    }
    return *this;
  }
  
  // copy costructor called 2 times!
  fakeDownloader(const fakeDownloader& that) {
    cout << "COPY" << endl;
  }
};

int main(){
  fakeDownloader d;

  cout << "Spawn new thread" << endl;
  thread t(d);
  t.join();

  return 0;
}
