from random import randint
import socket               	# Import socket module

class AlarmDevice:
	addr = '0.0.0.0'
	time = '0000'
	partner = None

	def __init__(self, addr, time):
		self.addr = addr
		self.time = time

	def get_addr(self):
		return self.addr

	def get_time(self):
		return self.time

	def get_partner(self):
		return self.partner

	def setPartner(self, partner):
		self.partner = partner

def findPair(ad, lst):
	for i in xrange(len(lst)):
		if ad.get_time() == lst[i].get_time() and lst[i].get_partner() is None:
			ad.setPartner(lst[i])
			lst[i].setPartner(ad)
			break

users = []

sock = socket.socket()         	# Create a socket object
host = socket.gethostname() 	# Get local machine name
port = 5000                		# Reserve a port for your service.
sock.bind((host, port))        	# Bind to the port
client_port = 45000
sock.listen(5)                	# Now wait for client connection.

first = True;

while True:
	client, addr = sock.accept()     # Establish connection with client.
	print 'Got connection from', addr

	data = client.recv(1024)
	print data

	if data[0:4] == 'join':		# 'join 0700'
		time = data[-4:]
		if all(u.get_addr() != addr[0] for u in users):
			users.append(AlarmDevice(addr[0], time))
			findPair(users[-1], users[:-1])
		print len(users)
		for p in users:
			print p.addr, p.time, p.partner
	elif data == 'snooze':
		if first:
			first = False
		else:
			for u in users:
				print u.get_addr()
				print addr[0]
				if u.get_addr() == addr[0]:
					tmp = socket.socket()
					print 'tmp socket initialized'
					tmp.connect(((u.get_partner()).get_addr(), client_port))
					print 'tmp connection created to ' + u.get_partner().get_addr() + ' on port ' + str(client_port)
					tmp.send('Unsnoozed!')
					print 'message sent'
		#client.send(str(randint(1, 10)))
	print 'closing socket'
	#client.close()                	# Close the connection