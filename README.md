# Self-improvement social network - REST API built with Spring

### Technologies used:
- Java 17
- Spring boot 3
- Hibernate 
- PostgreSQL 
- Maven 
- Project Lombok

### Implemented REST API documentation
This app uses Springdoc for API documenting. You can use its means to get the API docs, for example 
**hit Swagger UI: /swagger-ui/index.html** when the app is running

### Consistent response body format
All the responses issued by this REST API have similar structure represented by 
``response.dto.rkostiuk.selfimprovement.ResponseBody`` class, its fields are:    
- timestamp
- status
- statusCode
- message
- developerMessage
- data

Where *data* is of type Map and contains any data that is a request result.
Response example:
```
{
    "timestamp": "2023-04-16T15:01:57.667969743Z",
    "status": "OK",
    "statusCode": 200,
    "data": {
        "users": [
            {
                "id": 2,
                "name": "Peter",
                "surname": "Griffin",
                "birthday": null,
                "activityCount": 0
            }
        ]
    }
}
```

### Authorization

#### JWT

**Implemented server is stateless, client state is stored in JWT tokens.** JWT functionality is implemented via 
Spring Security oauth2 resource server.

**Following keys are required to sign and check JWT tokens:**
- /src/main/resources/certs/private.pem
- /src/main/resources/certs/public.pem

**Use openssl to generate these keys:**
1. change dir to /src/main/resources/certs/
2. create rsa key pair: `openssl genrsa -out keypair.pem 2048`
3. extract public key: `openssl rsa -in keypair.pem -pubout -out public.pem`
4. create private key in PKCS#8 format: `openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem`
5. you can now remove *keypair.pem*
