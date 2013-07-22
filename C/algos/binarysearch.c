#include <stdio.h>

int binarySearch(int *a, int aSize, int key){
  // l = low bound, h = high bound, m = medium point
  int l = 0;
  int h = aSize;
  while (l <= h){
    int m = (h - l)/2 + l;
    printf("* l=%d m=%d h=%d a[m]=%d\n", l, m, h,a[m]);
    if (key == a[m])
      return m;

    if (key < a[m]){
      h = m - 1;
    } else {
      l = m + 1;
    }
  }
  return -1;  
}

int main(){
  int k;
  int a[] = {0,11,22,33,44,55,66,77,88,99};
  int arraySize = sizeof(a)/sizeof(int);
  printf("Insert a number to search in the array:");
  scanf("%d", &k);
  printf("Array has %d elements\n", arraySize);
  printf("Item found at position %d\n", binarySearch(a, arraySize, k));  
  return 0;
}
