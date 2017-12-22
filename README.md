# spring-boot-postgres-on-k8s

This demo deploys a simple Spring Boot web application that connects to Postgres onto a Kubernetes cluster. 

You can watch this demo along with an introduction to Kubernetes concepts [here](https://www.youtube.com/watch?v=OsWXtVbTnv0).

## Prerequisites

- Kubernetes cluster with kubectl installed and configured to use your cluster
- docker cli installed, you must be signed into your Docker Hub account

## Deploy Spring Boot app and Postgres on Kubernetes
1. Deploy postgres with a persistent volume claim
   ```
   kubectl create -f specs/postgres.yml
   ```

1. Create a config map with the hostname of Postgres
   ```
   kubectl create configmap hostname-config --from-literal=postgres_host=$(kubectl get svc postgres -o jsonpath="{.spec.clusterIP}")
   ```

1. Build the Spring Boot app

   ```
   ./mvnw -DskipTests package
   ```

1. Build a Docker image and push the image to Docker Hub
   ```
   docker build -t <your Docker Hub account>/spring-boot-postgres-on-k8s:v1 .
   docker push <your Docker Hub account>/spring-boot-postgres-on-k8s:v1
   ```

1. Replace `<your Docker Hub account>` with your account name in `specs/spring-boot-app.yml`, then deploy the app
   ```
   kubectl create -f specs/spring-boot-app.yml
   ```

1. Create an external load balancer for your app
   ```
   kubectl expose deployment spring-boot-postgres-sample --type=LoadBalancer --port=8080
   ```

1. Get the External IP address of Service, then the app will be accessible at `http://<External IP Address>:8080`
   ```
   kubectl get svc spring-boot-postgres-sample
   ```
   > **Note:** It may take a few minutes for the load balancer to be created

1. Scale your application
   ```
   kubectl scale deployment spring-boot-postgres-sample --replicas=3
   ```

## Updating your application
1. Update the image that the containers in your deployment are using
   ```
   kubectl set image deployment/spring-boot-postgres-sample spring-boot-postgres-sample=<your Docker Hub account>/spring-boot-postgres-on-k8s:v2
   ```

## Deleting the Resources
1. Delete the Spring Boot app deployment
   ```
   kubectl delete -f specs/spring-boot-app.yml
   ```

1. Delete the service for the app
   ```
   kubectl delete svc spring-boot-postgres-sample
   ```

1. Delete the hostname config map
   ```
   kubectl delete cm hostname-config
   ```

1. Delete Postgres
   ```
   kubectl delete -f specs/postgres.yml
   ```
