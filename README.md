### How to run

Run
```
sbt docker:publishLocal && docker run -p 8080:8080 zio-scope-issue:latest
```
and
```
while true; do curl localhost:8080/status || echo fail; echo ''; sleep 0.2; done
```
The defect should appear within a minute.
