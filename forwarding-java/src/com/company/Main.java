package com.company;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;


public class Main {
    public static final String API_USER = "";
    public static final String API_KEY = "";
    public static final String API_URL = "https://admin.mailroute.net";
    public static final String FORWARDING_URL = API_URL + "/api/v1/forwarding_emails/";

    public static void main(String[] args) {
        Unirest.setDefaultHeader("Content-Type", "application/json");
        Unirest.setDefaultHeader("Authorization", "ApiKey " + API_USER + ":" + API_KEY);

        // create new forwarding record
        // POST /api/v1/forwarding_emails/
        // Body: {'email_account': '/api/v1/email_account/10@forwarder.com/', 'enabled': True, 'emails': ['forward_to@example.com']}

        // Example output:
        // {
        //   "created_at": "Fri, 4 May 2018 09:50:16 +0000",
        //   "email_account": "/api/v1/email_account/97825/",
        //   "emails": [
        //     "forward_to@example.com",
        //   ],
        //   "enabled": true,
        //   "id": 15,
        //   "max_external_emails": 2,
        //   "max_internal_emails": 1,
        //   "resource_uri": "/api/v1/forwarding_emails/15/",
        //   "updated_at": "Fri, 4 May 2018 09:50:45 +0000"
        // }
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(FORWARDING_URL)
                    .body("{\n" +
                        "\"email_account\": \"/api/v1/email_account/8@forwarder.com/\",\n" +
                        "\"emails\": [\"forward_to@example.com\"],\n" +
                        "\"enabled\": true\n" +
                        "}")
                    .asJson();
            System.out.println(jsonResponse.getBody().getObject());
        } catch (Exception e) {

        }

        // get forwarding settings
        // GET /api/v1/forwarding_emails/?email_account=9@forwarder.com

        // Example output:
        // {
        //   "meta": {
        //     "limit": 20,
        //     "next": null,
        //     "offset": 0,
        //     "previous": null,
        //     "total_count": 1
        //   },
        //   "objects": [
        //     {
        //       "created_at": "Fri, 4 May 2018 09:37:30 +0000",
        //       "email_account": "/api/v1/email_account/97824/",
        //       "emails": [
        //         "forward_to@example.com",
        //         "forward_to2@example.com"
        //       ],
        //       "enabled": true,
        //       "id": 14,
        //       "max_external_emails": 2,
        //       "max_internal_emails": 1,
        //       "resource_uri": "/api/v1/forwarding_emails/14/",
        //       "updated_at": "Fri, 4 May 2018 09:42:41 +0000"
        //     }
        //   ]
        // }
        String resource_uri = "";
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(FORWARDING_URL)
                    .queryString("email_account", "8@forwarder.com")
                    .asJson();
            System.out.println(jsonResponse.getBody().getObject().getJSONArray("objects").getJSONObject(0));
            resource_uri = (String)jsonResponse.getBody().getObject().getJSONArray("objects").getJSONObject(0).get("resource_uri");
        } catch (Exception e) {

        }

        //update forwarding settings
        // PATCH /api/v1/forwarding_emails/15/
        // Body: {'enabled': True, 'emails': ['forward_to@example.com', 'forward_to2@example.com']}

        // Example output:
        // {
        //   "created_at": "Fri, 4 May 2018 09:50:16 +0000",
        //   "email_account": "/api/v1/email_account/97825/",
        //   "emails": [
        //     "forward_to@example.com",
        //     "forward_to2@example.com"
        //   ],
        //   "enabled": true,
        //   "id": 15,
        //   "max_external_emails": 2,
        //   "max_internal_emails": 1,
        //   "resource_uri": "/api/v1/forwarding_emails/15/",
        //   "updated_at": "Fri, 4 May 2018 09:50:45 +0000"
        // }
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.patch(API_URL + resource_uri)
                    .body("{\n" +
                            "\"emails\": [\"forward_to@example.com\", \"forward_to2@example.com\"],\n" +
                            "\"enabled\": true\n" +
                            "}")
                    .asJson();
            System.out.println(jsonResponse.getBody().getObject());
        } catch (Exception e) {

        }

    }
}
