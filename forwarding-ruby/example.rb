require 'rest-client'
require 'json'

API_USER = ''
API_KEY = ''
API_URL = 'https://admin.mailroute.net'
FORWARDING_URL = API_URL + '/api/v1/forwarding_emails/'

AUTH_HEADER = {
    content_type: :json,
    'Authorization': 'ApiKey ' + API_USER + ':' + API_KEY
}

# create forwarding options
# POST /api/v1/forwarding_emails/
# Body: {'email_account': '/api/v1/email_account/10@forwarder.com/', 'enabled': True, 'emails': ['forward_to@example.com']}

# Example output:
# {
#   "created_at": "Fri, 4 May 2018 09:50:16 +0000",
#   "email_account": "/api/v1/email_account/97825/",
#   "emails": [
#     "forward_to@example.com",
#   ],
#   "enabled": true,
#   "id": 15,
#   "max_external_emails": 2,
#   "max_internal_emails": 1,
#   "resource_uri": "/api/v1/forwarding_emails/15/",
#   "updated_at": "Fri, 4 May 2018 09:50:45 +0000"
# }
r = RestClient::Request.execute(method: :post, url: FORWARDING_URL, payload:
    {
        "email_account": '/api/v1/email_account/6@forwarder.com/',
        "emails": ['email1@example.com'],
        "enabled": true
    }.to_json,
    headers: AUTH_HEADER)
puts JSON.parse(r.body)


# get forwarding options
# GET /api/v1/forwarding_emails/?email_account=9@forwarder.com
# Example output:
# {
#   "meta": {
#     "limit": 20,
#     "next": null,
#     "offset": 0,
#     "previous": null,
#     "total_count": 1
#   },
#   "objects": [
#     {
#       "created_at": "Fri, 4 May 2018 09:37:30 +0000",
#       "email_account": "/api/v1/email_account/97824/",
#       "emails": [
#         "forward_to@example.com",
#         "forward_to2@example.com"
#       ],
#       "enabled": true,
#       "id": 14,
#       "max_external_emails": 2,
#       "max_internal_emails": 1,
#       "resource_uri": "/api/v1/forwarding_emails/14/",
#       "updated_at": "Fri, 4 May 2018 09:42:41 +0000"
#     }
#   ]
# }
r = RestClient::Request.execute(method: :get, url: FORWARDING_URL, headers: {params: {email_account: '6@forwarder.com'}}.update(AUTH_HEADER))
obj = JSON.parse(r.body)['objects'][0]
puts obj

# update forwarding options
# PATCH /api/v1/forwarding_emails/15/
# Body: {'enabled': True, 'emails': ['forward_to@example.com', 'forward_to2@example.com']}

# Example output:
# {
#   "created_at": "Fri, 4 May 2018 09:50:16 +0000",
#   "email_account": "/api/v1/email_account/97825/",
#   "emails": [
#     "forward_to@example.com",
#     "forward_to2@example.com"
#   ],
#   "enabled": true,
#   "id": 15,
#   "max_external_emails": 2,
#   "max_internal_emails": 1,
#   "resource_uri": "/api/v1/forwarding_emails/15/",
#   "updated_at": "Fri, 4 May 2018 09:50:45 +0000"
# }
resource_uri = obj['resource_uri']
r = RestClient::Request.execute(method: :patch, url: API_URL + resource_uri, payload:
    {
        "emails": ['email1@example.com', 'email2@example.com'],
        "enabled": true
    }.to_json,
    headers: AUTH_HEADER)
puts JSON.parse(r.body)

