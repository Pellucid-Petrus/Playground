#!/usr/bin/python
"""
    *******************************************************
    *             DynDNS client for Tophost.it            *
    *                                                     *
    *  Copyrights:                                        *
    *      Daniele Paganelli                              *
    *      Antonio Aloisio <gnuton@gnuton.org>            *
    *                                                     *
    *  License: CreativeCommons Attribution-ShareAlike    *
    *                                                     *
    *******************************************************
"""
import os

##### CONFIGURATION #####
# Set tophost credentials as your environment variables or replace the ones you can find below
userid = os.environ.get("TH_USER", "yourdomain.it")
passwd = os.environ.get("TH_PASS", "yourpassword")


# Domain names to which you wanna update the IP address. 
# it can be a list like ["home", "gameserver"]. Those records must be already set in cpanel and they have to be A records.
DYNAMIC_HOSTS=['home']


##### DO NOT EDIT THIS###
logip='/tmp/thdns.ip'

storedip=None
test=True
ip=''

help="""Utilizzo: thdns [<opzioni> <ip>]
ARGOMENTI:
	[<ip>]: Aggiorna i nomi all'ip dato in argomento (equivalente al flag -i <ip>, o --ip=<ip>).
OPZIONI:
	-f [<ip>], --forza[=<ip] : Forza l'aggiornamento (nota: non abusare di questa opzione per non sovraccaricare i server)


	-h, --help : stampa questo messaggio di aiuto

Tutte le opzioni e gli argomenti sono facoltativi. Nel caso non venga specificato un ip ne' in argomento ne' tra le opzioni, verra' ottenuto in rete tramite il servizio checkip.dyndns.org (non funziona con i proxy).

Esempi:
	thdns -f 192.168.1.1 -> forza l'aggiornamento all'ip
	thdns 192.168.1.1 / thdns -g 192.168.1.1 -> se %s non corrisponde a 192.168.1.1, aggiorna
	thdns -> ottiene l'indirizzo dalla rete; se non corrisponde a %s, aggiorna (equivalente a thdns -g)
""" % (logip,logip,logip)

ua='User-Agent','thdns/0.1 (+http://daniele.modena1.it/code/thdns/view)'

#######################

from httplib import HTTPSConnection,HTTPS
from urllib2 import urlopen
from urllib import urlencode
from base64 import encodestring
from string import strip
from sys import argv,exit
from os.path import exists
from getopt import getopt

def getip():
	"""Ottieni l'ip da checkip.dyndns.org"""
        print " - Detecting public IP"
	si='Address: '
	r=urlopen('http://checkip.dyndns.org').read()
	i=r.find(si)+len(si)
	e=r.find('<',i)
	return r[i:e]

def getsid():
	"""Connetti al cpanel per ottenere l'id di sessione"""
	# "Basic" authentication encodes userid:password in base64. Note
	# that base64.encodestring adds some extra newlines/carriage-returns
	# to the end of the result. string.strip is a simple way to remove
	# these characters.
        print " - Fetching session id from CPanel for user %s" % userid
	global sid,psi
	auth = 'Basic ' + strip(encodestring(userid + ':' + passwd))
	conn = HTTPSConnection('cp.tophost.it')
	conn.putrequest('GET', '/dnsjump.php')
	conn.putheader('Authorization', auth )
	conn.putheader(ua[0],ua[1])
	conn.endheaders()
	r=conn.getresponse()
	if r.status!=200:
		print 'Connessione fallita: ',r.status,r.reason
		exit(1)
	psi=r.getheader('Set-Cookie').replace('PHPSESSID=','').replace('; path=/','')
	page=r.read()
	bound='<input type="hidden" name="sid" value="'
	s=page.find(bound); e=page.find('">',s)
	sid=page[s+len(bound):e]
	conn.close()
	return sid,psi

def dnscp(act,params):
	"""Effettua l'azione richiesta sul pannello dns"""
	conn=HTTPS('ns1.th.seeweb.it')
	conn.putrequest('POST', act)
	conn.putheader('Host', 'ns1.th.seeweb.it')
	conn.putheader("Content-type", "application/x-www-form-urlencoded")
	conn.putheader('Cookie', 'seeweb='+psi )
	conn.putheader("Content-length", "%d" % len(params))
	conn.putheader(ua[0],ua[1])
	conn.endheaders()
	conn.send(params)
	reply, msg, hdrs = conn.getreply()
	page = conn.getfile().read()
	conn.close()
	return reply,msg,hdrs,page

def dnsinfo(name):
	"""Controlla quale indirizzo ip era presente nel pannello dns di tophost.
	Questa funzione fa il parsing della pagina per individuare 
	l'ip associato al nome richiesto."""
	b1='<input type="hidden" name="name" value="%s">' % name
	i=data.find(b1)+len(b1)
	b2='<input type="hidden" name="value" value="'
	i=data.find(b2,i)+len(b2)
	e=data.find('">',i)
	return data[i:e]

def update(name):
	"""Aggiorna il valore per il nome richiesto, se necessario"""
	global ip
	old=dnsinfo(name)	# L'ip del DNS
	if ip==old:
		# Esci dalla funzione se l'ip del DNS era corretto
		print ' - Hostname %s.%s, IP: %s, update not needed' % (name, userid, old)
		return False
	
	# Modifica il nome
	params = urlencode({'name':name,
		'old_value':old,
		'new_value':ip,
		'type':'A',
		'action':'Modifica'})
	dnscp('/dnscp/index.php?page=edit_record',params)
        print ' - Hostname %s.%s, IP: %s, updated to %s' % (name, userid, old, ip)
	return True

#######################
# SCRIPT
#######################

# Lettura parametri
if __name__ == "__main__":
	if len(argv)>1:
		opts, args=getopt(argv[1:],'fh',['force','help'])
		for o,a in opts:
			if o in ['-h','--help']:
				print help
				exit(0)
			if o in ['-f','--force']:
				test=False
				ip=a
		if len(args)==1:
			ip=args[0]

	if ip=='':
		ip=getip()
		print " - Public IP:" + ip 

	if test==True:
		# Compara con l'ip memorizzato
		if exists(logip):
			storedip=open(logip,'r').read()
		if storedip==ip:
			print "Update not needed, same ip";
			exit(0)
	

	# Log in to CPanel 
	getsid()
	params = urlencode({'sid': sid, 'b1': 'Vai al pannello del DNS'})
	data=dnscp('/dnscp/',params)[-1]

	# Updates DNS for hostanames
	for n in DYNAMIC_HOSTS:
		update(n)

	open(logip,'w').write(ip)
	
