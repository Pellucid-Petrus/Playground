/*
 * Telephone words
 *  telephone layout is like this
 * 
 *     1           2(ABC)      3(DEF)     
 *     4(GHI)      5(JKL)      6(MNO)
 *     7(PRS)      8(TUV)      9(WXY)
 *                 0
 *
 *  print all possible phrases from a
 *  7-digits long number
 * 
 * number of combinations = min: 2+3^5 and max 3^7
 */
#include <iostream>
#include <string>

using namespace std;

#define NOT_VALID_RANGE -1

const int minCharAllowed = static_cast<int>('0'); 
const int maxCharAllowed = static_cast<int>('9'); 

/*
 * first= int A + place[1-3] + offset
 */
char getLetter(int telephoneKey, int place){
  int first = (int) 'A';
  int firstTelephoneKey = 2;
  switch(telephoneKey){
    case 0: return '0';
    case 1: return '1';
    default:
      int offset = (telephoneKey- firstTelephoneKey) *3;
      return (char) (first + offset + place);
  }  
}

void getWord(const string& s, string w=""){
  // base case
  if (s.size() == w.size()){
    cout << w << endl;
    return;
  }

  // recursive case
  int i=w.size();
  int key = static_cast<int>(s[i]);

  //validating input
  if (key < minCharAllowed || key > maxCharAllowed)
    throw NOT_VALID_RANGE;

  //normalizing
  key -= minCharAllowed;
    
  int j = key>=2 ? 0 : 2;
  for (; j <3; ++j){
      string _w = w;
      char letter = getLetter(key, j);
      _w += letter;
      getWord(s,_w);
  }    
}

int main(){
  string s;
  while (s.size() != 7){
    cout << "insert a 7-digits long number:" << endl;
    cin >> s;
  }
  cout << "Possible words for: " << s << " are:" << endl;
  getWord(s);

  return 0;
}  
