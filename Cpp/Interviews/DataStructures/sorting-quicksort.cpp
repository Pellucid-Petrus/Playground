#include <iostream>
#include <algorithm>
#include <vector>

/**
 *  QuickSort
 *         4 2 3 1 5
 *         i   p   j
 *
 *  1. set index i,j and pivot=element in the middle
 *  2. partition step (while i <= j)
 *      - move i towards p until element in i >= p
 *      - move j towards p until element in j <= p
 *      - if i <= j swap elements
 *  3. recursion step
 *      -  run quicksort for left side (left,j), where j is < i after partitioning
 *      -  run quicksort for right side (i, right) 
 */

using namespace std;

void quicksort(vector<int>& a, const int left, const int right){
  //STEP 1 - Choosing pivot and setting indexes
  int i = left;
  int j = right;
  int p = a[(right + left)/2];

  //STEP 2 - Partitioning
  while (i <= j){
    //a. move j -> p until a[j] > p
    while (a[i] < p)
      ++i;
    while (a[j] > p)
      --j;
    if (i <= j){
      swap(a[i], a[j]);
      //move indexes
      ++i;
      --j;
    }
  }

  //STEP 3 - Recursion  
  if (left < j)
    quicksort(a, left, j);

  if (right >i)
    quicksort(a, i, right);
}

int main(){
  //array<int,5> a = {4,2,3,1,5};
  vector<int> a = {8,9,4,3,2,1,5}; 
  quicksort(a, 0, a.size()-1); 
  for (auto& i : a)
    cout << i << ",";
  return 0; 
}
