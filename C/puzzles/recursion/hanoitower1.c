/* Classic Hanoi problem
 * Given 3 pegs and 5 disks, move all disks from A to B. 
 * At each step all disk have to be in decrescent order from the bottom.

   * what happens when you move 1? 1 is moved from peg source to dest.
   * what happens when you move 2 or higher?? 
                     1: (1)->(3 tmp)   2: (1)->(2 dst)   1: (3tmp) ->(2 dst)
    1                  
    2                  2
    3            ==>   3          ==>     3           ====>  3
    4                  4                  4                  4   1
    5                  5       1          5   2   1          5   2
   (1) (2) (3)      (1) (2) (3)        (1) (2) (3)        (1) (2) (3)

    Visualise solution on http://wipos.p.lodz.pl/zylla/games/hf.html
 */

#include <stdio.h>

void moveOneDisk(int disk, int source, int dest) {
  printf("Move disk %d from %d to %d\n", disk, source, dest);
}

void moveDisk(int disk, int source, int dest, int temp) {
  if (disk == 1)
     moveOneDisk(disk, source, dest);
  else {
    int prevDisk = disk -1;
    moveDisk(prevDisk, source, temp, dest);
    moveOneDisk(disk, source, dest);
    moveDisk(prevDisk, temp, dest, source);
  }
}



int main() {
  int pegs[] = {1,2,3};
  moveDisk(3, pegs[0], pegs[1], pegs[2]);
}
