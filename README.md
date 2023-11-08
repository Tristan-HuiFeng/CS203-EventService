# CS203 Event Microservice
This project lasted around 3 months. We developed a ticketing software system to create a fair and reliable way for users to purchase tickets. It is a web-based software, aimed at making purchasing tickets easy and seamless. Without having to put heavy burdens such as queuing for a long time just to purchase tickets.  


# Tables of content
- [Motivations](#Motivations)
- [Task Allocation / Task Done](#Task-Allocation-/-Task-Done)
- [Libraries / Technologies Used](#Libraries-/-Technologies-Used)

## Try it Out
### Quick Start:
```
docker compose up
mvnw spring-boot:run
```
- Also run the spring boot for Order Microservice at this link: https://github.com/Tristan-HuiFeng/CS203-OrderService
- The frontend can be run at the 'code' directory using:
```
npm run dev
```
at this link: https://github.com/Tristan-HuiFeng/CS201


## Motivations
### Fair and Reliable Ticketing System is needed

Have you ever tried to purchase a ticket for your favorite concert and gotten a low queue number, but when it's your turn to make payment, suddenly you are unable to do so due to errors and hence lose the opportunity to secure a ticket? With our ticketing system, you are able to place a Purchase Request and be notified of the result at the end without having to face such an error and lose your opportunity. The absence of a "first-come-first-served” aspect would ensure that customers will not be penalized for having poor internet connections, devices without batteries, or inability to start queuing hours before the start of the ticket sales period.

### Free from the long purchase process

You have probably tried to camp for your favorite concert and prepare multiple devices such as your phone, laptop, and your friends' devices at a specific timing when you might have other important stuff to do. Our ticketing system solves that issue completely. You will now only have to place a Purchase Request of up to 4 tickets for the same event when Sales Round for a particular event is opened. Once the Sales Round ends, we will process all Purchase Requests and notify you of the result when processing is done. You will then have to make payment within a reasonable amount of time, once paid you will have secured your tickets, if it's not paid after the deadline, the tickets will be released back into the pool for the next sales round.


## Task Allocation / Task Done

### Backend Integration & DevOps - [Hui Feng](https://github.com/TLI-Tristan)
- API design and architecture
- Hosted our own authentication and authorization server, Keycloak
- Using OIDC for authentication and authorization, setting each microservice as a resource server, react as the client
- Stripe Hosted Checkout & Webhook Integration
- Automated Unit and Integration Test using Github Actions
- Hosted the microservices using AWS Fargate and connected to Amazon RDS for each microservice

### Frontend Lead & Integration - [Eric](https://github.com/Bk49)
- Ticketing Algorithm implementation
- Integrate with all backend APIs using Axios
- React app design and architecture
- Integration with Stripe checkout, Webhook & Keycloak API
- Hosted the frontend on Netlify

### Frontend Developer - [En Xi](https://github.com/pohenxi)
- Fulfill purchase request page
- Order confirmation page
- Filter event list
- Event details page

### Fronetend Developer - [Damien Tan](https://github.com/tyxdamien)
- User profile page
- Add / change payment method page
- Event listing page

### Software Tester - [Wesly Chua](https://github.com/weslychau-01)
- Created Unit tests for all of our service and controller
- Integration test for the main flow of our API
- Write comments for functions to ensure proper documentation

### Backend API (Order Microservice) - [Maverick](https://github.com/M4verrick)
- Order CRUD API
- Unit Test for Order API

## Libraries / Technologies Used
- GitHub
- Spring Boot
- Amazon RDS
- AWS Fargate
- AWS ECS
- Netlify
- React
- Axios
- Keycloak
- Stripe
