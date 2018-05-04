var qs = require('querystring')
var axios = require('axios')

var API_USER = ''
var API_KEY = ''
var API_URL = 'https://admin.mailroute.net'
var FORWARDING_URL = API_URL + '/api/v1/forwarding_emails/'

var r = axios.create({
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'ApiKey ' + API_USER + ':' + API_KEY
  }
})

// get forwarding data for email account 1@forwarder.com

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

r.get(FORWARDING_URL + '?' + qs.stringify({email_account: '1@forwarder.com'})).then(resp => {
    console.log(resp.data.objects[0])
})

// create forwarding

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
r.post(FORWARDING_URL, {
    "email_account": `/api/v1/email_account/1@forwarder.com/`,
    "emails": ['1@1.com'],
    "enabled": true
}).then(resp => {
    console.log(resp.data)
})

// update forwarding
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
r.get(FORWARDING_URL + '?' + qs.stringify({email_account: '1@forwarder.com'})).then(resp => {
    let uri = resp.data.objects[0].resource_uri //we need to know resource uri to make PATCH request with new data
    r.patch(API_URL + uri, {
        emails: ["email1@somedomain.com", "email2@somedomain.com"],
        enabled: true
    }).then(resp => {
        console.log(resp.data)
    }).catch(e => {
        console.log(e)
    })
})

