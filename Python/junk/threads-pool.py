import multiprocessing as mp
import time

class threadsMgr:
	__p = None
	nWorkers = mp.cpu_count()

	def __init__(self):
		""" Initialize a pool of worker processes """

		print "Using %d processes..." % self.nWorkers
		self.__p = mp.Pool(self.nWorkers)

	def __splitInChunks(self, l, n):
		""" split a list l in n chunks """
		# xrange generate starting positions
		for i in xrange(0, len(l), n):
			yield l[i : i+n]
	
	def calculate(self, data, func):
		""" Takes in data a list and func the function to execute on the list """
		queues = self.__splitInChunks(data, len(data)/ self.nWorkers+1)
		self.__p.map(testFunc, queues)

def testFunc(l):
	print "START"
	time.sleep(5)	
	print "DONE"

if __name__ == "__main__":
	tmgr = threadsMgr()
	data = range(100)
	tmgr.calculate(data, testFunc)

