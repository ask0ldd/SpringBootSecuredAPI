package com.example.restapi2.configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rsa") // rsa + publickey & rsa + privatekey looked into application properties
public record RsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
    // https://www.jmdoudoux.fr/java/dej/chap-records.htm
}
