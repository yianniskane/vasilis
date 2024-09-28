package com.directory.service;

import com.directory.entity.Contact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Resource
@Slf4j
@Transactional
public class Utility {
    private static final int MINIMUM = 11000;
    private static final int MAXIMUM = 20000;
    public static final String DATE_FORMAT_NOW = "dd-MM-yyyy";

    public static String generateJSON(List<?> list) {
        try {
            ObjectMapper mapper = (new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException var2) {
            return "";
        }
    }

    public static String generateJSON(Contact contact) {
        try {
            ObjectMapper mapper = (new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(contact);
        } catch (JsonProcessingException var2) {
            return "";
        }
    }

    public static Integer generateRandomContactCode() {
        return ThreadLocalRandom.current().nextInt(11000, 20001);
    }


    public static String stringToDate(String dateString) {
        String[] fromParam = dateString.split("-");
        return fromParam[2] + "-" + fromParam[1] + "-" + fromParam[0];
    }


    public static String stringToDateReverse(String dateString) {
        String[] fromParam = dateString.split("-");
        return fromParam[0] + fromParam[1] + fromParam[2];
    }


    public static boolean validateEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static boolean validateTelephone(String tel) {
        String removeDashes = tel.replaceAll("-", "");
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(removeDashes);
        return matcher.matches();
    }


    public static String shutdownRequest(String url) throws IOException {
        String result = "";
        HttpPost post = new HttpPost(url);
        post.addHeader("content-type", "application/json");

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            Throwable var4 = null;

            try {
                CloseableHttpResponse response = httpClient.execute(post);
                Throwable var6 = null;

                try {
                    result = EntityUtils.toString(response.getEntity());
                } catch (Throwable var31) {
                    var6 = var31;
                    throw var31;
                } finally {
                    if (response != null) {
                        if (var6 != null) {
                            try {
                                response.close();
                            } catch (Throwable var30) {
                                var6.addSuppressed(var30);
                            }
                        } else {
                            response.close();
                        }
                    }

                }
            } catch (Throwable var33) {
                var4 = var33;
                throw var33;
            } finally {
                if (httpClient != null) {
                    if (var4 != null) {
                        try {
                            httpClient.close();
                        } catch (Throwable var29) {
                            var4.addSuppressed(var29);
                        }
                    } else {
                        httpClient.close();
                    }
                }

            }
        } catch (Exception var35) {
            Exception e = var35;
            e.printStackTrace();
        }

        return result;
    }
}

