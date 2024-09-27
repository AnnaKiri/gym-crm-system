# Trainer Workload Service



The main idea behind the creation of this microservice is as follows.

Every time a training session is planned or cancelled for a trainer; this data is transferred to a newly created microservice.

The Microservice calculates the number of working hours of each trainer for each month and stores this information in in-memory database.
1. Request
   1. Trainer Username
   2. Trainer First Name
   3. Trainer Last Name
   4. IsActive
   5. Training date
   6. Training duration
   7. Action Type (ADD/DELETE)
2. Response
   1. 200 OK

In addition, when requesting the number of training hours from any of the trainers in a particular month, the microservice retrieves this data from the database and returns it to the requester.
Service should calculate as in-memory saved structure (in memory DB) trainer's monthly summary of the provided trainings.

The model should be the follow;
1. Trainer Username
2. Trainer First Name
3. Trainer Last Name
4. Trainer Status
5. Years (List)
   1. Months (List)
      1. Training summary duration

Update Existing Main Microservice implementation to call Secondary Microservice every time that new training added or deleted to the system.