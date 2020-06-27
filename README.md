# Пример использования провайдера Kalkancrypt XMLDSig JSR-105

Библиотека провайдера основана на **Apache XMLDsig** из [**Apache Santuario XML Security 2.1.5**](https://santuario.apache.org/) с добавлением поддержки ГОСТ-алгоритмов НУЦ РК. Провайдер позволяет подключить в веб-сервисах **[Apache WSS4J](https://ws.apache.org/wss4j/)**, который реализует стандарты WS-Security.

В данном примере для простоты используется **spring-ws-security**, но **Apache WSS4J** может использоваться и в других фреймворках. Подробную информацию можете найти в документациях соответствующих библиотек.

## Зависимости

Криптопровайдер НУЦ РК

```
<dependency>
  <groupId>kz.gov.pki.kalkan</groupId>
  <artifactId>knca_provider_jce_kalkan</artifactId>
  <version>0.6.1</version>
</dependency>
```

Дополнительные классы и конфигурация для Apache XML Security

```
<dependency>
  <groupId>kz.gov.pki.kalkan</groupId>
  <artifactId>xmldsig</artifactId>
  <version>0.3</version>
</dependency>
```

Провайдер JSR-105

```
<dependency>
  <!-- xmlsec 2.1.2 and higher is required -->
  <groupId>kz.gov.pki.kalkan</groupId>
  <artifactId>jsr105</artifactId>
  <version>1.0</version>
</dependency>
```

Apache XML Security 2.1.2 или выше

```
<dependency>
  <groupId>org.apache.santuario</groupId>
  <artifactId>xmlsec</artifactId>
  <version>2.1.5</version>
</dependency>
```

## Конфигурация

В примере используется

```java
org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor
```

, который уже инкапсулирует некоторые возможности WSS4J как для обработки входящих сообщений, так и для исходящих. Необходимо отметить, что дополнительные проверки сертификатов нужно реализовать отдельно.

## Пример вызова веб-сервиса

```echo '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://pki.gov.kz/sample/soap">
echo '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://pki.gov.kz/sample/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:getShinobiRequest>
         <soap:name>Naruto</soap:name>
      </soap:getShinobiRequest>
   </soapenv:Body>
</soapenv:Envelope>' | curl -H "content-type: text/xml" -d @- http://localhost:8080/ws
```

