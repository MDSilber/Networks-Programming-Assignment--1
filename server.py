import socket
import threading
import SocketServer
import sys
import random
broadcastLock = threading.Lock()
toSend = {}


#Reliable client handler
class ThreadedTCPRequestHandler(SocketServer.BaseRequestHandler):
   def handle(self):
        cur_thread = threading.current_thread()
        toSend[cur_thread.name] = []
        self.request.settimeout(0.2)
        while 1:
            try:
                data = self.request.recv(1024)
                broadcastLock.acquire()
                for threads in toSend:
                    toSend[threads].append(data)
                broadcastLock.release()
            except: 
                pass
            try:
                broadcastLock.acquire()
                for item in toSend[cur_thread.name]:
                    self.request.sendall(item)
                    toSend[cur_thread.name].remove(item)
            except:return
            finally: broadcastLock.release()
        
#Unreliable client handler
class ThreadedTCPRequestHandlerUnreliable(SocketServer.BaseRequestHandler):
   def handle(self):
        cur_thread = threading.current_thread()
        toSend[cur_thread.name] = []
        self.request.settimeout(0.2)
        while 1:
            try:
                data = self.request.recv(1024)
                num = random.randrange(1,100,1)
	        #~70% chance of dropping the message
                if(num > 70):
                    broadcastLock.acquire()
                    for threads in toSend:
                        toSend[threads].append(data)
                    broadcastLock.release()
            except: 
                pass
            #Send all the things that need to be sent
            try:
                broadcastLock.acquire()
                for item in toSend[cur_thread.name]:
                    self.request.sendall(item)
                    toSend[cur_thread.name].remove(item)
            except:return
            finally: broadcastLock.release()



class ThreadedTCPServer(SocketServer.ThreadingMixIn, SocketServer.TCPServer):
   
    daemon_threads = True
    allow_reuse_address = True

   
if __name__ == "__main__":

    HOST, PORT = "0.0.0.0", int(sys.argv[1])
    HOST2, PORT2 = "0.0.0.0", int(sys.argv[2])
    server = ThreadedTCPServer((HOST, PORT), ThreadedTCPRequestHandler)
    server2 = ThreadedTCPServer((HOST2, PORT2), ThreadedTCPRequestHandlerUnreliable)

    server_thread = threading.Thread(target=server.serve_forever)
    server_thread.daemon = False
    server_thread.start()

    server_thread2 = threading.Thread(target=server2.serve_forever)
    server_thread2.daemon = False
    server_thread2.start()
    