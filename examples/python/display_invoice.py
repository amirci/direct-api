import requests   # pip install requests
import json

SERVER = "https://www.vogogo.com"

# use HTTP Basic Authentication by joining api_key with app_token (seperated by colon :)
AUTH = ('YOUR_USERNAME', 'YOUR_API_KEY:YOUR_APP_TOKEN')

r = requests.get(SERVER + '/api/payment/v1/invoice/', auth=AUTH)

if r.status_code == 200:
    recent_invoice = r.json['objects'][0]
    
    print "invoice #%s for %s in the amount of %s%s" % (recent_invoice['number'], recent_invoice['customer_ref'], recent_invoice['amount'], recent_invoice['currency'])
    for item in recent_invoice['items']:
        print "   %s %s at %s each." % (float(item['quantity']), item['description'], item['unit_price'])
