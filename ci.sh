#! /bin/bash
set -e

BACKEND_IMAGE=kelog/csmsearch:latest

docker build -t ${BACKEND_IMAGE} . && docker push ${BACKEND_IMAGE}

kubectl get pods | grep csmsearch | awk '{print $1}' | xargs kubectl delete pod
