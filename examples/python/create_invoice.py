import requests   # pip install requests
import json

SERVER = "https://www.vogogo.com"

# use HTTP Basic Authentication by joining api_key with app_token (seperated by colon :)
AUTH = ('YOUR_USERNAME', 'YOUR_API_KEY:YOUR_APP_TOKEN')

# Create a new invoice
valid_invoice = json.dumps({'number': '1234',
    'customer_ref': 'Mr. Joe Jacob Blow',
    'customer_emails': 'joe@example.com',
    'customer_contact_info': '123 Fake ST\nCalgary\nAlberta\n403.987.1231',
    'profile_name': 'Jims Snow Removal',
    'profile_emails': 'snow@example.com',
    'profile_contact_info': '403.123.1234',
    'po': '1234',
    'currency': 'CAD',
    'accept_cc': 'True',
    'accept_obp': 'True',
    'due_date': '2012-10-10',
    'message': 'This is your invoice for services rendered.',
    'items': [{'code': 'GOLFPINK2012', 'description': 'Dozen pink golf balls', 'unit_price': '24.99', 'quantity': '2'},
            {'code': 'EXTREMECLUBS-M', 'description': '2012 Extreme Awesome Mens Golf Clubs', 'unit_price': '824.99', 'quantity': '1'},
            {'code': 'SHAFT-Z', 'description': 'Size club shafts', 'unit_price': '124.00', 'quantity': '2.5', 'tax': 'GST'}]
    })

headers = {'content-type': 'application/json'}
r = requests.post(SERVER + '/api/payment/v1/invoice/', auth=AUTH, data=valid_invoice, headers=headers)

if r.status_code == 201:  # 201 == 'CREATED'
    print "invoice Created"
