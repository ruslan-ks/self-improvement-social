# Self-improvement social network

Spring boot REST API project

### Technologies used:
- Java 17
- Spring boot 3
- Hibernate 
- PostgreSQL 
- Maven 
- Project Lombok

### Implemented REST API

| method | url                         | result                      | request body |
|--------|-----------------------------|-----------------------------|--------------|
| GET    | /users?page=N\[&pageSize=M] | List of MinimalUserResponse |              |

### Authorization

#### JWT

**Implemented server is stateless, client state is stored in JWT tokens.** JWT functionality is implemented via 
Spring Security oauth2 resource server.

**Following keys are required to sign/check JWT tokens:**
- /src/main/resources/certs/private.pem
- /src/main/resources/certs/public.pem

**Use openssl to generate these keys:**
1. change dir to /src/main/resources/certs/
2. create rsa key pair: ``openssl genrsa -out keypair.pem 2048``
3. extract public key: ``openssl rsa -in keypair.pem -pubout -out public.pem``
4. create private key in PKCS#8 format: ``openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem``
5. you can now remove *keypair.pem*
