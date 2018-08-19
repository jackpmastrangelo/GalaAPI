# GalaAPI

This repository hosts a Java Spring application for the gala ticketing app. 
This application is a REST API that can be hit by various client applications(Web/Mobile) via HTTP requests.

## API Specifications
This section is for laying out our REST endpoints, their urls, their required parameters, their behaviors and what they return.

`Note:` All endpoints will return 500 if something goes wrong and the action is unrecoverable. All endpoints will also return 400 Bad Request if required parameters were not provided.

`Note:` All endpoints marked as (Secure) require being logged in to access, and therefore should accept the authentication
key as a String in the header as well. These will return a 403 Forbidden if unauthenticated. For responses other than 200 you should add a header "gala-message" that gives the client more information.

### Get User Events (Secure)
`URL:` /events/users `HTTP Method`: GET

`Parameters:` N/A

`Expected behavior:` For the currently authenticated user, return a JSONArray containing all events for that user as JSONObjects.

`Responses:` 

| Response Code | Behaviour |
| --- | --- |
| 200 | User has events, return a JSONArray containing all events for that user as JSONObjects.|

### Create New Event (Secure)
`URL:` /events/users `HTTP Method`: POST

`Parameters:` name : String, place : String, startTime: Date, capacity : Integer

`Expected behavior:` Create a new event associated with this user.

`Responses:` 

| Response Code | Behaviour |
| --- | --- |
| 200 | Event creation was successful, return Event. |

### Get one event
`URL:` /events/{eventId} (as String) `HTTP Method`: GET

`Parameters:` N/A

`Expected behavior:` Return the event as a JSONObject.

`Responses:` 

| Response Code | Behaviour |
| --- | --- |
| 200 | Event was found, return it as a JSONObject. |
| 404 | Event was not found |

### Request Ticket
`URL:` /tickets/create (as String) `HTTP Method`: POST

`Parameters:` eventId : String, email : String

`Expected behavior:` If there is available capacity, create a new ticket associated with the given event, and send an email to
the given email address.

`Responses:` 

| Response Code | Behaviour |
| --- | --- |
| 200 | Ticket was created successfully, return ticketId as String |
| 404 | Event was not found |
| 409 | Could not create ticket due to lack of space in event |

### Validate Ticket (Secure)
`URL:` /tickets/validate (as String) `HTTP Method`: PUT

`Parameters:` eventId : String, ticketId : String

`Expected behavior:` If the given ticket belongs to the given event, and the ticket's status is active. Validate the ticket and make the ticket Validated. Otherwise return an unsuccessful response.

`Responses:` 

| Response Code | Behaviour |
| --- | --- |
| 200 | Ticket was validated successfully |
| 404 | Ticket not found |
| 406 | Ticket was found but unable to be validated for whatever reason, return `message` as String |
| 409 | Ticket is not associated with given event |
