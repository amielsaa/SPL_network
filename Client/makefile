CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lpthread

all: EchoClient
	g++ -o bin/echoExample bin/connectionHandler.o bin/echoClient.o bin/clientHandler.o bin/clientToServerHandler.o bin/serverToClientHandler.o $(LDFLAGS) 

EchoClient: bin/connectionHandler.o bin/echoClient.o bin/clientHandler.o bin/clientToServerHandler.o bin/serverToClientHandler.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp
	
bin/clientHandler.o: src/clientHandler.cpp
	g++ $(CFLAGS) -o bin/clientHandler.o src/clientHandler.cpp
	
bin/clientToServerHandler.o: src/clientToServerHandler.cpp
	g++ $(CFLAGS) -o bin/clientToServerHandler.o src/clientToServerHandler.cpp

bin/serverToClientHandler.o: src/serverToClientHandler.cpp
	g++ $(CFLAGS) -o bin/serverToClientHandler.o src/serverToClientHandler.cpp

.PHONY: clean
clean:
	rm -f bin/*
