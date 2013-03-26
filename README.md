design and implement a distributed facility booking
system based on client-server architecture. The server stores the information of all
facilities (e.g., meeting rooms, lecture theatres), including the name of each facility (a
variable-length string), the availability of the facility over seven days of a week (the
time is to be represented in the form of day/hour/minute, where day is of enumerated
type with possible values from Monday to Sunday, and hour and minute are integers),
and the bookings made by users. Each facility requires exclusive use. The server
program implements a set of services on the facilities for remote access by clients.
Meanwhile, the client program provides an interface for users to invoke these
services. On receiving a request input from the user, the client sends the request to
the server. The server performs the requested service and returns the result to the
client. The client then presents the result on the console to the user. The client-server
communication is carried out using UDP.
The services to be implemented by the server program and made available to
the users through the client program include:
Page 1
CPE416 Distributed Systems and CSC411 Distributed Computing
Course Project
1. A service that allows a user to query the availability of a facility on a selection of
one or multiple days by specifying the facility name and the days. If there does not
exist any facility with the specified name, an error message should be returned.
2. A service that allows a user to book a facility for a period of time by specifying the
facility name and the start and end times of the booking. On successful booking, a
unique confirmation ID is returned to the client and the availability of the facility
should be updated at the server. In case of incorrect user input (e.g., non-existing
facility name or the facility is completely or partially unavailable during the
requested period due to existing bookings), a proper error message should be
returned.
3. A service that allows a user to change its booking by specifying the confirmation
ID of the booking and an offset for changing (e.g., to advance the booking by 1
hour or to postpone the booking by 30 minutes). The change does not modify the
length of the time period booked. On successful change, an acknowledgement is
returned to the client and the availability of the facility should be updated at the
server. In case of incorrect user input (e.g., non-existing confirmation ID or the
facility is completely or partially unavailable during the new requested period due
to existing bookings), a proper error message should be returned.
4. A service that allows a user to monitor the availability of a facility over the week
through callback from the server for a designated time period called monitor
interval. To register, the client provides the facility name and the length of monitor
interval to the server. After registration, the Internet address and the port number
of the client are recorded by the server. During the monitoring interval, every time
a booking or an update is made by any client to the facility, the updated availability
of the facility over the week is sent by the server to the registered client(s) through
callback. After the expiration of the monitor interval, the client record is removed
from the server which will no longer deliver the availability updates to the client.
For simplicity, you may assume that the user that has issued a register request for
monitoring is blocked from inputting any new request until the monitor interval
expires, i.e., the client simply waits for the updates from the server during the
monitoring interval. As a result, you do not have to use multiple threads at a client.
However, your implementation should allow multiple clients to monitor the
availability of a facility concurrently.
facility-booking
================
