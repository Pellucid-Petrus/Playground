# 0, 1, 1, 2, 3, 5 
#
# f(n) = f(n-1) + f(n-2)

# Unoptimized implementation O(2^n)
import sys

def fibonacci(n):
  if n <= 1:
    return n
  else:
    f = fibonacci(n-1) + fibonacci(n-2)
    return f 

if __name__ == "__main__":
  print "Fibonacci.."
  n = int(raw_input("Insert a number:")); 
  print map(fibonacci, range(n));
  
