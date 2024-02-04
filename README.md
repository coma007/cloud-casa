# IOT project


### Team members
[Nemanja Majstorović](github.com/Nemanja3314)  
[Nemanja Dutina](github.com/eXtremeNemanja)  
[Milica Sladaković](github.com/coma007)

### Technologies

- Spring
- React
- Golang
- Docker
- PostgresSQL
- InfluxDB
- Paho MQTT
- Python
- Nginx
- Redis
- JMeter

### Running
- `bash ./init.sh`
- `sudo systemctl start nginx`;
- `sudo docker compose up`
- in casa-back directory, run casa-back Spring app in your prefered IDE or from command line `./gradlew bootRun` or `gradle bootRun`
- in casa-front, run casa-front React app from command line `npm i & npm start`
- in device-simulations, run simulations (after you have some registered devices on casa-back) from command line `go run main.go`
