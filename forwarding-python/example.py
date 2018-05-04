import json

import requests

API_USER = ''
API_KEY = ''
API_URL = 'https://admin.mailroute.net'
FORWARDING_URL = API_URL + '/api/v1/forwarding_emails/'

HEADERS = {
    'Content-Type': 'application/json',
    'Authorization': 'ApiKey ' + API_USER + ':' + API_KEY,
}


def get_forwarding_options(email):
    """
    GET /api/v1/forwarding_emails/?email_account=9@forwarder.com

    Example output:
    {
      "meta": {
        "limit": 20,
        "next": null,
        "offset": 0,
        "previous": null,
        "total_count": 1
      },
      "objects": [
        {
          "created_at": "Fri, 4 May 2018 09:37:30 +0000",
          "email_account": "/api/v1/email_account/97824/",
          "emails": [
            "forward_to@example.com",
            "forward_to2@example.com"
          ],
          "enabled": true,
          "id": 14,
          "max_external_emails": 2,
          "max_internal_emails": 1,
          "resource_uri": "/api/v1/forwarding_emails/14/",
          "updated_at": "Fri, 4 May 2018 09:42:41 +0000"
        }
      ]
    }
    """

    r = requests.get(FORWARDING_URL, params={'email_account': email},
                     headers=HEADERS)
    if r.status_code == 200:
        data = r.json()['objects']
        if data:
            return data[0]
        return None
    r.raise_for_status()


def create_forwarding_options(email, forwarding_emails_list):
    """
    POST /api/v1/forwarding_emails/
    Body: {'email_account': '/api/v1/email_account/10@forwarder.com/', 'enabled': True, 'emails': ['forward_to@example.com']}

    Example output:
    {
      "created_at": "Fri, 4 May 2018 09:50:16 +0000",
      "email_account": "/api/v1/email_account/97825/",
      "emails": [
        "forward_to@example.com",
      ],
      "enabled": true,
      "id": 15,
      "max_external_emails": 2,
      "max_internal_emails": 1,
      "resource_uri": "/api/v1/forwarding_emails/15/",
      "updated_at": "Fri, 4 May 2018 09:50:45 +0000"
    }
    """
    post_data = {
        'email_account': '/api/v1/email_account/%s/' % email,
        'emails': forwarding_emails_list,
        'enabled': True
    }
    r = requests.post(FORWARDING_URL, data=json.dumps(post_data),
                      headers=HEADERS)
    if r.status_code == 201:
        return r.json()
    r.raise_for_status()


def update_forwarding_options(forwarding_resource_uri, forwarding_emails_list,
                              enabled=True):
    """
    PATCH /api/v1/forwarding_emails/15/
    Body: {'enabled': True, 'emails': ['forward_to@example.com', 'forward_to2@example.com']}

    Example output:
    {
      "created_at": "Fri, 4 May 2018 09:50:16 +0000",
      "email_account": "/api/v1/email_account/97825/",
      "emails": [
        "forward_to@example.com",
        "forward_to2@example.com"
      ],
      "enabled": true,
      "id": 15,
      "max_external_emails": 2,
      "max_internal_emails": 1,
      "resource_uri": "/api/v1/forwarding_emails/15/",
      "updated_at": "Fri, 4 May 2018 09:50:45 +0000"
    }
    """
    patch_data = {
        'emails': forwarding_emails_list,
        'enabled': enabled
    }
    r = requests.patch(forwarding_resource_uri, data=json.dumps(patch_data),
                       headers=HEADERS)
    if r.status_code == 202:
        return r.json()
    r.raise_for_status()


fwd_opt = get_forwarding_options('10@forwarder.com')
if not fwd_opt:
    fwd_opt = create_forwarding_options('10@forwarder.com',
                                        ['forward_to@example.com'])

fwd_opt = update_forwarding_options(API_URL + fwd_opt['resource_uri'],
                      ['forward_to@example.com', 'forward_to2@example.com'])
print fwd_opt
