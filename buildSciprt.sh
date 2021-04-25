#!/bin/bash

cd member-service
echo "member service 빌드 시작";
./gradlew build
cd ..

cd gateway-service
echo "gateway service 빌드 시작"
./gradlew build
cd ..

cd service-discovery-service
echo "service-discovery service 빌드 시작"
./gradlew build
cd ..

cd ui-service
echo "ui service 빌드 시작"
./gradlew build

echo "빌드 완료"