package com.jakubstas.integration.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

public final class SlashCommandUtils {

    @Value("${slack.port}")
    private int slackPort;

    private final RestTemplate restTemplate = new TestRestTemplate();

    public final void slashNewCommand(final String userName, final String userId, final String proposedTime) {
        final String url = newUriBuilder().queryParam("user_name", userName).queryParam("user_id", userId).queryParam("text", proposedTime).queryParam("response_url", getResponseUrl()).build().toString();

        restTemplate.postForLocation(url, null);
    }

    public final void slashIaminCommand(final String userName, final String userId, final Optional<String> hostNameOptional) {
        final String url;

        if (hostNameOptional.isPresent()) {
            url = newUriBuilder().path("join").queryParam("user_name", userName).queryParam("user_id", userId).queryParam("text", hostNameOptional.get()).queryParam("response_url", getResponseUrl()).build().toString();
        } else {
            url = newUriBuilder().path("join").queryParam("user_name", userName).queryParam("user_id", userId).queryParam("response_url", getResponseUrl()).build().toString();
        }

        restTemplate.postForLocation(url, null);
    }

    public final void slashRescheduleCommand(final String userName, final String proposedTime) {
        final String url = newUriBuilder().path("update").queryParam("user_name", userName).queryParam("text", proposedTime).queryParam("response_url", getResponseUrl()).build().toString();

        restTemplate.postForLocation(url, null);
    }

    public final void slashCancelCommand(final String userName) {
        final String url = newUriBuilder().path("cancel").queryParam("user_name", userName).queryParam("response_url", getResponseUrl()).build().toString();

        restTemplate.postForLocation(url, null);
    }

    public final void slashGamesCommand(final String userName) {
        final String url = newUriBuilder().queryParam("user_name", userName).queryParam("response_url", getResponseUrl()).build().toString();

        restTemplate.getForEntity(url, null);
    }

    private UriComponentsBuilder newUriBuilder() {
        return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(8081).queryParam("token", "token123");
    }

    private String getResponseUrl() {
        return "http://localhost:" + slackPort;
    }
}
