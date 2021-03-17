# UrlShortener-API

[![Maintainability](https://api.codeclimate.com/v1/badges/45cfeb44822d09972ef7/maintainability)](https://codeclimate.com/github/zigri2612/UrlShortener-API/maintainability)

[Read about basic implementation here](https://dzone.com/articles/url-shortener-detailed-explanation)

[Read about more details here](https://dzone.com/articles/url-shortener-pt-2)

This is an API for Url shortener service like tiny url. 

Url shortener is service that converts long urls into short aliases to save space when sharing urls in messages, twitter, presentations etc.
When user opens short url, it will be automatically redirected to original (long) url.

# How to use 
+ With Docker and docker-compose: 

```sh
$ git clone https://github.com/zigri2612/UrlShortener-API.git
$ cd UrlShortener-API 
$ docker-compose up 
```

    - Open localhost:8080/swagger-ui.html to see endpoints. 

- Without Docker: 
```sh
$ git clone https://github.com/zigri2612/UrlShortener-API.git
```
    - Make sure you have access to local or any MySQL server.
    - Open project in your favorite editor and change application.properties file to point to your MySQL database
    - Build Spring project 
    - Open localhost:8080/swagger-ui.html to see endpoints.

