package com.example.academsoap;

import com.example.academsoap.ws.SistemaAcademicoWS;
import javax.xml.ws.Endpoint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcademSoapApplication implements CommandLineRunner {

    private static final String URL = "http://localhost:8080/sistemaacademico";

    public static void main(String[] args) {
        SpringApplication.run(AcademSoapApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Endpoint.publish(URL, new SistemaAcademicoWS());
        System.out.println("SistemaAcademicoWS disponível em " + URL);
    }
}
