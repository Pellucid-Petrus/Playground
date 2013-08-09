/*
 * Binary search
 * - requires an ordered array
 * - step 1 - get element in the middle and compare
 * - step 2 - if bigger then half the array and get the sub-array on the right
 *     opposite way in case it's smaller.
 * - step 3 - repeat step 1
 * - base case: no elements left or key found.
 */

#include <iostream>
#define NOT_FOUND -1

using namespace std;

int binarySearch(int array[] , int lower, int upper, int target ){
  cout << "LOW " << lower << " UPPER " << upper << endl;
  // base case
  if (lower == upper)
    if (array[lower] != target)
      return NOT_FOUND;
    else
      return lower;

  // recursive case
  int middle = ((upper-lower)/2)+lower;
  if (middle<0) return NOT_FOUND;

  int pivot = array[middle];
  cout << "MIDDLE " << middle << " PIVOT " << pivot << endl;
  if (pivot == target) return middle;
  if (pivot > target) return binarySearch(array, lower, middle -1, target);
  if (pivot < target) return binarySearch(array, middle +1, upper, target);
}

int main(){
  int v[] = {0,1,2,3,4,5,6,7,8,9,10};
  int i;
  cout << "Search a number from 0 to 9" << endl;
  cin >> i;
  if (i < 0){
    cout << "please enter a positive number" << endl;
    return -1;
  }
  cout << binarySearch(v, 0, (sizeof(v)/sizeof(int))-1, i) << endl;
}   
