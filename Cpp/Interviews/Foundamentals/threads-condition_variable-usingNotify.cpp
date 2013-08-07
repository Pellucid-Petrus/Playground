/**
 * condition_variable::notifyOne example
 * This method unblocks any thread waiting on *this
 */
#include <iostream>
#include <thread>
#include <chrono>
#include <condition_variable>

using namespace std;

int i = 0;
mutex cv_m;
condition_variable cv;

// thread running this, will be waiting for i != 0; 
void wait(){
  // does not lock yet
  unique_lock<mutex> lck(cv_m);
  cout << "Waiting" << endl;
  cv.wait(lck, []{ return i != 0; });
  cout << "finished waiting." << endl;  
}

// signal change value of i and tells to other thread to re-check i
void signal(){
  this_thread::sleep_for(chrono::seconds(1));
  cout << "Signal" << endl ; //  cv_m.try_lock() << endl;
  i = 21;
  cv.notify_all();
}

int main(){
  thread t1(wait), t2(signal);
  t1.join();
  t2.join();  
}
