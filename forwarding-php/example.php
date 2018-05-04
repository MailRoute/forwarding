<?php

include('./httpful.phar');

const API_USER = '';
const API_KEY = '';
const API_URL = 'https://admin.mailroute.net';
const FORWARDING_URL = API_URL . '/api/v1/forwarding_emails/';

const HEADERS = array(
    'Content-Type' => 'application/json',
    'Authorization' => 'ApiKey ' . API_USER . ':' . API_KEY,
);

use Httpful\Request;
$template = Request::init()
    ->addHeaders(HEADERS)
    ->expectsJson()             
    ->sendsJson();    
// Set it as a template
Request::ini($template);

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
$response = Request::get(FORWARDING_URL . '?email_account=7@forwarder.com')->send();
if ($response->code != 404 && $response->body->objects) {
    $obj = $response->body->objects[0];
    print_r($obj);
    $resource_uri = $obj->resource_uri;
} else {
    // create new forwarding settings
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
    $r = Request::post(FORWARDING_URL)
        ->body('{
            "email_account": "/api/v1/email_account/7@forwarder.com/",
            "emails": ["forward_to@example.com"],
            "enabled": true
        }')
        ->send();
    $obj = $r->body;
    print_r($obj);
    $resource_uri = $obj->resource_uri;
};

// update forwarding emails
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
$r = Request::patch(API_URL.$resource_uri)
    ->body('{
            "emails": ["forward_to@example.com", "forward_to2@example.com"],
            "enabled": true
        }')
    ->send();
$obj = $r->body;
print_r($obj);
