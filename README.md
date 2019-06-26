# cyria-cloud

## compile:  
	- mvn package -DskipTests
	- docker-compose -f docker-compose-base.yml -f docker-compose.yml build
	- docker-compose -f docker-compose-base.yml -f docker-compose.yml up
