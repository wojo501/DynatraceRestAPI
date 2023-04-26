# DynatraceRestAPI

## Running server
To start the server: 
1. navigate to the demo folder
2. run this command
```
gradlew bootRun
```
3. open PowerShell
4. run this command to test
```
curl 'http://localhost:8080/task1?number=100&code=eur'
```

## Evaluating tasks
Note:
date format: YYYY-MM-DD </br>
code: [link](https://nbp.pl/en/statistic-and-financial-reporting/rates/table-a/) </br>
number: between 0 and 255 </br>

task 1:
```
curl 'http://localhost:8080/task1?date=[date]&code=[code]'
```
Examples:
```
curl 'http://localhost:8080/task1?date=2020-05-05&code=usd'

curl 'http://localhost:8080/task1?date=2015-10-08&code=eur'
```

task 2:
```
curl 'http://localhost:8080/task2?number=[number]&code=[code]'
```
Examples:
```
curl 'http://localhost:8080/task2?number=150&code=aud'

curl 'http://localhost:8080/task2?number=36&code=bgn'
```

task 3:
```
curl 'http://localhost:8080/task2?number=[number]&code=[code]'
```
Examples:
```
curl 'http://localhost:8080/task2?number=130&code=hkd'

curl 'http://localhost:8080/task2?number=5&code=uah'
```

