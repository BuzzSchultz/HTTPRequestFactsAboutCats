package ru.homeworks.abatsanov;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String URL =
            "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) {
        HttpGet request = new HttpGet(URL);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        CloseableHttpResponse response = httpClient.execute(request)) {

            ObjectMapper mapper = new ObjectMapper();
            List<FactsAboutCats> factsAboutCatsList =
                    mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
                    });

            List<FactsAboutCats> votesNotNull = factsAboutCatsList.stream()
                    .filter(f -> f.getUpvotes() != null)
                    .filter(f -> Integer.parseInt(f.getUpvotes()) > 0)
                    .collect(Collectors.toList());
            votesNotNull.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
