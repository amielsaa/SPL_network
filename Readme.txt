----Running Server----
1. mvn clean
2. mvn compile

ThreadPerClient command:
3. mvn exec:java -Dexec.mainClass="bgu.spl.net.srv.TPCMain" -Dexec.args="<port>"

Reactor command:
3. mvn exec:java -Dexec.mainClass="bgu.spl.net.srv.ReactorMain" -Dexec.args="<port> <numthreads>"

----Running Client----
1. make
2. bin/BGSclient <ip-address> <port>

----Messages Examples----
REGISTER: REGISTER amiel 123 01-04-1997
LOGIN: LOGIN amiel 123 1
LOGOUT: LOGOUT
FOLLOW: FOLLOW 0 amiel
POST: POST content
PM: PM amiel content
LOGSTAT: LOGSTAT
STAT: STAT amiel|other_user
BLOCK: BLOCK amiel

----Filtered Words----
we placed the filtered words in the "SharedData" object
location : bgu.spl.net.api.obj.SharedData
under a list called "filterWords"