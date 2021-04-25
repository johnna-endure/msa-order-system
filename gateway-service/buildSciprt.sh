#!/bin/bash

cd ../member-service
echo "member service 빌드 시작";
./gradlew build

cd ../service-discovery-service
echo "eureka 빌드 시작";
./gradlew build

echo "1초 대기"
sleep 1s

echo "빌드 완료"