# spring-boot-postgres-on-k8s

## Prerequisites

- Kubernetes cluster with kubectl installer and configured to use your cluster. The Kubernetes cluster must be able to create persistent volumes and external load balancers.
- docker cli installed, and signed into your Docker Hub account

## Deploy Spring Boot app and Postgres on Kubernetes

1. Deploy postgres with a persistent volume claim
```
kubectl create -f specs/postgres.yml
```

1. Create a config map with the hostname of postgres
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

1. Update the deployment to use your image
```
kubectl create -f specs/spring-boot-app.yml
```

1. Create an external load balancer for your app
```
kubectl expose deployment spring-boot-postgres-sample --type=LoadBalancer --port=8080
```

1. Get the External IP address of Service
```
kubectl get svc spring-boot-postgres-sample
```

1. Use your app at http://<External IP Address>:8080
*** This may take a few minutes to show up

## Updating your application

1. Update the image that the container in your deployment is using
```
kubectl set image deployment/spring-boot-postgres-sample spring-boot-postgres-sample=<your Docker Hub account>/spring-boot-postgres-on-k8s:v2
```

1. Scale your application
```
kubectl scale deployment spring-boot-postgres-sample --replicas=3
```

## Deleting Resources

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

1. Delete postgres
```
kubectl delete -f specs/postgres.yml
```
