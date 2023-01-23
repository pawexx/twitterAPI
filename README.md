Steps to run this app

1. change {PWD} in docker-compose to app path
2. run "docker-compose up"
3. generate twitter auth key and paste to property twitter.api.authKey
4. run app


Tweets are synced every 5mins.

The endpoint for GET user tweets is under "/twitter/{userName}/tweets".