\# EventHub Engineering Rules



\## Project



EventHub is an Event \& Ticket Booking Platform.



The project is being built as a modular monolith.



\## Backend



\- Java 21

\- Spring Boot

\- Maven

\- PostgreSQL

\- Spring Data JPA

\- Hibernate

\- REST APIs

\- Bean Validation

\- JUnit

\- Mockito



\## Architecture



Use clear separation:



Controller

&#x20;   ↓

Service

&#x20;   ↓

Repository

&#x20;   ↓

Database



Controllers must not contain business logic.



Repositories must not contain business workflows.



Business rules belong in the service/domain layer.



\## Database



PostgreSQL is the source of truth.



Important business invariants should be protected by database constraints where appropriate.



Use timezone-aware timestamps.



Never store plaintext passwords.



Never use floating-point types for monetary values.



\## Development



Before changing code:



1\. Inspect the existing implementation.

2\. Understand dependencies.

3\. Identify affected files.

4\. Explain the planned change.

5\. Implement the smallest correct change.

6\. Run tests.

7\. Verify compilation.

8\. Report changed files and verification results.



Do not rewrite unrelated code.



Do not create duplicate implementations.



Do not introduce new libraries without justification.



\## AI Agent Rule



OpenCode must not invent business rules.



If a business requirement is ambiguous, stop and ask for clarification.



Existing documented business rules take precedence over assumptions.



\## Git



Make small, focused commits.



Do not modify another developer's unrelated work.



Never commit secrets, passwords, API keys, or local environment files.

