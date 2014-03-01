import urllib2
from xml.dom import minidom
import re
import time

class huuto:
	oldItemsFilePath = "oldids.dat"
	url_api="http://api.huuto.net/somt/0.9/categories/331/items/?page=%d"
	id_url="http://api.huuto.net/somt/0.9/items/"
        item_url="http://www.huuto.net/kohteet/item/"

        def getData(self, page):
                res = urllib2.urlopen(self.url_api % page)
                xml = res.read()
                return xml

        def getItems(self, page):
		print "Downloading page %d" % page
                xml = self.getData(page)
                xmldoc = minidom.parseString(xml)
                itemlist = xmldoc.getElementsByTagName('entry')
                return itemlist

        def getNewItemsForPage(self, page):
		itemList = []
                for item in self.getItems(page):
			i = dict() 
                        i["title"] = self.__getValue(item, "title")
                        i["time"] = self.__getValue(item, "updated")
			i["id"] = int(re.sub(self.id_url, '', self.__getValue(item, "id")))

			itemList.append(i)
		return itemList

	def getNewItems(self):
		itemlist = []
		page = 0
		while True:
			newItems = self.getNewItemsForPage(page)
			if not len(newItems):
				break
			itemlist.extend(newItems)
			page += 1
		return itemlist

	def showNewItems(self):
		res = list()
		res_with_link = list()

		# get new items
		newItems = self.getNewItems()
		print "Downloaded %d items" % len(newItems)
		# get old ones
		oldItemIDs = []
		try:
			with file(self.oldItemsFilePath, "r") as foldItemsFile:
				oldItemIDs = eval("".join(foldItemsFile.readlines()))
				foldItemsFile.close()
				print "Loaded %d old items" % len(oldItemIDs)
		except IOError as e:
			print "SKIP" 

		# print difference
	        newItemIDs = [x["id"] for x in newItems]
		#print "NEWITEM" + repr(set(newItemIDs))
		#print "oldItems" + repr(set(oldItemIDs))	
		diff = list(set(newItemIDs) - set(oldItemIDs))
		#print "NEW ITEMS:" + repr(newItemIDs)
		#print "OLD ITEMS:" + repr(oldItemIDs)
		#print "DIFF:" + repr(diff)
		i = 0
		for item in diff:
			# get title
			title = None
			for x in newItems:
				if x["id"] == item:
					title = x["title"]
			data_with_link ="%d\t%s%s\t %s" % (i, self.item_url, item, title)
			data = "%s(%s)\n" % (title, item)
			print data_with_link
			res_with_link.append(data_with_link)
			res.append(data)
			i+= 1

		# save new items to db
		with file(self.oldItemsFilePath, "w") as foldItemsFile:
			oldIDs = [x["id"] for x in newItems ]
			oldIDs = set(oldIDs)
			foldItemsFile.write(repr(oldIDs))
			foldItemsFile.close()

		return (res, res_with_link)

        def __getValue(self, mynode, val):
                res = None
                rv = mynode.getElementsByTagName(val)
                for v in rv:
                        var = v.childNodes[0].nodeValue
                        res = var
                return res

	def getNotifications(self):
		# never ending loop!
		while True:
			data = self.showNewItems()
			if len(data[0]):
				self.__sendNotification("\n".join(data[0]))
				self.__sendEmail("\n".join(data[1]))
			time.sleep(60 * 10) # 10 mins

	def __sendNotification(self, TEXT):
		import pynotify
                if not pynotify.init("Urgency"):
                        print "The script cannot display notifications, sorry!"
                        return
		n = pynotify.Notification("New item added toHuuto.net" , TEXT)
                n.set_urgency(pynotify.URGENCY_CRITICAL)
                n.set_timeout(60000)
                if not n.show():
                	print "Failed to send notification"
		
	def __sendEmail(self, TEXT):
		print "Sending email..."
		import smtplib

           	gmail_user = "gnuton.org40691"
            	gmail_pwd = ""
            	FROM = 'noreply@gnuton.org'
            	TO = ['gnuton@gnuton.org'] #must be a list
            	SUBJECT = "Huuto.com -" + TEXT[:20]

            	# Prepare actual message
            	message = """\From: %s\nTo: %s\nSubject: %s\n\n%s
            	""" % (FROM, ", ".join(TO), SUBJECT, TEXT)
            	try:
                	server = smtplib.SMTP("mail.gnuton.org", 587) #or port 465 doesn't seem to work!
                	server.ehlo()
                	server.starttls()
                	server.login(gmail_user, gmail_pwd)
                	server.sendmail(FROM, TO, message)
                	#server.quit()
                	server.close()
                	print 'successfully sent the mail'
            	except Exception as e:
                	print "failed to send mail" + e

if __name__ == "__main__":
        h = huuto()
        #h.showNewItems()
	h.getNotifications()
