## package

> mvn clean package -Dmaven.test.skip=true


## build

> docker build -t blog .

## run

> docker run -d -p 80:8080 --name blog --link blogmysql blog

