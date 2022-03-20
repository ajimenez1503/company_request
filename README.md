# How to build company_request

In this guide you will launch a local Kubernetes cluster, develop an app using the Spring Boot framework and deploy it
as a container in Kubernetes.

Ref:
- https://learnk8s.io/spring-boot-kubernetes-guide

## Create and run a Spring Boot Application

### Bootstrapping the app
- Go to https://start.spring.io/
  - Create a project with name `company_request`
    - Add dependencies:
      - Web -> Spring Web Starter: basic web stack support in Spring Boot
      - Actuator -> Spring Boot Actuator: provide health endpoints for our application
      - FreeMarker -> Apache FreeMarker: templating engine for HTMLs
      - MongoDB -> Spring Data MongoDB: driver and implementation for Spring Data interfaces to work with MongoDB
      - Lombok -> Lombok: library to avoid a lot of boilerplate code
    ![img.png](img/spring_initializr.png)
    - Generate the project and unzip it.

### Front-end
- Create the index view in `src/main/resources/templates/`
- Add the style:
  - Copy the line of code below and paste it in the head of the html file(s) you want to include tachyons in.
    - `<link rel="stylesheet" href="https://unpkg.com/tachyons@4.12.0/css/tachyons.min.css"/>`

#### Connecting a database
The database will store the requests.

- Add the URL of the database into `src/main/resources/application.properties`
```
spring.data.mongodb.uri=mongodb://localhost:27017/dev
```

### Run the application locally
```shell
mongod
mvn clean install spring-boot:run
```
- Open `http://localhost:8080`
![img.png](img/web_view.png)

Containerising the app
- Install docker
- Create Dockerfile in the root directory
```shell
FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-slim
WORKDIR /opt
ENV PORT 8080
EXPOSE 8080
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
```
- Build the container image
```shell
docker build -t company_request .
```
- Create a new Docker network.
  - The company_request and mongodb containers should communicate with each other.
```shell
docker network create company_request_network
```
- Run the mongodb docker image:
```shell
docker run \
  --name=mongo \
  --rm \
  --network=company_request_network \
  mongo
```
- Run the company_request docker image:
```shell
docker run \
  --name=company_request \
  --rm \
  --network=company_request_network \
  -p 8080:8080 \
  -e MONGO_URL=mongodb://mongo:27017/dev \
  company_request
```

## Uploading the container image to DockerHub
- Connect to the Docker Hub account:
```shell
docker login
```
- Set tag:
```shell
docker tag company_request ajimenez15/company_request:1.0.0
```
- Create repository in Dockerhub
- Upload image to DockerHub https://hub.docker.com/repository/docker/ajimenez15/company_request
```shell
docker push ajimenez15/company_request:1.0.0
```

## Kubernetes — the container orchestrator

### Creating a local Kubernetes cluster using Minikube
- Install kubectl https://kubernetes.io/docs/tasks/tools/
  - kubectl is the primary Kubernetes CLI
- Install Minikube https://kubernetes.io/docs/tasks/tools/install-minikube/
- Create a cluster
```shell
minikube start
```
- Verify the cluster was created
```shell
minikube status
kubectl cluster-info
```

### Defining a deployment and a service
- Create a folder named kube in your application directory. It will hold all the Kubernetes YAML files
```shell
mkdir kube
```
- Create `kube/deployment.yaml` for the spring app deployment.
- Create `Kube/service.yaml` for the spring app service.
- Create `kube/mongo.yaml` for the mongodb deployment, service and storage.

### Deploy the application
- Submit your resource definitions to Kubernetes
```shell
kubectl apply -f kube
```
- Watch your Pods coming alive
```shell
kubectl get pods --watch
```
- You should see two Pods transitioning from Pending to ContainerCreating to Running.
These Pods correspond to the company-request and MongoDB containers.
As soon as both Pods are in the Running state, your application is ready.
- In Minikube, a Service can be accessed with the following command:
The command should print the URL of the company-reqest Service.
```shell
minikube service company-request --url
```
- You can open the URL in a web browser.

### Scaling your app
- Increase the number of replicas to 2:
```shell
kubectl scale --replicas=2 deployment/company-request
```
![img.png](img/pods.png)

### Stop the deployment
- Find the deployments
```shell
kubectl get deploy
```
- Delete all the deployments, run below command:
```shell
kubectl delete deploy company-request
kubectl delete deploy mongo
```
