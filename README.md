# Gym CRM System

This is a Spring-based module designed to handle a gym CRM system. 
The application supports managing profiles for trainees, trainers, and training sessions.

## Configuration
The Spring application context is configured using Java-based configuration. The application uses a property file (application.properties) to configure paths and other settings.

## Storage
Each domain model entity (Trainer, Trainee, Training, TrainingType, User) is stored and retrieved from a separate in-memory store implemented using java.util.concurrent.ConcurrentHashMap. Storages are initialized with prepared data from files when the application starts.