# Tic Tac Toe

Currently, this is a minimal setup based on postgres, angular and spring boot.

## Setup

- Setup postgres. Write down _database name_, _username_ and _password_. I used docker 
```
mkdir -p $HOME/docker/volumes/postgres
docker run --rm --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres
```
- Use those values to fill in values here `src/main/resources/application.yml`
- run with `/mvnw spring-boot:run`
- Go to `localhost:8080`. If you will see message `Hello from db` - everything works!
