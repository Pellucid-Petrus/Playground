#!/usr/bin/python
"""DNS dinamico con TopHost.
Lancia thdns -h per informazioni sull'uso.
Daniele Paganelli @ 2007
CreativeCommons Attribution-ShareAlike"""

########################
# CONFIGURAZIONE
########################
# Nome utente e password per il pannello di controllo TopHost:
userid = "XXXX"
passwd = "YYYY"

# Nomi (domini di secondo livello) dei quali si desidera aggiornare l'ip: (devono gia' essere presenti come record di tipo A.)
dyn=['home']

# File dove memorizzare l'ip dell'ultimo aggiornamento:
logip='/tmp/thdns.ip'

# FINE CONFIGURAZIONE
#-------------------------

#######################
#######################
# COSTANTI
#######################


storedip=None
test=True
ip=''

help="""Utilizzo: thdns [<opzioni> <ip>]
ARGOMENTI:
	[<ip>]: Aggiorna i nomi all'ip dato in argomento (equivalente al flag -i <ip>, o --ip=<ip>).
OPZIONI:
	-f [<ip>], --forza[=<ip] : Forza l'aggiornamento (nota: non abusare di questa opzione per non sovraccaricare i server)

	-g [<ip>],--gentile[=<ip]: Controlla se l'aggiornamento e' necessario, paragonando l'ip attuale (in argomento o ottenuto dalla rete) all'ip dell'ultima chiamata al programma, memorizzato in %s. Questo e' il comportamento predefinito.

	-h, --help : stampa questo messaggio di aiuto

Tutte le opzioni e gli argomenti sono facoltativi. Nel caso non venga specificato un ip ne' in argomento ne' tra le opzioni, verra' ottenuto in rete tramite il servizio checkip.dyndns.org (non funziona con i proxy).

Esempi:
	thdns -f 192.168.1.1 -> forza l'aggiornamento all'ip
	thdns 192.168.1.1 / thdns -g 192.168.1.1 -> se %s non corrisponde a 192.168.1.1, aggiorna
	thdns -> ottiene l'indirizzo dalla rete; se non corrisponde a %s, aggiorna (equivalente a thdns -g)
""" % (logip,logip,logip)

ua='User-Agent','thdns/0.1 (+http://daniele.modena1.it/code/thdns/view)'

#######################
# FUNZIONI
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
        print "Old ip for %s is %s" % (name, old)
	if ip==old:
		# Esci dalla funzione se l'ip del DNS era corretto
		print '[%10s]\t mantengo %s' % (name,old)
		return False
	
	# Modifica il nome
	params = urlencode({'name':name,
		'old_value':old,
		'new_value':ip,
		'type':'A',
		'action':'Modifica'})
	dnscp('/dnscp/index.php?page=edit_record',params)
	print '[%10s]\t era %s, aggiorno a %s' % (name,old,ip)
	return True

#######################
# SCRIPT
#######################

# Lettura parametri
if len(argv)>1:
	opts, args=getopt(argv[1:],'fgh',['forza','help','gentile'])
	for o,a in opts:
		if o in ['-h','--help']:
			print help
			exit(0)
		if o in ['-f','--forza']:
			test=False
			ip=a
		if o in ['-g','--gentile']:
			test=True
			ip=a
	if len(args)==1:
		ip=args[0]

if ip=='':
	ip=getip()
	print "IP is:" + ip 

if test==True:
	# Compara con l'ip memorizzato
	if exists(logip):
		storedip=open(logip,'r').read()
	if storedip==ip:
		print "Update not needed, same ip";
		exit(0)
	else:
		open(logip,'w').write(ip)
else:
	open(logip,'w').write(ip)


# Inizio la sessione con il cpanel:
getsid()
params = urlencode({'sid': sid, 'b1': 'Vai al pannello del DNS'})
data=dnscp('/dnscp/',params)[-1]

# Aggiorna tutti i nomi richiesti sul pannello dns:
for n in dyn:
	update(n)
