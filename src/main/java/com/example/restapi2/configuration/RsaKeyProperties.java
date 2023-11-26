package com.example.restapi2.configuration;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;

import java.security.interfaces.RSAPublicKey;

// import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.example.restapi2.utils.KeyGeneratorUtility;

@Component
public class RSAKeyProperties {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAKeyProperties() {
        KeyPair pair = KeyGeneratorUtility.generateRSAKeys();
        this.publicKey = (RSAPublicKey) pair.getPublic();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
    }

    public RSAPublicKey getPublicKey() {
        return this.publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
