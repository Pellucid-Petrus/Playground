#include <iostream>
#include <string>
/**
 *               ABC
 *       A        B          C
 *    B    C    A   C      A   B        
 *    C    B    C   A      B   A
 *
 */
using namespace std;

void permutation(string s, int pos = 0){
    //cout << "PER " << s << " " << pos;

    // base case
    if (pos == s.size()-1) {
    	cout << "* " << s << endl;
        return; 
    }
   
    for (int i=pos; i < s.size(); ++i){
      string _s = s;
      swap(_s[pos], _s[i]);
      permutation(_s,pos+1);      
    }
    
}

int main(int argc, char **argv){
    if (argc != 2){
    	cout << "please pass a string as second argument" << endl;
	return -1;
    }
    string s = argv[1];
    cout << "String entered " << s << endl;
    permutation(s);
}
